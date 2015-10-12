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

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link CallCounter}.
 *
 * @author Barre Dijkstra
 */
public class CallCounterTest {
    private static final long MAX_CALLS_HOUR = 100;
    private static final long MAX_CALLS_SECOND = 50;

    private CallCounter counter;

    @Before
    public void setUp() {
        counter = new CallCounter(MAX_CALLS_SECOND, MAX_CALLS_HOUR);
    }

    @Test
    public void shouldSetLimits() {
        assertEquals(MAX_CALLS_HOUR, counter.getAvailableCallsForHour());
        assertEquals(MAX_CALLS_SECOND, counter.getAvailableCallsForSecond());
    }

    @Test
    public void shouldLogCall() {
        assertTrue(counter.call());

        assertEquals(1, counter.getCallsLastSecond());
        assertEquals(MAX_CALLS_SECOND - 1, counter.getAvailableCallsForSecond());

        assertEquals(1, counter.getCallsLastHour());
        assertEquals(MAX_CALLS_HOUR - 1, counter.getAvailableCallsForHour());
    }

    @Test
    public void shouldBlockCallsOnSecondLimit() {
        maxCallsForSecondLimit(counter);

        assertFalse(counter.call());
        assertEquals(MAX_CALLS_SECOND, counter.getCallsLastSecond());
    }

    @Test
    public void shouldAllowCallAfterSecondLimitExpiration() {
        maxCallsForSecondLimit(counter);

        assertFalse(counter.call());
        try {
            Thread.sleep(1001);
        } catch (InterruptedException e) {
        }
        assertTrue(counter.call());
    }

    @Test
    public void shouldBlockCallsOnHourLimit() {
        maxCallsForHourLimit(counter);

        assertFalse(counter.call());
        assertEquals(MAX_CALLS_HOUR, counter.getCallsLastHour());
    }

    private long maxCallsForSecondLimit(CallCounter counter) {
        long call = 0;
        while (counter.getAvailableCallsForSecond() > 0) {
            if (counter.call()) {
                call++;
            }
        }
        return call;
    }

    private long maxCallsForHourLimit(CallCounter counter) {
        long call = 0;
        while (counter.getAvailableCallsForHour() > 0) {
            if (counter.call()) {
                call++;
            } else {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                }
            }
        }
        return call;
    }
}
