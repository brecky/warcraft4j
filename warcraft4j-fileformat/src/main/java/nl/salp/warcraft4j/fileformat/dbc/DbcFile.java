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

package nl.salp.warcraft4j.fileformat.dbc;

import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static nl.salp.warcraft4j.io.datatype.DataTypeFactory.getByte;
import static nl.salp.warcraft4j.util.DataTypeUtil.getAverageBytesPerCharacter;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * DBC file with methods for reading and parsing the contained data into {@link DbcEntry} instances.
 *
 * @author Barre Dijkstra
 */
public class DbcFile {
    /** The character set used in DBC files for the DBC string table. */
    private static final Charset STRINGTABLE_CHARSET = StandardCharsets.US_ASCII;
    /** The hash of the filename. */
    private final long filenameHash;
    /** The name of the DBC file. */
    private final String filename;
    /** The lock for synchronize on when parsing the DBC file. */
    private final Lock parseLock;
    /** Supplier for the reader to be used for parsing the DBC file. */
    private final Supplier<DataReader> dataReaderSupplier;
    /** The name of the DBC file. */
    private DbcHeader header;

    /**
     * Create a new DBC file instance.
     *
     * @param filenameHash       The hash of the DBC filename.
     * @param dataReaderSupplier Supplier for the data reader to be used for parsing the DBC file.
     *
     * @throws IllegalArgumentException When the name is invalid.
     */
    public DbcFile(long filenameHash, Supplier<DataReader> dataReaderSupplier) throws IllegalArgumentException {
        this(filenameHash, null, dataReaderSupplier);
    }

    /**
     * Create a new DBC file instance.
     *
     * @param filenameHash       The hash of the DBC filename.
     * @param filename           The name of the DBC file.
     * @param dataReaderSupplier Supplier for the data reader to be used for parsing the DBC file.
     *
     * @throws IllegalArgumentException When the name is invalid.
     */
    public DbcFile(long filenameHash, String filename, Supplier<DataReader> dataReaderSupplier) throws IllegalArgumentException {
        if (dataReaderSupplier == null) {
            throw new IllegalArgumentException(format("Can't create a DbcFile instance for file %d (%s) without a data reader supplier.", filenameHash, filename));
        }
        this.filenameHash = filenameHash;
        this.filename = filename;
        this.dataReaderSupplier = dataReaderSupplier;
        this.parseLock = new ReentrantLock();
    }

    /**
     * Get the {@link  DataReader} for the DBC file.
     *
     * @return The data reader for the DBC file.
     */
    private DataReader getDataReader() {
        return dataReaderSupplier.get();
    }

    /**
     * Get the hash of the filename.
     *
     * @return The filename hash.
     */
    public long getFilenameHash() {
        return filenameHash;
    }

    /**
     * Get the name of the DBC file.
     *
     * @return The filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Get the header of the DBC file.
     *
     * @return The parsed header.
     *
     * @throws DataReadingException When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     * @throws DbcParsingException  When the header could not be parsed.
     */
    public DbcHeader getHeader() throws DataReadingException, DataParsingException, DbcParsingException {
        if (header == null) {
            parseLock.lock();
            try (DataReader reader = getDataReader()) {
                header = new DbcHeaderParser().parse(reader);
            } catch (IOException e) {
                throw new DbcParsingException("Error parsing DBC header.", e);
            } finally {
                parseLock.unlock();
            }
        }
        return header;
    }

    /**
     * Get all entries from the DBC file.
     *
     * @return The parsed entries.
     *
     * @throws DbcParsingException When the entries could not be parsed.
     */
    public List<DbcEntry> getEntries() throws DbcParsingException {
        DbcHeader header = getHeader();
        try (DataReader reader = getDataReader()) {
            return IntStream.range(0, header.getEntryCount())
                    .mapToObj(i -> getEntry(i, reader))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entries for DBC file %d (%s)", filenameHash, filename), e);
        }
    }

    /**
     * Get the entry at a specific index.
     *
     * @param index The index of the entry (counting from 0).
     *
     * @return Optional of the entry or empty when no entry was available at the given index.
     *
     * @throws DbcParsingException When parsing of the entry failed.
     */
    public Optional<DbcEntry> getEntry(int index) throws DbcParsingException {
        Optional<DbcEntry> entry;
        DbcHeader header = getHeader();
        if (index < 0 || index >= header.getEntryCount()) {
            entry = Optional.empty();
        } else {
            try (DataReader reader = getDataReader()) {
                entry = Optional.of(getEntry(index, reader));
            } catch (IOException e) {
                throw new DbcParsingException(format("Error parsing entry %d for DBC file %d (%s)", index, filenameHash, filename), e);
            }
        }
        return entry;
    }

