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

package nl.salp.warcraft4j.wowclient.databaseclient;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcReader {

    public <T> List<T> read(final File dbcFile, final Class<T> dbcModel) throws IOException {
        List<T> entries;

        try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(dbcFile))) {
            DbcHeader header = readHeader(inputStream);
            byte[] entryData = readBytes(inputStream, header.getRecordCount() * header.getRecordSize());
            byte[] stringsData = readBytes(inputStream, header.getStringBlockSize());

            DbcModel<T> model = new DbcModel<>(dbcModel);
            entries = model.parse(header, entryData, stringsData);
        }

        return entries;
    }

    /**
     * Read the {@link DbcHeader} from the input stream.
     *
     * @param inputStream The input stream.
     *
     * @return The DbcHeader.
     *
     * @throws IOException When reading failed.
     */
    private DbcHeader readHeader(InputStream inputStream) throws IOException {
        String magicString = readString(inputStream, 4);
        int recordCount = readInt(inputStream, ByteOrder.LITTLE_ENDIAN);
        int fieldCount = readInt(inputStream, ByteOrder.LITTLE_ENDIAN);
        int recordSize = readInt(inputStream, ByteOrder.LITTLE_ENDIAN);
        int stringBlockSize = readInt(inputStream, ByteOrder.LITTLE_ENDIAN);

        return new DbcHeader(magicString, recordCount, fieldCount, recordSize, stringBlockSize);
    }

    /**
     * Read a number of bytes from the stream.
     *
     * @param inputStream The stream to read from.
     * @param bytes       The number of bytes to read.
     *
     * @return The read bytes as a {@link byte[]}.
     *
     * @throws IOException When reading failed.
     */
    private byte[] readBytes(InputStream inputStream, int bytes) throws IOException {
        byte[] data = new byte[bytes];
        inputStream.read(data);
        return data;
    }

    /**
     * Read a String from the input stream, using an UTF-8 encoding (1 byte per character).
     *
     * @param inputStream The input stream to read from.
     * @param bytes       The number of bytes to read.
     *
     * @return The read String.
     *
     * @throws IOException When reading failed.
     */
    private String readString(InputStream inputStream, int bytes) throws IOException {
        return new String(readBytes(inputStream, bytes), StandardCharsets.US_ASCII);
    }

    /**
     * Read an unsigned {@code int} ({@code uint32)} from the stream, by reading an int and dropping the sign bit.
     *
     * @param inputStream The stream to read from.
     * @param byteOrder   The byte order the int is in.
     *
     * @return The read int.
     *
     * @throws IOException When reading failed.
     */
    private int readUInt(InputStream inputStream, ByteOrder byteOrder) throws IOException {
        return readInt(inputStream, byteOrder, false);
    }

    /**
     * Read a signed {@code int} ({@code int32)} from the stream.
     *
     * @param inputStream The stream to read from.
     * @param byteOrder   The byte order the int is in.
     *
     * @return The read int.
     *
     * @throws IOException When reading failed.
     */
    private int readInt(InputStream inputStream, ByteOrder byteOrder) throws IOException {
        return readInt(inputStream, byteOrder, true);
    }


    /**
     * Read an int (either {@code uint32} or {@code int32}) from the input stream.
     *
     * @param inputStream The input stream to read the int from.
     * @param byteOrder   The byte order the int is in.
     * @param signed      Whether or not the int is signed.
     *
     * @return The read int.
     *
     * @throws IOException When reading failed.
     */
    private int readInt(InputStream inputStream, ByteOrder byteOrder, boolean signed) throws IOException {
        byte[] data = readBytes(inputStream, 4);
        ByteBuffer byteBuffer = ByteBuffer.wrap(data);
        if (!byteBuffer.order().equals(byteOrder)) {
            byteBuffer.order(byteOrder);
        }
        int value;
        if (signed) {
            value = byteBuffer.getInt();
        } else {
            value = byteBuffer.get(1);
        }
        return value;
    }

}
