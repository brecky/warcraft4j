/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package nl.salp.warcraft4j.clientdata.io;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@WowReader} implementation for reading files.
 *
 * @author Barre Dijkstra
 */
public class FileDataReader extends DataReader {
    /** The input stream. */
    private InputStream stream;
    /** The position in the stream. */
    private int position;

    /**
     * Create a new FileDataReader instance for the given file.
     *
     * @param file The file.
     *
     * @throws java.io.IOException When the file could not be read.
     */
    public FileDataReader(File file) throws IOException {
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
