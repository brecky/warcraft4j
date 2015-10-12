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

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Checksum}.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.util.Checksum
 */
public class ChecksumTest {
    /** The value of the checksum data. */
    private static final byte[] DATA = "checksumTestData".getBytes(StandardCharsets.US_ASCII);
    /** The length of the checksum data. */
    private static final int DATA_LENGTH = DATA.length;
    /** The length to trim the checksum data to. */
    private static final int TRIMMED_LENGTH = DATA_LENGTH / 2;
    /** The value of the checksum data trimmed using a big endian byte order. */
    private static final byte[] TRIMMED_BIG_ENDIAN = ArrayUtils.subarray(DATA, 0, TRIMMED_LENGTH);
    /** The value of the checksum data trimmed using a little endian byte order. */
    private static final byte[] TRIMMED_LITTLE_ENDIAN = ArrayUtils.subarray(DATA, DATA.length - TRIMMED_LENGTH, DATA.length);

    /** The checksum being tested. */
    private Checksum checksum;

    @Before
    public void setUp() {
        this.checksum = new Checksum(DATA);
    }

    @Test
    public void shouldCreateChecksumFromData() {
        assertArrayEquals("Checksum data is not properly set.", DATA, checksum.getChecksum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateChecksumFromNullData() {
        new Checksum(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotCreateChecksumFromEmptyData() {
        new Checksum(new byte[0]);
    }

    @Test
    public void shouldUseDataHashAsObjectHash() {
        assertEquals(DataTypeUtil.hash(DATA), checksum.hashCode());
    }

    @Test
    public void shouldEncodeDataAsHexString() {
        assertEquals(DataTypeUtil.byteArrayToHexString(DATA), checksum.toHexString());
    }

    @Test
    public void shouldUseHexStringAsToString() {
        assertEquals(DataTypeUtil.byteArrayToHexString(DATA), checksum.toString());
    }

    @Test
    public void shouldNotTrimToEqualOrLargerSize() {
        Checksum trimmed = checksum.trim(DATA_LENGTH + 1);

        assertSame("New instance created for trimmed data with a larger size then the checksum", checksum, trimmed);
        assertArrayEquals("Data trimmed for a size larger then the checksum", DATA, trimmed.getChecksum());
    }

    @Test
    public void shouldNotTrimToEqualSize() {
        Checksum trimmed = checksum.trim(DATA_LENGTH);

        assertSame("New instance created for trimmed data with an equal size to the checksum", checksum, trimmed);
        assertArrayEquals("Data trimmed for a size equal to the checksum", DATA, trimmed.getChecksum());
    }

    @Test
    public void shouldTrimToSizeUsingDefaultEndianess() {
        Checksum trimmed = checksum.trim(TRIMMED_LENGTH);

        assertEquals("Invalid sized checksum for default endian trimmed data", TRIMMED_LENGTH, trimmed.length());
        assertNotSame("Same instance returned for default endian trimmed data", checksum, trimmed);
        assertArrayEquals("Default endian data not trimmed to size", TRIMMED_BIG_ENDIAN, trimmed.getChecksum());
    }

    @Test
    public void shouldTrimToSizeUsingBigEndianess() {
        Checksum trimmed = checksum.trim(TRIMMED_LENGTH, ByteOrder.BIG_ENDIAN);

        assertEquals("Invalid sized checksum for big endian trimmed data", TRIMMED_LENGTH, trimmed.length());
        assertNotSame("Same instance returned for big endian trimmed data", checksum, trimmed);
        assertArrayEquals("Big endian data not trimmed to size", TRIMMED_BIG_ENDIAN, trimmed.getChecksum());
    }


    @Test
    public void shouldTrimToSizeWithSmallEndianess() {
        Checksum trimmed = checksum.trim(TRIMMED_LENGTH, ByteOrder.LITTLE_ENDIAN);

        assertEquals("Invalid sized checksum for little endian trimmed data", TRIMMED_LENGTH, trimmed.length());
        assertNotSame("Same instance returned for little endian trimmed data", checksum, trimmed);
        assertArrayEquals("Little endian data not trimmed to size", TRIMMED_LITTLE_ENDIAN, trimmed.getChecksum());
    }

    @Test
    public void shouldTrimToSizeUsingDefaultEndianessForNullEndianess() {
        Checksum trimmed = checksum.trim(TRIMMED_LENGTH);

        assertEquals("Invalid sized checksum for null endian trimmed data", TRIMMED_LENGTH, trimmed.length());
        assertNotSame("Same instance returned for null endian trimmed data", checksum, trimmed);
        assertArrayEquals("Null endian data not trimmed to size", TRIMMED_BIG_ENDIAN, trimmed.getChecksum());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTrimToNegativeSize() {
        checksum.trim(-1);
    }

    @Test
    public void shouldEqualOnChecksumData() {
        Checksum other = new Checksum(DATA);

        assertTrue("Checksums with equal data are not reported equal.", checksum.equals(other));
    }

    @Test
    public void shouldNotEqualDifferetTypes() {
        assertFalse("Checksum equal to the unwrapped checksum data.", checksum.equals(DATA));
        assertFalse("Checksum equal to null.", checksum.equals(null));
        assertFalse("Checksum equal to String.", checksum.equals("someString"));
    }
}