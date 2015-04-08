package nl.salp.warcraft4j.clientdata.io;

import java.io.IOException;
import java.nio.ByteOrder;

import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * {@link DataReader} extension for random access data reading.
 *
 * @author Barre Dijkstra
 */
public abstract class RandomAccessDataReader extends DataReader {

    /**
     * Set the position of the reader cursor.
     *
     * @param position The position.
     *
     * @throws IOException When the position could not be set.
     */
    public abstract void position(long position) throws IOException;

    /**
     * Read the data in the given data type in the default byte order from the provided file position (moving the file pointer to the 1st byte after the read data).
     *
     * @param dataType The data type.
     * @param position The position in the file to read the data from.
     * @param <T>      The type of the data to read.
     *
     * @return The read data.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T read(DataType<T> dataType, long position) throws IOException, DataParsingException {
        position(position);
        return readNext(dataType);
    }

    /**
     * Read the data in the given data type from the provided file position (moving the file pointer to the 1st byte after the read data).
     *
     * @param dataType  The data type.
     * @param position  The position in the file to read the data from.
     * @param byteOrder The byte order of the data.
     * @param <T>       The type of the data to read.
     *
     * @return The read data.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T read(DataType<T> dataType, long position, ByteOrder byteOrder) throws IOException, DataParsingException {
        position(position);
        return readNext(dataType, byteOrder);
    }

    /**
     * Read the next complex value from the underlying data type.
     *
     * @param parser   The parser to use to parse the complex value.
     * @param position The position to read the value from.
     * @param <T>      The type of value to read.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public <T> T readNext(DataParser<T> parser, long position) throws IOException, DataParsingException {
        position(position);
        return parser.next(this);
    }

    /**
     * Read a terminated string from the underlying data.
     *
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public String readTerminatedString(long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getTerminatedString());
    }

    /**
     * Read a fixed length string from the underlying data.
     *
     * @param length   The length of the String.
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public String readFixedLengthString(int length, long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getFixedLengthString(length));
    }

    /**
     * Read a signed 32-bit integer from the underlying data.
     *
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public int readInt32(long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getInteger());
    }

    /**
     * Read a signed 32-bit integer array from the underlying data.
     *
     * @param entries  The number of array entries.
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public int[] readInt32Array(int entries, long position) throws IOException, DataParsingException {
        position(position);
        return toPrimitive(readNext(DataType.getInteger().asArrayType(entries)));
    }

    /**
     * Read a signed 16-bit short from the underlying data.
     *
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public short readShort(long position) throws IOException, DataParsingException, DataParsingException {
        position(position);
        return readNext(DataType.getShort());
    }

    /**
     * Read a signed 16-bit short array from the underlying data.
     *
     * @param entries  The number of array entries.
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public short[] readShortArray(int entries, long position) throws IOException, DataParsingException {
        position(position);
        return toPrimitive(readNext(DataType.getShort().asArrayType(entries)));
    }

    /**
     * Read a 32-bit float from the underlying data.
     *
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public float readFloat(long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getFloat());
    }

    /**
     * Read a boolean from the underlying data.
     *
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public boolean readBoolean(long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getBoolean());
    }

    /**
     * Read the a specified number of bytes as a byte[] from the underlying data.
     *
     * @param length   The number of bytes to read.
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public byte[] readBytes(int length, long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getByteArray(length));
    }

    /**
     * Read a byte from the underlying data.
     *
     * @param position The start position in the data.
     *
     * @return The value.
     *
     * @throws IOException          When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public byte readByte(long position) throws IOException, DataParsingException {
        position(position);
        return readNext(DataType.getByte());
    }
}
