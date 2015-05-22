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

package nl.salp.warcraft4j.clientdata.dbc.parser;

import nl.salp.warcraft4j.clientdata.io.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.clientdata.io.DataType.getTerminatedString;
import static nl.salp.warcraft4j.clientdata.util.io.DataTypeUtil.getAverageBytesPerCharacter;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * {@link DataParser} implementation for reading and parsing a {@link DbcStringTable}.
 *
 * @author Barre Dijkstra
 */
public class DbcStringTableParser extends RandomAccessDataParser<DbcStringTable> {
    private static final Charset STRINGTABLE_CHARSET = StandardCharsets.US_ASCII;
    /** The header of the DBC file to parse the string table from. */
    private final DbcHeader header;

    /**
     * Create a new instance.
     *
     * @param header The header of the DBC file to parse the string table from.
     */
    public DbcStringTableParser(DbcHeader header) {
        this.header = header;

    }

    /**
     * Parse the provided data to a StringBlock.
     *
     * @param data The data in raw byte[] format.
     *
     * @return The StringBlock.
     *
     * @throws IOException When reading of the data failed.
     */
    public DbcStringTable parse(byte[] data) throws IOException {
        return new DbcStringTable(parseStrings(data));
    }

    /**
     * Parse the data to a map with the strings indexed by their start position.
     *
     * @param stringData The data.
     *
     * @return The map.
     *
     * @throws IOException When reading of the data failed.
     */
    private Map<Integer, String> parseStrings(byte[] stringData) throws IOException {
        Map<Integer, String> strings = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(stringData);
        while (buffer.hasRemaining()) {
            int position = buffer.position();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byte b;
            while (buffer.hasRemaining() && (b = buffer.get()) != 0) {
                byteOut.write(b);
            }
            String value = new String(byteOut.toByteArray(), StandardCharsets.US_ASCII);
            if (!value.isEmpty()) {
                strings.put(position, value);
            }
        }
        return strings;
    }

    private Map<Integer, String> parseStringTable(DataReader reader) throws IOException, DataParsingException {
        Map<Integer, String> strings = new HashMap<>();

        int offset;
        if (reader.position() == header.getStringTableStartingOffset()) {
            offset = header.getStringTableStartingOffset();
        } else if (reader.position() == 0) {
            offset = 0;
        } else if (RandomAccessDataReader.class.isAssignableFrom(reader.getClass()) && reader.size() == header.getStringTableBlockSize()) {
            ((RandomAccessDataReader) reader).position(0);
            offset = 0;
        } else if (RandomAccessDataReader.class.isAssignableFrom(reader.getClass()) && reader.size() > header.getStringTableBlockSize()) {
            ((RandomAccessDataReader) reader).position(header.getStringTableStartingOffset());
            offset = header.getStringTableStartingOffset();
        } else {
            throw new DataParsingException(format("Invalid data reader cursor position and unable to set the position. (dataReader: %s, position: %d, stringTableOffset: %d)", reader.getClass().getName(), reader.position(), header.getStringTableStartingOffset()));
        }

        final int tableSize = header.getStringTableBlockSize();
        long readBytes = 0;
        while (reader.hasRemaining() && readBytes < tableSize) {
            int position = (int) reader.position() - offset;
            String value = reader.readNext(getTerminatedString(STRINGTABLE_CHARSET));
            int valueSize = value.length() * getAverageBytesPerCharacter(STRINGTABLE_CHARSET);

            readBytes = readBytes + valueSize;
            if (isNotEmpty(value)) {
                strings.put(position, value);
            }
        }
        /*
        long readBytes = 0;
        while (reader.hasRemaining() && readBytes < header.getStringTableBlockSize()) {
            int position = (int) (reader.position() - offset);

            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byte b;
            while (reader.hasRemaining() && (b = reader.readNext(DataType.getByte())) != 0) {
                byteOut.write(b);
                readBytes++;
            }
            String value = new String(byteOut.toByteArray(), StandardCharsets.US_ASCII);
            if (!value.isEmpty()) {
                strings.put(position, value);
            }
        }
        */
        return strings;
    }


    @Override
    public DbcStringTable parse(DataReader reader) throws IOException, DataParsingException {
        Map<Integer, String> strings;
        if (header.getStringTableBlockSize() > 2) {
            strings = parseStringTable(reader);
        } else {
            strings = Collections.emptyMap();
        }
        return new DbcStringTable(strings);
    }

    @Override
    public int getInstanceDataSize() {
        return header.getStringTableBlockSize();
    }
}
