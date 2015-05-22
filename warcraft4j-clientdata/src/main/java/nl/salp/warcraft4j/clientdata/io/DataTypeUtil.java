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

package nl.salp.warcraft4j.clientdata.io;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.Arrays;

/**
 * Utility methods for data types.
 *
 * @author Barre Dijkstra
 */
public class DataTypeUtil {
    /**
     * Private constructor to prevent instantiation.
     */
    private DataTypeUtil() {
    }

    /**
     * Create a 4-element byte[] (32bit) from the given int value.
     *
     * @param value The value.
     *
     * @return The byte[].
     */
    public static byte[] toByteArray(int value) {
        return new byte[]{
                (byte) (value >>> 24),
                (byte) (value >>> 16),
                (byte) (value >>> 8),
                (byte) (value)
        };
    }

    /**
     * Create a 8-element byte[] (64bit) from the given long value.
     *
     * @param value The value.
     *
     * @return The byte[].
     */
    public static byte[] toByteArray(long value) {
        return BigInteger.valueOf(value).toByteArray();
    }

    /**
     * Trim the byte array by removing the leading 0's.
     * <p/>
     * This function is mostly useful in combination with functions like {@code BigInteger#toByteArray()} which prepends with a signing byte (0x00 in case of postive numbers).
     *
     * @param data The data to trim.
     *
     * @return The trimmed data.
     */
    public static byte[] trim(byte[] data) {
        int idx = 0;
        while (idx < data.length) {
            if (data[idx] != 0) {
                break;
            }
            idx++;
        }
        byte[] trimmedData;
        if (idx > 0 && idx < data.length) {
            trimmedData = Arrays.copyOfRange(data, idx, data.length);
        } else {
            trimmedData = data;
        }
        return trimmedData;
    }

    /**
     * Get the average number of bytes a character can consist of in the provided charset.
     *
     * @param charset The character set.
     *
     * @return The maximum number of bytes per character.
     */
    public static int getAverageBytesPerCharacter(Charset charset) {
        int avg = 0;
        if (charset != null) {
            // TODO Determine whether it should be rounded or cut off.
            avg = Math.round(charset.newEncoder().averageBytesPerChar());
        }
        return avg;
    }

    /**
     * Get the maximum number of bytes a character can consist of in the provided charset.
     *
     * @param charset The character set.
     *
     * @return The maximum number of bytes per character.
     */
    public static int getMaximumBytesPerCharacter(Charset charset) {
        int max = 0;
        if (charset != null) {
            // TODO Determine whether it should be rounded or cut off.
            max = Math.round(charset.newEncoder().maxBytesPerChar());
        }
        return max;
    }
}
