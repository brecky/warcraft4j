package nl.salp.warcraft4j.clientdata.io;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.READ;

/**
 * {@WowReader} implementation for reading random access files, allowing to move the cursor in the file.
 *
 * @author Barre Dijkstra
 */
public class RandomAccessFileDataReader extends RandomAccessDataReader {
    /** The file channel for the file. */
    private FileChannel channel;

    /**
     * Create a new RandomAccessFileDataReader instance for the given file.
     *
     * @param file The file.
     *
     * @throws java.io.IOException When the file could not be read.
     */
    public RandomAccessFileDataReader(File file) throws IOException {
        channel = FileChannel.open(Paths.get(file.toURI()), READ);
    }

    @Override
    public final long position() {
        try {
            return channel.position();
        } catch (IOException e) {
            throw new RuntimeException("Unable to get the random access file position", e);
        }
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return channel.size() > position();
    }

    @Override
    public long remaining() throws IOException {
        return channel.size() - channel.position();
    }

    @Override
    public long size() throws IOException{
        return channel.size();
    }

    @Override
    public final <T> T readNext(DataType<T> dataType) throws IOException {
        return readNext(dataType, dataType.getDefaultByteOrder());
    }


    @Override
    public final <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException {
        byte[] data = new byte[dataType.getLength()];
        ByteBuffer buffer = ByteBuffer.allocate(dataType.getLength());
        buffer.order(byteOrder);
        channel.read(buffer);
        return dataType.readNext(buffer);
    }

    @Override
    public void position(long position) throws IOException {
        channel.position(position);
    }

    @Override
    public final void close() throws IOException {
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
    }
}
