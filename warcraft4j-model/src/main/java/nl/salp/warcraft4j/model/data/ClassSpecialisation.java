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
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ClassSpecialisation extends StaticDataEntity {
    /** The character class the specialisation is for. */
    private final CharacterClass characterClass;
    /** The specialisation description. */
    private final String description;
    /** The role the specialisation fulfills. */
    private final ClassRole role;
    /** The name of the icon for the specialisation. */
    private final String icon;
    /** The name of the background image for the specialisation. */
    private final String backgroundImage;

    /**
     * Create a new class specialisation.
     *
     * @param wowId           The ID of the class specialisation as used in World of Warcraft.
     * @param name            The name of the class specialisation.
     * @param characterClass  The character class the specialisation is for.
     * @param description     The description of the specialisation.
     * @param role            The role the specialisation fulfills.
     * @param icon            The name of the icon for the specialisation.
     * @param backgroundImage The name of the background iamge for the specialisation.
     */
    public ClassSpecialisation(long wowId, String name, CharacterClass characterClass, String description, ClassRole role, String icon, String backgroundImage) {
        super(wowId, name);
        this.characterClass = characterClass;
        this.description = description;
        this.role = role;
        this.icon = icon;
        this.backgroundImage = backgroundImage;
    }

    /**
     * Get the character class the specialisation is for.
     *
     * @return The character class.
     */
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    /**
     * Get the description of the specialisation.
     *
     * @return The description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Get the role the specialisation fulfills.
     *
     * @return The role.
     */
    public ClassRole getRole() {
        return role;
    }

    /**
     * Get the name of the icon for the class specialisation.
     *
     * @return The name of the icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Get the name of the background image for the class specialisation.
     *
     * @return The name of the background image.
     */
    public String getBackgroundImage() {
        return backgroundImage;
    }
}
