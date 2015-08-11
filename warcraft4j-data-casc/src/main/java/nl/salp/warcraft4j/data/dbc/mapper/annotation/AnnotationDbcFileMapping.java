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
package nl.salp.warcraft4j.data.dbc.mapper.annotation;

import nl.salp.warcraft4j.data.dbc.DbcEntry;
import nl.salp.warcraft4j.data.dbc.DbcType;
import nl.salp.warcraft4j.data.dbc.mapper.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapper.DbcFileMapping;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
class AnnotationDbcFileMapping implements DbcFileMapping {
    private final Class<? extends DbcEntry> targetClass;
    private final String dbcFilename;
    private final DbcType dbcType;
    private final String mappingMethod = "field"; // TODO Refactor.
    private final DbcFieldMapping[] fieldMappings;

    public AnnotationDbcFileMapping(Class<? extends DbcEntry> targetClass, String dbcFilename, DbcType dbcType, DbcFieldMapping[] fieldMappings) {
        this.targetClass = targetClass;
        this.dbcFilename = dbcFilename;
        this.dbcType = dbcType;
        this.fieldMappings = fieldMappings;
    }

    @Override
    public String getDbcFilename() {
        return dbcFilename;
    }

    @Override
    public Class<? extends DbcEntry> getTargetClass() {
        return targetClass;
    }

    @Override
    public DbcType getDbcType() {
        return dbcType;
    }

    @Override
    public String getMappingMethod() {
        return mappingMethod;
    }

    @Override
    public DbcFieldMapping[] getFieldMappings() {
        return fieldMappings;
    }
}
