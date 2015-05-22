package nl.salp.warcraft4j.clientdata.io;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static nl.salp.warcraft4j.clientdata.io.DataTypeTestUtil.createRandomByteBuffer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for a {@code byte[]} implementation of {@link DataType} ({@link nl.salp.warcraft4j.clientdata.io.DataType#getByteArray(int)}).
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.clientdata.io.DataType#getByteArray(int)
 */
public class ByteArrayDataTypeTest {
    /** The number of array elements. */
    private static final int ARRAY_SIZE = 5;
    /** The size of the data type in bytes. */
    private static final int DATATYPE_SIZE = 1;
    /** The size of the data array in bytes. */
    private static final int DATA_SIZE = ARRAY_SIZE * DATATYPE_SIZE;
    /** The data type being tested. */
    private DataType<byte[]> dataType;


    /**
     * Set up the test context, executed before every unit test (method) execution.
     */
    @Before
    public void setUp() {
        dataType = DataType.getByteArray(ARRAY_SIZE);
    }

    @Test
    public void shouldGetDataSize() {
        assertEquals("Wrong data size returned.", DATA_SIZE, dataType.getLength());
    }

    @Test
    public void shouldReadExpectedNumberOfBytes() {
        ByteBuffer buffer = createRandomByteBuffer(DATA_SIZE);

        byte[] result = dataType.readNext(buffer);

        assertNotNull("Got a null value after reading from a byte buffer.", result);
        assertEquals("Didn't read all bytes.", 0, buffer.remaining());
        assertEquals("Didn't read the expected amount of entries.", ARRAY_SIZE, result.length);
    }

    @Test
    public void shouldReadExpectedJavaType() {
        ByteBuffer buffer = createRandomByteBuffer(DATA_SIZE);

        Object result = dataType.readNext(buffer);

        assertEquals("Read an object of the wrong type", byte[].class, result.getClass());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldCreateArray() {
        dataType.newArray(ARRAY_SIZE);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldCreateArrayDataType() {
        dataType.asArrayType(ARRAY_SIZE);
    }

}
