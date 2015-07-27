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
package nl.salp.warcraft4j.clientdata.dbc.mapper;

import java.io.IOException;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcHeader;
import nl.salp.warcraft4j.clientdata.dbc.DbcStringTable;
import nl.salp.warcraft4j.clientdata.io.parser.DataParsingException;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.parser.RandomAccessDataParser;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
public class DbcEntryParser<T extends DbcEntry> extends RandomAccessDataParser<T> {
    /** The mapping to create the entries for. */
    private final DbcFileMapping mapping;
    /** The parsed DBC file header. */
    private final DbcHeader header;
    /** The StringTable for the DBC file. */
    private final DbcStringTable stringTable;

    /**
     * Create a new entry parser.
     *
     * @param mapping     The mapping to create the entries for.
     * @param header      The parsed DBC file header.
     * @param stringTable The StringTable for the DBC file.
     */
    public DbcEntryParser(DbcFileMapping mapping, DbcHeader header, DbcStringTable stringTable) {
        this.mapping = mapping;
        this.header = header;
        this.stringTable = stringTable;
    }

    @Override
    public T parse(DataReader reader) throws IOException, DataParsingException {
        // TODO Implement me!
        return null;
    }

    @Override
    public int getInstanceDataSize() {
        return header.getEntrySize();
    }
}
