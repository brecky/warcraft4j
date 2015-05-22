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
import nl.salp.warcraft4j.clientdata.io.DataType;
import nl.salp.warcraft4j.clientdata.io.RandomAccessDataReader;
import nl.salp.warcraft4j.clientdata.io.RandomAccessFileDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static java.lang.String.format;

/**
 * {@link DbcFileParser} implementation using random file access.
 *
 * @author Barre Dijkstra
 */
public class RandomAccessDbcFileParser extends DataReaderDbcFileParser<RandomAccessDataReader> {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RandomAccessDbcFileParser.class);

    /**
     * Create a new parser instance.
     *
     * @param dbcDirectory The path of the directory containing the DBC/DB2 files.
     *
     * @throws IllegalArgumentException When the directory is invalid.
     */
    public RandomAccessDbcFileParser(String dbcDirectory) throws IllegalArgumentException {
        super(dbcDirectory);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * Create a new parser instance.
     *
     * @param dbcDirectory The directory containing the DBC/DB2 files.
     *
     * @throws IllegalArgumentException When the directory is invalid.
     */
    public RandomAccessDbcFileParser(File dbcDirectory) throws IllegalArgumentException {
        super(dbcDirectory);
    }

    @Override
    protected RandomAccessDataReader getDataReader(File file) throws IOException {
        return new RandomAccessFileDataReader(file);
    }

    @Override
    public <T extends DbcEntry> Set<T> parse(Class<T> mappingType) throws DbcParsingException {
        try (RandomAccessDataReader reader = getDataReader(mappingType)) {
            DbcHeader header = readHeader(reader);
            DbcStringTable stringTable = readStringTable(reader, header);

            Set<T> entries = new HashSet<>(header.getEntryCount());
            reader.position(header.getEntryBlockStartingOffset());
            for (int i = 0; i < header.getEntryCount(); i++) {
                entries.add(reader.readNext(new DbcEntryParser<T>(mappingType, header, stringTable)));
            }
            return entries;
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entries of mapping type %s", mappingType.getName()), e);
        }
    }

    @Override
    public boolean isDirectAccessSupported() {
        return true;
    }

    @Override
    public <T extends DbcEntry> T parse(Class<T> mappingType, int index) throws DbcParsingException, DbcEntryNotFoundException, UnsupportedOperationException {
        try (RandomAccessDataReader reader = getDataReader(mappingType)) {
            getLogger().debug("Reading {} entry with index {} with file position {}/{}", mappingType.getName(), index, reader.position(), reader.remaining());
            DbcHeader header = readHeader(reader);
            DbcStringTable stringTable = readStringTable(reader, header);
            int position = getPositionForEntry(header, index);
            reader.position(position);
            DbcEntryParser<T> parser = new DbcEntryParser<>(mappingType, header, stringTable);
            T instance = parser.parse(reader);
            getLogger().debug("Parsed {} entry with index {} (%s)", mappingType.getName(), index, instance);
            return instance;
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entry with index %d of mapping type %s", mappingType.getName(), index), e);
        }
    }

    private long findOffsetForEntryId(int id, RandomAccessDataReader reader, DbcHeader header) throws IOException, DbcEntryNotFoundException, DbcParsingException {
        Map<Integer, Long> indexes = getEntryIndexes(header, reader);
        long offset = -1;
        if (indexes.containsKey(id)) {
            offset = indexes.get(id);
        }
        return offset;
    }

    private Map<Integer, Long> getEntryIndexes(DbcHeader header, RandomAccessDataReader reader) throws IOException, DbcParsingException {
        final Map<Integer, Long> indexes = new HashMap<>(header.getEntryCount());
        final int entrySize = header.getEntrySize();
        long position = header.getEntryBlockStartingOffset();
        for (int index = 0; index < header.getEntryCount(); index++) {
            reader.position(position);
            int id = reader.readNext(DataType.getInteger());
            indexes.put(id, position);
            position = position + entrySize;
        }
        return indexes;
    }

    /**
     * Calculate the position of an entry with a given index in the dbc file.
     *
     * @param header The dbc file.
     * @param index  The idnex of the entry.
     *
     * @return The position.
     */
    private int getPositionForEntry(DbcHeader header, int index) {
        int offset = header.getEntryBlockStartingOffset();
        offset = offset + (index * header.getEntrySize());
        LOGGER.debug("Calculated offset {} for entry with [index: {}, headerSize: {}b, entryBlockSize: {}, entries: {}, entrySize: {}b, minIdd: {}]",
                offset, index, header.getHeaderSize(), header.getEntryBlockSize(), header.getEntryCount(), header.getEntrySize(), header.getMinimumEntryId()
        );
        return offset;
    }

    @Override
    public DbcStringTable parseStringTable(String filename) throws DbcParsingException, UnsupportedOperationException {
        try (RandomAccessDataReader reader = getDataReader(getFile(filename))) {
            getLogger().debug("Reading string table from {}", filename);
            DbcHeader header = readHeader(reader);
            reader.position(header.getStringTableStartingOffset());
            DbcStringTable stringTable = readStringTable(reader, header);
            getLogger().debug("Read and parsed string table from dbc file {}.", filename);
            return stringTable;
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing string table of file %s", filename), e);
        }
    }

    @Override
    public <T extends DbcEntry> DbcStringTable parseStringTable(Class<T> mappingType) throws DbcParsingException, UnsupportedOperationException {
        return parseStringTable(getFilename(mappingType));
    }
}
