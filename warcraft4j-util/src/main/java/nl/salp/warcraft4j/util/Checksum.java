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

import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Optional;

import static java.lang.String.format;
import static org.apache.commons.lang3.ArrayUtils.subarray;

/**
 * Generic checksum.
 *
 * @author Barre Dijkstra
 */
public class Checksum {
    /** The checksum value. */
    private final byte[] checksum;
    /** The hash. */
    private final int hash;

    /**
     * Create a new instance.
     *
     * @param checksum The checksum.
     *
     * @throws IllegalArgumentException When the provided checksum is empty.
     */
    public Checksum(byte[] checksum) throws IllegalArgumentException {
        this.checksum = Optional.ofNullable(checksum)
                .filter(c -> c.length > 0)
                .orElseThrow(() -> new IllegalArgumentException("Can't create a checksum from an empty array"));
        this.hash = DataTypeUtil.hash(checksum);
    }

    /**
     * Get the checksum value.
     *
     * @return The checksum.
     */
    public byte[] getChecksum() {
        return checksum;
    }

    /**
     * Get the length of the checksum in bytes.
     *
     * @return The length.
     */
    public int length() {
        return checksum.length;
    }

    /**
     * Trim the checksum to a length, resizing it (cutting off the right bytes) if the checksum is bigger then the provided length.
     *
     * @param length The length in bytes.
     *
     * @return A new checksum instance with a maximum of the provided length.
     *
     * @throws IllegalArgumentException When the length is {@code 0} or negative.
     */
    public Checksum trim(int length) throws IllegalArgumentException {
        return trim(length, ByteOrder.BIG_ENDIAN);
    }


    /**
     * Trim the checksum to a length, resizing it (using the byte order for cut-off) if the checksum is bigger then the provided length.
     *
     * @param length    The maximum length in bytes.
     * @param byteOrder The byte order to determine which side to cut off the bytes.
     *
     * @return A new checksum instance with checksum data or the current checksum instance or the current checksum instance when the current data is equal or smaller in length.
     *
     * @throws IllegalArgumentException When the length is {@code 0} or negative.
     */
    public Checksum trim(int length, ByteOrder byteOrder) throws IllegalArgumentException {
        Checksum instance;
        if (length < 1) {
            throw new IllegalArgumentException(format("Can't trim a %d byte checksum to %d bytes.", checksum.length, length));
        }
        if (length >= checksum.length) {
            instance = this;
        } else if (ByteOrder.LITTLE_ENDIAN.equals(byteOrder)) {
            instance = new Checksum(subarray(checksum, checksum.length - length, checksum.length));
        } else {
            instance = new Checksum(subarray(checksum, 0, length));
        }
        return instance;
    }


    /**
     * Get the hex string representation of the checksum.
     *
     * @return The hex string.
     */
    public String toHexString() {
        return DataTypeUtil.byteArrayToHexString(checksum);
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
        if (obj != null && Checksum.class.isAssignableFrom(obj.getClass())) {
            eq = Arrays.equals(checksum, ((Checksum) obj).checksum);
        }
        return eq;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return DataTypeUtil.byteArrayToHexString(checksum);
    }
}