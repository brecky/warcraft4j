package nl.salp.warcraft4j.io.datatype;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.RandomUtils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Utility methods
 *
 * @author Barre Dijkstra
 */
final class DataTypeTestUtil {
    /** The default byte order. */
    public static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.LITTLE_ENDIAN;

    /**
     * Private constructor to prevent instantiation.
     */
    private DataTypeTestUtil() {
    }

    /**
     * Create a ByteBuffer for the provided data using the default byte order ({@link DataTypeTestUtil#DEFAULT_BYTE_ORDER}).
     *
     * @param data The data.
     *
     * @return The ByteBuffer for the data.
     */
    public static ByteBuffer createByteBuffer(byte[] data) {
        return createByteBuffer(data, DEFAULT_BYTE_ORDER);
    }

    /**
     * Create a ByteBuffer for the provided data using the provided byte order.
     *
     * @param data      The data.
     * @param byteOrder The byte order.
     *
     * @return The ByteBuffer for the data.
     */
    public static ByteBuffer createByteBuffer(byte[] data, ByteOrder byteOrder) {
        return ByteBuffer.wrap(data).order(byteOrder);
    }

    /**
     * Create a ByteBuffer that provides random data with a provided length.
     *
     * @param bytes The number of random bytes to generate.
     *
     * @return The ByteBuffer with the random data.
     */
    public static ByteBuffer createRandomByteBuffer(int bytes) {
        return createByteBuffer(RandomUtils.nextBytes(bytes));
    }

    /**
     * Create a ByteBuffer that provides random data with a provided length, appended with the provided data using the default byte order ({@link DataTypeTestUtil#DEFAULT_BYTE_ORDER}).
     *
     * @param bytes          The number of bytes of random data before the provided data.
     * @param postRandomData The data to append to the random data.
     *
     * @return The ByteBuffer with the random date.
     */
    public static ByteBuffer createRandomByteBuffer(int bytes, byte[] postRandomData) {
        return createRandomByteBuffer(bytes, postRandomData, DEFAULT_BYTE_ORDER);
    }

    /**
     * Create a ByteBuffer that provides random data with a provided length, appended with the provided data.
     *
     * @param bytes          The number of bytes of random data before the provided data.
     * @param postRandomData The data to append to the random data.
     * @param byteOrder      The byte order for the data.
     *
     * @return The ByteBuffer with the random date.
     */
    public static ByteBuffer createRandomByteBuffer(int bytes, byte[] postRandomData, ByteOrder byteOrder) {
        byte[] data = RandomUtils.nextBytes(bytes);
        data = ArrayUtils.addAll(data, postRandomData);
        return createByteBuffer(data, byteOrder);
    }
}
