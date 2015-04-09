package nl.salp.warcraft4j.clientdata.io;

import org.junit.Before;
import org.junit.Test;

import java.nio.ByteBuffer;

import static nl.salp.warcraft4j.clientdata.io.DataTypeTestUtil.createRandomByteBuffer;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Unit tests for a {@code java.lang.Boolean} implementation of {@link DataType} ({@link DataType#getBoolean()}).
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.clientdata.io.DataType#getBoolean()
 */
public class BooleanDataTypeTest {
    /** The size of the data type in bytes. */
    private static final int DATATYPE_SIZE = 1;
    /** The data type being tested. */
    private DataType<Boolean> dataType;


    /**
     * Set up the test context, executed before every unit test (method) execution.
     */
    @Before
    public void setUp() {
        dataType = DataType.getBoolean();
    }

    @Test
    public void shouldGetDataSize() {
        assertEquals("Wrong data size returned.", DATATYPE_SIZE, dataType.getLength());
    }

    @Test
    public void shouldReadExpectedNumberOfBytes() {
        ByteBuffer buffer = createRandomByteBuffer(DATATYPE_SIZE);

        Object result = dataType.readNext(buffer);

        assertNotNull("Got a null value after reading from a byte buffer.", result);
        assertEquals("Didn't read all bytes.", 0, buffer.remaining());
    }

    @Test
    public void shouldReadExpectedJavaType() {
        ByteBuffer buffer = createRandomByteBuffer(DATATYPE_SIZE);

        Object result = dataType.readNext(buffer);

        assertEquals("Read an object of the wrong type", Boolean.class, result.getClass());
    }

    @Test
    public void shouldCreateArray() {
        final int entries = 5;

        Boolean[] array = dataType.newArray(entries);

        assertNotNull("New array instance returned a null value.", array);
        assertEquals("New array instance returned an array of the wrong size.", entries, array.length);
    }

    @Test
    public void shouldCreateArrayDataType() {
        DataType<Boolean[]> array = dataType.asArrayType(5);

        assertNotNull("New array type returned a null value.", array);
    }
}
