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
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "SpellVisualKit.db2")
public class SpellVisualKitEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_VISUAL_KIT;

    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown2;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown3;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown4;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown5;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown6;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown7;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown8;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown9;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown10;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown11;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown12;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown13;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown14;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown15;
    @DbcFieldMapping(order = 16, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown16;
    @DbcFieldMapping(order = 17, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown17;
    @DbcFieldMapping(order = 18, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown18;
    @DbcFieldMapping(order = 19, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown19;
    @DbcFieldMapping(order = 20, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown20;
    @DbcFieldMapping(order = 21, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown21;
    @DbcFieldMapping(order = 22, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown22;
    @DbcFieldMapping(order = 23, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown23;
    @DbcFieldMapping(order = 24, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown24;
    @DbcFieldMapping(order = 25, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown25;
    @DbcFieldMapping(order = 26, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown26;
    @DbcFieldMapping(order = 27, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown27;
    @DbcFieldMapping(order = 28, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown28;
    @DbcFieldMapping(order = 29, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown29;
    @DbcFieldMapping(order = 30, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown30;
    @DbcFieldMapping(order = 31, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown31;
    @DbcFieldMapping(order = 32, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown32;
    @DbcFieldMapping(order = 33, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown33;
    @DbcFieldMapping(order = 34, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown34;
    @DbcFieldMapping(order = 35, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown35;
    @DbcFieldMapping(order = 36, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown36;
    @DbcFieldMapping(order = 37, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown37;
    @DbcFieldMapping(order = 38, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown38;
    @DbcFieldMapping(order = 39, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown39;
    @DbcFieldMapping(order = 40, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown40;
    @DbcFieldMapping(order = 41, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown41;
    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
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
