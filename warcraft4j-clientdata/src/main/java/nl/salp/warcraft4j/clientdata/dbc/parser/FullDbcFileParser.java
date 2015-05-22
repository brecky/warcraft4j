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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import static java.lang.String.format;

/**
 * {@link DbcFileParser} implementation for reading full files.
 *
 * @author Barre Dijkstra
 */
public class FullDbcFileParser extends DataReaderDbcFileParser {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FullDbcFileParser.class);

    /**
     * Create a new parser instance.
     *
     * @param dbcDirectory The path to the directory where the DBC/DB2 files that are used are stored.
     *
     * @throws IllegalArgumentException When the directory is invalid.
     */
    public FullDbcFileParser(String dbcDirectory) throws IllegalArgumentException {
        super(dbcDirectory);
    }

    /**
     * Create a new parser instance.
     *
     * @param dbcDirectory The directory where the DBC/DB2 files that are used are stored.
     *
     * @throws IllegalArgumentException When the directory is invalid.
     */
    public FullDbcFileParser(File dbcDirectory) throws IllegalArgumentException {
        super(dbcDirectory);
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    @Override
    protected DataReader getDataReader(File file) throws IOException {
        return new FileDataReader(file);
    }

    @Override
    public <T extends DbcEntry> Set<T> parse(Class<T> mappingType) throws DbcParsingException {
        try (DataReader reader = new FileDataReader(getFile(mappingType))) {
            getLogger().debug("Parsing instances for {} from dbc file {} in directory {}", mappingType.getName(), getFilename(mappingType), dbcDirectory.getPath());
            DbcHeader header = readHeader(reader);
            byte[] entryData = readDataBlock(reader, header);
            DbcStringTable stringTable = readStringTable(reader, header);
            Set<T> entries = parseEntries(mappingType, entryData, header, stringTable);
            getLogger().debug("Finished parsing {} {} instances with {} stringTable entries.", entries.size(), mappingType.getName(), stringTable.getNumberOfEntries());
            return entries;
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entries of mapping type %s", mappingType.getName()), e);
        }
    }

    @Override
    public boolean isDirectAccessSupported() {
        return false;
    }

    /**
     * This method will always throw a {@code UnsupportedOperationException}.
     * {@inheritDoc}
     */
    @Override
    public <T extends DbcEntry> T parse(Class<T> mappingType, int id) throws DbcParsingException, DbcEntryNotFoundException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Random access in files is not supported by the FullDbcFileParser.");
    }

    /**
     * This method will always throw a {@code UnsupportedOperationException}.
     * {@inheritDoc}
     */
    @Override
    public DbcStringTable parseStringTable(String filename) throws DbcParsingException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Random access in files is not supported by the FullDbcFileParser.");
    }

    /**
     * This method will always throw a {@code UnsupportedOperationException}.
     * {@inheritDoc}
     */
    @Override
    public <T extends DbcEntry> DbcStringTable parseStringTable(Class<T> mappingType) throws DbcParsingException, UnsupportedOperationException {
        throw new UnsupportedOperationException("Random access in files is not supported by the FullDbcFileParser.");
    }
}