    /**
     * Read the entry with an index from a reader.
     *
     * @param index  The index of the entry.
     * @param reader The reader to read the entry from.
     *
     * @return The entry.
     *
     * @throws DbcParsingException When reading the entry failed.
     */
    private DbcEntry getEntry(int index, DataReader reader) throws DbcParsingException {
        DbcHeader header = getHeader();
        int entryOffset = header.getEntryBlockStartingOffset() + (index * header.getEntrySize());
        byte[] entryData = reader.read(DataTypeFactory.getByteArray(header.getEntrySize()), entryOffset);
        return new DbcEntry(filenameHash, header.getEntryFieldCount(), entryData);
    }

    /**
     * Get all ID values of all entries, in the same order as in the file.
     *
     * @return The ID values of the entries in the file.
     *
     * @throws DbcParsingException When the IDs could not be parsed.
     */
    public int[] getEntryIds() throws DbcParsingException {
        DbcHeader header = getHeader();
        try (DataReader reader = getDataReader()) {
            return IntStream.range(0, header.getEntryCount())
                    .map(i -> getEntryId(i, reader))
                    .toArray();
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entry IDs for DBC file %d (%s)", filenameHash, filename), e);
        }
    }

    /**
     * Get the ID of an entry at a specific index.
     *
     * @param index The index of the entry (counting from 0).
     *
     * @return Optional of the id of the entry or empty when no entry was available at the given index.
     *
     * @throws DbcParsingException When parsing of the entry failed.
     */
    public OptionalInt getEntryId(int index) throws DbcParsingException {
        OptionalInt id;
        DbcHeader header = getHeader();
        if (index < 0 || index >= header.getEntryCount()) {
            id = OptionalInt.empty();
        } else {
            try (DataReader reader = getDataReader()) {
                id = OptionalInt.of(getEntryId(index, reader));
            } catch (IOException e) {
                throw new DbcParsingException(format("Error parsing entry id %d for DBC file %d (%s)", index, filenameHash, filename), e);
            }
        }
        return id;
    }

    /**
     * Get the entry with a specific id.
     * <p>
     * This method performs a linear file read of all entry ids until a match id has been encountered, please note that this can be quite expensive.
     * </p>
     *
     * @param id The id of the entry.
     *
     * @return Optional of entry if it was available or empty when no entry was found with the given id.
     *
     * @throws DbcParsingException When parsing of the entries failed.
     */
    public Optional<DbcEntry> getEntryWithId(int id) throws DbcParsingException {
        DbcEntry entry = null;
        DbcHeader header = getHeader();
        try (DataReader reader = getDataReader()) {
            int index = 0;
            while (entry == null && index < header.getEntryCount()) {
                if (id == getEntryId(index, reader)) {
                    entry = getEntry(index, reader);
                } else {
                    index++;
                }
            }
        } catch (IOException e) {
            throw new DbcParsingException(format("Error finding entry with id %d for DBC file %d (%s)", id, filenameHash, filename), e);
        }
        return Optional.ofNullable(entry);
    }

