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

package nl.salp.warcraft4j.wowclient.util;

import java.math.BigInteger;

/**
 * Fowler-Noll-Vo hash implementation.
 *
 * @author Barre Dijkstra
 */
public class FowlerNollVoHash {
    /** The offset for 32-bit hashes. */
    private static final BigInteger OFFSET_32 = new BigInteger("811c9dc5", 16);
    /** The prime for 32-bit hashes. */
    private static final BigInteger PRIME_32 = new BigInteger("01000193", 16);
    /** The mod for 32-bit hashes. */
    private static final BigInteger MOD_32 = new BigInteger("2").pow(32);
    /** The offset for 64-bit hashes. */
    private static final BigInteger OFFSET_64 = new BigInteger("cbf29ce484222325", 16);
    /** The prime for 64-bit hashes. */
    private static final BigInteger PRIME_64 = new BigInteger("100000001b3", 16);
    /** The mod for 64-bit hashes. */
    private static final BigInteger MOD_64 = new BigInteger("2").pow(64);

    /** The hash offset. */
    private final BigInteger offset;
    /** The prime to hash with. */
    private final BigInteger prime;
    /** The mod to hash with. */
    private final BigInteger mod;

    /**
     * Create a new FowlerNollVoHash.
     *
     * @param offset The hash offset.
     * @param prime  The prime to hash with.
     * @param mod    The mod to hash with.
     */
    public FowlerNollVoHash(BigInteger offset, BigInteger prime, BigInteger mod) {
        this.offset = offset;
        this.prime = prime;
        this.mod = mod;
    }

    /**
     * Hash using the fnv-1 algorithm.
     *
     * @param data The data to hash.
     *
     * @return The hash for the data.
     */
    public BigInteger fnv1(byte[] data) {
        BigInteger hash = offset;
        for (byte b : data) {
            hash = hash.multiply(prime).mod(mod);
            hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
        }
        return hash;
    }

    /**
     * Hash using the fnv-1a algorithm.
     *
     * @param data The data to hash.
     *
     * @return The hash for the data.
     */
    public BigInteger fnv1a(byte[] data) {
        BigInteger hash = offset;
        for (byte b : data) {
            hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
            hash = hash.multiply(prime).mod(mod);
        }
        return hash;
    }

    /**
     * Create a Fowler-Noll-Vo hash instance that returns a 32-bit hash.
     *
     * @return The 32-bit fnv-hash instance.
     */
    public static FowlerNollVoHash hash32() {
        return new FowlerNollVoHash(OFFSET_32, PRIME_32, MOD_32);
    }

    /**
     * Create a Fowler-Noll-Vo hash instance that returns a 64-bit hash.
     *
     * @return The 64-bit fnv-hash instance.
     */
    public static FowlerNollVoHash hash64() {
        return new FowlerNollVoHash(OFFSET_64, PRIME_64, MOD_64);
    }

}
