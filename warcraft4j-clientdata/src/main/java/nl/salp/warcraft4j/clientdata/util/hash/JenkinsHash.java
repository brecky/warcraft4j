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

package nl.salp.warcraft4j.clientdata.util.hash;

import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * Jenkins hash implementation.
 *
 * @author Barre Dijkstra
 */
public class JenkinsHash implements Hash {
    /** Bit mask for limiting to 4 byte values. */
    private static final long BITMASK_4BYTE_VALUE = 0xFFFFFFFFL;
    /** The "golden ratio". */
    private static final long GOLDEN_RATIO = 0x09e3779b9L;

    private long a;
    private long b;
    private long c;

    /**
     * Hash mix the current values.
     */
    private void mixHash() {
        a = mix(a, b, c, c >> 13);
        b = mix(b, c, a, leftShift(a, 8));
        c = mix(c, a, b, b >> 13);
        a = mix(a, b, c, c >> 12);
        b = mix(b, c, a, leftShift(a, 16));
        c = mix(c, a, b, b >> 5);
        a = mix(a, b, c, c >> 3);
        b = mix(b, c, a, leftShift(a, 10));
        c = mix(c, a, b, b >> 15);
    }

    /**
     * Helper method for mixing a single value to help {@link JenkinsHash#mixHash()} readability.
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
     * Get a long value (4 bytes) from the data, starting at the offset.
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
        return hash.toByteArray();

    }

    public long jenkinsHash(byte[] data, long continuationHash) {
        a = GOLDEN_RATIO;
        b = GOLDEN_RATIO;
        c = continuationHash;

        int pos = 0;
        int len;
        for (len = data.length; len >= 12; len -= 12) {
            a = add(a, getLong(data, pos));
            b = add(b, getLong(data, pos + 4));
            c = add(c, getLong(data, pos + 8));
            mixHash();
            pos += 12;
        }

        c += data.length;

        switch (len) {
            case 11:
                c = add(c, leftShift(byteToLong(data[pos + 10]), 24));
            case 10:
                c = add(c, leftShift(byteToLong(data[pos + 9]), 16));
            case 9:
                c = add(c, leftShift(byteToLong(data[pos + 8]), 8));
                // the first byte of c is reserved for the length
            case 8:
                b = add(b, leftShift(byteToLong(data[pos + 7]), 24));
            case 7:
                b = add(b, leftShift(byteToLong(data[pos + 6]), 16));
            case 6:
                b = add(b, leftShift(byteToLong(data[pos + 5]), 8));
            case 5:
                b = add(b, byteToLong(data[pos + 4]));
            case 4:
                a = add(a, leftShift(byteToLong(data[pos + 3]), 24));
            case 3:
                a = add(a, leftShift(byteToLong(data[pos + 2]), 16));
            case 2:
                a = add(a, leftShift(byteToLong(data[pos + 1]), 8));
            case 1:
                a = add(a, byteToLong(data[pos + 0]));
            default:
                // noop
        }
        mixHash();

        return c;
    }

    public long jenkinsHash(byte[] data) {
        return jenkinsHash(data, 0);
    }

    /**
     * Hash the data using the 'one-at-a-time' hashing described in the Jenkins' hashing paper.
     *
     * @param data The data.
     *
     * @return The hash.
     */
    public byte[] oneAtATimeHash(byte[] data) {
        int hash = 0;
        for (byte b : data) {
            hash += b;
            hash += (hash << 10);
            hash ^= (hash >> 6);
        }
        hash += (hash << 3);
        hash ^= (hash >> 11);
        hash += (hash << 15);
        return ByteBuffer.allocate(4).putInt(hash).array();
    }
}
