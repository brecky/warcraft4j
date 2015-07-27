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
package nl.salp.warcraft4j.clientdata.casc;

import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeUtil;

import java.util.Arrays;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class Checksum {
    private static final long FNV_PRIME32 = 16777619L;
    private static final long FNV_OFFSET32 = 2166136261L;
    private final byte[] checksum;
    private final int hash;

    public Checksum(byte[] checksum) {
        this.checksum = Optional.ofNullable(checksum).filter(c -> c.length > 0).orElseThrow(() -> new IllegalArgumentException("Can't create a checksum from an empty array"));
        this.hash = to32BitFnv1aHash(this.checksum);
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
            trimmed = Arrays.copyOf(checksum, length);
            // trimmed = Arrays.copyOfRange(checksum, 0, length);
        }
        return new Checksum(trimmed);
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
        return DataTypeUtil.byteArrayToHexString(checksum);
    }

    private static int to32BitFnv1aHash(byte[] value) {
        long hash = FNV_OFFSET32;
        for (byte b : value) {
            hash ^= b;
            hash *= FNV_PRIME32;
        }

        return (int) (hash & 0xFFFFFFFF);
    }
}
