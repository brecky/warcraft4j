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
@DbcMapping(file = "ChrRaces.dbc")
public class CharacterRaceEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CHARACTER_RACE;
    // TODO Fix fields.
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int factionId;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int explorationSoundId;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int maleDisplayId;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int femaleDisplayId;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String clientPrefix;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int baseLanguage;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32)
    private int creatureType;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32)
    private int resSicknessSpellId;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32)
    private int splashSoundId;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String clientFileString;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.UINT32)
    private int cinematicSequenceId;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32)
    private int alliance;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcFieldMapping(order = 16, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameFemale;
    @DbcFieldMapping(order = 17, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String nameMale;
    @DbcFieldMapping(order = 18, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String facialHairCustomizationMale;
    @DbcFieldMapping(order = 19, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String facialHairCustomizationFemale;
    @DbcFieldMapping(order = 20, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String hairCustomization;
    @DbcFieldMapping(order = 21, dataType = DbcDataType.UINT32)
    private int raceRelated;
    @DbcFieldMapping(order = 22, dataType = DbcDataType.UINT32)
    private int unalteredVisualRaceId;
    @DbcFieldMapping(order = 23, dataType = DbcDataType.UINT32)
    private int unalteredMaleCreatureSoundDataId;
    @DbcFieldMapping(order = 24, dataType = DbcDataType.UINT32)
    private int unalteredFemaleCreatureSoundDataId;
    @DbcFieldMapping(order = 25, dataType = DbcDataType.UINT32)
    private int charComponentTextureLayoutId;
    @DbcFieldMapping(order = 26, dataType = DbcDataType.UINT32)
    private int defaultClassID;
    @DbcFieldMapping(order = 27, dataType = DbcDataType.UINT32)
    private int createScreenFileDataId;
    @DbcFieldMapping(order = 28, dataType = DbcDataType.UINT32)
    private int selectScreenFileDataId;
    @DbcFieldMapping(order = 29, dataType = DbcDataType.FLOAT, numberOfEntries = 3)
    private float[] maleCustomizeOffset;
    @DbcFieldMapping(order = 30, dataType = DbcDataType.FLOAT, numberOfEntries = 3)
    private float[] femaleCustomizeOffset;
    @DbcFieldMapping(order = 31, dataType = DbcDataType.UINT32)
    private int neutralRaceId;
    @DbcFieldMapping(order = 32, dataType = DbcDataType.UINT32)
    private int lowResScreenFileDataId;
    @DbcFieldMapping(order = 33, dataType = DbcDataType.UINT32)
    private int highResMaleDisplayId;
    @DbcFieldMapping(order = 34, dataType = DbcDataType.UINT32)
    private int highResFemaleDisplayId;
    @DbcFieldMapping(order = 35, dataType = DbcDataType.UINT32)
    private int charComponentTexLayoutHiResId;
    @DbcFieldMapping(order = 36, dataType = DbcDataType.UINT32, knownMeaning = false)
    private int unknown;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getFlags() {
        return flags;
    }

    public int getFactionId() {
        return factionId;
    }

    public int getExplorationSoundId() {
        return explorationSoundId;
    }

    public int getMaleDisplayId() {
        return maleDisplayId;
    }

    public int getFemaleDisplayId() {
        return femaleDisplayId;
    }

    public String getClientPrefix() {
        return clientPrefix;
    }

    public int getBaseLanguage() {
        return baseLanguage;
    }

    public int getCreatureType() {
        return creatureType;
    }

    public int getResSicknessSpellId() {
        return resSicknessSpellId;
    }

    public int getSplashSoundId() {
        return splashSoundId;
    }

    public String getClientFileString() {
        return clientFileString;
    }

    public int getCinematicSequenceId() {
        return cinematicSequenceId;
    }

    public int getAlliance() {
        return alliance;
    }

    public String getName() {
        return name;
    }

    public String getNameFemale() {
        return nameFemale;
    }

    public String getNameMale() {
        return nameMale;
    }

    public String getFacialHairCustomizationMale() {
        return facialHairCustomizationMale;
    }

    public String getFacialHairCustomizationFemale() {
        return facialHairCustomizationFemale;
    }

    public String getHairCustomization() {
        return hairCustomization;
    }

    public int getRaceRelated() {
        return raceRelated;
    }

    public int getUnalteredVisualRaceId() {
        return unalteredVisualRaceId;
    }

    public int getUnalteredMaleCreatureSoundDataId() {
        return unalteredMaleCreatureSoundDataId;
    }

    public int getUnalteredFemaleCreatureSoundDataId() {
        return unalteredFemaleCreatureSoundDataId;
    }

    public int getCharComponentTextureLayoutId() {
        return charComponentTextureLayoutId;
    }

    public int getDefaultClassID() {
        return defaultClassID;
    }

    public int getCreateScreenFileDataId() {
        return createScreenFileDataId;
    }

    public int getSelectScreenFileDataId() {
        return selectScreenFileDataId;
    }

    public float[] getMaleCustomizeOffset() {
        return maleCustomizeOffset;
    }

    public float[] getFemaleCustomizeOffset() {
        return femaleCustomizeOffset;
    }

    public int getNeutralRaceId() {
        return neutralRaceId;
    }

    public int getLowResScreenFileDataId() {
        return lowResScreenFileDataId;
    }

    public int getHighResMaleDisplayId() {
        return highResMaleDisplayId;
    }

    public int getHighResFemaleDisplayId() {
        return highResFemaleDisplayId;
    }

    public int getCharComponentTexLayoutHiResId() {
        return charComponentTexLayoutHiResId;
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