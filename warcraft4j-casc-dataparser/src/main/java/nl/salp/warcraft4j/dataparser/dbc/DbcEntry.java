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

import nl.salp.warcraft4j.LazyInstance;
import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcEntry {
    private static final DataType<Integer> STRINGTABLE_REF_DATATYPE = DataTypeFactory.getInteger();
    private static final DataType<Integer> ID_DATATYPE = DataTypeFactory.getInteger();
    private static final int ID_OFFSET = 0;
    private final long filenameHash;
    private final int fieldCount;
    private transient final LazyInstance<byte[]> entryData;
    private int hash;

    public DbcEntry(long filenameHash, int fieldCount, byte[] entryData) throws IllegalArgumentException {
        this(filenameHash, fieldCount, new LazyInstance<>(entryData));
        if (entryData == null || entryData.length == 0) {
            throw new IllegalArgumentException("Can't create a DBC entry with no data.");
        }
    }

    public DbcEntry(long filenameHash, int fieldCount, Supplier<byte[]> entryDataSupplier) throws IllegalArgumentException {
        this(filenameHash, fieldCount, new LazyInstance<>(entryDataSupplier));
        if (entryDataSupplier == null) {
            throw new IllegalArgumentException("Can't create a DBC entry without a data supplier.");
        }
    }

    public DbcEntry(long filenameHash, int fieldCount, LazyInstance<byte[]> entryData) throws IllegalArgumentException {
        if (entryData == null) {
            throw new IllegalArgumentException("Can't create a DBC entry with no data.");
        }
        this.filenameHash = filenameHash;
        this.fieldCount = fieldCount;
        this.entryData = entryData;
        this.hash = 0;
    }

    public long getFilenameHash() {
        return filenameHash;
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public int getEntrySize() {
        return entryData.get().length;
    }

    public int getId() throws DbcParsingException {
        return read(ID_OFFSET, ID_DATATYPE);
    }

    public <T> T getValue(int offset, DataType<T> dataType) throws DbcParsingException {
        return read(offset, dataType);
    }

    public Optional<String> getStringTableValue(int offset, DbcStringTable stringTable) throws DbcParsingException {
        if (stringTable == null) {
            throw new DbcParsingException("Unable to get a StringTable value from a null StringTable.");
        }
        int stringTableOffset = read(offset, STRINGTABLE_REF_DATATYPE);
        return stringTable.getEntry(stringTableOffset);
    }

    public byte[] getRawEntryData() {
        return entryData.get();
    }

    private <T> T read(int offset, DataType<T> dataType) {
        if (dataType == null) {
            throw new DbcParsingException("Unable to get a value for a null data type.");
        }
        if (offset < 0 || offset > (getEntrySize() - dataType.getLength())) {
            throw new DbcParsingException(format("Unable to get a %d byte value from a %d byte entry from offset %d", dataType.getLength(), getEntrySize(), offset));
        }
        return dataType.readNext(ByteBuffer.wrap(entryData.get(), offset, dataType.getLength()), ByteOrder.LITTLE_ENDIAN);
    }

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

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}