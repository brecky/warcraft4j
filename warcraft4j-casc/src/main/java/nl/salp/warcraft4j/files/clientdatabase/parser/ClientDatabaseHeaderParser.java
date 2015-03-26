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
public class ClientDatabaseHeaderParser {
    public ClientDatabaseHeader parse(DataReader reader) throws IOException {
        String magicString = reader.readNextFixedLengthString(4);
        ClientDatabaseHeader header;
        if (ClientDatabaseDbcHeader.isHeaderFor(magicString)) {
            header = new ClientDatabaseDbcHeaderParser().read(magicString, reader);
        } else if (ClientDatabaseDb2Header.isHeaderFor(magicString)) {
            header = new ClientDatabaseDb2HeaderParser().read(magicString, reader);
        } else {
            throw new IllegalArgumentException(format("Cannot parse the header for unknown DBC file type with magic string %s", magicString));
        }
        return header;
    }
}
