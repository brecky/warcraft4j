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
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "SpellChainEffects.dbc")
public class SpellChainEffectEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.SPELL_CHAIN_EFFECT;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.FLOAT)
    private float averageSegmentLength;
    @DbcField(order = 3, dataType = DbcDataType.FLOAT)
    private float width;
    @DbcField(order = 4, dataType = DbcDataType.FLOAT)
    private float noiseScale;
    @DbcField(order = 5, dataType = DbcDataType.FLOAT)
    private float textCoordinateScale;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int segmentDuration;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int segmentDelay;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int jointCount;
    @DbcField(order = 10, dataType = DbcDataType.FLOAT)
    private float jointOffsetRadius;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int jointsPerMinorJoint;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int minorJointsPerMajorJoint;
    @DbcField(order = 13, dataType = DbcDataType.FLOAT)
    private float minorJointScale;
    @DbcField(order = 14, dataType = DbcDataType.FLOAT)
    private float majorJointScale;
    @DbcField(order = 15, dataType = DbcDataType.FLOAT)
    private float jointMoveSpeed;
    @DbcField(order = 16, dataType = DbcDataType.FLOAT)
    private float jointSmoothness;
    @DbcField(order = 17, dataType = DbcDataType.FLOAT)
    private float minimumDurationBetweenJointJumps;
    @DbcField(order = 18, dataType = DbcDataType.FLOAT)
    private float maximumDurationBetweenJointJumps;
    @DbcField(order = 19, dataType = DbcDataType.FLOAT)
    private float waveHeight;
    @DbcField(order = 20, dataType = DbcDataType.FLOAT)
    private float waveFrequency;
    @DbcField(order = 21, dataType = DbcDataType.FLOAT)
    private float waveSpeed;
    @DbcField(order = 22, dataType = DbcDataType.FLOAT)
    private float minimumWaveAngle;
    @DbcField(order = 23, dataType = DbcDataType.FLOAT)
    private float maximumWaveAngle;
    @DbcField(order = 24, dataType = DbcDataType.FLOAT)
    private float minimumWaveSpin;
    @DbcField(order = 25, dataType = DbcDataType.FLOAT)
    private float maximumWaveSpin;
    @DbcField(order = 26, dataType = DbcDataType.FLOAT)
    private float arcHeight;
    @DbcField(order = 27, dataType = DbcDataType.FLOAT)
    private float minimumArcAngle;
    @DbcField(order = 28, dataType = DbcDataType.FLOAT)
    private float maximumArcAngle;
    @DbcField(order = 29, dataType = DbcDataType.FLOAT)
    private float minimumArcSpin;
    @DbcField(order = 30, dataType = DbcDataType.FLOAT)
    private float maximumArcSpin;
    @DbcField(order = 31, dataType = DbcDataType.FLOAT)
    private float delayBetweenEffects;
    @DbcField(order = 32, dataType = DbcDataType.FLOAT)
    private float minimumFlickerOnDuration;
    @DbcField(order = 33, dataType = DbcDataType.FLOAT)
    private float maximumFlickerOnDuration;
    @DbcField(order = 34, dataType = DbcDataType.FLOAT)
    private float minimumFlickerOffDuration;
    @DbcField(order = 35, dataType = DbcDataType.FLOAT)
    private float maximumFlickerOffDuration;
    @DbcField(order = 36, dataType = DbcDataType.FLOAT)
    private float pulseSpeed;
    @DbcField(order = 37, dataType = DbcDataType.FLOAT)
    private float pulseOnLength;
    @DbcField(order = 38, dataType = DbcDataType.FLOAT)
    private float pulseFadeLength;
    @DbcField(order = 39, dataType = DbcDataType.BYTE)
    private byte alpha;
    @DbcField(order = 40, dataType = DbcDataType.BYTE)
    private byte red;
    @DbcField(order = 41, dataType = DbcDataType.BYTE)
    private byte green;
    @DbcField(order = 42, dataType = DbcDataType.BYTE)
    private byte blue;
    @DbcField(order = 43, dataType = DbcDataType.BYTE)
    private byte blendMode;
    @DbcField(order = 44, dataType = DbcDataType.BYTE, numberOfEntries = 3, padding = true)
    private transient byte[] padding1;
    @DbcField(order = 45, dataType = DbcDataType.UINT32)
    private int renderLayer;
    @DbcField(order = 46, dataType = DbcDataType.FLOAT)
    private float textureLength;
    @DbcField(order = 47, dataType = DbcDataType.FLOAT)
    private float wavePhase;
    @DbcField(order = 48, dataType = DbcDataType.UINT32, numberOfEntries = 11)
    private int[] spellChainEffectId; // 11
    @DbcField(order = 49, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String texture;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public float getAverageSegmentLength() {
        return averageSegmentLength;
    }

    public float getWidth() {
        return width;
    }

    public float getNoiseScale() {
        return noiseScale;
    }

    public float getTextCoordinateScale() {
        return textCoordinateScale;
    }

    public int getSegmentDuration() {
        return segmentDuration;
    }

    public int getSegmentDelay() {
        return segmentDelay;
    }

    public int getFlags() {
        return flags;
    }

    public int getJointCount() {
        return jointCount;
    }

    public float getJointOffsetRadius() {
        return jointOffsetRadius;
    }

    public int getJointsPerMinorJoint() {
        return jointsPerMinorJoint;
    }

    public int getMinorJointsPerMajorJoint() {
        return minorJointsPerMajorJoint;
    }

    public float getMinorJointScale() {
        return minorJointScale;
    }

    public float getMajorJointScale() {
        return majorJointScale;
    }

    public float getJointMoveSpeed() {
        return jointMoveSpeed;
    }

    public float getJointSmoothness() {
        return jointSmoothness;
    }

    public float getMinimumDurationBetweenJointJumps() {
        return minimumDurationBetweenJointJumps;
    }

    public float getMaximumDurationBetweenJointJumps() {
        return maximumDurationBetweenJointJumps;
    }

    public float getWaveHeight() {
        return waveHeight;
    }

    public float getWaveFrequency() {
        return waveFrequency;
    }

    public float getWaveSpeed() {
        return waveSpeed;
    }

    public float getMinimumWaveAngle() {
        return minimumWaveAngle;
    }

    public float getMaximumWaveAngle() {
        return maximumWaveAngle;
    }

    public float getMinimumWaveSpin() {
        return minimumWaveSpin;
    }

    public float getMaximumWaveSpin() {
        return maximumWaveSpin;
    }

    public float getArcHeight() {
        return arcHeight;
    }

    public float getMinimumArcAngle() {
        return minimumArcAngle;
    }

    public float getMaximumArcAngle() {
        return maximumArcAngle;
    }

    public float getMinimumArcSpin() {
        return minimumArcSpin;
    }

    public float getMaximumArcSpin() {
        return maximumArcSpin;
    }

    public float getDelayBetweenEffects() {
        return delayBetweenEffects;
    }

    public float getMinimumFlickerOnDuration() {
        return minimumFlickerOnDuration;
    }

    public float getMaximumFlickerOnDuration() {
        return maximumFlickerOnDuration;
    }

    public float getMinimumFlickerOffDuration() {
        return minimumFlickerOffDuration;
    }

    public float getMaximumFlickerOffDuration() {
        return maximumFlickerOffDuration;
    }

    public float getPulseSpeed() {
        return pulseSpeed;
    }

    public float getPulseOnLength() {
        return pulseOnLength;
    }

    public float getPulseFadeLength() {
        return pulseFadeLength;
    }

    public byte getAlpha() {
        return alpha;
    }

    public byte getRed() {
        return red;
    }

    public byte getGreen() {
        return green;
    }

    public byte getBlue() {
        return blue;
    }

    public byte getBlendMode() {
        return blendMode;
    }

    public int getRenderLayer() {
        return renderLayer;
    }

    public float getTextureLength() {
        return textureLength;
    }

    public float getWavePhase() {
        return wavePhase;
    }

    public int[] getSpellChainEffectId() {
        return spellChainEffectId;
    }

    public String getTexture() {
        return texture;
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
