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

package nl.salp.warcraft4j.hash;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.math.BigInteger;
import java.util.Arrays;

/**
 * Fowler-Noll-Vo hash implementations (FNV-1 and FNV-1a).
 *
 * @author Barre Dijkstra
 */
public abstract class FowlerNollVoHash implements Hash {
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
    protected final BigInteger offset;
    /** The prime to hash with. */
    protected final BigInteger prime;
    /** The mod to hash with. */
    protected final BigInteger mod;

    /**
     * Create a new FowlerNollVoHash.
     *
     * @param offset The hash offset.
     * @param prime  The prime to hash with.
     * @param mod    The mod to hash with.
     */
    protected FowlerNollVoHash(BigInteger offset, BigInteger prime, BigInteger mod) {
        this.offset = offset;
        this.prime = prime;
        this.mod = mod;
    }

    @Override
    public byte[] hash(String data) {
        return hash(StringUtils.getBytesUtf8(data));
    }

    @Override
    public String hashHexString(byte[] data) {
        return Hex.encodeHexString(hash(data));
    }

    @Override
    public String hashHexString(String data) {
        return hashHexString(StringUtils.getBytesUtf8(data));
    }

    protected static byte[] sizeHashArray(byte[] hash, int hashSizeBits) {
        int size = hashSizeBits / 8;
        byte[] resized;
        if (size == hash.length) {
            resized = hash;
        } else if (size > hash.length) {
            // TODO Optimise.
            resized = new byte[size - hash.length];
            Arrays.fill(resized, 0, size - hash.length, (byte) 0x0);
            ArrayUtils.addAll(resized, hash);
        } else {
            // Truncate based on big endian ordering
            resized = ArrayUtils.subarray(hash, hash.length - size, hash.length);
        }
        return resized;
    }

    /**
     * Create a Fowler-Noll-Vo hash instance that returns a 32-bit hash calculated with FNV-1.
     *
     * @return The 32-bit FNV-1 hash instance.
     */
    public static FowlerNollVoHash hash32fnv1() {
        return new Fnv1Hash(32, OFFSET_32, PRIME_32, MOD_32);
    }

    /**
     * Create a Fowler-Noll-Vo hash instance that returns a 64-bit hash calculated with FNV-1.
     *
     * @return The 64-bit FNV-1 hash instance.
     */
    public static FowlerNollVoHash hash64fnv1() {
        return new Fnv1Hash(64, OFFSET_64, PRIME_64, MOD_64);
    }

    /**
     * Create a Fowler-Noll-Vo hash instance that returns a 32-bit hash calculated with FNV-1a.
     *
     * @return The 32-bit FNV-1a hash instance.
     */
    public static FowlerNollVoHash hash32fnv1a() {
        return new Fnv1aHash(32, OFFSET_32, PRIME_32, MOD_32);
    }

    /**
     * Create a Fowler-Noll-Vo hash instance that returns a 64-bit hash calculated with FNV-1a.
     *
     * @return The 64-bit FNV-1a hash instance.
     */
    public static FowlerNollVoHash hash64fnv1a() {
        return new Fnv1aHash(64, OFFSET_64, PRIME_64, MOD_64);
    }

    /**
     * {@link FowlerNollVoHash} implementation for FNV-1.
     */
    private static class Fnv1Hash extends FowlerNollVoHash {
        private final int hashSizeBits;
        /**
         * Create a new Fnv1Hash.
         *
         * @param offset The hash offset.
         * @param prime  The prime to hash with.
         * @param mod    The mod to hash with.
         */
        protected Fnv1Hash(int hashSizeBits, BigInteger offset, BigInteger prime, BigInteger mod) {
            super(offset, prime, mod);
            this.hashSizeBits = hashSizeBits;
        }

        @Override
        public byte[] hash(byte[] data) {
            BigInteger hash = offset;
            for (byte b : data) {
                hash = hash.multiply(prime).mod(mod);
                hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
            }
            return sizeHashArray(hash.toByteArray(), hashSizeBits);
        }
    }

    /**
     * {@link FowlerNollVoHash} implementation for FNV-1a.
     */
    private static class Fnv1aHash extends FowlerNollVoHash {
        private final int hashSizeBits;
        /**
         * Create a new Fnv1aHash.
         *
         * @param offset The hash offset.
         * @param prime  The prime to hash with.
         * @param mod    The mod to hash with.
         */
        protected Fnv1aHash(int hashSizeBits, BigInteger offset, BigInteger prime, BigInteger mod) {
            super(offset, prime, mod);
            this.hashSizeBits = hashSizeBits;
        }

        @Override
        public byte[] hash(byte[] data) {
            BigInteger hash = offset;
            for (byte b : data) {
                hash = hash.xor(BigInteger.valueOf((int) b & 0xff));
                hash = hash.multiply(prime).mod(mod);
            }
            return sizeHashArray(hash.toByteArray(), hashSizeBits);
        }
    }

}
