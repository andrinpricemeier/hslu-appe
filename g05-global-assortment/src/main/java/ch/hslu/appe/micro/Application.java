/*
 * Copyright 2020 Roland Christen, HSLU Informatik, Switzerland
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ch.hslu.appe.micro;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import org.slf4j.LoggerFactory;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.JaegerTrace;
import ch.hslu.appe.bus.MessageBus;
import ch.hslu.appe.bus.MessageRoutes;
import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.bus.RabbitMqMessageBus;
import ch.hslu.appe.handlers.CancelArticleReservationsHandler;
import ch.hslu.appe.handlers.ListArticlesHandler;
import ch.hslu.appe.handlers.ListRestockingsHandler;
import ch.hslu.appe.handlers.OrderArticlesHandler;
import ch.hslu.appe.handlers.ReserveArticlesHandler;
import ch.hslu.appe.handlers.RestockArticlesHandler;
import ch.hslu.appe.messages.CancelArticleReservationsMessage;
import ch.hslu.appe.messages.ListArticlesMessage;
import ch.hslu.appe.messages.ListRestockingsMessage;
import ch.hslu.appe.messages.OrderArticlesMessage;
import ch.hslu.appe.messages.ReserveArticlesMessage;
import ch.hslu.appe.messages.RestockArticlesMessage;
import ch.hslu.appe.monitoring.LokiMonitoring;
import ch.hslu.appe.repositories.MongoDBRestockingRepository;
import ch.hslu.appe.repositories.RandomInMemoryArticleRepository;
import ch.hslu.appe.stock.api.StockFactory;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;

public final class Application {

    private Application() {
    }

    public static void main(final String[] args) throws InterruptedException {
        try { 
            // We don't want MongoDB to spam our logs.
            setMongoDBLoggingtoINFO();
            final RabbitMqConfig busConfig = new RabbitMqConfig();
            final var appConfig = new ApplicationConfig();
            final String exchangeName = busConfig.getExchange();
            final BusConnector busConnector = new BusConnector();
            busConnector.connect();
            final var trace = new JaegerTrace();
            final var monitoring = new LokiMonitoring();
            final MessageBus bus = new RabbitMqMessageBus(exchangeName, busConfig.getQueue(), busConnector);
            final var stock = StockFactory.getStock();
            final var articleRepository = new RandomInMemoryArticleRepository(stock);    
            final var restockingRepository = new MongoDBRestockingRepository(appConfig.getDbUrl(), appConfig.getDbPort(), appConfig.getDbUser(), appConfig.getDbPassword());
            // We don't want MongoDB to spam our logs.
            setMongoDBLoggingtoINFO();
            final var listArticlesHandler = new ListArticlesHandler(articleRepository, bus, trace, monitoring);
            bus.registerHandler(MessageRoutes.GLOBALASSORTMENT_ARTICLE_LIST, listArticlesHandler, ListArticlesMessage.class);
            final var reserveArticlesHandler = new ReserveArticlesHandler(articleRepository, bus, trace, monitoring);
            bus.registerHandler(MessageRoutes.GLOBALASSORTMENT_ARTICLE_RESERVE, reserveArticlesHandler, ReserveArticlesMessage.class);
            final var cancelReservationsHandler = new CancelArticleReservationsHandler(articleRepository, trace, monitoring);
            bus.registerHandler(MessageRoutes.GLOBALASSORTMENT_ARTICLE_RESERVATION_CANCEL, cancelReservationsHandler, CancelArticleReservationsMessage.class);
            final var restockArticlesHandler = new RestockArticlesHandler(restockingRepository, trace, bus, monitoring);
            bus.registerHandler(MessageRoutes.GLOBALASSORTMENT_ARTICLE_RESTOCK, restockArticlesHandler, RestockArticlesMessage.class);
            final var listRestockingsHandler = new ListRestockingsHandler(restockingRepository, bus, trace, monitoring);
            bus.registerHandler(MessageRoutes.GLOBALASSORTMENT_ARTICLE_RESTOCK_LIST, listRestockingsHandler, ListRestockingsMessage.class);
            final var orderArticlesHandler = new OrderArticlesHandler(articleRepository, trace, bus, monitoring);
            bus.registerHandler(MessageRoutes.GLOBALASSORTMENT_ARTICLE_ORDER, orderArticlesHandler, OrderArticlesMessage.class);
            bus.startListen();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    private static void setMongoDBLoggingtoINFO() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final var rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.INFO);
    }
}