    /**
     * Read the id from an entry with an index from a reader.
     *
     * @param index  The index of the entry.
     * @param reader The reader to read the entry from.
     *
     * @return The entry id.
     *
     * @throws DbcParsingException When reading the entry id failed.
     */
    private int getEntryId(int index, DataReader reader) throws DbcParsingException {
        DbcHeader header = getHeader();
        int entryOffset = header.getEntryBlockStartingOffset() + (index * header.getEntrySize());
        return reader.read(DataTypeFactory.getInteger(), entryOffset, ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * Parse the full string table of the DBC file.
     *
     * @return The parsed string table as a map containing the indexed values.
     *
     * @throws DbcParsingException When the string table data could not be parsed.
     */
    public DbcStringTable getStringTable() throws DbcParsingException {
        Map<Integer, String> stringTable;
        if (isStringTableEntriesPresent()) {
            stringTable = new HashMap<>();
            try (DataReader reader = getDataReader()) {
                final int tableSize = getHeader().getStringTableBlockSize();
                final int tableStart = getHeader().getStringTableStartingOffset();
                reader.position(tableStart);
                long readBytes = 0;
                while (reader.hasRemaining() && readBytes < tableSize) {
                    int position = (int) reader.position() - tableStart;
                    String value = reader.readNext(DataTypeFactory.getTerminatedString(STRINGTABLE_CHARSET));
                    int valueSize = value.length() * getAverageBytesPerCharacter(STRINGTABLE_CHARSET);

                    readBytes = readBytes + valueSize;
                    if (isNotEmpty(value)) {
                        stringTable.put(position, value);
                    }
                }
            } catch (IOException e) {
                throw new DbcParsingException("Error parsing string table.", e);
            }
        } else {
            stringTable = Collections.emptyMap();
        }
        return new DbcStringTable(stringTable);
    }

    /**
     * Parse a value from the string table.
     * <p>
     * If the id does not point to the start of a string table entry, it is considered to be a non-found id and {@code null} is returned.
     * </p>
     *
     * @param stringTableId The id of the value.
     *
     * @return The parsed value or {@code null} when no string table entry was found with the given id.
     *
     * @throws IllegalArgumentException When the id is invalid.
     * @throws DbcParsingException      When a problem occurred parsing the DBC file.
     */
    public String getStringTableValue(int stringTableId) throws IllegalArgumentException, DbcParsingException {
        if (stringTableId < 0 || stringTableId >= getHeader().getStringTableBlockSize()) {
            throw new IllegalArgumentException(format("The id %d is invalid for the string table of %s, which is %d bytes long.", stringTableId, filename, getHeader()
                    .getStringTableBlockSize()));
        }
        String value = null;
        try (DataReader reader = getDataReader()) {
            long position = getHeader().getStringTableStartingOffset() + stringTableId;
            if (reader.read(getByte(), position - 1) == 0) {
                value = reader.read(DataTypeFactory.getTerminatedString(STRINGTABLE_CHARSET), position);
            }
        } catch (IOException e) {
            throw new DbcParsingException(format("Error reading string table value %d", stringTableId), e);
        }
        return value;
    }

    /**
     * Check if the id is a valid id for a string table entry in the DBC file.
     * <p>
     * Note that this is an expensive operation that parses the DBC file, so it advised to minimise the usage in frequently called code or code that has performance requirements.
     * </p>
     *
     * @param stringTableId The id.
     *
     * @return {@code true} if the id is valid.
     *
     * @throws DbcParsingException When a problem occurred parsing the DBC file.
     */
    public boolean isValidStringTableId(int stringTableId) throws DbcParsingException {
        boolean valid = false;
        if (stringTableId >= 0 && stringTableId < getHeader().getStringTableBlockSize()) {
            try (DataReader reader = getDataReader()) {
                long position = getHeader().getStringTableStartingOffset() + stringTableId - 1;
                valid = reader.read(getByte(), position) == 0;
            } catch (IOException e) {
                throw new DbcParsingException(format("Error reading string table for validating entry with id %d", stringTableId), e);
            }
        }
        return valid;
    }

    /**
     * Get the number of entries available in the DBC file.
     *
     * @return The number of entries.
     *
     * @throws DbcParsingException When there was a problem parsing the DBC file header.
     */
    public int getNumberOfEntries() throws DbcParsingException {
        return getHeader().getEntryCount();
    }

    /**
     * Get the size of a single entry in bytes.
     *
     * @return The size of an entry in bytes.
     *
     * @throws DbcParsingException When there was a problem parsing the DBC file header.
     */
    public int getEntrySize() throws DbcParsingException {
        return getHeader().getEntrySize();
    }

    /**
     * Get the number of fields an entry has.
     *
     * @return The number of fields of an entry.
     *
     * @throws DbcParsingException When there was a problem parsing the DBC file header.
     */
    public int getNumberOfEntryFields() throws DbcParsingException {
        return getHeader().getEntryFieldCount();
    }

    /**
     * Check if there are string table entries available in the DBC file.
     *
     * @return {@code true} if there is at least 1 string table entry.
     *
     * @throws DbcParsingException When there was a problem parsing the DBC file header.
     */
    public boolean isStringTableEntriesPresent() throws DbcParsingException {
        return getHeader().getStringTableBlockSize() > 2;
    }
}
