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

package nl.salp.warcraft4j.clientdatabase.entry;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "AreaTable.dbc")
public class AreaTableEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.AREA_TABLE;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int mapId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int parentAreaId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int areaBit;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] flags; // 2
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int soundProviderPref;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int soundProviderPrefUnderwater;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int ambienceId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int zoneMusic;
    @DbcField(order = 10, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String zoneName;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int introSound;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int explorationLevel;
    @DbcField(order = 13, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String areaName;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int factionGroupMask;
    @DbcField(order = 15, dataType = DbcDataType.UINT32, numberOfEntries = 4)
    private int[] liquidTypeId; // 4
    @DbcField(order = 16, dataType = DbcDataType.FLOAT)
    private float ambientMultiplier;
    @DbcField(order = 17, dataType = DbcDataType.UINT32)
    private int mountFlags;
    @DbcField(order = 18, dataType = DbcDataType.UINT32)
    private int uwIntroSound;
    @DbcField(order = 19, dataType = DbcDataType.UINT32)
    private int uwZoneMusic;
    @DbcField(order = 20, dataType = DbcDataType.UINT32)
    private int uwAmbience;
    @DbcField(order = 21, dataType = DbcDataType.UINT32)
    private int worldPvpId;  // -> World_PVP_Area.dbc
    @DbcField(order = 22, dataType = DbcDataType.UINT32)
    private int pvpCombatWorldStateId;
    @DbcField(order = 23, dataType = DbcDataType.UINT32)
    private int wildBattlePetLevelMin;
    @DbcField(order = 24, dataType = DbcDataType.UINT32)
    private int wildBattlePetLevelMax;
    @DbcField(order = 25, dataType = DbcDataType.UINT32)
    private int windSettingsId;

    @Override
    public ClientDatabaseEntryType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getMapId() {
        return mapId;
    }

    public int getParentAreaId() {
        return parentAreaId;
    }

    public int getAreaBit() {
        return areaBit;
    }

    public int[] getFlags() {
        return flags;
    }

    public int getSoundProviderPref() {
        return soundProviderPref;
    }

    public int getSoundProviderPrefUnderwater() {
        return soundProviderPrefUnderwater;
    }

    public int getAmbienceId() {
        return ambienceId;
    }

    public int getZoneMusic() {
        return zoneMusic;
    }

    public String getZoneName() {
        return zoneName;
    }

    public int getIntroSound() {
        return introSound;
    }

    public int getExplorationLevel() {
        return explorationLevel;
    }

    public String getAreaName() {
        return areaName;
    }

    public int getFactionGroupMask() {
        return factionGroupMask;
    }

    public int[] getLiquidTypeId() {
        return liquidTypeId;
    }

    public float getAmbientMultiplier() {
        return ambientMultiplier;
    }

    public int getMountFlags() {
        return mountFlags;
    }

    public int getUwIntroSound() {
        return uwIntroSound;
    }

    public int getUwZoneMusic() {
        return uwZoneMusic;
    }

    public int getUwAmbience() {
        return uwAmbience;
    }

    public int getWorldPvpId() {
        return worldPvpId;
    }

    public int getPvpCombatWorldStateId() {
        return pvpCombatWorldStateId;
    }

    public int getWildBattlePetLevelMin() {
        return wildBattlePetLevelMin;
    }

    public int getWildBattlePetLevelMax() {
        return wildBattlePetLevelMax;
    }

    public int getWindSettingsId() {
        return windSettingsId;
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
