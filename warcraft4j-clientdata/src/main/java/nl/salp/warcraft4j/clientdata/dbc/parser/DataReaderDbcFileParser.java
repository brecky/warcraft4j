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
import nl.salp.warcraft4j.clientdata.io.DataParsingException;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * {@link DbcFileParser} for {@link DataReader} based implementations.
 *
 * @author Barre Dijkstra
 */
abstract class DataReaderDbcFileParser<T extends DataReader> implements DbcFileParser {
    /** The logger instance. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(RandomAccessDbcFileParser.class);
    /** The directory where the DBC files are located on the filesystem. */
    protected final File dbcDirectory;

    /**
     * Create a new parser instance.
     *
     * @param dbcDirectory The path to the directory.
     *
     * @throws IllegalArgumentException When the directory is invalid.
     * @throws IOException              When there was a problem reading from the directory.
     */
    public DataReaderDbcFileParser(String dbcDirectory) throws IllegalArgumentException, IOException {
        this(new File(dbcDirectory));
    }

    /**
     * Create a new parser instance.
     *
     * @param dbcDirectory The directory.
     *
     * @throws IllegalArgumentException When the directory is invalid.
     * @throws IOException              When there was a problem reading from the directory.
     */
    public DataReaderDbcFileParser(File dbcDirectory) throws IllegalArgumentException, IOException {
        if (dbcDirectory == null) {
            throw new IllegalArgumentException("Can't create a DirectAccessDbcFileParser with a null dbc directory");
        }
        if (!dbcDirectory.exists() || !dbcDirectory.isDirectory() || !dbcDirectory.canRead()) {
            throw new IllegalArgumentException(format("Error creating a DirectAccessDbcFileParser with a non-existing or unreadable dbc directory %s", dbcDirectory.getPath()));
        }
        this.dbcDirectory = dbcDirectory.getCanonicalFile();
    }

    @Override
    public DbcHeader parseHeader(String filename) throws IOException, DbcParsingException {
        try (T dataReader = getDataReader(getFile(filename))) {
            LOGGER.debug(format("Parsing header of dbc file %s.", filename));
            return readHeader(dataReader);
        }
    }

    /**
     * Get a new data reader instance for a file.
     *
     * @param file The file to create the reader for.
     *
     * @return The data reader instance.
     *
     * @throws IOException When creating the reader for the file failed.
     */
    protected abstract T getDataReader(File file) throws IOException;


    /**
     * Get a new data reader instance for a entry mapping type.
     *
     * @param mappingType The entry mapping type.
     *
     * @return The data reader instance.
     *
     * @throws IOException When creating the read failed.
     */
    protected T getDataReader(Class<? extends DbcEntry> mappingType) throws IOException {
        return getDataReader(getFile(mappingType));
    }

    /**
     * Read and parse the dbc header from the data reader.
     * <p/>
     * This method assumes the data reader is already on the header position.
     *
     * @param dataReader The data reader to read the dbc header from.
     *
     * @return The dbc header.
     *
     * @throws IOException         When reading the dbc header failed.
     * @throws DbcParsingException When parsing the dbc header failed.
     */
    protected DbcHeader readHeader(T dataReader) throws IOException, DbcParsingException {
        final Timer timer = Timer.start();
        try {
            DbcHeader header = dataReader.readNext(new DbcHeaderParser());
            LOGGER.debug(format("Read and parsed header block in %d ms. (%d bytes with type %s).", timer.stop(), header.getHeaderSize(), header.getMagicString()));
            return header;
        } catch (DataParsingException e) {
            throw new DbcParsingException(e);
        }
    }

    /**
     * Read the data block from the data reader.
     * <p/>
     * This method assumes the data reader is already on the data block position.
     *
     * @param dataReader The data reader to read the data block from.
     * @param header     The dbc file header.
     *
     * @return The data block.
     *
     * @throws IOException         When reading the data block failed.
     * @throws DbcParsingException When parsing the data block failed.
     */
    protected byte[] readDataBlock(T dataReader, DbcHeader header) throws IOException, DbcParsingException {
        final Timer timer = Timer.start();
        try {
            byte[] dataBlock = dataReader.readNext(DataType.getByteArray(header.getEntryBlockSize()));
            LOGGER.debug(format("Read and parsed data block in %d ms (total of %d bytes for %d entries with %d fields and %d bytes per entry).", timer.stop(), header.getEntryBlockSize(), header.getEntryCount(), header.getEntryFieldCount(), header.getEntrySize()));
            return dataBlock;
        } catch (DataParsingException e) {
            throw new DbcParsingException(e);
        }
    }

    /**
     * Skip the data block on the data reader.
     *
     * @param dataReader The data reader.
     * @param header     The db file header.
     *
     * @throws IOException When skipping the data block failed.
     */
    protected void skipDataBlock(T dataReader, DbcHeader header) throws IOException {
        final Timer timer = Timer.start();
        try {
            dataReader.skip(header.getEntryBlockSize());
            LOGGER.debug(format("Skipped data block in %d ms (total of %d bytes for %d entries with %d fields and %d bytes per entry).", timer.stop(), header.getEntryBlockSize(), header.getEntryCount(), header.getEntryFieldCount(), header.getEntrySize()));
        } catch (DataParsingException e) {
            throw new DbcParsingException(e);
        }
    }

