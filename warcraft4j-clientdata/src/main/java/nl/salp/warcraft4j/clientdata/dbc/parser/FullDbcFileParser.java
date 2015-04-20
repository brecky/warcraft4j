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

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.FileDataReader;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class FullDbcFileParser extends DataReaderDbcFileParser {
    public FullDbcFileParser(String dbcDirectory) throws IllegalArgumentException, IOException {
        super(dbcDirectory);
    }

    public FullDbcFileParser(File dbcDirectory) throws IllegalArgumentException, IOException {
        super(dbcDirectory);
    }

    @Override
    protected DataReader getDataReader(File file) throws IOException {
        return new FileDataReader(file);
    }

    @Override
    public <T extends DbcEntry> Set<T> parse(Class<T> mappingType) throws IOException, DbcParsingException {
        File file = getFile(mappingType);
        LOGGER.debug(format("[parse::%s] Parsing %s from %s in %s", mappingType.getSimpleName(), mappingType.getName(), getFilename(mappingType), dbcDirectory.getPath()));
        try (DataReader reader = new FileDataReader(file)) {
            Timer timer = Timer.start();
            DbcHeader header = readHeader(reader);
            byte[] entryData = readDataBlock(reader, header);
            DbcStringTable stringTable = readStringTable(reader, header);
            Set<T> entries = parseEntries(mappingType, entryData, header, stringTable);
            LOGGER.debug(format("[parse::%s] Parsed type with %d entries in %d ms.", mappingType.getSimpleName(), entries.size(), timer.stop()));
            return entries;
        }
    }

    @Override
    public boolean isDirectAccessSupported() {
        return false;
    }

    @Override
    public <T extends DbcEntry> T parse(Class<T> mappingType, int id) throws IOException, DbcParsingException, DbcEntryNotFoundException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Random access in files is not supported by the FullDbcFileParser.");
    }

    @Override
    public DbcStringTable parseStringTable(String filename) throws IOException, DbcParsingException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Random access in files is not supported by the FullDbcFileParser.");
    }

    @Override
    public <T extends DbcEntry> DbcStringTable parseStringTable(Class<T> mappingType) throws IOException, DbcParsingException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Random access in files is not supported by the FullDbcFileParser.");
    }


    public DbcFile parseFile(String filename, String basePath) throws IOException, IllegalArgumentException {
        LOGGER.debug(format("[parseFile::%s] Parsing %s in %s", filename, filename, basePath));
        File file = new File(basePath, filename);
        try (DataReader reader = new FileDataReader(file)) {
            Timer timer = Timer.start();
            DbcHeader header = reader.readNext(new DbcHeaderParser());
            LOGGER.debug(format("[parseFile::%s] Parsing type %s with %d records with %d fields and %d bytes per record.", filename, header.getMagicString(), header.getEntryCount(), header.getEntryFieldCount(), header.getEntrySize()));
            reader.skip(header.getEntryBlockSize()); // Skip the entries.
            DbcStringTable stringTable = reader.readNext(new DbcStringTableParser(header));
            LOGGER.debug(format("[parseFile::%s] Parsed %d bytes of StringBlock data to %d StringBlock entries.", filename, header.getStringTableBlockSize(), stringTable.getNumberOfEntries()));
            DbcFile dbcFile = new DbcFile(filename, header, stringTable);
            LOGGER.debug(format("[parseFile::%s] Parsed file data in %dms.", filename, timer.stop()));
            return dbcFile;
        }
    }
}
