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
package nl.salp.warcraft4j.dataparser.dbc.mapper.json;

import java.nio.ByteOrder;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.salp.warcraft4j.dataparser.dbc.DbcType;
import nl.salp.warcraft4j.dataparser.dbc.mapper.DbcFieldMapping;
import nl.salp.warcraft4j.dataparser.dbc.mapping.DbcDataType;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
class JsonDbcFieldMapping implements DbcFieldMapping {
    private static final int ENTRIES_DEFAULT = 1;
    private static final int LENGTH_DEFAULT = 1;
    private static final boolean PADDING_DEFAULT = false;
    private static final boolean KNOWN_MEANING_DEFAULT = true;
    private static final String ENDIANESS_DEFAULT = "default";
    private static final String ENDIANESS_BIG = "big";
    private static final String ENDIANESS_LITTLE = "little";

    @JsonProperty(required = true)
    private String name;
    @JsonProperty(required = true)
    private int column;
    @JsonProperty(required = true)
    private String type;
    @JsonProperty(defaultValue = "1")
    private int entries;
    @JsonProperty(defaultValue = "0")
    private int length;
    @JsonProperty(defaultValue = "false")
    private boolean padding;
    @JsonProperty(defaultValue = "true")
    private boolean knownMeaning;
    @JsonProperty(defaultValue = ENDIANESS_DEFAULT)
    private String endianness;
    @JsonProperty(required = false)
    private String references;

    public JsonDbcFieldMapping() {
        this.entries = ENTRIES_DEFAULT;
        this.length = LENGTH_DEFAULT;
        this.padding = PADDING_DEFAULT;
        this.knownMeaning = KNOWN_MEANING_DEFAULT;
        this.endianness = ENDIANESS_DEFAULT;
    }

    public JsonDbcFieldMapping(String name, int column, String type, int entries, int length, boolean padding, boolean knownMeaning, String endianness, String references) {
        this.name = name;
        this.column = column;
        this.type = type;
        this.entries = entries;
        this.length = length;
        this.padding = padding;
        this.knownMeaning = knownMeaning;
        this.endianness = endianness;
        this.references = references;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getDbcColumn() {
        return column;
    }

    @Override
    public DbcDataType getDataType() {
        return DbcDataType.valueOf(type);
    }

    @Override
    public int getEntryCount() {
        return entries;
    }

    @Override
    public int getEntryLength() {
        return length;
    }

    @Override
    public boolean isPadding() {
        return padding;
    }

    @Override
    public ByteOrder getByteOrder() {
        ByteOrder order = null;
        if (ENDIANESS_BIG.equalsIgnoreCase(endianness)) {
            order = ByteOrder.BIG_ENDIAN;
        } else if (ENDIANESS_LITTLE.equalsIgnoreCase(endianness)) {
            order = ByteOrder.LITTLE_ENDIAN;
        }
        // FIXME ENDIANESS_DEFAULT now leaves the value null.
        return order;
    }

    @Override
    public DbcType getReferencedType() {
        DbcType ref = null;
        if (isNotEmpty(references)) {
            ref = DbcType.valueOf(references);
        }
        return ref;
    }

    @Override
    public boolean isMeaningKnown() {
        return knownMeaning;
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
