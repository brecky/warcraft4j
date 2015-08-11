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
import nl.salp.warcraft4j.data.dbc.mapping.DbcReference;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "AnimationData.dbc")
public class AnimationDataEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ANIMATION_DATA;
    @DbcFieldMapping(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcFieldMapping(order = 2, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    /*
0: weapon not affected by animation,
4: sheathe weapons automatically,
16: sheathe weapons automatically,
32: unsheathe weapons.
     */
    @DbcFieldMapping(order = 3, dataType = DbcDataType.UINT32)
    private int flags;
    @DbcFieldMapping(order = 4, dataType = DbcDataType.UINT32)
    @DbcReference(type = DbcType.ANIMATION_DATA)
    private int fallbackId;
    @DbcFieldMapping(order = 5, dataType = DbcDataType.UINT32)
    private int behaviourId;
    @DbcFieldMapping(order = 6, dataType = DbcDataType.UINT32)
    private int behaviourTier; // 0 = normal, 3 = flying


    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getFlags() {
        return flags;
    }

    public int getFallbackId() {
        return fallbackId;
    }

    public int getBehaviourId() {
        return behaviourId;
    }

    public int getBehaviourTier() {
        return behaviourTier;
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
