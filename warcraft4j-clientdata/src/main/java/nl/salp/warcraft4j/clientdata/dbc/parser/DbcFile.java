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

import nl.salp.warcraft4j.clientdata.io.ByteArrayDataReader;
import nl.salp.warcraft4j.clientdata.io.DataReader;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class DbcFile {
    private final String filename;
    private final DbcHeader header;
    private final byte[] data;
    private final DbcStringTable stringTable;

    public DbcFile(String filename, DbcHeader header, byte[] data, DbcStringTable stringTable) {
        validate(filename, header, data, stringTable);
        this.filename = filename;
        this.header = header;
        this.data = data;
        this.stringTable = stringTable;
    }

    private void validate(String filename, DbcHeader header, byte[] data, DbcStringTable stringBlock) {
        if (isEmpty(filename)) {
            throw new IllegalArgumentException("Unable to create a client database file with a null filename.");
        }
        if (header == null) {
            throw new IllegalArgumentException(format("Unable to create client database file %s with a null header.", filename));
        }
        if (data == null || data.length != header.getEntryBlockSize()) {
            int dataLength = (data == null ? 0 : data.length);
            throw new IllegalArgumentException(format("Invalid data received for client database file %s. Expected %d bytes, received %d.", filename, dataLength, header.getEntryBlockSize()));
        }
        if ((stringBlock == null || stringBlock.getNumberOfEntries() == 0) && header.getStringTableBlockSize() > 2) {
            throw new IllegalArgumentException(format("No StringBlock received for client database file %s, while a StringBlock of %d bytes was expected", filename, header.getStringTableBlockSize()));
        }
        if ((stringBlock != null && stringBlock.getNumberOfEntries() > 0) && header.getStringTableBlockSize() <= 2) {
            throw new IllegalArgumentException(format("StringBlock received for client database file %s, while no StringBlock was expected", filename));
        }
    }

    public String getFilename() {
        return filename;
    }

    public DbcHeader getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }

    public DataReader getDataReader() {
        return new ByteArrayDataReader(data);
    }

    public DbcStringTable getStringTable() {
        return stringTable;
    }
}
