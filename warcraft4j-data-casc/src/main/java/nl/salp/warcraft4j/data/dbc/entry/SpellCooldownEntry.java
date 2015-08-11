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

import nl.salp.warcraft4j.data.dbc.*;
import nl.salp.warcraft4j.data.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "SpellCooldowns.dbc")
public class SpellCooldownEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_COOLDOWN;

    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL)
    private int spellId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.DIFFICULTY)
    private int difficultyId;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int categoryRecoveryTime;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int recoveryTime;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int startRecoveryTime;

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

    public int getCategoryRecoveryTime() {
        return categoryRecoveryTime;
    }

    public int getRecoveryTime() {
        return recoveryTime;
    }

    public int getStartRecoveryTime() {
        return startRecoveryTime;
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
