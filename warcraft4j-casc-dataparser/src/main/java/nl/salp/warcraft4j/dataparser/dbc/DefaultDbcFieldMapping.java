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

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DefaultDbcFieldMapping<T> implements DbcFieldMapping<T> {
    private final int index;
    private final String fieldName;
    private final boolean padding;
    private final Class<T> javaDataType;
    private final DbcDataType dbcDataType;
    private final int entryLength;
    private final int numberOfEntries;

    public DefaultDbcFieldMapping(int index, Class<T> javaDataType, DbcDataType dbcDataType) {
        this(index, format(DEFAULT_FIELD_NAME_MASK, index), DEFAULT_PADDING, javaDataType, dbcDataType, DEFAULT_ENTRY_LENGTH, DEFAULT_NUMBER_ENTRIES);
    }

    public DefaultDbcFieldMapping(int index, String fieldName, boolean padding, Class<T> javaDataType, DbcDataType dbcDataType, int entryLength, int numberOfEntries) {
        if (index < 0) {
            throw new DbcMappingException(format("Unable to create a DBC field mapping for field index %d", index));
        }
        if (javaDataType == null) {
            throw new DbcMappingException("Unable to create a DBC field mapping for a null Java data type");
        }
        if (dbcDataType == null) {
            throw new DbcMappingException("Unable to create a DBC field mapping for a null DBC data type");
        }

        this.index = index;
        if (isEmpty(fieldName)) {
            this.fieldName = format(DEFAULT_FIELD_NAME_MASK, index);
        } else {
            this.fieldName = fieldName;
        }
        this.padding = padding;
        this.javaDataType = javaDataType;
        this.dbcDataType = dbcDataType;
        this.entryLength = entryLength;
        this.numberOfEntries = numberOfEntries;
    }

    @Override
    public int getIndex() {
        return index;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public Class<T> getJavaDataType() {
        return javaDataType;
    }

    @Override
    public DbcDataType getDbcDataType() {
        return dbcDataType;
    }

    @Override
    public boolean isPadding() {
        return padding;
    }

    @Override
    public int getEntryLength() {
        return entryLength;
    }

    @Override
    public int getNumberOfEntries() {
        return numberOfEntries;
    }

    @Override
    public int getFieldSize() {
        return entryLength * numberOfEntries;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private int index;
        private String fieldName;
        private boolean padding;
        private Class<T> javaDataType;
        private DbcDataType dbcDataType;
        private int entryLength;
        private int numberOfEntries;

        public Builder() {
            this.padding = DEFAULT_PADDING;
            this.entryLength = DEFAULT_ENTRY_LENGTH;
            this.numberOfEntries = DEFAULT_NUMBER_ENTRIES;
        }

        public Builder<T> withIndex(int index) {
            this.index = index;
            return this;
        }

        public Builder<T> withFieldName(String fieldName) {
            this.fieldName = fieldName;
            return this;
        }

        public Builder<T> withPadding(boolean padding) {
            this.padding = padding;
            return this;
        }

        public Builder<T> withJavaDataType(Class<T> javaDataType) {
            this.javaDataType = javaDataType;
            return this;
        }

        public Builder<T> withDbcDataType(DbcDataType dbcDataType) {
            this.dbcDataType = dbcDataType = dbcDataType;
            return this;
        }

        public Builder<T> withEntryLength(int entryLength) {
            this.entryLength = entryLength;
            return this;
        }

        public Builder<T> withNumberOfEntries(int numberOfEntries) {
            this.numberOfEntries = numberOfEntries;
            return this;
        }

        public DefaultDbcFieldMapping<T> build() throws DbcMappingException {
            return new DefaultDbcFieldMapping<>(index, fieldName, padding, javaDataType, dbcDataType, entryLength, numberOfEntries);
        }
    }
}
