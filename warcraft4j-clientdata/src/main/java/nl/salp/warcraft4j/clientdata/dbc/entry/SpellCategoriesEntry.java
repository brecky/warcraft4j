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
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "SpellCategories.dbc")
public class SpellCategoriesEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_CATEGORIES;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL)
    private int spellId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int difficultyId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_CATEGORY)
    private int category;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int defenseType;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int dispelType;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int mechanic;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int preventionType;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int startRecoveryCategory;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int chargeCategory;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getDifficultyId() {
        return difficultyId;
    }

    public int getCategory() {
        return category;
    }

    public int getDefenseType() {
        return defenseType;
    }

    public int getDispelType() {
        return dispelType;
    }

    public int getMechanic() {
        return mechanic;
    }

    public int getPreventionType() {
        return preventionType;
    }

    public int getStartRecoveryCategory() {
        return startRecoveryCategory;
    }

    public int getChargeCategory() {
        return chargeCategory;
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
