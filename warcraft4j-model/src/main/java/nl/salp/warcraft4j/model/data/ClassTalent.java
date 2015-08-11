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
 * Class talent.
 *
 * @author Barre Dijkstra
 */
public class ClassTalent extends StaticDataEntity {
    /** The tier of the talent. */
    private final int tier;
    /** The column of the talent. */
    private final int column;
    /** The character class the talent is for. */
    private final CharacterClass characterClass;
    /** The class specialisation the talent is for or {@code null} if it's not specialisation specific. */
    private final ClassSpecialisation classSpecialisation;
    /** The spell the talent provides. */
    private final Spell spell;

    /**
     * Create a new talent.
     *
     * @param wowId               The ID of the class talent as used in World of Warcraft.
     * @param name                The name of the class talent.
     * @param tier                The tier of the talent.
     * @param column              The column of the talent.
     * @param characterClass      The character class the talent is for.
     * @param classSpecialisation The class specialisation the talent is for or {@code null} if it's not specialisation specific.
     * @param spell               The spell the talent provides.
     */
    public ClassTalent(long wowId, String name, int tier, int column, CharacterClass characterClass, ClassSpecialisation classSpecialisation, Spell spell) {
        super(wowId, name);
        this.tier = tier;
        this.column = column;
        this.characterClass = characterClass;
        this.classSpecialisation = classSpecialisation;
        this.spell = spell;
    }

    /**
     * Get the tier of the talent.
     *
     * @return The tier.
     */
    public int getTier() {
        return tier;
    }

    /**
     * Get the column of the talent.
     *
     * @return The column.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Get the character class the talent is for.
     *
     * @return The character class.
     */
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    /**
     * Get the class specialisation the talent is for.
     *
     * @return The class specialisation or {@code null} for a non-specialisation specific talent.
     */
    public ClassSpecialisation getClassSpecialisation() {
        return classSpecialisation;
    }

    /**
     * Get the spell the talent provides.
     *
     * @return The spell.
     */
    public Spell getSpell() {
        return spell;
    }
}
