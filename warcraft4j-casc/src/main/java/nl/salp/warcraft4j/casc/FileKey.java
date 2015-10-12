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

import nl.salp.warcraft4j.util.Checksum;
import nl.salp.warcraft4j.util.DataTypeUtil;

import java.util.Arrays;

import static java.lang.String.format;
import static nl.salp.warcraft4j.util.DataTypeUtil.byteArrayToHexString;

/**
 * {@link Checksum} for file data segment keys.
 *
 * @author Barre Dijkstra
 * @see IndexEntry
 * @see EncodingEntry
 */
public class FileKey extends Checksum {
    /** The length of the checksum in bytes. */
    public static final int FILEKEY_LENGTH = 9;
    /** File key. */
    private final byte[] fileKey;
    /** The hash of the file key. */
    private final int hash;

    /**
     * Create a new file key instance.
     *
     * @param checksum The checksum to create the key from (can also be a 16-byte checksum).
     *
     * @throws IllegalArgumentException When the checksum is invalid.
     */
    public FileKey(byte[] checksum) throws IllegalArgumentException {
        super(checksum);
        if (getChecksum().length > FILEKEY_LENGTH) {
            fileKey = trim(FILEKEY_LENGTH).getChecksum();
        } else if (getChecksum().length == FILEKEY_LENGTH) {
            fileKey = checksum;
        } else {
            throw new IllegalArgumentException(format("Unable to create a 9 byte file key from a %d byte array.", checksum.length));
        }
        hash = DataTypeUtil.hash(fileKey);
    }

    /**
     * Get the 9-byte file key.
     *
     * @return The file key.
     */
    public byte[] getFileKey() {
        return fileKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && FileKey.class.isAssignableFrom(obj.getClass())) {
            eq = Arrays.equals(fileKey, ((FileKey) obj).fileKey);
        }
        return eq;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return byteArrayToHexString(fileKey);
    }
}