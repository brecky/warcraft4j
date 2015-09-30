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

import java.util.Arrays;
import java.util.Optional;

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
        this.checksum = Optional.ofNullable(checksum).filter(c -> c.length > 0).orElseThrow(() -> new IllegalArgumentException("Can't create a checksum from an empty array"));
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
     * Trim the checksum to a length.
     *
     * @param length The length in bytes.
     *
     * @return A new checksum instance with a maximum of the provided length.
     */
    public Checksum trim(int length) {
        byte[] trimmed = checksum;
        if (length < checksum.length) {
            trimmed = subarray(checksum, 0, length);
        }
        return new Checksum(trimmed);
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