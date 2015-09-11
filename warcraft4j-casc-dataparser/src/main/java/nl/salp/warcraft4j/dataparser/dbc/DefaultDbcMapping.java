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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public abstract class DefaultDbcMapping<T> implements DbcMapping<T> {
    private final long dbcFilenameHash;
    private final Map<Integer, DbcFieldMapping<?>> fieldsMappings;
    private final int entrySize;

    public DefaultDbcMapping(long dbcFilenameHash, Set<DbcFieldMapping<?>> fieldMappings) {
        this.dbcFilenameHash = dbcFilenameHash;
        this.fieldsMappings = Optional.ofNullable(fieldMappings)
                .filter(f -> !f.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Unable to create a DBC mapping with no field mappings"))
                .stream()
                .collect(Collectors.toMap(DbcFieldMapping::getIndex, m -> m));
        this.entrySize = this.fieldsMappings.values().stream()
                .mapToInt(DbcFieldMapping::getFieldSize)
                .sum();
    }

    @Override
    public long getDbcFilenameHash() {
        return dbcFilenameHash;
    }

    @Override
    public int getFieldCount() {
        return fieldsMappings.size();
    }

    @Override
    public int getFieldEntryLength(int index) throws DbcMappingException {
        return getField(index).getEntryLength();
    }

    @Override
    public Class<?> getFieldJavaType(int index) throws DbcMappingException {
        return getField(index).getJavaDataType();
    }

    @Override
    public String getFieldName(int index) throws DbcMappingException {
        return getField(index).getFieldName();
    }

    @Override
    public int getFieldNumberOfEntries(int index) throws DbcMappingException {
        return getField(index).getNumberOfEntries();
    }

    @Override
    public int getFieldSize(int index) throws DbcMappingException {
        return getField(index).getFieldSize();
    }

    @Override
    public boolean isArray(int index) throws DbcMappingException {
        return getField(index).getNumberOfEntries() > 1;
    }

    public boolean isValidForMapping(DbcEntry entry) {
        boolean valid = entry != null;
        valid = valid && (entry.getFilenameHash() == dbcFilenameHash);
        valid = valid && (entry.getFieldCount() == getFieldCount());
        valid = valid && (entry.getEntrySize() == entrySize);
        return valid;
    }

    protected DbcFieldMapping<?> getField(int index) throws DbcMappingException {
        return Optional.ofNullable(fieldsMappings.get(index))
                .orElseThrow(() -> new DbcMappingException(format("No field mapping available with index %d for DBC mapping %s with %d fields",
                        index, getDbcFilenameHash(), getFieldCount())));
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
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