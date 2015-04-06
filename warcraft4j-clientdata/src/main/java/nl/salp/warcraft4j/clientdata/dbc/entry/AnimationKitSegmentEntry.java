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
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcFile(file = "AnimKitSegment.dbc")
public class AnimationKitSegmentEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ANIMATION_KIT_SEGMENT;
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int parentAnimationKitId;
    @DbcField(order = 3, dataType = DbcDataType.UINT32)
    private int orderIndex;
    @DbcField(order = 4, dataType = DbcDataType.UINT32)
    private int animationId;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int animationStartTime;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int animationKitConfigId;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int startCondition;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int startConditionParameter;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int startConditionDelay;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int endCondition;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int endConditionParameter;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int endConditionDelay;
    @DbcField(order = 13, dataType = DbcDataType.FLOAT)
    private float speed;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int segmentFlags;
    @DbcField(order = 15, dataType = DbcDataType.UINT32)
    private int forcedVariation;
    @DbcField(order = 16, dataType = DbcDataType.UINT32)
    private int overrideConfigFlags;
    @DbcField(order = 17, dataType = DbcDataType.UINT32)
    private int loopToSegmentIndex;

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getParentAnimationKitId() {
        return parentAnimationKitId;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public int getAnimationId() {
        return animationId;
    }

    public int getAnimationStartTime() {
        return animationStartTime;
    }

    public int getAnimationKitConfigId() {
        return animationKitConfigId;
    }

    public int getStartCondition() {
        return startCondition;
    }

    public int getStartConditionParameter() {
        return startConditionParameter;
    }

    public int getStartConditionDelay() {
        return startConditionDelay;
    }

    public int getEndCondition() {
        return endCondition;
    }

    public int getEndConditionParameter() {
        return endConditionParameter;
    }

    public int getEndConditionDelay() {
        return endConditionDelay;
    }

    public float getSpeed() {
        return speed;
    }

    public int getSegmentFlags() {
        return segmentFlags;
    }

    public int getForcedVariation() {
        return forcedVariation;
    }

    public int getOverrideConfigFlags() {
        return overrideConfigFlags;
    }

    public int getLoopToSegmentIndex() {
        return loopToSegmentIndex;
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
