package nl.salp.warcraft4j.wowclient.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@link WowReader} implementation that uses a {@code byte[]} as underlying data.
 */
public class WowByteArrayReader extends WowReader {
    /** The ByteBuffer holding the data. */
    private ByteBuffer buffer;

    /**
     * Create a new WowByteArrayReader, wrapping the byte array.
     *
     * @param data The byte[] to wrap.
     */
    public WowByteArrayReader(byte[] data) {
        buffer = ByteBuffer.wrap(data).order(DEFAULT_BYTE_ORDER);
    }

    @Override
    public int position() {
        return buffer.position();
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return buffer.hasRemaining();
    }

    @Override
    public <T> T readNext(DataType<T> dataType) throws IOException {
        return readNext(dataType, dataType.getDefaultByteOrder());
    }

    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException {
        byte[] data;
        if (dataType.getLength() < 1) {
            data = new byte[buffer.remaining()];
        } else {
            data = new byte[dataType.getLength()];
        }
        // FIXME Method dumps all remaining data for var-length data types (and thus moving the position to the end). Think of a different way to deal (shallow copy?) :-)
        buffer.get(data);
        return dataType.readNext(ByteBuffer.wrap(data).order(byteOrder));
    }
}
