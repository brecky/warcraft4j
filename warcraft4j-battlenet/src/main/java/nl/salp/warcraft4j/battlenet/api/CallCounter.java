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

package nl.salp.warcraft4j.battlenet.api;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

/**
 * Simple rolling call counter for checking call counts per second and hour.
 *
 * @author Barre Dijkstra
 */
class CallCounter {
    /** An hour in milliseconds. */
    private static final long HOUR = 3600000;
    /** A second in milliseconds. */
    private static final long SECOND = 1000;
    /** Queue with the logged execution times. */
    private Queue<Long> executionTimes;
    private long maxCallsPerHour;
    private long maxCallsPerSecond;

    public CallCounter(long maxCallsPerHour, long maxCallsPerSecond) {
        this.executionTimes = new ConcurrentLinkedQueue<>();
        this.maxCallsPerHour = maxCallsPerSecond;
        this.maxCallsPerSecond = maxCallsPerHour;
    }

    /**
     * Check if a call is allowed.
     *
     * @return {@code true}  if a call is allowed.
     */
    private boolean isCallAllowed() {
        return getCallsLastHour() < maxCallsPerHour && getCallsLastSecond() < maxCallsPerSecond;
    }

    /**
     * Attempt a call.
     *
     * @return {@code true} if a call is allowed and logged, {@code false} if the call is not allowed.
     */
    public boolean call() {
        clean();
        boolean callAllowed = false;
        if (isCallAllowed()) {
            callAllowed = executionTimes.add(System.currentTimeMillis());
        }
        return callAllowed;
    }

    /**
     * Get the number of calls last hour.
     *
     * @return The number of calls within the last hour.
     */
    public long getCallsLastHour() {
        return getCallsLastPeriod(floorHour());
    }

    /**
     * Get the number of calls still allowed within the current hour.
     *
     * @return The number of calls.
     */
    public long getAvailableCallsForHour() {
        return maxCallsPerHour - getCallsLastHour();
    }

    /**
     * Get the number of calls still allowed within the current second.
     *
     * @return The number of calls.
     */
    public long getAvailableCallsForSecond() {
        return maxCallsPerSecond - getCallsLastSecond();
    }

    /**
     * Get the number of calls in the last second.
     *
     * @return The number of calls.
     */
    public long getCallsLastSecond() {
        return getCallsLastPeriod(floorSeconds());
    }

    /**
     * Get the number of calls in the last period.
     *
     * @param floorTime Lambda that provides the time when the period started.
     *
     * @return The number of calls in the period.
     */
    private long getCallsLastPeriod(long floorTime) {
        int calls = 0;
        for (long time : executionTimes) {
            if (time >= floorTime) {
                calls++;
            }
        }
        return calls;
    }

    /**
     * Clean up all expired calls.
     */
    private void clean() {
        while (!executionTimes.isEmpty() && executionTimes.peek() < floorHour()) {
            executionTimes.poll();
        }
    }

    /**
     * Get the floor time for calls within the last second.
     *
     * @return The floor time.
     */
    private long floorSeconds() {
        return System.currentTimeMillis() - SECOND;
    }

    /**
     * Get the floor time for calls within the last hour.
     *
     * @return The floor time.
     */
    private long floorHour() {
        return System.currentTimeMillis() - HOUR;
    }

    /**
     * Time unit to limit on.
     */
    public static enum TimeUnit {
        /** Milliseconds. */
        MILLISECONDS(1),
        /** Seconds. */
        SECONDS(1000),
        /** Minutes. */
        MINUTES(60000),
        /** Hours. */
        HOURS(3600000);

        /** The duration in milliseconds. */
        private final long durationMs;

        /**
         * Create a new TimeUnit.
         *
         * @param durationMs The duration of the time unit in milliseconds.
         */
        private TimeUnit(long durationMs) {
            this.durationMs = durationMs;
        }

        /**
         * The duration of the time unit in milliseconds.
         *
         * @return The milliseconds.
         */
        public long milliseconds() {
            return durationMs;
        }
    }

    /**
     * Limit of calls per time unit.
     */
    public static class Limit {
        private final long limit;
        private final TimeUnit timeUnit;

        public Limit(long limit, TimeUnit timeUnit) {
            this.limit = limit;
            this.timeUnit = timeUnit;
        }

        public long getLimit() {
            return limit;
        }

        public TimeUnit getTimeUnit() {
            return timeUnit;
        }

        public long floorTime() {
            return System.currentTimeMillis() - timeUnit.milliseconds();
        }
    }
}
