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

import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.util.LazyInstance;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * Client database file entry.
 *
 * @author Barre Dijkstra
 */
public class DbcEntry {
    /** The datatype for a stringtable reference. */
    private static final DataType<Integer> STRINGTABLE_REF_DATATYPE = DataTypeFactory.getInteger();
    /** The datatype for the entry id. */
    private static final DataType<Integer> ID_DATATYPE = DataTypeFactory.getInteger();
    /** The offset for the id. */
    private static final int ID_OFFSET = 0;
    /** The hash of the name of the DBC file containing the entry. */
    private final long filenameHash;
    /** The number of fields. */
    private final int fieldCount;
    /** Lazy instance of the unparsed entry data. */
    private transient final LazyInstance<byte[]> entryData;
    /** Cached hashcode value for the entry. */
    private int hash;

    /**
     * Create a new entry instance.
     *
     * @param filenameHash The hash of the name of the DBC file containing the entry.
     * @param fieldCount   The number of fields.
     * @param entryData    The unparsed entry data.
     *
     * @throws IllegalArgumentException When invalid arguments were provided.
     */
    public DbcEntry(long filenameHash, int fieldCount, byte[] entryData) throws IllegalArgumentException {
        this(filenameHash, fieldCount, new LazyInstance<>(entryData));
        if (entryData == null || entryData.length == 0) {
            throw new IllegalArgumentException("Can't create a DBC entry with no data.");
        }
    }

    /**
     * Create a new entry instance.
     *
     * @param filenameHash      The hash of the name of the DBC file containing the entry.
     * @param fieldCount        The number of fields.
     * @param entryDataSupplier Supplier for the unparsed entry data.
     *
     * @throws IllegalArgumentException When invalid arguments were provided.
     */
    public DbcEntry(long filenameHash, int fieldCount, Supplier<byte[]> entryDataSupplier) throws IllegalArgumentException {
        this(filenameHash, fieldCount, new LazyInstance<>(entryDataSupplier));
        if (entryDataSupplier == null) {
            throw new IllegalArgumentException("Can't create a DBC entry without a data supplier.");
        }
    }

    /**
     * Create a new entry instance.
     *
     * @param filenameHash The hash of the name of the DBC file containing the entry.
     * @param fieldCount   The number of fields.
     * @param entryData    The unparsed entry data as a {@link LazyInstance}.
     *
     * @throws IllegalArgumentException When invalid arguments were provided.
     */
    public DbcEntry(long filenameHash, int fieldCount, LazyInstance<byte[]> entryData) throws IllegalArgumentException {
        if (entryData == null) {
            throw new IllegalArgumentException("Can't create a DBC entry with no data.");
        }
        this.filenameHash = filenameHash;
        this.fieldCount = fieldCount;
        this.entryData = entryData;
        this.hash = 0;
    }

    /**
     * Get the hash of the name of the DBC file containing the entry.
     *
     * @return The DBC filename hash.
     *
     * @see DbcFile#getFilenameHash()
     */
    public long getFilenameHash() {
        return filenameHash;
    }

    /**
     * Get the number of entry fields.
     *
     * @return The number of fields.
     *
     * @see DbcFile#getNumberOfEntryFields()
     */
    public int getFieldCount() {
        return fieldCount;
    }

    /**
     * Get the size of the entry in bytes.
     * <p>
     * Please note that this reads and initialises the entry data if it hasn't been read yet.
     * </p>
     *
     * @return The size in bytes.
     */
    public int getEntrySize() {
        return entryData.get().length;
    }

    /**
     * Get the id of the entry.
     *
     * @return The id.
     *
     * @throws DbcParsingException When reading the id failed.
     */
    public int getId() throws DbcParsingException {
        return read(ID_OFFSET, ID_DATATYPE);
    }

    /**
     * Get a value from the entry.
     *
     * @param offset   The offset of the value from the start of the entry data in bytes.
     * @param dataType The datatype of the value.
     * @param <T>      The type of the value.
     *
     * @return The value.
     *
     * @throws DbcParsingException When reading the value failed.
     */
    public <T> T getValue(int offset, DataType<T> dataType) throws DbcParsingException {
        return read(offset, dataType);
    }

    /**
     * Get a field as a resolved stringtable value.
     *
     * @param offset      The offset of the value from the start of the entry data in bytes.
     * @param stringTable The stringtable to resolve the field value on.
     *
     * @return Optional containing the stringtable value if a value was available for the field.
     *
     * @throws DbcParsingException When reading the field or resolving the stringtable value failed.
     */
    public Optional<String> getStringTableValue(int offset, DbcStringTable stringTable) throws DbcParsingException {
        if (stringTable == null) {
            throw new DbcParsingException("Unable to get a StringTable value from a null StringTable.");
        }
        int stringTableOffset = read(offset, STRINGTABLE_REF_DATATYPE);
        return stringTable.getEntry(stringTableOffset);
    }

    /**
     * Get the unparsed entry data.
     * <p>
     * Please note that this reads and initialises the entry data if it hasn't been read yet.
     * </p>
     *
     * @return The unparsed entry data.
     */
    public byte[] getUnparsedEntryData() {
        return entryData.get();
    }

    /**
     * Read a value from the unparsed data.
     *
     * @param offset   The offset of the value from the start of the entry data in bytes.
     * @param dataType The datatype of the value.
     * @param <T>      The type of the value.
     *
     * @return The value.
     *
     * @throws DbcParsingException When reading the value failed.
     */
    private <T> T read(int offset, DataType<T> dataType) throws DbcParsingException {
        if (dataType == null) {
            throw new DbcParsingException("Unable to get a value for a null data type.");
        }
        if (offset < 0 || offset > (getEntrySize() - dataType.getLength())) {
            throw new DbcParsingException(format("Unable to get a %d byte value from a %d byte entry from offset %d", dataType.getLength(), getEntrySize(), offset));
        }
        return dataType.readNext(ByteBuffer.wrap(entryData.get(), offset, dataType.getLength()), ByteOrder.LITTLE_ENDIAN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        if (hash == 0) {
            hash = new HashCodeBuilder()
                    .append(filenameHash)
                    .append(getId())
                    .append(fieldCount)
                    .toHashCode();
        }
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}