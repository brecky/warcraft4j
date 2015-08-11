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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "AreaTrigger.dbc")
public class AreaTriggerEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.AREA_TRIGGER;
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int mapId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.FLOAT, numberOfEntries = 3)
    private float[] position;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int phaseUsageFlags;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int phaseId;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int phaseGroupId;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.FLOAT)
    private float radius;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.FLOAT)
    private float boxLength;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.FLOAT)
    private float boxWidth;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.FLOAT)
    private float boxHeight;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.FLOAT)
    private float boxYaw;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.UINT32)
    private int shapeType;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.UINT32)
    private int shapeId;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32)
    private int areaTriggerActionSetId;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.UINT32)
    private int flags;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getMapId() {
        return mapId;
    }

    public float[] getPosition() {
        return position;
    }

    public int getPhaseUsageFlags() {
        return phaseUsageFlags;
    }

    public int getPhaseId() {
        return phaseId;
    }

    public int getPhaseGroupId() {
        return phaseGroupId;
    }

    public float getRadius() {
        return radius;
    }

    public float getBoxLength() {
        return boxLength;
    }

    public float getBoxWidth() {
        return boxWidth;
    }

    public float getBoxHeight() {
        return boxHeight;
    }

    public float getBoxYaw() {
        return boxYaw;
    }

    public int getShapeType() {
        return shapeType;
    }

    public int getShapeId() {
        return shapeId;
    }

    public int getAreaTriggerActionSetId() {
        return areaTriggerActionSetId;
    }

    public int getFlags() {
        return flags;
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
