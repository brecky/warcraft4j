package nl.salp.warcraft4j.wowclient.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@WowReader} implementation for files.
 *
 * @author Barre Dijkstra
 */
public class WowFileReader extends WowReader {
    /** The input stream. */
    private InputStream stream;
    /** The position in the stream. */
    private int position;

    /**
     * Create a new WowFileReader instance for the given file.
     *
     * @param file The file.
     *
     * @throws IOException When the file could not be read.
     */
    public WowFileReader(File file) throws IOException {
        stream = new BufferedInputStream(new FileInputStream(file));
        position = 0;
    }

    @Override
    public final int position() {
        return position;
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return stream.available() > 0;
    }

    @Override
    public final <T> T readNext(DataType<T> dataType) throws IOException {
        return readNext(dataType, dataType.getDefaultByteOrder());
    }


    @Override
    public final <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException {
        byte[] data = new byte[dataType.getLength()];
        stream.read(data);
        position += data.length;
        ByteBuffer buffer = ByteBuffer.wrap(data).order(byteOrder);
        return dataType.readNext(buffer);
    }

    @Override
    public final void close() throws IOException {
        if (stream != null) {
            stream.close();
        }
    }
}
