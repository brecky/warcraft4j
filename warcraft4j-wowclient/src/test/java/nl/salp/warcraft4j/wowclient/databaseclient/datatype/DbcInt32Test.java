package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import org.apache.commons.lang3.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

import java.nio.ByteOrder;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcInt32Test {
    /** The byte[4] big endian value of the test data ({@code 0000 0001 0000 0001 0000 0001 0000 0000 = byte{1, 1, 1, 0}}). */
    private static final byte[] DATA_BYTEARRAY_BIG_ENDIAN = new byte[]{1, 1, 1, 0};
    /** The byte[4] little endian value of the test data ({@code 0000 0000 0000 0001 0000 0001 0000 0001 = byte{0, 1, 1, 1}}). */
    private static final byte[] DATA_BYTEARRAY_LITTLE_ENDIAN = new byte[]{0, 1, 1, 1};
    /** The integer value of the test data ({@code 0000 0001 0000 0001 0000 0001 0000 0000 = 16843008}). */
    private static final int DATA_INT = 16843008;
    /** The data type under test. */
    private DbcInt32 dataType;

    @Before
    public void setUp() {
        dataType = new DbcInt32();
    }

    @Test
    public void shouldReturnName() {
        assertEquals("Integer", dataType.getName());
    }

    @Test
    public void shouldReturnDbcName() {
        assertEquals("int32", dataType.getDbcName());
    }

    @Test
    public void shouldReturnLengthInBytes() {
        assertEquals(4, dataType.getByteLength());
    }

    @Test
    public void shouldReturnJavaImplementation() {
        assertEquals(Integer.class, dataType.getJavaClass());
    }

    @Test
    public void shouldParseByteArray() {
        assertEquals(DATA_INT, dataType.parse(DATA_BYTEARRAY_BIG_ENDIAN).intValue());
    }

    @Test
    public void shouldParseByteArrayLittleEndian() {
        assertEquals(DATA_INT, dataType.parse(DATA_BYTEARRAY_LITTLE_ENDIAN, ByteOrder.LITTLE_ENDIAN).intValue());
    }

    @Test
    public void shouldParseByteArrayBigEndian() {
        assertEquals(DATA_INT, dataType.parse(DATA_BYTEARRAY_BIG_ENDIAN, ByteOrder.BIG_ENDIAN).intValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForParsingNullByteArray() {
        dataType.parse(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForParsingNullByteArrayWithEndianess() {
        dataType.parse(null, ByteOrder.BIG_ENDIAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSmallerByteArray() {
        byte[] data = new byte[]{1, 1, 1};
        dataType.parse(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForSmallerByteArrayWithEndianess() {
        byte[] data = new byte[]{1, 1, 1};
        dataType.parse(data, ByteOrder.BIG_ENDIAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForBiggerByteArray() {
        byte[] data = new byte[]{1, 1, 1, 1, 1};
        dataType.parse(data);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForBiggerByteArrayWithEndianess() {
        byte[] data = new byte[]{1, 1, 1, 1, 1};
        dataType.parse(data, ByteOrder.BIG_ENDIAN);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForByteArrayWithNullEndian() {
        dataType.parse(DATA_BYTEARRAY_BIG_ENDIAN, null);
    }

    @Test
    public void shouldConvertToByteArray() {
        try {
            assertArrayEquals(DATA_BYTEARRAY_BIG_ENDIAN, dataType.convert(DATA_INT));
        } catch (AssertionError e) {
            System.out.println(ArrayUtils.toString(DATA_BYTEARRAY_BIG_ENDIAN) + " != " + ArrayUtils.toString(dataType.convert(DATA_INT)));
        }
    }

    @Test
    public void shouldConvertToByteArrayWithLittleEndian() {
        assertArrayEquals(DATA_BYTEARRAY_LITTLE_ENDIAN, dataType.convert(DATA_INT, ByteOrder.LITTLE_ENDIAN));
    }

    @Test
    public void shouldConvertToByteArrayWithBigEndian() {
        assertArrayEquals(DATA_BYTEARRAY_BIG_ENDIAN, dataType.convert(DATA_INT, ByteOrder.BIG_ENDIAN));
    }
}
