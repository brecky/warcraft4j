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
package nl.salp.warcraft4j.util;

import org.junit.Before;
import org.junit.Test;

import java.util.function.Supplier;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for {@link LazyInstance}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.util.LazyInstance
 */
public class LazyInstanceTest {
    /** The test data. */
    private static final String DATA = "Test object";

    /** Mock supplier. */
    private Supplier<String> supplier;

    @Before
    public void setUp() {
        this.supplier = mock(Supplier.class);
        when(this.supplier.get()).thenReturn(DATA);
    }

    @Test
    public void shouldResolveWhenInitialisedWithValue() {
        LazyInstance<String> instance = new LazyInstance<>(DATA);

        assertTrue("LazyInstance initialised with data is not marked resolved.", instance.isResolved());
        assertEquals(DATA, instance.get());
    }

    @Test
    public void shouldResolveInstanceFromSupplierOnce() {
        LazyInstance<String> instance = new LazyInstance<>(supplier);

        assertFalse(instance.isResolved());
        verify(supplier, never()).get();

        assertEquals(DATA, instance.get());
        assertEquals(DATA, instance.get());
        assertTrue(instance.isResolved());

        verify(supplier, times(1)).get();
    }
}
