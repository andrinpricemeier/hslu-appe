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

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.JaegerTrace;
import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.db.MongoDBCustomerRepository;
import ch.hslu.appe.entities.Customer;
import ch.hslu.appe.entities.CustomerRepository;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Application: sets up the bus and listens for messages.
 */
public final class Application {
    private static final Logger LOG = LoggerFactory.getLogger(Application.class);

    /**
     * private constructor.
     */
    private Application() {
    }

    public static int reverse(int x) {
        final String s = Integer.toString(x);
        final StringBuilder out = new StringBuilder();
        boolean isNegative = false;
        for (int i = s.length() - 1; i >= 0; i--) {
            final String ch = Character.toString(s.charAt(i));
            if (ch.equals("-")) {
                isNegative = true;
            } else {
                out.append(ch);
            }
        }
        final String finalStr = isNegative ? "-" + out.toString() : out.toString();
        try {
            return Integer.parseInt(finalStr);
        } catch (NumberFormatException ex) {
            return 0;
        }
    }

    /**
     * main-Methode. Startet einen Timer für den HeartBeat.
     *
     * @param args not used.
     */
    public static void main(final String[] args) {
        int reverse = reverse(123);
        int reverse2 = reverse(-123);
        int reverse3 = reverse(120);
        int reverse4 = reverse(0);
        final ApplicationConfig appConfig = new ApplicationConfig();
        final RabbitMqConfig busConfig = new RabbitMqConfig();
        final String exchangeName = busConfig.getExchange();
        final BusConnector bus = new BusConnector();
        final var trace = new JaegerTrace();

        try {
            // Set minimum logging level of MongoDB to INFO
            setMongoDBLoggingtoINFO();

            bus.connect();

            final CustomerRepository customerRepository = new MongoDBCustomerRepository(appConfig.getDbUser(),
                    appConfig.getDbPassword(), appConfig.getDbUrl(), appConfig.getDbPort());
            final GetAllCustomerHandler getAllCustomerHandler = new GetAllCustomerHandler(customerRepository, trace,
                    exchangeName, bus);

            // add three sample customers to the database
            if (customerRepository.getAllCustomer().isEmpty()) {
                customerRepository.add(new Customer(1, "Hans", "Muster", "Musterstrasse", "1", "1000", "Luzern"));
                customerRepository.add(new Customer(2, "Peter", "Meier", "Musterstrasse", "2", "2000", "Zürich"));
                customerRepository.add(new Customer(3, "Lisa", "Fischer", "Musterstrasse", "3", "3000", "Bern"));
                LOG.info("Added sample customer entries in database, customer count in database: "
                        + customerRepository.getAllCustomer().size());
            }

            bus.listenFor(exchangeName, busConfig.getQueueGetAll(), "customer.getall", getAllCustomerHandler);
            Thread.currentThread().join();

            getAllCustomerHandler.close();

        } catch (Exception e) {
            LOG.error(e.toString());
        }
    }

    /**
     * sets the minimum logging level of mongodb to INFO
     */
    private static void setMongoDBLoggingtoINFO() {
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        final var rootLogger = loggerContext.getLogger("org.mongodb.driver");
        rootLogger.setLevel(Level.INFO);
    }
}
