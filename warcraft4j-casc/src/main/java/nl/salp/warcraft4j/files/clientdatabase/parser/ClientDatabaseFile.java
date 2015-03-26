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

package nl.salp.warcraft4j.files.clientdatabase.parser;

import nl.salp.warcraft4j.io.ByteArrayDataReader;
import nl.salp.warcraft4j.io.DataReader;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseFile {
    private final String filename;
    private final ClientDatabaseHeader header;
    private final byte[] data;
    private final ClientDatabaseStringBlock stringBlock;

    public ClientDatabaseFile(String filename, ClientDatabaseHeader header, byte[] data, ClientDatabaseStringBlock stringBlock) {
        validate(filename, header, data, stringBlock);
        this.filename = filename;
        this.header = header;
        this.data = data;
        this.stringBlock = stringBlock;
    }

    private void validate(String filename, ClientDatabaseHeader header, byte[] data, ClientDatabaseStringBlock stringBlock) {
        if (isEmpty(filename)) {
            throw new IllegalArgumentException("Unable to create a client database file with a null filename.");
        }
        if (header == null) {
            throw new IllegalArgumentException(format("Unable to create client database file %s with a null header.", filename));
        }
        if (data == null || data.length != header.getRecordBlockSize()) {
            int dataLength = (data == null ? 0 : data.length);
            throw new IllegalArgumentException(format("Invalid data received for client database file %s. Expected %d bytes, received %d.", filename, dataLength, header.getRecordBlockSize()));
        }
        if ((stringBlock == null || stringBlock.getAvailablePositions().isEmpty()) && header.getStringBlockSize() > 2) {
            throw new IllegalArgumentException(format("No StringBlock received for client database file %s, while a StringBlock of %d bytes was expected", filename, header.getStringBlockSize()));
        }
        if ((stringBlock != null && !stringBlock.getAvailablePositions().isEmpty()) && header.getStringBlockSize() <= 2) {
            throw new IllegalArgumentException(format("StringBlock received for client database file %s, while no StringBlock was expected", filename));
        }
    }

    public String getFilename() {
        return filename;
    }

    public ClientDatabaseHeader getHeader() {
        return header;
    }

    public byte[] getData() {
        return data;
    }

    public DataReader getDataReader() {
        return new ByteArrayDataReader(data);
    }

    public ClientDatabaseStringBlock getStringBlock() {
        return stringBlock;
    }
}
