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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "SpellMisc.db2")
public class SpellMiscEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_MISC;

    // FIXME It looks like the fields are still off (and certainly not consistent across all sources that are online).
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, numberOfEntries = 14, knownMeaning = false)
    private int[] attributes; // FIXME Determine the actual fields
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_CAST_TIME)
    private int castingTimeId;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_DURATION)
    private int durationId;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_RANGE)
    private int range;
    @DbcField(order = 8, dataType = DbcDataType.FLOAT)
    private float speed;
    @DbcField(order = 9, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    @DbcReference(type = DbcType.SPELL_VISUAL)
    private int[] spellVisualIds;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_ICON)
    private int iconId;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_ICON)
    private int activeIconId;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int schoolMask;
    @DbcField(order = 13, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown13;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int[] getAttributes() {
        return attributes;
    }

    public int getCastingTimeId() {
        return castingTimeId;
    }

    public int getDurationId() {
        return durationId;
    }

    public int getRange() {
        return range;
    }

    public float getSpeed() {
        return speed;
    }

    public int[] getSpellVisualIds() {
        return spellVisualIds;
    }

    public int getIconId() {
        return iconId;
    }

    public int getActiveIconId() {
        return activeIconId;
    }

    public int getSchoolMask() {
        return schoolMask;
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
