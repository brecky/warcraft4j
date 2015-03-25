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

import nl.salp.warcraft4j.io.DataReader;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseDb2HeaderParser {
    public ClientDatabaseDb2Header read(String magicString, DataReader reader) throws IOException {
        if (!ClientDatabaseDb2Header.isHeaderFor(magicString)) {
            throw new IllegalArgumentException(format("Unknown DB2 magic String encountered: %s", magicString));
        }
        return parseDb2Header(reader);
    }

    private ClientDatabaseDb2Header parseDb2Header(DataReader reader) throws IOException {
        int recordCount = reader.readNextInt32();
        int fieldCount = reader.readNextInt32();
        int recordSize = reader.readNextInt32();
        int stringBlockSize = reader.readNextInt32();
        int stringBlockHash = reader.readNextInt32();
        int build = reader.readNextInt32();
        int timestampLastWritten = reader.readNextInt32();

        if (build > ClientDatabaseDb2ExtHeader.LAST_BUILD_PRE_EXTENDED_HEADER) {
            return parseDb2ExtHeader(reader, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten);
        } else {
            return new ClientDatabaseDb2Header(ClientDatabaseDb2Header.MAGICSTRING, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten);
        }
    }

    private ClientDatabaseDb2ExtHeader parseDb2ExtHeader(DataReader reader, int recordCount, int fieldCount, int recordSize, int stringBlockSize, int stringBlockHash, int build, int timestampLastWritten) throws IOException {
        int minId = reader.readNextInt32();
        int maxId = reader.readNextInt32();
        int locale = reader.readNextInt32();
        byte[] unknown = reader.readNextBytes(4);
        int[] indices;
        short[] rowLengths;
        if (maxId > 0) {
            int entryCount = maxId - minId + 1;
            indices = reader.readNextInt32Array(entryCount);
            rowLengths = reader.readNextShortArray(entryCount);
        } else {
            indices = new int[0];
            rowLengths = new short[0];
        }
        return new ClientDatabaseDb2ExtHeader(ClientDatabaseDb2ExtHeader.MAGICSTRING, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten, minId, maxId, locale, unknown, indices, rowLengths);
    }
}
