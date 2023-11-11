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
import java.text.DateFormatSymbols;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import ch.hslu.appe.entities.MonthStat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import ch.hslu.appe.bus.BusConnector;
import ch.hslu.appe.bus.MessageReceiver;
import ch.hslu.appe.bus.RabbitMqConfig;
import ch.hslu.appe.entities.Student;

/**
 * Beispielcode für Implementation eines Servcies mit RabbitMQ.
 */
public final class ServiceTemplate implements AutoCloseable, MessageReceiver {

    private static final Logger LOG = LogManager.getLogger(ServiceTemplate.class);

    private static final String ROUTE_STUDENT_REGISTER = "student.register";
    private static final String ROUTE_STATISTICS_TOP_MONTH = "statistics.top-month";
    private static final String ROUTE_STATISTICS_CHANGED = "statistics.changed";
    private static final String ROUTE_DEEP_THOUGHT_ASK = "deep-thought.ask";

    private final String exchangeName;
    private final BusConnector bus;

    /**
     * @throws IOException      IO-Fehler.
     * @throws TimeoutException Timeout.
     */
    ServiceTemplate() throws IOException, TimeoutException {

        // thread info
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Service started", threadName);

        // setup rabbitmq connection
        this.exchangeName = new RabbitMqConfig().getExchange();
        this.bus = new BusConnector();
        this.bus.connect();

        // start message receivers
        this.receiveStatisticsChange();
        this.receiveDeepThoughQuestions(new QuestionReceiver(exchangeName, bus));
    }

    /**
     * Erzeugt eine Student-Entity und sendet einen Event.
     *
     * @return Student.
     * @throws IOException          IOException.
     * @throws InterruptedException InterruptedException.
     */
    public Student registerStudent() throws IOException, InterruptedException {

        // create new student
        final Student student = new Student(1, "D", "E", 6);
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(student);

        // send message to register student in registry, sync communication (awaiting response)
        LOG.debug("Sending synchronous message to broker with routing [{}]", ROUTE_STUDENT_REGISTER);
        String response = bus.talkSync(exchangeName, ROUTE_STUDENT_REGISTER, data);

        // receive new student object
        if (response == null) {
            LOG.error("Received no response. Timeout occurred. Will retry later");
            return null;
        }
        LOG.debug("Received response: [{}]", response);
        Student registeredStudent = mapper.readValue(response, Student.class);

        // return new object
        return registeredStudent;
    }

    /**
     * TODO
     */
    public String askAboutUniverse() throws IOException, InterruptedException {

        // create question
        final String question = "What is the answer to the Ultimate Question of Life, the Universe, and Everything?";

        // send question to deep thought
        LOG.debug("Sending synchronous message to broker with routing [{}]", ROUTE_DEEP_THOUGHT_ASK);
        String response = bus.talkSync(exchangeName, ROUTE_DEEP_THOUGHT_ASK, question);

        // receive answer
        if (response == null) {
            LOG.debug("Received no response. Timeout occurred. Will retry later");
            return null;
        }
        LOG.debug("Received response to question \"{}\": {}", question, response);
        return response;
    }

    /**
     * @param topMonth
     * @param studentCount
     * @throws IOException
     * @throws InterruptedException
     */
    private void broadcastTopBirthMonth(Integer topMonth, Integer studentCount) throws IOException, InterruptedException {

        // month most students born in
        String monthName = new DateFormatSymbols().getMonths()[topMonth - 1];
        LOG.debug("Most students were born in {}: currently {} students", monthName, studentCount);
        MonthStat monthStat = new MonthStat(topMonth, studentCount);

        // broadcast top birth month event, async communication
        ObjectMapper mapper = new ObjectMapper();
        String data = mapper.writeValueAsString(monthStat);
        LOG.debug("Sending asynchronous message to broker with routing [{}]", ROUTE_STATISTICS_TOP_MONTH);
        bus.talkAsync(exchangeName, ROUTE_STATISTICS_TOP_MONTH, data);
    }

    /**
     * @throws IOException
     */
    private void receiveStatisticsChange() throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", ROUTE_STATISTICS_CHANGED);
        bus.listenFor(exchangeName, "ServiceTemplate | " + ROUTE_STATISTICS_CHANGED, ROUTE_STATISTICS_CHANGED, this);
    }

    /**
     * @throws IOException
     */
    private void receiveDeepThoughQuestions(MessageReceiver receiver) throws IOException {
        LOG.debug("Starting listening for messages with routing [{}]", ROUTE_DEEP_THOUGHT_ASK);
        bus.listenFor(exchangeName, "ServiceTemplate | " + ROUTE_DEEP_THOUGHT_ASK, ROUTE_DEEP_THOUGHT_ASK, receiver);
    }

    /**
     * @see ch.hslu.appe.bus.MessageReceiver#onMessageReceived(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public void onMessageReceived(final String route, final String replyTo, final String corrId, final String message) {

        // log event
        String threadName = Thread.currentThread().getName();
        LOG.debug("[Thread: {}] Begin message processing", threadName);
        LOG.debug("Received message with routing [{}]", route);

        // unpack received message data
        ObjectMapper mapper = new ObjectMapper();
        TypeReference<TreeMap<Integer, Integer>> typeRef = new TypeReference<>() {
            // empty
        };
        try {

            // process message data
            TreeMap<Integer, Integer> statistics = mapper.readValue(message, typeRef);
            Integer topMonth = this.findTopMonth(statistics);

            //
            this.broadcastTopBirthMonth(topMonth, statistics.get(topMonth));

            //Thread.sleep(5000);
            Thread.sleep(0);


        } catch (IOException | InterruptedException e) {
            LOG.error(e);
            e.printStackTrace();
        } finally {
            // thread info
            LOG.debug("[Thread: {}] End message processing", threadName);
        }
    }

    private Integer findTopMonth(TreeMap<Integer, Integer> statistics) {

        Integer topMonth = 0;
        Integer maxStudents = 0;
        for (Integer month : statistics.keySet()) {
            Integer studentCount = statistics.get(month);
            String monthName = new DateFormatSymbols().getMonths()[month - 1];
            LOG.debug("Students born in {}: {}", monthName, studentCount);
            if (studentCount > maxStudents) {
                maxStudents = studentCount;
                topMonth = month;
            }
        }

        return topMonth;

    }

    /**
     * @see java.lang.AutoCloseable#close()
     */
    @Override
    public void close() {
        bus.close();
    }
}
