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
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Demo für Applikationsstart.
 */
public final class Application {

    /**
     * TimerTask für periodische Ausführung.
     */
    private static final class HeartBeat extends TimerTask {

        private static final Logger LOG = LogManager.getLogger(HeartBeat.class);

        private ServiceTemplate service;

        HeartBeat() {
            try {
                this.service = new ServiceTemplate();
            } catch (IOException | TimeoutException e) {
                LOG.error(e);
            }
        }

        @Override
        public void run() {
            try {
                service.registerStudent();
                service.askAboutUniverse();
            } catch (IOException | InterruptedException e) {
                LOG.error(e);
            }
        }
    }

    /**
     * Privater Konstruktor.
     */
    private Application() {
    }

    /**
     * main-Methode. Startet einen Timer für den HeartBeat.
     *
     * @param args not used.
     */
    public static void main(final String[] args) {
        final Timer timer = new Timer();
        timer.schedule(new HeartBeat(), 0, 10000);
    }
}
