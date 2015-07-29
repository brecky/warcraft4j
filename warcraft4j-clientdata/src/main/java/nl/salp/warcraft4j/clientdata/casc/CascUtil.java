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

import nl.salp.warcraft4j.clientdata.util.hash.JenkinsHash;

import java.nio.charset.StandardCharsets;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public final class CascUtil {
    private static final long FNV_PRIME32 = 16777619L;
    private static final long FNV_OFFSET32 = 2166136261L;

    private CascUtil() {
    }

    /**
     * Generate a simple 32-bit hash for the byte array (using 32-bit FNV-1a).
     *
     * @param value The value to hash.
     *
     * @return The hash.
     */
    public static int hash(byte[] value) {
        if (value == null || value.length == 0) {
            return 0;
        }
        long hash = FNV_OFFSET32;
        for (byte b : value) {
            hash ^= b;
            hash *= FNV_PRIME32;
        }
        return (int) (hash & 0xFFFFFFFF);
    }

    public static long hashFilename(String filename) {
        if (isEmpty(filename)) {
            return 0;
        }
        byte[] data = filename.trim().replace('/', '\\').toUpperCase().getBytes(StandardCharsets.US_ASCII);
        return JenkinsHash.hashLittle2(data, data.length);
    }
}
