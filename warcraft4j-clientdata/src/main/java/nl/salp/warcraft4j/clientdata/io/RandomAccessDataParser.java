package nl.salp.warcraft4j.clientdata.io;

import java.io.IOException;

import static java.lang.String.format;

/**
 * Data parser that parses instances from a data reader with support for indexed reading through a {@link RandomAccessDataReader}.
 *
 * @author Barre Dijkstra
 */
public abstract class RandomAccessDataParser<T> extends DataParser<T> {
    /**
     * Read an parse an instance from the offset on the reader.
     * <p/>
     * <p/>
     * If the offset is reader has a size of the data type and the offset != {@code 0} then the reader is reset to position 0.
     *
     * @param reader The reader to read from.
     * @param offset The offset to start reading from.
     *
     * @return The parsed data.
     *
     * @throws IOException              When reading the data failed.
     * @throws DataParsingException     When parsing the data failed.
     * @throws IllegalArgumentException When an invalid offset has been provided.
     */
    public T read(RandomAccessDataReader reader, long offset) throws IOException, DataParsingException {
        if ((offset + getInstanceDataSize()) > reader.size()) {
            throw new IllegalArgumentException(format("Unable to read %d bytes at position %d from a data reader with a size of %d bytes.", getInstanceDataSize(), offset, reader.size()));
        } else if (offset < 0) {
            throw new IllegalArgumentException("Unable to reader data from an offset < 0");
        } else if (getInstanceDataSize() == reader.size()) {
            reader.position(0);
        } else {
            reader.position(offset);
        }
        return parse(reader);
    }
}
