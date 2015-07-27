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
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcFieldMapping;
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
@DbcMapping(file = "SkillLine.dbc")
public class SkillLineEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SKILL_LINE;

    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int categoryId; // FIXME Found a mentioning of it being a reference to SkillLineCategory.dbc ?!?! (might be an unparsed file) else reference to TradeSkillCategoryEntry?
    @DbcFieldMapping(order = 3, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String displayName;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_ICON)
    private int spellIconId;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String alternativeVerb;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int canLink; // FIXME Not a boolean?
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SKILL_LINE)
    private int parentSkillLineId;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int flags;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public int getSpellIconId() {
        return spellIconId;
    }

    public String getAlternativeVerb() {
        return alternativeVerb;
    }

    public int getCanLink() {
        return canLink;
    }

    public int getParentSkillLineId() {
        return parentSkillLineId;
    }

    public int getFlags() {
        return flags;
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
