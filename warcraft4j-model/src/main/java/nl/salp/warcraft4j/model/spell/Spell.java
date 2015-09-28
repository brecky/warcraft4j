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
package nl.salp.warcraft4j.model.spell;

import nl.salp.warcraft4j.model.WowEntity;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Spell extends WowEntity {
    // private int id;
    private String name; // ref -> StringTable
    private String subText; // ref -> StringTable
    private String description; // ref -> StringTable
    private String auraDescription; // ref -> StringTable
    // private RuneCost runeCost; // ref [unknown] -> SpellRunecost.db2
    // private SpellMissile missile; // ref [unknown] -> SpellMissile.db2
    private String descriptionVariable; // ref -> SpellDescriptionVariables.dbc, col 2 -> StringTable entry
    // private SpellScaling scaling; // ref [unknown] -> SpellScaling.dbc (and GtSpellScaling.dbc contains 2 columns, first level, 2nd float, prob scaling factor)
    // private SpellAuraOption auraOption; // ref -> SpellAuraOptions.dbc
    // private SpellAuraRestriction auraRestriction; // ref -> SpellAuraRestrictions.db2
    // private SpellCastingRequirement castingRequirement; // ref [unknown] -> SpellCastingRequirements.db2
    // private SpellCategory category; // ref -> SpellCategories.dbc
    // private SpellClassOption classOption; // ref [unknown] -> SpellClassOptions.db2
    // private SpellCooldown cooldown; // ref -> SpellCooldowns.dbc
    // private SpellEquippedItem equippedItem; // ref -> SpellEquippedItems.dbc
    // private SpellInterrupt interrupt; // ref -> SpellInterrupts.dbc
    // private SpellLevel level; // ref -> SpellLevels.dbc
    // private SpellReagent reagent; // ref [unknown] -> SpellReagents.db2
    // private SpellShapeshift shapeshift; // ref [unknown] -> SpellShapeshift.dbc
    // private SpellTargetRestriction targetRestriction; // ref [unknown] -> SpellTargetRestrictions.dbc
    // private SpellTotem totem; // ref [unknown] -> SpellTotems.db2
    // private SpellRequiredProject requiredProject; // ref -> ????
    // private SpellMisc misc; // ref -> SpellMisc.db2

    public Spell(int id, String name, String subText, String description, String auraDescription, String descriptionVariable) {
        super(id);
        this.name = name;
        this.subText = subText;
        this.description = description;
        this.auraDescription = auraDescription;
        this.descriptionVariable = descriptionVariable;
    }

    public String getName() {
        return name;
    }

    public String getSubText() {
        return subText;
    }

    public String getDescription() {
        return description;
    }

    public String getAuraDescription() {
        return auraDescription;
    }

    public String getDescriptionVariable() {
        return descriptionVariable;
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
