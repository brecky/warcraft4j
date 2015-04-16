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
@DbcMapping(file = "SpellItemEnchantment.dbc")
public class SpellItemEnchantmentEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_ITEM_ENCHANTMENT;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int charges;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] effects;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] effectPointsMinimum;
    @DbcField(order = 5, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] effectArguments;
    @DbcField(order = 6, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ITEM_VISUAL)
    private int itemVisualId;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ITEM)
    private int sourceItemId;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SPELL_ITEM_ENCHANTMENT_CONDITION)
    private int conditionId;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.SKILL_LINE)
    private int requiredSkillId;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int requiredSkillRank;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int minimumLevel;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int maximumLevel;
    @DbcField(order = 15, dataType = DbcDataType.UINT32)
    private int itemLevel;
    @DbcField(order = 16, dataType = DbcDataType.UINT32)
    private int scalingClass;
    @DbcField(order = 17, dataType = DbcDataType.UINT32)
    private int scalingClassRestricted;
    @DbcField(order = 18, dataType = DbcDataType.FLOAT, numberOfEntries = 3)
    private float[] effectScalingPoints;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getCharges() {
        return charges;
    }

    public int[] getEffects() {
        return effects;
    }

    public int[] getEffectPointsMinimum() {
        return effectPointsMinimum;
    }

    public int[] getEffectArguments() {
        return effectArguments;
    }

    public String getName() {
        return name;
    }

    public int getItemVisualId() {
        return itemVisualId;
    }

    public int getFlags() {
        return flags;
    }

    public int getSourceItemId() {
        return sourceItemId;
    }

    public int getConditionId() {
        return conditionId;
    }

    public int getRequiredSkillId() {
        return requiredSkillId;
    }

    public int getRequiredSkillRank() {
        return requiredSkillRank;
    }

    public int getMinimumLevel() {
        return minimumLevel;
    }

    public int getMaximumLevel() {
        return maximumLevel;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public int getScalingClass() {
        return scalingClass;
    }

    public int getScalingClassRestricted() {
        return scalingClassRestricted;
    }

    public float[] getEffectScalingPoints() {
        return effectScalingPoints;
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
