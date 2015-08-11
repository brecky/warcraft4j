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
package nl.salp.warcraft4j.data.dbc.mapper.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import nl.salp.warcraft4j.data.dbc.DbcEntry;
import nl.salp.warcraft4j.data.dbc.DbcType;
import nl.salp.warcraft4j.data.dbc.mapper.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapper.DbcFileMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
class JsonDbcFileMapping implements DbcFileMapping {
    private static final String MAPPING_DEFAULT = "field";

    @JsonProperty(required = true)
    private String filename;
    @JsonProperty(required = true)
    private String targetClass;
    @JsonProperty(required = true)
    private String dbcType;
    @JsonProperty(defaultValue = MAPPING_DEFAULT)
    private String mapping;
    @JsonProperty(required = true)
    private JsonDbcFieldMapping[] fields;

    public JsonDbcFileMapping() {
        this.mapping = MAPPING_DEFAULT;
        this.fields = new JsonDbcFieldMapping[0];
    }

    public JsonDbcFileMapping(String filename, String targetClass, String dbcType, String mapping, JsonDbcFieldMapping[] fields) {
        this.filename = filename;
        this.targetClass = targetClass;
        this.dbcType = dbcType;
        this.mapping = mapping;
        this.fields = fields;
    }

    @Override
    public String getDbcFilename() {
        return filename;
    }

    @Override
    public Class<? extends DbcEntry> getTargetClass() {
        try {
            return (Class<? extends DbcEntry>) Class.forName(targetClass);
        } catch (ClassNotFoundException e) {
            // TODO Replace with parsing exception
            throw new RuntimeException(format("Can't find target class %s for mapping of DBC file %s.", targetClass, filename));
        } catch (ClassCastException e) {
            // TODO Replace with parsing exception
            throw new RuntimeException(format("Target class %s for mapping of DBC file %s is not a DbcEntry.", targetClass, filename));
        }
    }

    @Override
    public DbcType getDbcType() {
        DbcType type = DbcType.valueOf(dbcType);
        if (type == null) {
            // TODO Replace with parsing exception
            throw new RuntimeException(format("DBC type %s of mapping from DBC file %s to target class %s is unknown.", dbcType, filename, targetClass));
        }
        return type;
    }

    @Override
    public String getMappingMethod() {
        return mapping;
    }

    @Override
    public DbcFieldMapping[] getFieldMappings() {
        return fields;
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
