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

import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link DataTypeUtil}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.util.DataTypeUtil
 */
public class DataTypeUtilTest {

    @Test
    public void shouldDecodeHexStringToByteArray() {
        assertArrayEquals(new byte[] {0, 4, 10, 16, 32, 64, 127}, DataTypeUtil.hexStringToByteArray("00040a1020407f"));
    }

    @Test
    public void shouldEncodeByteArrayToHexString() {
        assertEquals("00040a1020407f", DataTypeUtil.byteArrayToHexString(new byte[] {0, 4, 10, 16, 32, 64, 127}));
    }

    @Test
    public void shouldCalculateSizeShortArray() {
        assertEquals(2, DataTypeUtil.getSizeInBytes((short) 1));
        assertEquals(10, DataTypeUtil.getSizeInBytes((short) 1, (short) 2, (short) 3, (short) 4, (short) 5));
        assertEquals(0, DataTypeUtil.getSizeInBytes(new short[0]));
    }

    @Test
    public void shouldCalculateSizeIntArray() {
        assertEquals(4, DataTypeUtil.getSizeInBytes(1));
        assertEquals(20, DataTypeUtil.getSizeInBytes(1, 2, 3, 4, 5));
        assertEquals(0, DataTypeUtil.getSizeInBytes(new int[0]));
    }

    @Test
    public void shouldCalculateSizeLongArray() {
        assertEquals(8, DataTypeUtil.getSizeInBytes(1L));
        assertEquals(40, DataTypeUtil.getSizeInBytes(1L, 2L, 3L, 4L, 5L));
        assertEquals(0, DataTypeUtil.getSizeInBytes(new long[0]));
    }

    @Test
    public void shouldCreateByteArrayFromInt() {
        int value = Short.MAX_VALUE + 10;
        byte[] intArray = new byte[Integer.BYTES];
        ByteBuffer.wrap(intArray).putInt(value);

        assertArrayEquals(intArray, DataTypeUtil.toByteArray(value));
    }

    @Test
    public void shouldCreateByteArrayFromLong() {
        long value = (Integer.MAX_VALUE) + 10L;
        byte[] longArray = new byte[Long.BYTES];
        ByteBuffer.wrap(longArray).asLongBuffer().put(value);
        assertArrayEquals(longArray, DataTypeUtil.toByteArray(value));
    }
}
