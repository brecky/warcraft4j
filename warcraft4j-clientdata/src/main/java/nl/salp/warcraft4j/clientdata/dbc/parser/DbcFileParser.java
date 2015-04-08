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
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import nl.salp.warcraft4j.clientdata.io.ByteArrayDataReader;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.FileDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class DbcFileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbcFileParser.class);

    private <T extends DbcEntry> DbcMapping getDbcFile(Class<T> template) {
        DbcMapping dbcMapping = template.getAnnotation(DbcMapping.class);
        if (dbcMapping == null) {
            throw new IllegalArgumentException(format("Unable to parse the template class %s with no DbcFile annotation.", template.getName()));
        }
        return dbcMapping;
    }

    public DbcFile parseFile(String filename, String basePath) throws IOException, IllegalArgumentException {
        LOGGER.debug(format("[parse::%s] Parsing %s in %s", filename, filename, basePath));
        File file = new File(basePath, filename);
        try (DataReader reader = new FileDataReader(file)) {
            Timer timer = Timer.start();
            DbcHeader header = reader.readNext(new DbcHeaderParser());
            LOGGER.debug(format("[parseFile::%s] Parsing type %s with %d records with %d fields and %d bytes per record.", filename, header.getMagicString(), header.getEntryCount(), header.getEntryFieldCount(), header.getEntrySize()));
            byte[] entryData = reader.readNextBytes(header.getEntryBlockSize());
            DbcStringTable stringTable = reader.readNext(new DbcStringTableParser(header));
            LOGGER.debug(format("[parseFile::%s] Parsed %d bytes of StringBlock data to %d StringBlock entries.", filename, header.getStringTableBlockSize(), stringTable.getNumberOfEntries()));
            DbcFile dbcFile = new DbcFile(filename, header, entryData, stringTable);
            LOGGER.debug(format("[parseFile::%s] Parsed file data in %dms.", filename, timer.stop()));
            return dbcFile;
        }
    }


    public <T extends DbcEntry> Set<T> parse(Class<T> template, String basePath) throws IOException, IllegalArgumentException {
        LOGGER.debug(format("[parse::%s] Parsing %s from %s in %s", template.getSimpleName(), template.getName(), getDbcFile(template).file(), basePath));
        File file = new File(basePath, getDbcFile(template).file());
        try (DataReader reader = new FileDataReader(file)) {
            return parse(template, reader);
        }
    }

    private <T extends DbcEntry> Set<T> parse(Class<T> template, DataReader reader) throws IOException {
        Timer timer = Timer.start();
        DbcHeader header = reader.readNext(new DbcHeaderParser());
        LOGGER.debug(format("[parse::%s] Parsing type %s with %d records with %d fields and %d bytes per record.", template.getSimpleName(), header.getMagicString(), header.getEntryCount(), header.getEntryFieldCount(), header.getEntrySize()));
        byte[] entryData = reader.readNextBytes(header.getEntryBlockSize());
        DbcStringTable stringTable = reader.readNext(new DbcStringTableParser(header));
        LOGGER.debug(format("[parse::%s] Parsed %d bytes of StringTable data to %d StringTable entries.", template.getSimpleName(), header.getStringTableBlockSize(), stringTable.getNumberOfEntries()));
        Set<T> entries = parseEntries(template, entryData, header, stringTable);
        LOGGER.debug(format("[parse::%s] Parsed type with %d entries in %d ms.", template.getSimpleName(), entries.size(), timer.stop()));
        return entries;
    }

    private <T extends DbcEntry> Set<T> parseEntries(Class<T> template, byte[] data, DbcHeader header, DbcStringTable stringTable) throws IOException {
        Set<T> entries = new HashSet<>(header.getEntryCount());

        DataReader reader = new ByteArrayDataReader(data);

        DbcEntryParser<T> parser = new DbcEntryParser<>(template, header, stringTable);
        for (int i = 0; i < header.getEntryCount(); i++) {
            T instance = reader.readNext(parser);
            entries.add(instance);
        }
        return entries;
    }

    private static class Timer {
        private long startTime;
        private long endTime;

        private Timer(long startTime) {
            this.startTime = startTime;
        }

        public static Timer start() {
            return new Timer(System.currentTimeMillis());
        }

        public long stop() {
            endTime = System.currentTimeMillis();
            return getDuration();
        }

        public long getDuration() {
            return endTime - startTime;
        }
    }
}
