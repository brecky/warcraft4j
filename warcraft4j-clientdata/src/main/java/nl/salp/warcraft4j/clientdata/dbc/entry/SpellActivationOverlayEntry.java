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
@DbcMapping(file = "SpellActivationOverlay.dbc")
public class SpellActivationOverlayEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_ACTIVATION_OVERLAY;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL)
    private int spellId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int overlayFileDataId;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int screenLocationId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int color;
    @DbcField(order = 6, dataType = DbcDataType.FLOAT)
    private float scale;
    @DbcField(order = 7, dataType = DbcDataType.UINT32, numberOfEntries = 4)
    private int[] iconHighlightSpellClassMasks;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int triggerType;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SOUND_ENTRY)
    private int soundEntriesId;

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

    public int getOverlayFileDataId() {
        return overlayFileDataId;
    }

    public int getScreenLocationId() {
        return screenLocationId;
    }

    public int getColor() {
        return color;
    }

    public float getScale() {
        return scale;
    }

    public int[] getIconHighlightSpellClassMasks() {
        return iconHighlightSpellClassMasks;
    }

    public int getTriggerType() {
        return triggerType;
    }

    public int getSoundEntriesId() {
        return soundEntriesId;
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