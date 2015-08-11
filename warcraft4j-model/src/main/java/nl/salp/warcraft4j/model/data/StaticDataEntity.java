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

package nl.salp.warcraft4j.model.data;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Base class for World of Warcraft "static data" entities.
 *
 * @author Barre Dijkstra
 */
abstract class StaticDataEntity implements Serializable {
    /** The ID of the entity as used in World of Warcraft. */
    protected final long wowId;
    /** The name of the entity. */
    protected final String name;

    /**
     * Create a new StaticDataEntity.
     *
     * @param wowId The ID of the entity as used in World of Warcraft.
     * @param name  The name of the entity.
     */
    protected StaticDataEntity(long wowId, String name) {
        this.wowId = wowId;
        this.name = name;
    }

    /**
     * Get the ID of the entity as used in World of Warcraft.
     *
     * @return The ID.
     */
    public long getWowId() {
        return wowId;
    }

    /**
     * Get the name of the entity.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
