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

package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "AreaPOI.db2")
public class AreaPointOfInterestEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.AREA_POINT_OF_INTEREST;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int importance;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int factionId;
    @DbcField(order = 5, dataType = DbcDataType.FLOAT, numberOfEntries = 2)
    private float[] position; // 2
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int continentId;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int areaId;
    @DbcField(order = 9, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 10, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int worldStateId;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int playerConditionId;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int worldMapLink;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int portLocationId;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getImportance() {
        return importance;
    }

    public int getIconId() {
        return iconId;
    }

    public int getFactionId() {
        return factionId;
    }

    public float[] getPosition() {
        return position;
    }

    public int getContinentId() {
        return continentId;
    }

    public int getFlags() {
        return flags;
    }

    public int getAreaId() {
        return areaId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getWorldStateId() {
        return worldStateId;
    }

    public int getPlayerConditionId() {
        return playerConditionId;
    }

    public int getWorldMapLink() {
        return worldMapLink;
    }

    public int getPortLocationId() {
        return portLocationId;
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
