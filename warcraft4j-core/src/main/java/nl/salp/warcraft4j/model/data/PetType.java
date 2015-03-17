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

/**
 * Companion pet type.
 *
 * @author Barre Dijkstra
 */
public class PetType extends StaticDataEntity {
    /** The key. */
    private final String key;
    /** The ability type. */
    private final long abilityType;
    /** The pet type the type is strong against. */
    private final PetType strongAgainst;
    /** The pet type the type is weak against. */
    private final PetType weakAgainst;

    /**
     * Create a new StaticDataEntity.
     *
     * @param wowId         The ID of the entity as used in World of Warcraft.
     * @param name          The name of the entity.
     * @param key           The key.
     * @param abilityType   The ability type.
     * @param strongAgainst The pet type the type is strong against.
     * @param weakAgainst   The pet type the type is weak against.
     */
    protected PetType(long wowId, String name, String key, long abilityType, PetType strongAgainst, PetType weakAgainst) {
        super(wowId, name);
        this.key = key;
        this.abilityType = abilityType;
        this.strongAgainst = strongAgainst;
        this.weakAgainst = weakAgainst;
    }

    /**
     * Get the key of the pet type.
     *
     * @return The key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the ability type.
     *
     * @return The ability type.
     */
    public long getAbilityType() {
        return abilityType;
    }

    /**
     * Get the pet type the type is strong against.
     *
     * @return The pet type the type is strong against.
     */
    public PetType getStrongAgainst() {
        return strongAgainst;
    }

    /**
     * Get the pet type the type is weak against.
     *
     * @return The pet type the type is weak against.
     */
    public PetType getWeakAgainst() {
        return weakAgainst;
    }
}
