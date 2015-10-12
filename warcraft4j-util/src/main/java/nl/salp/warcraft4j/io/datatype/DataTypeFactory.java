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
package nl.salp.warcraft4j.io.datatype;

import java.nio.charset.Charset;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DataTypeFactory {
    /** Single 8-bit signed byte instance. */
    private static final DataType<Byte> BYTE = new ByteDataType();
    /** Single 8-bit unsigned byte instance wrapped in a 16-bit signed {@code short}. */
    private static final DataType<Short> UNSIGNED_BYTE = new UnsignedByteDataType();
    /** Single 16-bit signed short instance. */
    private static final DataType<Short> SHORT = new ShortDataType();
    /** Single 16-bit unsigned short instance wrapped in a 32-bit signed {@code integer}. */
    private static final DataType<Integer> UNSIGNED_SHORT = new UnsignedShortDataType();
    /** Single 32-bit signed integer instance. */
    private static final DataType<Integer> INTEGER = new IntegerDataType();
    /** Single 32-bit unsigned integer instance wrapped in a 64-bit signed {@code long}. */
    private static final DataType<Long> UNSIGNED_INTEGER = new UnsignedIntegerDataType();
    /** Single 64-bit signed long instance. */
    private static final DataType<Long> LONG = new LongDataType();
    /** Single 32-bit signed float instance. */
    private static final DataType<Float> FLOAT = new FloatDataType();
    /** Single 64-bit signed double instance. */
    private static final DataType<Double> DOUBLE = new DoubleDataType();
    /** Single boolean instance. */
    private static final DataType<Boolean> BOOLEAN = new BooleanDataType();
    /** Single terminated string instance. */
    private static final DataType<String> TERMINATED_STRING = new TerminatedStringDataType();
    /** Single String instance terminated by an EOL, {@code 0x0} or EOF. */
    private static final DataType<String> STRING_LINE = new StringLineDataType();

    /**
     * Get a {@link DataType} implementation for 32-bit signed integers.
     *
     * @return The data type.
     */
    public static DataType<Integer> getInteger() {
        return INTEGER;
    }

    /**
     * Get a {@link DataType} implementation for a 32-bit unsigned integer wrapped in a 64-bit signed long.
     *
     * @return The data type.
     */
    public static DataType<Long> getUnsignedInteger() {
        return UNSIGNED_INTEGER;
    }

    /**
     * Get a {@link DataType} implementation for terminated strings (zero-terminated or end of data) using the specified character set.
     *
     * @param charset The character set to use.
     *
     * @return The data type.
     */
    public static DataType<String> getTerminatedString(Charset charset) {
        return new TerminatedStringDataType(charset);
    }

    /**
     * Get a {@link DataType} implementation for 32-bit signed floats.
     *
     * @return The data type.
     */
    public static DataType<Float> getFloat() {
        return FLOAT;
    }

    /**
     * Get a {@link DataType} implementation for booleans.
     *
     * @return The data type.
     */
    public static DataType<Boolean> getBoolean() {
        return BOOLEAN;
    }

    /**
     * Get a {@link DataType} implementation for 8-bit signed bytes.
     *
     * @return The data type.
     */
    public static DataType<Byte> getByte() {
        return BYTE;
    }

    /**
     * Get a {@link DataType} implementation for a 8-bit unsigned byte wrapped in a 16-bit signed short.
     *
     * @return The data type.
     */
    public static DataType<Short> getUnsignedByte() {
        return UNSIGNED_BYTE;
    }

    /**
     * Get a {@link DataType} implementation for a native byte[].
     *
     * @param length The length of the array in bytes.
     *
     * @return The data type.
     */
    public static DataType<byte[]> getByteArray(int length) {
        return new ByteArrayDataType(length);
    }

    /**
     * Get a {@link DataType} implementation for fixed length strings using the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
     *
     * @param length The length of the string.
     *
     * @return The data type.
     */
    public static DataType<String> getFixedLengthString(int length) {
        return new FixedLengthStringDataType(length);
    }

    /**
     * Get a {@link DataType} implementation for fixed length strings using a specified character set.
     *
     * @param length  The length of the string.
     * @param charset The character set to use.
     *
     * @return The data type.
     */
    public static DataType<String> getFixedLengthString(int length, Charset charset) {
        return new FixedLengthStringDataType(length, charset);
    }

    /**
     * Get a {@link DataType} implementation for terminated strings (zero-terminated or end of data) using the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
     *
     * @return The data type.
     */
    public static DataType<String> getTerminatedString() {
        return TERMINATED_STRING;
    }

    /**
     * Get a {@link DataType} implementation for reading string lines (end-of-line or end of data) using the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
     *
     * @return The data type.
     */
    public static DataType<String> getStringLine() {
        return STRING_LINE;
    }

    /**
     * Get a {@link DataType} implementation for a 16-bit signed short.
     *
     * @return The data type.
     */
    public static DataType<Short> getShort() {
        return SHORT;
    }

    /**
     * Get a {@link DataType} implementation for a 16-bit unsigned short wrapped in a 32-bit signed integer.
     *
     * @return The data type.
     */
    public static DataType<Integer> getUnsignedShort() {
        return UNSIGNED_SHORT;
    }

    /**
     * Get a {@link DataType} implementation for a 64-bit signed long.
     *
     * @return The data type.
     */
    public static DataType<Long> getLong() {
        return LONG;
    }

    /**
     * Get a {@link DataType} implementation for a 64-bit signed double.
     *
     * @return The data type.
     */
    public static DataType<Double> getDouble() {
        return DOUBLE;
    }
}
