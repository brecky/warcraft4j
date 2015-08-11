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
package nl.salp.warcraft4j;

import java.util.Arrays;
import java.util.Optional;

import static nl.salp.warcraft4j.DataTypeUtil.byteArrayToHexString;
import static org.apache.commons.lang3.ArrayUtils.subarray;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Checksum {
    private final byte[] checksum;
    private final int hash;

    public Checksum(byte[] checksum) {
        this.checksum = Optional.ofNullable(checksum).filter(c -> c.length > 0).orElseThrow(() -> new IllegalArgumentException("Can't create a checksum from an empty array"));
        this.hash = DataTypeUtil.hash(checksum);
    }

    public byte[] getChecksum() {
        return checksum;
    }

    public int length() {
        return checksum.length;
    }

    public Checksum trim(int length) {
        byte[] trimmed = checksum;
        if (length < checksum.length) {
            trimmed = subarray(checksum, 0, length);
        }
        return new Checksum(trimmed);
    }

    public String toHexString() {
        return byteArrayToHexString(checksum);
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && Checksum.class.isAssignableFrom(obj.getClass())) {
            eq = Arrays.equals(checksum, ((Checksum) obj).checksum);
        }
        return eq;
    }

    @Override
    public String toString() {
        return byteArrayToHexString(checksum);
    }
}
