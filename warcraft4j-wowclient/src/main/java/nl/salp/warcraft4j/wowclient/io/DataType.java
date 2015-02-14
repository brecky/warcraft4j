package nl.salp.warcraft4j.wowclient.io;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Data type for reading various types of data types (with different byte ordering) via a {@link WowReader}.
 *
 * @author Barre Dijkstra
 */
public abstract class DataType<T> {
    /** The default character set. */
    private static final Charset DEFAULT_CHARACTERSET = StandardCharsets.UTF_8;
    /** Single int32 instance. */
    private static final DataType<Integer> INT32 = new Int32DataType();
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

    /**
     * Get a {@link DataType} implementation for 32-bit signed integers.
     *
     * @return The data type.
     */
    public static DataType<Integer> getInt32() {
        return INT32;
    }

    /**
     * Get a {@link DataType} implementation for terminated strings (zero-terminated or end of data).
     *
     * @return The data type.
     */
    public static DataType<String> getTerminatedString() {
        return TERMINATED_STRING;
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
     * Get a {@link DataType} implementation for a byte-arrays.
     *
     * @return The data type.
     */
    public static DataType<byte[]> getByteArray(int length) {
        return new ByteArrayDataType(length);
    }

    /**
     * Get a {@link DataType} implementation for fixed length strings.
     *
     * @param length The length of the string.
     *
     * @return The data type.
     */
    public static DataType<String> getFixedLengthString(int length) {
        return new FixedLengthStringDataType(length);
    }

    /**
     * Get a {@link DataType} implementation for a 16-bit short.
     *
     * @return The data type.
     */
    public static DataType<Short> getShort() {
        return SHORT;
    }

    /**
     * Get a {@link DataType} implementation for an 32-bit signed int array.
     *
     * @param entries The number of array entries.
     *
     * @return The data type.
     */
    public static DataType<int[]> getInt32Array(int entries) {
        return new IntArrayDataType(entries);
    }

    /**
     * Get a {@link DataType} implementation for an 16-bit signed short array.
     *
     * @param entries The number of array entries.
     *
     * @return The data type.
     */
    public static DataType<short[]> getShortArray(int entries) {
        return new ShortArrayDataType(entries);
    }

    /**
     * Get the length of the data type in bytes.
     *
     * @return The length.
     */
    public abstract int getLength();

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

    /**
     * {@link DataType} implementation for a zero-terminated string or a string till the end of the buffer.
     */
    private static class TerminatedStringDataType extends DataType<String> {
        @Override
        public int getLength() {
            return -1;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public String readNext(ByteBuffer buffer) {
            ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
            byte c;
            while ((c = buffer.get()) != 0) {
                byteStream.write(c);
            }
            return new String(byteStream.toByteArray(), DEFAULT_CHARACTERSET);
        }
    }

    /**
     * {@link DataType} implementation for a 32-bit signed integer.
     */
    private static class Int32DataType extends DataType<Integer> {
        @Override
        public int getLength() {
            return 4;
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

        /**
         * Create a new FixedLengthString data type instance.
         *
         * @param length The length of the string.
         */
        public FixedLengthStringDataType(int length) {
            this.length = length;
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
        public String readNext(ByteBuffer buffer) {
            byte[] data = new byte[length];
            buffer.get(data);
            return new String(data, DEFAULT_CHARACTERSET);
        }
    }

    /**
     * {@link DataType} implementation for a boolean.
     */
    private static class BooleanDataType extends DataType<Boolean> {

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

        @Override
        public int getLength() {
            return 4;
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
     * {@link DataType} implementation for an int[].
     */
    private static class IntArrayDataType extends DataType<int[]> {
        /** The number of entries in the array. */
        private final int entries;

        /**
         * Create a new IntArrayDataType with a specific number of array entries.
         *
         * @param entries The number of entries.
         */
        public IntArrayDataType(int entries) {
            this.entries = entries;
        }

        @Override
        public int getLength() {
            return entries * 4;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return ByteOrder.LITTLE_ENDIAN;
        }

        @Override
        public int[] readNext(ByteBuffer buffer) {
            int[] entries = new int[this.entries];
            for (int i = 0; i < this.entries; i++) {
                entries[i] = buffer.getInt();
            }
            return entries;
        }
    }

    /**
     * {@link DataType} implementation for a short.
     */
    private static class ShortDataType extends DataType<Short> {

        @Override
        public int getLength() {
            return 2;
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
     * {@link DataType} implementation for a short[].
     */
    private static class ShortArrayDataType extends DataType<short[]> {
        /** The number of array entries. */
        private final int entries;

        /**
         * Create a new ShortArrayDataType with a specific number of array entries.
         *
         * @param entries The number of array entries.
         */
        public ShortArrayDataType(int entries) {
            this.entries = entries;
        }

        @Override
        public int getLength() {
            return entries * 2;
        }

        @Override
        public ByteOrder getDefaultByteOrder() {
            return null;
        }

        @Override
        public short[] readNext(ByteBuffer buffer) {
            short[] entries = new short[this.entries];
            for (int i = 0; i < this.entries; i++) {
                entries[i] = buffer.getShort();
            }
            return entries;
        }
    }
}
