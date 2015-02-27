package nl.salp.warcraft4j.wowclient.io;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for a array wrapper implementations of {@link DataType} ({@link DataType#asArrayType(int)}).
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.wowclient.io.DataType#asArrayType(int)
 */

public class ArrayWrapperDataTypeTest {
    /** The array element count. */
    private static final int ARRAY_SIZE = 5;

    /** The data type where the array data type is created from. */
    private DataType<Integer> dataType;
    /** The array data type under test. */
    private DataType<Integer[]> arrayType;

    @Before
    public void setUp() {
        dataType = DataType.getInteger();
        arrayType = dataType.asArrayType(ARRAY_SIZE);
    }

    @Test
    public void shouldCreateArrayWrapper() {
        assertNotNull("Array data type wrapper created is null", arrayType);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionForArrayWrapperTypeOfArrayType() {
        arrayType.asArrayType(ARRAY_SIZE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowExceptionForArrayOfArrayType() {
        arrayType.newArray(ARRAY_SIZE);
    }

    @Test
    public void shouldCalculateDataLength() {
        int dataSize = dataType.getLength() * ARRAY_SIZE;
        assertEquals("Invalid data block size calculated for a array data type.", dataSize, arrayType.getLength());
    }

    @Test
    public void shouldUseSameByteOrderAsBaseDataType() {
        assertEquals("Different byte order returned as the base type.", dataType.getDefaultByteOrder(), arrayType.getDefaultByteOrder());
    }

    @Test
    public void shouldReadDataAsArray() {
        int dataSize = dataType.getLength() * ARRAY_SIZE;
        ByteBuffer byteBuffer = DataTypeTestUtil.createRandomByteBuffer(dataSize);

        Integer[] data = arrayType.readNext(byteBuffer);

        assertNotNull("Null object returned when reading from a byte buffer.", data);
        assertEquals("Invalid number of elements read from the buffer.", ARRAY_SIZE, data.length);
        assertEquals("ByteBuffer of exact size not fully read.", 0, byteBuffer.remaining());
    }
}
