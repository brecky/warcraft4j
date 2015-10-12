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

import nl.salp.warcraft4j.util.DataTypeUtil;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link FileKey}.
 *
 * @author Barre Dijkstra
 * @see FileKey
 */
public class FileKeyTest {
    private static final byte[] FILE_KEY = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8};
    private static final byte[] CHECKSUM = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};

    @Test
    public void shouldCreateInstanceForChecksum() {
        FileKey fileKey = new FileKey(FILE_KEY);
        byte[] data = fileKey.getFileKey();

        assertEquals(FileKey.FILEKEY_LENGTH, data.length);
        assertArrayEquals(FILE_KEY, data);
    }

    @Test
    public void shouldTrimFileKeyForTooBigChecksum() {
        FileKey fileKey = new FileKey(CHECKSUM);
        byte[] data = fileKey.getFileKey();

        assertEquals(FileKey.FILEKEY_LENGTH, data.length);
        assertArrayEquals(FILE_KEY, data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForTooSmallChecksum() {
        byte[] checksum = new byte[FileKey.FILEKEY_LENGTH - 1];
        Arrays.fill(checksum, (byte) 0);
        new FileKey(checksum);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNullChecksum() {
        new FileKey(null);
    }

    @Test
    public void shouldStoreChecksumAndFileKey() {
        FileKey fileKey = new FileKey(CHECKSUM);

        assertArrayEquals(FILE_KEY, fileKey.getFileKey());
        assertArrayEquals(CHECKSUM, fileKey.getChecksum());
    }

    @Test
    public void shouldOnlyEqualFileKey() {
        byte[] otherChecksum = new byte[] {0, 1, 2, 3, 4, 5, 6, 7, 8, -1, -1, -1};
        FileKey otherFileKey = new FileKey(otherChecksum);
        FileKey fileKey = new FileKey(CHECKSUM);

        assertTrue(fileKey.equals(otherFileKey));
    }

    @Test
    public void shouldGenerateHashCodeFromFileKey() {
        int fileKeyHash = DataTypeUtil.hash(FILE_KEY);
        int checksumHash = DataTypeUtil.hash(CHECKSUM);
        FileKey fileKey = new FileKey(CHECKSUM);

        assertNotEquals(checksumHash, fileKey.hashCode());
        assertEquals(fileKeyHash, fileKey.hashCode());
    }

    @Test
    public void shouldUseFileKeyForToString() {
        String fileKeyString = DataTypeUtil.byteArrayToHexString(FILE_KEY);
        String checksumString = DataTypeUtil.byteArrayToHexString(CHECKSUM);
        FileKey fileKey = new FileKey(CHECKSUM);

        assertNotEquals(checksumString, fileKey.toString());
        assertEquals(fileKeyString, fileKey.toString());
    }
}