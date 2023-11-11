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
import ch.hslu.appe.bus.MessageReceiver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public final class QuestionReceiver implements MessageReceiver {

    private static final Logger LOG = LogManager.getLogger(QuestionReceiver.class);
    private final String exchangeName;
    private final BusConnector bus;

    public QuestionReceiver(String exchangeName, BusConnector bus) {
        this.exchangeName = exchangeName;
        this.bus = bus;
    }

    /**
     * @see MessageReceiver#onMessageReceived(String, String, String, String)
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {

        // receive message and reply
        try {
            bus.reply(exchangeName, replyTo, corrId, "42");
        } catch (IOException e) {
            LOG.error(e);
        }

    }

}
