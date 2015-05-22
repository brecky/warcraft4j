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

import nl.salp.warcraft4j.clientdata.dbc.*;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "SkillLineAbility.dbc")
public class SkillLineAbilityEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SKILL_LINE_ABILITY;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SKILL_LINE)
    private int skillLineId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL)
    private int spellId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int raceMask; // Mask based on CharacterRaceEntry entries
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int classMask; // Mask based on CharacterClassEntry entries
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int minimumSkillLineRank;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL)
    private int supersedesSpellId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int acquireMethod;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int trivialSkillLineRankHigh;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int trivialSkillLineRankLow;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int numberOfSkillUps;
    @DbcField(order = 12, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int uniqueBit;
    @DbcField(order = 13, dataType = DbcDataType.UINT32, knownMeaning = false)
    @DbcReference(type = DbcType.TRADE_SKILL_CATEGORY)
    private int tradeSkillCategoryId;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getSkillLineId() {
        return skillLineId;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getRaceMask() {
        return raceMask;
    }

    public int getClassMask() {
        return classMask;
    }

    public int getMinimumSkillLineRank() {
        return minimumSkillLineRank;
    }

    public int getSupersedesSpellId() {
        return supersedesSpellId;
    }

    public int getAcquireMethod() {
        return acquireMethod;
    }

    public int getTrivialSkillLineRankHigh() {
        return trivialSkillLineRankHigh;
    }

    public int getTrivialSkillLineRankLow() {
        return trivialSkillLineRankLow;
    }

    public int getNumberOfSkillUps() {
        return numberOfSkillUps;
    }

    public int getUniqueBit() {
        return uniqueBit;
    }

    public int getTradeSkillCategoryId() {
        return tradeSkillCategoryId;
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