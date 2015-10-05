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

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Optional;

/**
 * Utility methods for data types.
 *
 * @author Barre Dijkstra
 */
public final class DataTypeUtil {
    /**
     * Private constructor to prevent instantiation.
     */
    private DataTypeUtil() {
    }

    /**
     * Convert a hex string to a byte[].
     *
     * @param hexString The hex string to convert.
     *
     * @return The byte[].
     */
    public static byte[] hexStringToByteArray(String hexString) {
        byte[] array = new byte[hexString.length() / 2];
        for (int i = 0; i < array.length; ++i) {
            int pos = i * 2;
            int value = Integer.parseInt(hexString.substring(pos, pos + 2), 16);
            array[i] = (byte) (value & 255);
        }
        return array;
    }

    /**
     * Convert a byte[] to a hex string.
     *
     * @param data The data to convert.
     *
     * @return The hex string.
     */
    public static String byteArrayToHexString(byte[] data) {
        return Hex.encodeHexString(data);
    }


    /**
     * Get the size of the values in a short[] in bytes.
     *
     * @param values The values to get the size for.
     *
     * @return The size in bytes.
     */
    public static int getSizeInBytes(short... values) {
        return values.length << 1;
    }

    /**
     * Get the size of the values in an int[] in bytes.
     *
     * @param values The values to get the size for.
     *
     * @return The size in bytes.
     */
    public static int getSizeInBytes(int... values) {
        return values.length << 2;
    }

    /**
     * Get the size of the values in a long[] in bytes.
     *
     * @param values The values to get the size for.
     *
     * @return The size in bytes.
     */
    public static int getSizeInBytes(long... values) {
        return values.length << 3;
    }

    /**
     * Create a fixed-size 32-bit (4 byte) byte[] from an int value using a big endian byte order.
     *
     * @param value The value.
     *
     * @return The byte[].
     */
    public static byte[] toByteArray(int value) {
        return toByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Create a fixed-size 32-bit (4 byte) byte[] from an int value using a byte order.
     *
     * @param value     The value.
     * @param byteOrder The byte order, using {@code ByteOrder#BIG_ENDIAN} when the byte order is {@code null}.
     *
     * @return The byte[].
     */
    public static byte[] toByteArray(int value, ByteOrder byteOrder) {
        byte[] intArray = new byte[Integer.BYTES];
        ByteBuffer.wrap(intArray)
                .order(Optional.ofNullable(byteOrder)
                        .orElse(ByteOrder.BIG_ENDIAN))
                .putInt(value);
        return intArray;
    }


    /**
     * Create a fixed size 64-bit (8 byte) byte[] from the given long value using a big endian byte order.
     *
     * @param value The value.
     *
     * @return The byte[].
     */
    public static byte[] toByteArray(long value) {
        return toByteArray(value, ByteOrder.BIG_ENDIAN);
    }

    /**
     * Create a fixed-size 64-bit (8 byte) byte[] from an int value using a byte order.
     *
     * @param value     The value.
     * @param byteOrder The byte order, using {@code ByteOrder#BIG_ENDIAN} when the byte order is {@code null}.
     *
     * @return The byte[].
     */
    public static byte[] toByteArray(long value, ByteOrder byteOrder) {
        byte[] longArray = new byte[Long.BYTES];
        ByteBuffer.wrap(longArray)
                .order(Optional.ofNullable(byteOrder)
                        .orElse(ByteOrder.BIG_ENDIAN))
                .putLong(value);
        return longArray;
    }

    /**
     * Generate a hash for a byte array using a 32-bit FNV-1a hash.
     *
     * @param data The array to hash.
     *
     * @return The hash for the data.
     */
    public static int hash(byte[] data) {
        if (data == null || data.length == 0) {
            return 0;
        }
        long hash = 2166136261L;
        for (byte b : data) {
            hash ^= b;
            hash *= 16777619L;
        }
        return (int) hash;
    }

    /**
     * Convert the first 4 bytes of an array to a big endian int.
     *
     * @param value The byte array.
     *
     * @return The int represented by the array.
     */
    public static int toInt(byte[] value) {
        if (value == null) {
            return 0;
        }
        int val = 0;
        if (value.length >= 1) {
            val |= ((value[0] & 0xFF) << 24);
        }
        if (value.length >= 2) {
            val |= ((value[1] & 0xFF) << 16);
        }
        if (value.length >= 3) {
            val |= ((value[2] & 0xFF) << 8);
        }
        if (value.length >= 4) {
            val |= ((value[3] & 0xFF));
        }
        return val;
    }

    /**
     * Convert the first 8 bytes of an array to a big endian long.
     *
     * @param value The byte array.
     *
     * @return The long represented by the array.
     */
    public static long toLong(byte[] value) {
        if (value == null) {
            return 0;
        }
        int b = toInt(ArrayUtils.subarray(value, 0, value.length / 2));
        int a = toInt(ArrayUtils.subarray(value, value.length / 2, value.length));

        return ((((long) b) << 32) | ((long) a & 0xFFFFFFFFL));
    }

    /**
     * Convert the first 4 bytes of an array to a int.
     *
     * @param value     The byte array.
     * @param byteOrder The byte order of the int.
     *
     * @return The int represented by the array.
     */
    public static int toInt(byte[] value, ByteOrder byteOrder) {
        return ByteBuffer.wrap(value).order(byteOrder).getInt();
    }

    /**
     * Trim the byte array by removing the leading 0's.
     * <p>
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
