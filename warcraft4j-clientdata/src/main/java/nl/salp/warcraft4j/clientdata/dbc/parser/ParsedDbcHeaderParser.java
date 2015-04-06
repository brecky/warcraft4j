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

import nl.salp.warcraft4j.clientdata.io.DataReader;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ParsedDbcHeaderParser {
    public ParsedDbcHeader read(String magicString, DataReader reader) throws IOException {
        if (!ParsedDbcHeader.isHeaderFor(magicString)) {
            throw new IllegalArgumentException(format("Unknown DBC magic String encountered: %s", magicString));
        }
        return parseDbcHeader(reader);
    }

    private ParsedDbcHeader parseDbcHeader(DataReader reader) throws IOException {
        int recordCount = reader.readNextInt32();
        int fieldCount = reader.readNextInt32();
        int recordSize = reader.readNextInt32();
        int stringBlockSize = reader.readNextInt32();

        return new ParsedDbcHeader(ParsedDbcHeader.MAGICSTRING, recordCount, fieldCount, recordSize, stringBlockSize);
    }
}
