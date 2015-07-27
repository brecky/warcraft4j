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
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "BattlePetSpecies.db2")
public class BattlePetSpeciesEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.BATTLE_PET_SPECIES;
    // TODO Implement me!
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int creatureId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int iconId;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int spellId;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int petFamilyId;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknownField1;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String source;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public int getIconId() {
        return iconId;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getPetFamilyId() {
        return petFamilyId;
    }

    public int getUnknownField1() {
        return unknownField1;
    }

    public int getFlags() {
        return flags;
    }

    public String getSource() {
        return source;
    }

    public String getDescription() {
        return description;
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
