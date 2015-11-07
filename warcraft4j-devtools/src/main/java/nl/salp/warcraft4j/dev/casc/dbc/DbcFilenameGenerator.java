/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.salp.warcraft4j.dev.casc.dbc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DbcFilenameGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbcFilenameGenerator.class);
    private static final String[] CHARS = new String[]{"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", " ", "_", "-", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};
    private static final String FORMAT_DBC = "DBFILESCLIENT\\%s.DBC";
    private static final String FORMAT_DB2 = "DBFILESCLIENT\\%s.DB2";
    private final int maxLength;
    private final BlockingQueue<String> queue;
    private final Supplier<Consumer<String>> consumer;

    public DbcFilenameGenerator(int maxLength, Supplier<Consumer<String>> consumer) {
        this.maxLength = maxLength;
        this.queue = new ArrayBlockingQueue<>(1_000_000);
        this.consumer = consumer;
    }

    public void execute() {
        int cores = 32;
        int generators = 1;
        int consumers = cores - generators;

        ExecutorService executorService = Executors.newFixedThreadPool(cores);
        Generator generator = new Generator(CHARS, maxLength);
        /*
        IntStream.range(0, consumers).forEach(i -> executorService.execute(() -> {
            LOGGER.debug("Started consumer {}", i);
            Consumer<String> consumer = this.consumer.get();
            String val = null;
            //long tryCount = 0;
            while (generator.isAvailable() || !queue.isEmpty()) {
                while (val == null && (generator.isAvailable() || !queue.isEmpty())) {
                    //tryCount++;
                    try {
                        val = queue.poll(10, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                    }
                    //if (tryCount % 1000 == 0) {
                    //    LOGGER.debug("Starvation for consumer {} with {} tries", i);
                    //}
                }
                if (val != null) {
                    //tryCount = 0;
                    consumer.accept(val);
                    val = null;
                }
            }
            LOGGER.debug("Shutting down consumer {}", i);
        }));
        */
        IntStream.range(0, generators).forEach(i -> executorService.execute(() -> {
            LOGGER.debug("Started generator {}", i);
            while (generator.isAvailable()) {
                String value = generator.next();
                //add(format(FORMAT_DBC, value));
                //add(format(FORMAT_DB2, value));
            }
            LOGGER.debug("Shutting down generator {}", i);
        }));

        LOGGER.debug("Done...");
        executorService.shutdown();
    }

    private void add(String value) {
        boolean added = false;
        //long tries = 0;
        while (!added) {
            //tries++;
            try {
                added = queue.offer(value, 10, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
            }
            //if (tries % 1000 == 0) {
            //    LOGGER.debug("Congestion for adding value with {} attempts.", tries);
            //}
        }
    }

    private static class Generator {
        private final String[] chars;
        private final int[] positions;
        private final int maxLength;
        private final int maxPosition;
        private boolean available;

        private Generator(String[] chars, int maxLength) {
            this.maxLength = maxLength;
            this.maxPosition = chars.length;
            this.chars = chars;
            this.positions = new int[maxLength];
            this.available = true;
            Arrays.fill(this.positions, -1);
            this.positions[0] = 0;
        }

        public String next() {
            String val = IntStream.of(positions)
                    .filter(i -> i >= 0)
                    .filter(i -> i < maxPosition)
                    .mapToObj(i -> chars[i])
                    .map(String::valueOf)
                    .collect(Collectors.joining());
            available = inc(0);
            if (isEmpty(val) && available) {
                val = next();
            }
            return val;
        }


        private boolean inc(int position) {
            boolean inc = false;
            if (positions[position] == -1) {
                LOGGER.debug("Switching over to a String length of {} characters", position + 1);
            }
            if (position < maxLength && positions[position] >= maxPosition - 1) {
                if (inc(position + 1)) {
                    positions[position] = 0;
                    inc = true;
                }
            } else if (position < maxLength) {
                positions[position] = positions[position] + 1;
                inc = true;
            }
            return inc;
        }

        public boolean isFinished() {
            return !isAvailable();
        }

        public boolean isAvailable() {
            return available;
        }
    }
}
