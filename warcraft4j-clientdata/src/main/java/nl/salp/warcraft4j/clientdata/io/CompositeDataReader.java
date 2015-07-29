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

import nl.salp.warcraft4j.clientdata.io.datatype.DataType;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.clientdata.io.parser.DataParsingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CompositeDataReader extends RandomAccessDataReader {
    private static final int CHUNK_SIZE = 1024;
    private final ByteArrayDataReader reader;

    public CompositeDataReader(Supplier<? extends DataReader>... readers) throws IOException {
        this(Arrays.asList(readers), 0, 0);
    }

    public CompositeDataReader(List<Supplier<? extends DataReader>> readers) throws IOException {
        this(readers, 0, 0);
    }

    public CompositeDataReader(List<Supplier<? extends DataReader>> readers, long offset, long length) throws IOException {
        this.reader = new ByteArrayDataReader(getData(readers, offset, length));
    }

    private byte[] getData(List<Supplier<? extends DataReader>> readers, long offset, long length) throws IOException {
        ByteArrayOutputStream data = new ByteArrayOutputStream();
        long position = 0;
        for (Supplier<? extends DataReader> readerSupplier : readers) {
            try (DataReader reader = readerSupplier.get()) {
                if (offset > 0 && reader.hasRemaining() && position < offset) {
                    long skipped = Math.min(reader.remaining(), offset - position);
                    reader.skip(skipped);
                    position += skipped;
                } else {
                    while (reader.hasRemaining() && (length < 1 || position < offset + length)) {
                        int chunkSize = (int) Math.min(CHUNK_SIZE, reader.remaining());
                        data.write(reader.readNext(DataTypeFactory.getByteArray(chunkSize)));
                        position += chunkSize;
                    }
                }
            }
        }
        return data.toByteArray();
    }

    @Override
    public long position() {
        return reader.position();
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return reader.hasRemaining();
    }

    @Override
    public long remaining() throws IOException {
        return reader.remaining();
    }

    @Override
    public long size() throws IOException {
        return reader.size();
    }

    @Override
    public void position(long position) throws IOException {
        reader.position(position);
    }

    @Override
    public void skip(long bytes) throws IOException {
        reader.skip(bytes);
    }

    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        return reader.readNext(dataType, byteOrder);
    }

    @Override
    public void close() throws IOException {
        reader.close();
    }
}
