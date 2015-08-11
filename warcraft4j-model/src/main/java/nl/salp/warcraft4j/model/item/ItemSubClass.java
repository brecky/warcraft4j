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

package nl.salp.warcraft4j.model.item;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ItemSubClass {
    /** The id of the sub class. */
    private int id;
    /** The name of the sub class. */
    private String name;
    /** The plural name of the sub class ({@code null} when there is no different plural form). */
    private String namePlural;
    /** TODO Refactor. */
    private int displayFlags;
    /** TODO Refactor. */
    private int weaponParrySequenceId;
    /** TODO Refactor. */
    private int weaponReadySequenceId;
    /** TODO Refactor. */
    private int weaponAttackSequenceId;
    /** TODO Refactor. */
    private int weaponSwingSize;
    /** TODO Refactor. */
    private int prerequisiteProficiencyId;
    /** TODO Refactor. */
    private int postrequisiteProficiencyId;

    public ItemSubClass(int id, String name, String namePlural, int displayFlags, int weaponParrySequenceId, int weaponReadySequenceId, int weaponAttackSequenceId, int weaponSwingSize, int prerequisiteProficiencyId, int postrequisiteProficiencyId) {
        this.id = id;
        this.name = name;
        this.namePlural = namePlural;
        this.displayFlags = displayFlags;
        this.weaponParrySequenceId = weaponParrySequenceId;
        this.weaponReadySequenceId = weaponReadySequenceId;
        this.weaponAttackSequenceId = weaponAttackSequenceId;
        this.weaponSwingSize = weaponSwingSize;
        this.prerequisiteProficiencyId = prerequisiteProficiencyId;
        this.postrequisiteProficiencyId = postrequisiteProficiencyId;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNamePlural() {
        return namePlural;
    }

    public int getDisplayFlags() {
        return displayFlags;
    }

    public int getWeaponParrySequenceId() {
        return weaponParrySequenceId;
    }

    public int getWeaponReadySequenceId() {
        return weaponReadySequenceId;
    }

    public int getWeaponAttackSequenceId() {
        return weaponAttackSequenceId;
    }

    public int getWeaponSwingSize() {
        return weaponSwingSize;
    }

    public int getPrerequisiteProficiencyId() {
        return prerequisiteProficiencyId;
    }

    public int getPostrequisiteProficiencyId() {
        return postrequisiteProficiencyId;
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
