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
package nl.salp.warcraft4j.dev.dbc.analysis;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.dev.dbc.DbcUtils;

import java.lang.reflect.Field;
import java.util.Collection;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class UnknownDbcMappingResult {
    private final Class<? extends DbcEntry> mappingType;
    private final int fieldCount;
    private final Collection<Field> unknownFields;

    public UnknownDbcMappingResult(Class<? extends DbcEntry> mappingType, int fieldCount, Collection<Field> unknownFields) {
        this.mappingType = mappingType;
        this.fieldCount = fieldCount;
        this.unknownFields = unknownFields;
    }

    public Class<? extends DbcEntry> getMappingType() {
        return mappingType;
    }

    public String getMappedFile() {
        return DbcUtils.getMappedFile(mappingType);
    }

    public int getFieldCount() {
        return fieldCount;
    }

    public Collection<Field> getUnknownFields() {
        return unknownFields;
    }

    public int getUnknownFieldCount() {
        return unknownFields.size();
    }

    public float getUnknownPercentage() {
        return (((float) getUnknownFieldCount()) / fieldCount) * 100;
    }

    public float getKnownPercentage() {
        return 100 - getUnknownPercentage();
    }
}
