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
@DbcMapping(file = "AnimKitSegment.dbc")
public class AnimationKitSegmentEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ANIMATION_KIT_SEGMENT;
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int parentAnimationKitId;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int orderIndex;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int animationId;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int animationStartTime;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int animationKitConfigId;
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int startCondition;
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int startConditionParameter;
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32)
    private int startConditionDelay;
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32)
    private int endCondition;
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32)
    private int endConditionParameter;
    @DbcFieldMapping(order = 12, dataType = DbcDataType.UINT32)
    private int endConditionDelay;
    @DbcFieldMapping(order = 13, dataType = DbcDataType.FLOAT)
    private float speed;
    @DbcFieldMapping(order = 14, dataType = DbcDataType.UINT32)
    private int segmentFlags;
    @DbcFieldMapping(order = 15, dataType = DbcDataType.UINT32)
    private int forcedVariation;
    @DbcFieldMapping(order = 16, dataType = DbcDataType.UINT32)
    private int overrideConfigFlags;
    @DbcFieldMapping(order = 17, dataType = DbcDataType.UINT32)
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
