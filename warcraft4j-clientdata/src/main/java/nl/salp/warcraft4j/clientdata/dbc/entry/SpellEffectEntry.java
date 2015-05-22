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
@DbcMapping(file = "SpellEffect.dbc")
public class SpellEffectEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_EFFECT;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.DIFFICULTY)
    private int difficultyId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int effect; // TODO ref to spell ?
    @DbcField(order = 4, dataType = DbcDataType.FLOAT)
    private float effectAmplitude;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int effectAura; // TODO ref to spell aura?
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int effectAuraPeriod;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int effectBasePoints;
    @DbcField(order = 8, dataType = DbcDataType.FLOAT)
    private float effectBonusCoefficient;
    @DbcField(order = 9, dataType = DbcDataType.FLOAT)
    private float effectChainAmplitude;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int effectChainTargets;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int effectDieSides;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int effectItemType;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int effectMechanic;
    @DbcField(order = 14, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] effectMiscValue;
    @DbcField(order = 15, dataType = DbcDataType.FLOAT)
    private float effectPointsPerResource;
    @DbcField(order = 16, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] effectRadiusIndex;
    @DbcField(order = 17, dataType = DbcDataType.FLOAT)
    private float effectRealPointsPerLevel;
    @DbcField(order = 18, dataType = DbcDataType.UINT32, numberOfEntries = 4)
    private int[] effectSpellClassMasks;
    @DbcField(order = 19, dataType = DbcDataType.UINT32)
    private int effectTriggerSpell;
    @DbcField(order = 20, dataType = DbcDataType.FLOAT)
    private float effectPositionFacing;
    @DbcField(order = 21, dataType = DbcDataType.UINT32, numberOfEntries = 2)
    private int[] implicitTargets;
    @DbcField(order = 22, dataType = DbcDataType.UINT32)
    private int spellId;
    @DbcField(order = 23, dataType = DbcDataType.UINT32)
    private int effectIndex;
    @DbcField(order = 24, dataType = DbcDataType.UINT32)
    private int effectAttribute;
    @DbcField(order = 25, dataType = DbcDataType.FLOAT)
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
