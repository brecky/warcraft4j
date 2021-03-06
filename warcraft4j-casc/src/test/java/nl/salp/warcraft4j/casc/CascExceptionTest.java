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
package nl.salp.warcraft4j.casc;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Unit tests for {@link CascException}.
 *
 * @author Barre Dijkstra
 * @see CascException
 */
public class CascExceptionTest {
    private static final String MESSAGE = "exception message";
    private static final String CAUSE_MESSAGE = "cause exeption";
    private static final Exception CAUSE = new Exception(CAUSE_MESSAGE);

    @Test
    public void shouldCreateExceptionWithMessage() {
        CascException exception = new CascException(MESSAGE);

        assertEquals(MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    public void shouldCreateExceptionWithCause() {
        CascException exception = new CascException(CAUSE);

        assertEquals(String.valueOf(CAUSE), exception.getMessage());
        assertEquals(CAUSE, exception.getCause());
    }

    @Test
    public void shouldCreateExceptionWithMessageAndCause() {
        CascException exception = new CascException(MESSAGE, CAUSE);

        assertEquals(MESSAGE, exception.getMessage());
        assertEquals(CAUSE, exception.getCause());
    }
}
