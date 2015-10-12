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
package nl.salp.warcraft4j.util.unsafe;

import org.junit.Test;

import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link NativeMemorySegment}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.util.unsafe.NativeMemorySegment
 */
public class NativeMemorySegmentTest {
    private static final long SEGMENT_SIZE = 1024 * 1024;
    private static final long DATA_OFFSET = 10;

    @Test
    public void shouldAllocateMemory() throws Exception {
        try (NativeMemorySegment segment = new NativeMemorySegment(SEGMENT_SIZE)) {
            assertEquals(SEGMENT_SIZE, segment.getSize());
            assertTrue(segment.getAddress() > 0);
            assertFalse(segment.isClosed());
        }
    }

    @Test
    public void shouldReleaseMemory() throws Exception {
        NativeMemorySegment segment = null;
        try {
            segment = new NativeMemorySegment(SEGMENT_SIZE);
            assertEquals(SEGMENT_SIZE, segment.getSize());
            assertTrue(segment.getAddress() > 0);
            assertFalse(segment.isClosed());
        } finally {
            if (segment == null) {
                fail("Segment not initialised.");
            } else {
                segment.close();
                assertEquals(0, segment.getSize());
                assertEquals(-1, segment.getAddress());
                assertTrue(segment.isClosed());
            }
        }

    }

    @Test
    public void shouldResizeMemory() throws Exception {
        final long newSize = SEGMENT_SIZE * 2;
        try (NativeMemorySegment segment = new NativeMemorySegment(SEGMENT_SIZE)) {
            assertEquals(SEGMENT_SIZE, segment.getSize());

            segment.resize(newSize);

            assertEquals(newSize, segment.getSize());
            assertTrue(segment.getAddress() > 0);
        }
    }

    @Test
    public void shouldStoreByteArray() throws Exception {
        final byte[] data = "this is a value for testing.".getBytes(StandardCharsets.US_ASCII);

        try (NativeMemorySegment segment = new NativeMemorySegment(SEGMENT_SIZE)) {
            long dataAddress = segment.put(DATA_OFFSET, data);
            assertEquals(segment.getAddress() + DATA_OFFSET, dataAddress);

            byte[] storedData = segment.get(DATA_OFFSET, data.length);
            assertArrayEquals(data, storedData);

            byte storedByte = segment.get(DATA_OFFSET + 1);
            assertEquals(data[1], storedByte);
        }
    }

    @Test
    public void shouldAcceptStoringEmptyData() throws Exception {
        try (NativeMemorySegment segment = new NativeMemorySegment(SEGMENT_SIZE)) {
            long address = segment.put(DATA_OFFSET);
            assertEquals(segment.getAddress() + DATA_OFFSET, address);
        }
    }
}
