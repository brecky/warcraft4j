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

package nl.salp.warcraft4j.dataparser.dbc;

import nl.salp.warcraft4j.dataparser.dbc.mapping.DbcMapping;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.RandomAccessDataReader;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

import static java.lang.String.format;
import static nl.salp.warcraft4j.io.datatype.DataTypeFactory.getByte;
import static nl.salp.warcraft4j.io.datatype.DataTypeFactory.getTerminatedString;
import static nl.salp.warcraft4j.DataTypeUtil.getAverageBytesPerCharacter;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * DBC file with methods for reading and parsing the contained data into {@link DbcEntry} instances.
 *
 * @author Barre Dijkstra
 */
public class DbcFile {
    /** The character set used in DBC files for the DBC string table. */
    private static final Charset STRINGTABLE_CHARSET = StandardCharsets.US_ASCII;
    /** The name of the DBC file. */
    private final String dbcName;
    /** The lock for synchronize on when parsing the DBC file. */
    private final Lock parseLock;
    /** Supplier for the reader to be used for parsing the DBC file. */
    private final Supplier<RandomAccessDataReader> dataReaderSupplier;
    /** The name of the DBC file. */
    private DbcHeader header;

    /**
     * Create a new DBC file instance.
     *
     * @param dbcName            The name of the DBC file.
     * @param dataReaderSupplier Supplier for the data reader to be used for parsing the DBC file.
     *
     * @throws IllegalArgumentException When the name is invalid.
     */
    public DbcFile(String dbcName, Supplier<RandomAccessDataReader> dataReaderSupplier) throws IllegalArgumentException {
        if (isEmpty(dbcName)) {
            throw new IllegalArgumentException("Can't create a DbcFile instance without no file name.");
        }
        try (RandomAccessDataReader reader = dataReaderSupplier.get()) {
            // no-op
        } catch (IOException e) {
            throw new IllegalArgumentException(format("Error opening DbcFile %s", dbcName), e);
        }
        this.dbcName = dbcName;
        this.dataReaderSupplier = dataReaderSupplier;
        this.parseLock = new ReentrantLock();
    }

    /**
     * Get the {@link  RandomAccessDataReader} for the DBC file.
     *
     * @return The data reader for the DBC file.
     */
    private RandomAccessDataReader getDataReader() {
        return dataReaderSupplier.get();
    }

    /**
     * Get the name of the DBC file.
     *
     * @return The name of the file.
     */
    public String getDbcName() {
        return dbcName;
    }

    /**
     * Parse the header of the DBC file.
     *
     * @return The parsed header.
     *
     * @throws DbcParsingException When the header could not be parsed.
     */
    public DbcHeader getHeader() throws DbcParsingException {
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
     * Parse all entries in the DBC file.
     *
     * @param mappingType The entry mapping type to parse the entries to.
     * @param <T>         The type of the entries.
     *
     * @return The parsed entries.
     *
     * @throws IllegalArgumentException When the mapping type is invalid for the DBC file.
     * @throws DbcParsingException      When the entries could not be parsed.
     */
    public <T extends DbcEntry> Collection<T> parseEntries(Class<T> mappingType) throws IllegalArgumentException, DbcParsingException {
        if (!isValidMappingForFile(mappingType)) {
            throw new IllegalArgumentException(format("Can't parse the entries of dbc file %s with the mapping type %s", dbcName, mappingType.getName()));
        }
        DbcHeader header = getHeader();
        DbcStringTable stringTable = parseStringTable();
        try (RandomAccessDataReader reader = getDataReader()) {
            Collection<T> entries = new HashSet<>(header.getEntryCount());
            reader.position(header.getEntryBlockStartingOffset());
            for (int i = 0; i < header.getEntryCount(); i++) {
                entries.add(reader.readNext(new DbcEntryParser<>(mappingType, header, stringTable)));
            }
            return entries;
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entries of mapping type %s", mappingType.getName()), e);
        }
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
        try (RandomAccessDataReader reader = getDataReader()) {
            int[] ids = new int[header.getEntryCount()];
            reader.position(header.getEntryBlockStartingOffset());
            for (int i = 0; i < header.getEntryCount(); i++) {
                long offset = header.getEntryBlockStartingOffset() + (i * header.getEntrySize());
                ids[i] = reader.read(DataTypeFactory.getInteger(), offset, ByteOrder.LITTLE_ENDIAN);
            }
            return ids;
        } catch (IOException e) {
            throw new DbcParsingException("Error parsing entry identifiers", e);
        }
    }

    /**
     * Parse a single entry from the DBC file.
     *
     * @param index       The index of the entry (counting from 0, must be {@code 0 >= index < number of entries}).
     * @param mappingType The entry mapping type to parse the entry to.
     * @param <T>         The type of the entry.
     *
     * @return The parsed entry.
     *
     * @throws IllegalArgumentException When the index or mapping type are invalid for the DBC file.
     * @throws DbcParsingException      When the entry could not be parsed.
     */
    public <T extends DbcEntry> T parseEntryWithIndex(int index, Class<T> mappingType) throws IllegalArgumentException, DbcParsingException {
        if (index < 0 || index >= getNumberOfEntries()) {
            throw new DbcEntryNotFoundException(index, mappingType);
        }
        if (!isValidMappingForFile(mappingType)) {
            throw new IllegalArgumentException(format("Can't parse entry %d of dbc file %s with the mapping type %s", index, dbcName, mappingType.getName()));
        }
        DbcHeader header = getHeader();
        DbcStringTable stringTable = parseStringTable();
        try (RandomAccessDataReader reader = getDataReader()) {
            int offset = header.getEntrySize() * index;
            reader.position(header.getEntryBlockStartingOffset() + offset);
            return reader.readNext(new DbcEntryParser<>(mappingType, header, stringTable));
        } catch (IOException e) {
            throw new DbcParsingException(format("Error parsing entry with index %d of mapping type %s", index, mappingType.getName()), e);
        }
    }

    /**
     * Parse the full string table of the DBC file.
     *
     * @return The parsed string table as a map containing the indexed values.
     *
     * @throws DbcParsingException When the string table data could not be parsed.
     */
    public DbcStringTable parseStringTable() throws DbcParsingException {
        Map<Integer, String> stringTable;
        if (isStringTableEntriesPresent()) {
            stringTable = new HashMap<>();
            try (RandomAccessDataReader reader = getDataReader()) {
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
    public String parseStringTableValue(int stringTableId) throws IllegalArgumentException, DbcParsingException {
        if (stringTableId < 0 || stringTableId >= getHeader().getStringTableBlockSize()) {
            throw new IllegalArgumentException(format("The id %d is invalid for the string table of %s, which is %d bytes long.", stringTableId, dbcName, getHeader().getStringTableBlockSize()));
        }
        String value = null;
        try (RandomAccessDataReader reader = getDataReader()) {
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
            try (RandomAccessDataReader reader = getDataReader()) {
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

    /**
     * Check if the entry mapping type is valid for the DBC file.
     *
     * @param mappingType The entry mapping type.
     * @param <T>         The type of the mapping.
     *
     * @return {@code true} if the type is valid for the DBC file, {@code false} if not.
     */
    public <T extends DbcEntry> boolean isValidMappingForFile(Class<T> mappingType) {
        return (mappingType != null) && (mappingType.isAnnotationPresent(DbcMapping.class)) && (dbcName.equals(mappingType.getAnnotation(DbcMapping.class).file()));
    }
}
