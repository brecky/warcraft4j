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
@DbcMapping(file = "SpellEffect.dbc")
public class SpellEffectEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_EFFECT;

    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.DIFFICULTY)
    private int difficultyId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int effect; // TODO ref to spell ?
    @DbcFieldMapping(order = 4, dataType = DbcDataType.FLOAT)
    private float effectAmplitude;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int effectAura; // TODO ref to spell aura?
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int effectAuraPeriod;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int effectBasePoints;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.FLOAT)
    private float effectBonusCoefficient;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.FLOAT)
    private float effectChainAmplitude;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32)
    private int effectChainTargets;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32)
    private int effectDieSides;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.UINT32)
    private int effectItemType;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.UINT32)
    private int effectMechanic;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] effectMiscValue;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.FLOAT)
    private float effectPointsPerResource;
    @DbcFieldMapping(order = 16, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] effectRadiusIndex;
    @DbcFieldMapping(order = 17, dataType = DbcDataType.FLOAT)
    private float effectRealPointsPerLevel;
    @DbcFieldMapping(order = 18, dataType = DbcDataType.UINT32, numberOfEntries = 4)
    private int[] effectSpellClassMasks;
    @DbcFieldMapping(order = 19, dataType = DbcDataType.UINT32)
    private int effectTriggerSpell;
    @DbcFieldMapping(order = 20, dataType = DbcDataType.FLOAT)
    private float effectPositionFacing;
    @DbcFieldMapping(order = 21, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] implicitTargets;
    @DbcFieldMapping(order = 22, dataType = DbcDataType.UINT32)
    private int spellId;
    @DbcFieldMapping(order = 23, dataType = DbcDataType.UINT32)
    private int effectIndex;
    @DbcFieldMapping(order = 24, dataType = DbcDataType.UINT32)
    private int effectAttribute;
    @DbcFieldMapping(order = 25, dataType = DbcDataType.FLOAT)
    private float bonusCoefficientFromAp;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getDifficultyId() {
        return difficultyId;
    }

    public int getEffect() {
        return effect;
    }

    public float getEffectAmplitude() {
        return effectAmplitude;
    }

    public int getEffectAura() {
        return effectAura;
    }

    public int getEffectAuraPeriod() {
        return effectAuraPeriod;
    }

    public int getEffectBasePoints() {
        return effectBasePoints;
    }

    public float getEffectBonusCoefficient() {
        return effectBonusCoefficient;
    }

    public float getEffectChainAmplitude() {
        return effectChainAmplitude;
    }

    public int getEffectChainTargets() {
        return effectChainTargets;
    }

    public int getEffectDieSides() {
        return effectDieSides;
    }

    public int getEffectItemType() {
        return effectItemType;
    }

    public int getEffectMechanic() {
        return effectMechanic;
    }

    public int[] getEffectMiscValue() {
        return effectMiscValue;
    }

    public float getEffectPointsPerResource() {
        return effectPointsPerResource;
    }

    public int[] getEffectRadiusIndex() {
        return effectRadiusIndex;
    }

    public float getEffectRealPointsPerLevel() {
        return effectRealPointsPerLevel;
    }

    public int[] getEffectSpellClassMasks() {
        return effectSpellClassMasks;
    }

    public int getEffectTriggerSpell() {
        return effectTriggerSpell;
    }

    public float getEffectPositionFacing() {
        return effectPositionFacing;
    }

    public int[] getImplicitTargets() {
        return implicitTargets;
    }

    public int getSpellId() {
        return spellId;
    }

    public int getEffectIndex() {
        return effectIndex;
    }

    public int getEffectAttribute() {
        return effectAttribute;
    }

    public float getBonusCoefficientFromAp() {
        return bonusCoefficientFromAp;
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
