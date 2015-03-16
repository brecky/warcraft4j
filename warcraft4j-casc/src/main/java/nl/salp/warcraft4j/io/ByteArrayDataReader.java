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

package nl.salp.warcraft4j.io;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@link DataReader} implementation that uses a {@code byte[]} as underlying data.
 */
public class ByteArrayDataReader extends DataReader {
    /** The ByteBuffer holding the data. */
    private ByteBuffer buffer;

    /**
     * Create a new ByteArrayDataReader, wrapping the provided byte array.
     *
     * @param data The byte[] to wrap.
     */
    public ByteArrayDataReader(byte[] data) {
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
