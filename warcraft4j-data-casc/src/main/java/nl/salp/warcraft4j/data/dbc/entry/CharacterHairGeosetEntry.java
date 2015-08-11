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
package nl.salp.warcraft4j.data.dbc.entry;

import nl.salp.warcraft4j.data.dbc.DbcEntry;
import nl.salp.warcraft4j.data.dbc.DbcType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "CharHairGeosets.dbc")
public class CharacterHairGeosetEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHARACTER_HAIR_GEOSET;
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.CHARACTER_RACE)
    private int raceId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int gender; // 0: male, 1: female
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int variationId;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int variationType;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int geosetId;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int geosetType;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int bald; // 0: hair, 1: bald
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32)
    private int colorIndex;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getRaceId() {
        return raceId;
    }

    public int getGender() {
        return gender;
    }

    public boolean isMale() {
        return gender == 0;
    }

    public int getVariationId() {
        return variationId;
    }

    public int getVariationType() {
        return variationType;
    }

    public int getGeosetId() {
        return geosetId;
    }

    public int getGeosetType() {
        return geosetType;
    }

    public int getBald() {
        return bald;
    }

    public boolean isBald() {
        return (bald == 1);
    }

    public int getColorIndex() {
        return colorIndex;
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