    /**
     * Read and parse the string table from the data reader.
     * <p/>
     * This method assumes the data reader is already on the string table position.
     *
     * @param dataReader The data reader to read the string table from.
     * @param header     The dbc file header.
     *
     * @return The string table.
     *
     * @throws IOException         When reading the string table failed.
     * @throws DbcParsingException When parsing the string table failed.
     */
    protected DbcStringTable readStringTable(T dataReader, DbcHeader header) throws IOException, DbcParsingException {
        final Timer timer = Timer.start();
        try {
            DbcStringTable stringTable = dataReader.readNext(new DbcStringTableParser(header));
            LOGGER.debug(format("Read and parsed string table in %d ms (%d bytes of data with %d entries).", timer.stop(), header.getStringTableBlockSize(), stringTable.getNumberOfEntries()));
            return stringTable;
        } catch (DataParsingException e) {
            throw new DbcParsingException(e);
        }
    }

    @Override
    public <T extends DbcEntry> DbcFile parseMetaData(Class<T> mappingType) throws IOException, DbcParsingException {
        return parseMetaData(getFilename(mappingType));
    }

    @Override
    public DbcFile parseMetaData(String filename) throws IOException, DbcParsingException {
        final Timer timer = Timer.start();
        try (T reader = getDataReader(getFile(filename))) {
            LOGGER.debug(format("Parsing dbc file meta-data of file %s.", filename));
            DbcHeader header = readHeader(reader);
            skipDataBlock(reader, header);
            DbcStringTable stringTable = readStringTable(reader, header);
            return new DbcFile(filename, header, stringTable);
        } finally {
            LOGGER.debug(format("Finished parsing dbc file meta-data of file %s in %d ms.", filename, timer.stop()));
        }
    }

    /**
     * Get the full file reference for a dbc file.
     *
     * @param fileName The name of the dbc file.
     *
     * @return The file referenec to the full path of the file.
     *
     * @throws IOException When the file doesn't exist or can't be read.
     */
    protected final File getFile(String fileName) throws IOException {
        File file = new File(dbcDirectory, fileName);
        if (!file.exists() || !file.isFile() || !file.canRead()) {
            throw new IOException(format("Can't read the header of the dbc file %s in the directory %s due to the file missing or being unreadable", fileName, dbcDirectory.getPath()));
        }
        return file.getCanonicalFile();
    }

    protected final File getFile(Class<? extends DbcEntry> mappingType) throws IOException {
        return getFile(getFilename(mappingType));
    }

    /**
     * Get name of the dbc file that is mapped by the entry mapping type.
     *
     * @param mappingType The entry mapping type.
     * @param <T>         The entry type.
     *
     * @return The dbc filename.
     *
     * @throws IllegalArgumentException
     */
    protected <T extends DbcEntry> String getFilename(Class<T> mappingType) {
        DbcMapping dbcMapping = mappingType.getAnnotation(DbcMapping.class);
        if (dbcMapping == null) {
            throw new IllegalArgumentException(format("Unable to parse the template class %s with no DbcFile annotation.", mappingType.getName()));
        }
        if (isEmpty(dbcMapping.file())) {
            throw new IllegalArgumentException(format("No DBC or DB2 file specified on the DbcFile annotation of template class %s", mappingType.getName()));
        }
        return dbcMapping.file();
    }

    /**
     * Parse the entries from the provided data.
     *
     * @param mappingType The entry mapping type.
     * @param data        The raw dbc entry data block.
     * @param header      The dbc file header.
     * @param stringTable The dbc file string table.
     * @param <T>         The type of the entry.
     *
     * @return The parsed entries.
     *
     * @throws IOException         When reading the data failed.
     * @throws DbcParsingException When parsing the entries failed.
     */
    protected <T extends DbcEntry> Set<T> parseEntries(Class<T> mappingType, byte[] data, DbcHeader header, DbcStringTable stringTable) throws IOException, DbcParsingException {
        Set<T> entries = new HashSet<>(header.getEntryCount());
        DataReader reader = new ByteArrayDataReader(data);
        DbcEntryParser<T> parser = new DbcEntryParser<>(mappingType, header, stringTable);
        for (int i = 0; i < header.getEntryCount(); i++) {
            try {
                T instance = reader.readNext(parser);
                entries.add(instance);
            } catch (DataParsingException e) {
                throw new DbcParsingException(format("Error parsing dbc entry %d of type %s", i, mappingType.getName()));
            }
        }
        return entries;
    }

    /**
     * Simple timer for timing execution time.
     */
    protected static class Timer {
        /** The start time (epoch). */
        private long startTime;
        /** The end time (epoch). */
        private long endTime;

        /**
         * Create a new timer instance.
         *
         * @param startTime The time the timer started.
         */
        private Timer(long startTime) {
            this.startTime = startTime;
        }

        /**
         * Start a new timer.
         *
         * @return The timer.
         */
        public static Timer start() {
            return new Timer(System.currentTimeMillis());
        }

        /**
         * Stop the timer.
         *
         * @return The timed duration in milliseconds.
         */
        public long stop() {
            endTime = System.currentTimeMillis();
            return getDuration();
        }

        /**
         * Get the timed duration in milliseconds (will be negative while still running).
         *
         * @return The timed duration.
         */
        public long getDuration() {
            return endTime - startTime;
        }
    }
}
