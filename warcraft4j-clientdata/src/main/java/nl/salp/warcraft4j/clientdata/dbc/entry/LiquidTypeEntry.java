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
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "LiquidType.dbc")
public class LiquidTypeEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.LIQUID_TYPE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown2;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown3;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown4;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown5;
    @DbcField(order = 6, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown6;
    @DbcField(order = 7, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown7;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown8;
    @DbcField(order = 9, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown9;
    @DbcField(order = 10, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown10;
    @DbcField(order = 11, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown11;
    @DbcField(order = 12, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown12;
    @DbcField(order = 13, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown13;
    @DbcField(order = 14, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown14;
    @DbcField(order = 15, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown15;
    @DbcField(order = 16, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown16;
    @DbcField(order = 17, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown17;
    @DbcField(order = 18, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown18;
    @DbcField(order = 19, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown19;
    @DbcField(order = 20, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown20;
    @DbcField(order = 21, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown21;
    @DbcField(order = 22, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown22;
    @DbcField(order = 23, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown23;
    @DbcField(order = 24, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown24;
    @DbcField(order = 25, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown25;
    @DbcField(order = 26, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown26;
    @DbcField(order = 27, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown27;
    @DbcField(order = 28, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown28;
    @DbcField(order = 29, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown29;
    @DbcField(order = 30, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown30;
    @DbcField(order = 31, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown31;
    @DbcField(order = 32, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown32;
    @DbcField(order = 33, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown33;
    @DbcField(order = 34, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown34;
    @DbcField(order = 35, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown35;
    @DbcField(order = 36, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown36;
    @DbcField(order = 37, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown37;
    @DbcField(order = 38, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown38;
    @DbcField(order = 39, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown39;
    @DbcField(order = 40, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown40;
    @DbcField(order = 41, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown41;
    @DbcField(order = 42, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown42;
    @DbcField(order = 43, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown43;
    @DbcField(order = 44, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown44;
    @DbcField(order = 45, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown45;
    @DbcField(order = 46, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown46;
    @DbcField(order = 47, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown47;
    @DbcField(order = 48, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown48;
    @DbcField(order = 49, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown49;
    @DbcField(order = 50, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown50;
    @DbcField(order = 51, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown51;
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