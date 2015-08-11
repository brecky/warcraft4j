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
@DbcMapping(file = "ChatChannels.dbc")
public class ChatChannelEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHAT_CHANNEL;
    /*
   CHANNEL_DBC_FLAG_NONE       = 0x00000,
   CHANNEL_DBC_FLAG_INITIAL    = 0x00001,              // General, Trade, LocalDefense, LFG
   CHANNEL_DBC_FLAG_ZONE_DEP   = 0x00002,              // General, Trade, LocalDefense, GuildRecruitment
   CHANNEL_DBC_FLAG_GLOBAL     = 0x00004,              // WorldDefense
   CHANNEL_DBC_FLAG_TRADE      = 0x00008,              // Trade, LFG
   CHANNEL_DBC_FLAG_CITY_ONLY  = 0x00010,              // Trade, GuildRecruitment, LFG
   CHANNEL_DBC_FLAG_CITY_ONLY2 = 0x00020,              // Trade, GuildRecruitment, LFG
   CHANNEL_DBC_FLAG_DEFENSE    = 0x10000,              // LocalDefense, WorldDefense
   CHANNEL_DBC_FLAG_GUILD_REQ  = 0x20000,              // GuildRecruitment
   CHANNEL_DBC_FLAG_LFG        = 0x40000,              // LFG
   CHANNEL_DBC_FLAG_UNK1       = 0x80000,              // General
     */
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int factionGroup;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String shortcut;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getFlags() {
        return flags;
    }

    public int getFactionGroup() {
        return factionGroup;
    }

    public String getName() {
        return name;
    }

    public String getShortcut() {
        return shortcut;
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
