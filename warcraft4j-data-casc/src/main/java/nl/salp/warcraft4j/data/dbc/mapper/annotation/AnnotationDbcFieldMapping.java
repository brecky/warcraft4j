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

import java.nio.ByteOrder;

import nl.salp.warcraft4j.data.dbc.DbcType;
import nl.salp.warcraft4j.data.dbc.mapper.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcDataType;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
class AnnotationDbcFieldMapping implements DbcFieldMapping {
    private final nl.salp.warcraft4j.data.dbc.mapping.DbcFieldMapping mapping;
    private final String name;
    private final DbcType referencedType;

    public AnnotationDbcFieldMapping(String name, DbcType referencedType, nl.salp.warcraft4j.data.dbc.mapping.DbcFieldMapping mapping) {
        this.name = name;
        this.referencedType = referencedType;
        this.mapping = mapping;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getDbcColumn() {
        return mapping.order();
    }

    @Override
    public DbcDataType getDataType() {
        return mapping.dataType();
    }

    @Override
    public int getEntryCount() {
        return mapping.numberOfEntries();
    }

    @Override
    public int getEntryLength() {
        return mapping.length();
    }

    @Override
    public boolean isPadding() {
        return mapping.padding();
    }

    @Override
    public ByteOrder getByteOrder() {
        ByteOrder order = null;
        if (mapping.endianess() == 1) {
            order = ByteOrder.BIG_ENDIAN;
        } else if (mapping.endianess() == -1) {
            order = ByteOrder.LITTLE_ENDIAN;
        }
        return order;
    }

    @Override
    public DbcType getReferencedType() {
        return referencedType;
    }

    @Override
    public boolean isMeaningKnown() {
        return mapping.knownMeaning();
    }
}
