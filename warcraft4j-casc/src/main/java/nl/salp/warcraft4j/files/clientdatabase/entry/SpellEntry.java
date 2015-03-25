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

package nl.salp.warcraft4j.files.clientdatabase.entry;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "Spell.dbc")
public class SpellEntry implements ClientDatabaseEntry {
    private static final ClientDatabaseEntryType ENTRY_TYPE = ClientDatabaseEntryType.SPELL;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String subText;
    @DbcField(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 5, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String auraDescription;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int runeCostId;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int spellMissileId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int descriptionVariableId;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int scalingId;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int auraOptions;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int auraRestrictionId;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int castingRequirementsId;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int categoriesId;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int classOptionsId;
    @DbcField(order = 15, dataType = DbcDataType.UINT32)
    private int cooldownsId;
    @DbcField(order = 16, dataType = DbcDataType.UINT32)
    private int equippedItemsId;
    @DbcField(order = 17, dataType = DbcDataType.UINT32)
    private int interruptsId;
    @DbcField(order = 18, dataType = DbcDataType.UINT32)
    private int levelsId;
    @DbcField(order = 19, dataType = DbcDataType.UINT32)
    private int reagentsId;
    @DbcField(order = 20, dataType = DbcDataType.UINT32)
    private int shapeshiftId;
    @DbcField(order = 21, dataType = DbcDataType.UINT32)
    private int targetRestrictionsId;
    @DbcField(order = 22, dataType = DbcDataType.UINT32)
    private int totemsId;
    @DbcField(order = 23, dataType = DbcDataType.UINT32)
    private int requiredProjectId;
    @DbcField(order = 24, dataType = DbcDataType.UINT32)
    private int miscId;

    @Override
    public ClientDatabaseEntryType getEntryType() {
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
