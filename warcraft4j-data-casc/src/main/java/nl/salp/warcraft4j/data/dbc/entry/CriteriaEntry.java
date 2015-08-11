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

import nl.salp.warcraft4j.data.dbc.DbcEntry;
import nl.salp.warcraft4j.data.dbc.DbcType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.data.dbc.mapping.DbcFieldMapping;
import nl.salp.warcraft4j.data.dbc.mapping.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "Criteria.dbc")
public class CriteriaEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.CRITERIA;
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.UINT32)
    private int type;
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int criteriaLinkId; // Id of the link to various entry types, based on the criteria type
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    private int startEvent;                                      // 3
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int startAsset;                                      // 4
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int startTimer;                                      // 5
    @DbcFieldMapping(order = 7, dataType = DbcDataType.UINT32)
    private int failEvent;                                       // 6
    @DbcFieldMapping(order = 8, dataType = DbcDataType.UINT32)
    private int failAsset;                                       // 7
    @DbcFieldMapping(order = 9, dataType = DbcDataType.UINT32)
    private int modifierTreeId;                                  // 8
    @DbcFieldMapping(order = 10, dataType = DbcDataType.UINT32)
    private int flags;                                         // 9
    @DbcFieldMapping(order = 11, dataType = DbcDataType.UINT32)
    private int eligibilityWorldStateId;                         // 10
    @DbcFieldMapping(order = 12, dataType = DbcDataType.UINT32)
    private int eligibilityWorldStateValue;                      // 11

    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getCriteriaLinkId() {
        return criteriaLinkId;
    }

    public int getStartEvent() {
        return startEvent;
    }

    public int getStartAsset() {
        return startAsset;
    }

    public int getStartTimer() {
        return startTimer;
    }

    public int getFailEvent() {
        return failEvent;
    }

    public int getFailAsset() {
        return failAsset;
    }

    public int getModifierTreeId() {
        return modifierTreeId;
    }

    public int getFlags() {
        return flags;
    }

    public int getEligibilityWorldStateId() {
        return eligibilityWorldStateId;
    }

    public int getEligibilityWorldStateValue() {
        return eligibilityWorldStateValue;
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
