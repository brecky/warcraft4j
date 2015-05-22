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

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import static nl.salp.warcraft4j.clientdata.io.DataTypeUtil.getAverageBytesPerCharacter;

/**
 * Data type for reading various types of data types (with different byte ordering) via a {@link DataReader}.
 *
 * @author Barre Dijkstra
 */
public abstract class DataType<T> {
    /** The default character set. */
    private static final Charset DEFAULT_CHARACTERSET = StandardCharsets.US_ASCII;
    /** Single integer instance. */
    private static final DataType<Integer> INTEGER = new IntegerDataType();
    /** Single terminated string instance. */
    private static final DataType<String> TERMINATED_STRING = new TerminatedStringDataType();
    /** Single float instance. */
    private static final DataType<Float> FLOAT = new FloatDataType();
    /** Single boolean instance. */
    private static final DataType<Boolean> BOOLEAN = new BooleanDataType();
    /** Single byte instance. */
    private static final DataType<Byte> BYTE = new ByteDataType();
    /** Single short instance. */
    private static final DataType<Short> SHORT = new ShortDataType();
    /** Single long instance. */
    private static final DataType<Long> LONG = new LongDataType();
    /** Single double instance. */
    private static final DataType<Double> DOUBLE = new DoubleDataType();

    /**
     * Get a {@link DataType} implementation for integers.
     *
     * @return The data type.
     */
    public static DataType<Integer> getInteger() {
        return INTEGER;
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
     * Get a {@link DataType} implementation for floats.
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
     * Get a {@link DataType} implementation for bytes.
     *
     * @return The data type.
     */
    public static DataType<Byte> getByte() {
        return BYTE;
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
     * Get a {@link DataType} implementation for a short.
     *
     * @return The data type.
     */
    public static DataType<Short> getShort() {
        return SHORT;
    }

    /**
     * Get a {@link DataType} implementation for a long.
     *
     * @return The data type.
     */
    public static DataType<Long> getLong() {
        return LONG;
    }

    /**
     * Get a {@link DataType} implementation for a double.
     *
     * @return The data type.
     */
    public static DataType<Double> getDouble() {
        return DOUBLE;
    }

    /**
     * Create a new native array for the type with the given number of entries.
     *
     * @param entries The number of entries.
     *
     * @return The new array.
     *
     * @throws UnsupportedOperationException When the creation of an array is not supported (e.g. for array wrappers).
     */
    protected abstract T[] newArray(int entries) throws UnsupportedOperationException;

    /**
     * Get a native array type for the data type.
     *
     * @param entries the number of entries.
     *
     * @return The DataType that supports parsing to a native array of the type.
     *
     * @throws UnsupportedOperationException When the creation of a native array type is not supported (e.g. for array types).
     */
    public DataType<T[]> asArrayType(int entries) throws UnsupportedOperationException {
        return new ArrayWrapper<>(this, entries);
    }

    /**
     * Get the length of the data type in bytes.
     *
     * @return The length or {@code &lt;= 0} for variable length data types.
     *
     * @see #isVariableLength()
     */
    public abstract int getLength();

    /**
     * Check if the data type is a variable length data type.
     *
     * @return {@code true} if the data type is variable length.
     */
    public boolean isVariableLength() {
        return getLength() <= 0;
    }

    /**
     * Get the byte to act as a terminator byte for variable length data types.
     * <p>
     * This method may be overwritten by implementations.
     * </p>
     *
     * @return The terminator byte.
     */
    public byte getVariableLengthTerminator() {
        return 0x0;
    }

    /**
     * Get the default byte order for the data type.
     *
     * @return The default byte order.
     */
    public abstract ByteOrder getDefaultByteOrder();

    /**
     * Read the next value in the given data type from the given ByteBuffer.
     *
     * @param buffer The ByteBuffer.
     *
     * @return The next value.
     */
    public abstract T readNext(ByteBuffer buffer);

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * {@link DataType} implementation that wraps a {@link DataType} as a native array.
     *
     * @param <K> The type of the data type to wrap.
     */
    private static class ArrayWrapper<K> extends DataType<K[]> {
        /** The wrapped data type. */
        private final DataType<K> wrappedType;
        /** The number of entries for the array. */
        private final int arrayLength;

        /**
         * Create a new ArrayWrapper.
         *
         * @param wrappedType The data type to wrap as an array.
         * @param arrayLength The number of elements in the array.
         */
        public ArrayWrapper(DataType<K> wrappedType, int arrayLength) {
            this.wrappedType = wrappedType;
            this.arrayLength = arrayLength;
        }

        @Override
        protected K[][] newArray(int entries) {
            throw new UnsupportedOperationException("Creation of arrays from arrays is not supported.");
        }

        @Override
        public DataType<K[][]> asArrayType(int entries) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Creation of array an array data type from array and array data type is not supported.");
        }

        @Override
        public int getLength() {
            return wrappedType.getLength() * arrayLength;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return wrappedType.getDefaultByteOrder();
        }

        @Override
        public K[] readNext(ByteBuffer buffer) {
            K[] entries = wrappedType.newArray(arrayLength);
            for (int i = 0; i < arrayLength; i++) {
                entries[i] = wrappedType.readNext(buffer);
            }
            return entries;
        }
    }

    /**
     * {@link DataType} implementation for a zero-terminated string or a string till the end of the buffer.
     */
    private static class TerminatedStringDataType extends DataType<String> {
        /** The length of the terminated String in bytes, set to -1 to indicate a variable length. */
        private static final int LENGTH_BYTES = -1;
        /** The character to decode the String with. */
        private final Charset charset;
        /** The number of bytes per character. */
        private final int bytesPerChar;

        /**
         * Create a new TerminatedStringDataType with the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
         */
        public TerminatedStringDataType() {
            this(DEFAULT_CHARACTERSET);
        }

        /**
         * Create a new TerminatedStringDataType.
         *
         * @param charset The character set to use for decoding the characters.
         */
        public TerminatedStringDataType(Charset charset) {
            this.charset = charset;
            this.bytesPerChar = getAverageBytesPerCharacter(charset);
        }

        @Override
        protected String[] newArray(int entries) throws UnsupportedOperationException {
            return new String[entries];
        }

        @Override
        public int getLength() {
            return LENGTH_BYTES;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public String readNext(ByteBuffer buffer) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte c;
            while (buffer.hasRemaining() && (c = buffer.get()) != 0) {
                byteStream.write(c);
            }
            return new String(byteStream.toByteArray(), charset);
        }
    }

    /**
     * {@link DataType} implementation for a 32-bit signed integer.
     */
    private static class IntegerDataType extends DataType<Integer> {
        /** Size of the data type in bytes (Integer.SIZE is in bits). */
        private static final int BYTES = Integer.SIZE / 8;

        @Override
        protected Integer[] newArray(int entries) throws UnsupportedOperationException {
            return new Integer[entries];
        }

        @Override
        public int getLength() {
            return BYTES;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Integer readNext(ByteBuffer buffer) {
            return buffer.getInt();
        }
    }

    /**
     * {@link DataType} implementation for a fixed length string.
     */
    private static class FixedLengthStringDataType extends DataType<String> {
        /** The length of the string. */
        private final int length;
        /** The character set to decode the String with. */
        private final Charset charset;
        /** The number of bytes per character for the character set. */
        private final int bytesPerCharacter;

        /**
         * Create a new FixedLengthString data type instance using the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
         *
         * @param length The length of the string (in characters).
         */
        public FixedLengthStringDataType(int length) {
            this(length, DEFAULT_CHARACTERSET);
        }

        /**
         * Create a new FixedLengthString data type instance with a specific character set.
         *
         * @param length  The length of the string (in characters).
         * @param charset The character set to decode the String with.
         */
        public FixedLengthStringDataType(int length, Charset charset) {
            this.length = length;
            this.charset = charset;
            this.bytesPerCharacter = getAverageBytesPerCharacter(charset);
        }

        @Override
        protected String[] newArray(int entries) throws UnsupportedOperationException {
            return new String[entries];
        }

        @Override
        public int getLength() {
            return length * bytesPerCharacter;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public String readNext(ByteBuffer buffer) {
            byte[] data = new byte[getLength()];
            buffer.get(data);
            return new String(data, charset);
        }
    }

    /**
     * {@link DataType} implementation for a boolean.
     */
    private static class BooleanDataType extends DataType<Boolean> {

        @Override
        protected Boolean[] newArray(int entries) throws UnsupportedOperationException {
            return new Boolean[entries];
        }

        @Override
        public int getLength() {
            return 1;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Boolean readNext(ByteBuffer buffer) {
            return getValue(buffer.get());
        }

        /**
         * Get the boolean value of a byte.
         *
         * @param data The byte to get the value from.
         *
         * @return The boolean value of the byte.
         */
        private boolean getValue(byte data) {
            return (data >= 1) ? true : false;
        }
    }

    /**
     * {@link DataType} implementation for a 32-bit signed float.
     */
    private static class FloatDataType extends DataType<Float> {
        /** Size of the data type in bytes (Float.SIZE is in bits). */
        private static final int BYTES = Float.SIZE / 8;

        @Override
        protected Float[] newArray(int entries) throws UnsupportedOperationException {
            return new Float[entries];
        }

        @Override
        public int getLength() {
            return BYTES;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Float readNext(ByteBuffer buffer) {
            return buffer.getFloat();
        }
    }

    /**
     * {@link DataType} implementation for a byte.
     */
    private static class ByteDataType extends DataType<Byte> {

        @Override
        protected Byte[] newArray(int entries) throws UnsupportedOperationException {
            return new Byte[entries];
        }

        @Override
        public int getLength() {
            return 1;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Byte readNext(ByteBuffer buffer) {
            return buffer.get();
        }
    }


    /**
     * {@link DataType} implementation for a byte[].
     */
    private static class ByteArrayDataType extends DataType<byte[]> {
        /** The number of entries in the byte[]. */
        private final int length;

        /**
         * Create a new ByteArrayDataType of a specific length.
         *
         * @param length The number of entries in the byte[].
         */
        public ByteArrayDataType(int length) {
            this.length = length;
        }

        @Override
        protected byte[][] newArray(int entries) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Creation of arrays from arrays is not supported.");
        }

        @Override
        public DataType<byte[][]> asArrayType(int entries) throws UnsupportedOperationException {
            throw new UnsupportedOperationException("Creation of an array data types from array data types is not supported.");
        }


        @Override
        public int getLength() {
            return length;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public byte[] readNext(ByteBuffer buffer) {
            byte[] data = new byte[length];
            buffer.get(data);
            return data;
        }
    }

    /**
     * {@link DataType} implementation for a short.
     */
    private static class ShortDataType extends DataType<Short> {
        /** Size of the data type in bytes (Short.SIZE is in bits). */
        private static final int BYTES = Short.SIZE / 8;

        @Override
        protected Short[] newArray(int entries) throws UnsupportedOperationException {
            return new Short[entries];
        }

        @Override
        public int getLength() {
            return BYTES;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Short readNext(ByteBuffer buffer) {
            return buffer.getShort();
        }
    }

    /**
     * {@link DataType} implementation for a long.
     */
    private static class LongDataType extends DataType<Long> {
        /** Size of the data type in bytes (Short.SIZE is in bits). */
        private static final int BYTES = Long.SIZE / 8;

        @Override
        protected Long[] newArray(int entries) throws UnsupportedOperationException {
            return new Long[entries];
        }

        @Override
        public int getLength() {
            return BYTES;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Long readNext(ByteBuffer buffer) {
            return buffer.getLong();
        }
    }

    /**
     * {@link DataType} implementation for a double.
     */
    private static class DoubleDataType extends DataType<Double> {
        /** Size of the data type in bytes (Short.SIZE is in bits). */
        private static final int BYTES = Double.SIZE / 8;

        @Override
        protected Double[] newArray(int entries) throws UnsupportedOperationException {
            return new Double[entries];
        }

        @Override
        public int getLength() {
            return BYTES;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public Double readNext(ByteBuffer buffer) {
            return buffer.getDouble();
        }
    }
}
