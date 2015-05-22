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
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "PlayerCondition.db2")
public class PlayerConditionEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.PLAYER_CONDITION;

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
    @DbcField(order = 52, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown52;
    @DbcField(order = 53, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown53;
    @DbcField(order = 54, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown54;
    @DbcField(order = 55, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown55;
    @DbcField(order = 56, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown56;
    @DbcField(order = 57, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown57;
    @DbcField(order = 58, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown58;
    @DbcField(order = 59, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown59;
    @DbcField(order = 60, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown60;
    @DbcField(order = 61, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown61;
    @DbcField(order = 62, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown62;
    @DbcField(order = 63, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown63;
    @DbcField(order = 64, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown64;
    @DbcField(order = 65, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown65;
    @DbcField(order = 66, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown66;
    @DbcField(order = 67, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown67;
    @DbcField(order = 68, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown68;
    @DbcField(order = 69, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown69;
    @DbcField(order = 70, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown70;
    @DbcField(order = 71, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown71;
    @DbcField(order = 72, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown72;
    @DbcField(order = 73, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown73;
    @DbcField(order = 74, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown74;
    @DbcField(order = 75, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown75;
    @DbcField(order = 76, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown76;
    @DbcField(order = 77, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown77;
    @DbcField(order = 78, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown78;
    @DbcField(order = 79, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown79;
    @DbcField(order = 80, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown80;
    @DbcField(order = 81, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown81;
    @DbcField(order = 82, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown82;
    @DbcField(order = 83, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown83;
    @DbcField(order = 84, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown84;
    @DbcField(order = 85, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown85;
    @DbcField(order = 86, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown86;
    @DbcField(order = 87, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown87;
    @DbcField(order = 88, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown88;
    @DbcField(order = 89, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown89;
    @DbcField(order = 90, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown90;
    @DbcField(order = 91, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown91;
    @DbcField(order = 92, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown92;
    @DbcField(order = 93, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown93;
    @DbcField(order = 94, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown94;
    @DbcField(order = 95, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown95;
    @DbcField(order = 96, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown96;
    @DbcField(order = 97, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown97;
    @DbcField(order = 98, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown98;
    @DbcField(order = 99, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown99;
    @DbcField(order = 100, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown100;
    @DbcField(order = 101, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown101;
    @DbcField(order = 102, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown102;
    @DbcField(order = 103, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown103;
    @DbcField(order = 104, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown104;
    @DbcField(order = 105, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown105;
    @DbcField(order = 106, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown106;
    @DbcField(order = 107, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown107;
    @DbcField(order = 108, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown108;
    @DbcField(order = 109, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown109;
    @DbcField(order = 110, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown110;
    @DbcField(order = 111, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown111;
    @DbcField(order = 112, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown112;
    @DbcField(order = 113, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown113;
    @DbcField(order = 114, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown114;
    @DbcField(order = 115, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown115;
    @DbcField(order = 116, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown116;
    @DbcField(order = 117, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown117;
    @DbcField(order = 118, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown118;
    @DbcField(order = 119, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown119;
    @DbcField(order = 120, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown120;
    @DbcField(order = 121, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown121;
    @DbcField(order = 122, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown122;
    @DbcField(order = 123, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown123;
    @DbcField(order = 124, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown124;
    @DbcField(order = 125, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown125;
    @DbcField(order = 126, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown126;
    @DbcField(order = 127, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown127;
    @DbcField(order = 128, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown128;
    @DbcField(order = 129, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown129;
    @DbcField(order = 130, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown130;
    @DbcField(order = 131, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown131;
    @DbcField(order = 132, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown132;
    @DbcField(order = 133, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown133;
    @DbcField(order = 134, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown134;
    @DbcField(order = 135, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown135;
    @DbcField(order = 136, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown136;
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
