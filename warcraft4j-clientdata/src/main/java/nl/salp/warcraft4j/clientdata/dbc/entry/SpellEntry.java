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
@DbcMapping(file = "Spell.dbc")
public class SpellEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL;

    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String subText;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String auraDescription;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int runeCostId;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int spellMissileId;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int descriptionVariableId;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32)
    private int scalingId;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32)
    private int auraOptions;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32)
    private int auraRestrictionId;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.UINT32)
    private int castingRequirementsId;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.UINT32)
    private int categoriesId;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32)
    private int classOptionsId;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.UINT32)
    private int cooldownsId;
    @DbcFieldMapping(order = 16, dataType = DbcDataType.UINT32)
    private int equippedItemsId;
    @DbcFieldMapping(order = 17, dataType = DbcDataType.UINT32)
    private int interruptsId;
    @DbcFieldMapping(order = 18, dataType = DbcDataType.UINT32)
    private int levelsId;
    @DbcFieldMapping(order = 19, dataType = DbcDataType.UINT32)
    private int reagentsId;
    @DbcFieldMapping(order = 20, dataType = DbcDataType.UINT32)
    private int shapeshiftId;
    @DbcFieldMapping(order = 21, dataType = DbcDataType.UINT32)
    private int targetRestrictionsId;
    @DbcFieldMapping(order = 22, dataType = DbcDataType.UINT32)
    private int totemsId;
    @DbcFieldMapping(order = 23, dataType = DbcDataType.UINT32)
    private int requiredProjectId;
    @DbcFieldMapping(order = 24, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int miscId;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getSubText() {
        return subText;
    }

    public String getAuraDescription() {
        return auraDescription;
    }

    public int getRuneCostId() {
        return runeCostId;
    }

    public int getSpellMissileId() {
        return spellMissileId;
    }

    public int getDescriptionVariableId() {
        return descriptionVariableId;
    }

    public int getScalingId() {
        return scalingId;
    }

    public int getAuraOptions() {
        return auraOptions;
    }

    public int getAuraRestrictionId() {
        return auraRestrictionId;
    }

    public int getCastingRequirementsId() {
        return castingRequirementsId;
    }

    public int getCategoriesId() {
        return categoriesId;
    }

    public int getClassOptionsId() {
        return classOptionsId;
    }

    public int getCooldownsId() {
        return cooldownsId;
    }

    public int getEquippedItemsId() {
        return equippedItemsId;
    }

    public int getInterruptsId() {
        return interruptsId;
    }

    public int getLevelsId() {
        return levelsId;
    }

    public int getReagentsId() {
        return reagentsId;
    }

    public int getShapeshiftId() {
        return shapeshiftId;
    }

    public int getTargetRestrictionsId() {
        return targetRestrictionsId;
    }

    public int getTotemsId() {
        return totemsId;
    }

    public int getRequiredProjectId() {
        return requiredProjectId;
    }

    public int getMiscId() {
        return miscId;
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
