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

import nl.salp.warcraft4j.util.DataTypeUtil;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;

import java.math.BigInteger;

/**
 * Bob Jenkins' lookup2 hash implementation.
 *
 * @author Barre Dijkstra
 */
public class Lookup2Hash implements Hash {
    /** Bit mask for limiting to 4 byte values. */
    private static final long BITMASK_4BYTE_VALUE = 0xFFFFFFFFL;
    /** The "golden ratio". */
    private static final long GOLDEN_RATIO = 0x09e3779b9L;

    /**
     * Hash mix the current values.
     */
    private void mixHash(State state) {
        state.a = mix(state.a, state.b, state.c, state.c >> 13);
        state.b = mix(state.b, state.c, state.a, leftShift(state.a, 8));
        state.c = mix(state.c, state.a, state.b, state.b >> 13);
        state.a = mix(state.a, state.b, state.c, state.c >> 12);
        state.b = mix(state.b, state.c, state.a, leftShift(state.a, 16));
        state.c = mix(state.c, state.a, state.b, state.b >> 5);
        state.a = mix(state.a, state.b, state.c, state.c >> 3);
        state.b = mix(state.b, state.c, state.a, leftShift(state.a, 10));
        state.c = mix(state.c, state.a, state.b, state.b >> 15);
    }

    /**
     * Helper method for mixing a single value to help {@link Lookup2Hash#mixHash(State)} readability.
     *
     * @param target The value to mix.
     * @param first  The first value to mix with.
     * @param second The second value to mix with.
     * @param third  The third value to mix with (the result of a shift of the 2nd value).
     *
     * @return The mixed value.
     */
    private long mix(long target, long first, long second, long third) {
        long result = target;
        result = subtract(result, first);
        result = subtract(result, second);
        result = xor(result, third);
        return result;
    }

    /**
     * Parse a byte into an unsigned long.
     *
     * @param b The byte to convert.
     *
     * @return The byte as unsigned long.
     */
    private long byteToLong(byte b) {
        long val = b & 0x7F;
        if ((b & 0x80) != 0) {
            val += 128;
        }
        return val;
    }

    /**
     * Add two long values and limit the result to 4 bytes.
     *
     * @param value The value to add to.
     * @param add   The value to add.
     *
     * @return The result, as a 4 byte value.
     */
    private long add(long value, long add) {
        return (value + add) & BITMASK_4BYTE_VALUE;
    }

    /**
     * Subtract two long values and limit the result to 4 bytes.
     *
     * @param value The value to subtract from.
     * @param sub   The value to subtract.
     *
     * @return The result, as a 4 byte value.
     */
    private long subtract(long value, long sub) {
        return (value - sub) & BITMASK_4BYTE_VALUE;
    }

    /**
     * Xor two long values and limit the result to 4 bytes.
     *
     * @param value The value to xor.
     * @param xor   The value to xor with.
     *
     * @return The result, as a 4 byte value.
     */
    private long xor(long value, long xor) {
        return (value ^ xor) & BITMASK_4BYTE_VALUE;
    }

    /**
     * Left shift a long value and limit the result to 4 bytes.
     *
     * @param value The value to shift.
     * @param shift The number of positions to shift.
     *
     * @return The result, as a 4 byte value.
     */
    private long leftShift(long value, int shift) {
        return (value << shift) & BITMASK_4BYTE_VALUE;
    }

    /**
     * Get a long value (4 bytes) from the datstate.a, starting at the offset.
     *
     * @param data   The data to get the long from.
     * @param offset The offset to start at.
     *
     * @return The long value of the 4 bytes.
     */
    private long getLong(byte[] data, int offset) {
        return (byteToLong(data[offset + 0])
                + (byteToLong(data[offset + 1]) << 8)
                + (byteToLong(data[offset + 2]) << 16)
                + (byteToLong(data[offset + 3]) << 24));
    }

    @Override
    public byte[] hash(byte[] data) {
        BigInteger hash = BigInteger.valueOf(jenkinsHash(data));
        return DataTypeUtil.trim(hash.toByteArray());

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

    /**
     * Calculate the hash for the provided datstate.a, allowing for continuation of a previous hash (in case of segmented data).
     *
     * @param data             The data to hash.
     * @param continuationHash The hash to continue from or {@code 0} if the hash is a new hash.
     *
     * @return The hash.
     */
    public long jenkinsHash(byte[] data, long continuationHash) {
        State state = new State(GOLDEN_RATIO, GOLDEN_RATIO, continuationHash);

        int pos = 0;
        int len;
        for (len = data.length; len >= 12; len -= 12) {
            state.a = add(state.a, getLong(data, pos));
            state.b = add(state.b, getLong(data, pos + 4));
            state.c = add(state.c, getLong(data, pos + 8));
            mixHash(state);
            pos += 12;
        }

        state.c += data.length;

        switch (len) {
            case 11:
                state.c = add(state.c, leftShift(byteToLong(data[pos + 10]), 24));
            case 10:
                state.c = add(state.c, leftShift(byteToLong(data[pos + 9]), 16));
            case 9:
                state.c = add(state.c, leftShift(byteToLong(data[pos + 8]), 8));
                // the first byte of c is reserved for the length
            case 8:
                state.b = add(state.b, leftShift(byteToLong(data[pos + 7]), 24));
            case 7:
                state.b = add(state.b, leftShift(byteToLong(data[pos + 6]), 16));
            case 6:
                state.b = add(state.b, leftShift(byteToLong(data[pos + 5]), 8));
            case 5:
                state.b = add(state.b, byteToLong(data[pos + 4]));
            case 4:
                state.a = add(state.a, leftShift(byteToLong(data[pos + 3]), 24));
            case 3:
                state.a = add(state.a, leftShift(byteToLong(data[pos + 2]), 16));
            case 2:
                state.a = add(state.a, leftShift(byteToLong(data[pos + 1]), 8));
            case 1:
                state.a = add(state.a, byteToLong(data[pos + 0]));
            default:
                // noop
        }
        mixHash(state);

        return state.c;
    }

    /**
     * Calculate a new hash of the provided data.
     *
     * @param data The data to create a hash for.
     *
     * @return The hash.
     */
    public long jenkinsHash(byte[] data) {
        return jenkinsHash(data, 0);
    }

    /**
     * TODO state holder as a quick fix to keep the implementation stateless until I have time to refactor the whole implementation.
     */
    private class State {
        public long a;
        public long b;
        public long c;

        public State(long a, long b, long c) {
            this.a = a;
            this.b = b;
            this.c = c;
        }
    }
}
