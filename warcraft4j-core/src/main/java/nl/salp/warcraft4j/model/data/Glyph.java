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
 * Glyph.
 *
 * @author Barre Dijkstra
 */
public class Glyph extends StaticDataEntity {
    /** the glyph type. */
    private final GlyphType type;
    /** The item of the glyph. */
    private final Item glyphItem;
    /** The icon name. */
    private final String icon;
    /** The character class the glyph applies to. */
    private final CharacterClass characterClass;
    /** The class specialisations the glyph is exclusive for or empty if there is no specialisation restriction. */
    private final ClassSpecialisation[] classSpecialisations;
    /** The glyphs which usage is excluded by this glyph. */
    private final Glyph[] excludes;

    /**
     * Create a new glyph.
     *
     * @param wowId                The ID of the entity as used in World of Warcraft.
     * @param name                 The name of the entity.
     * @param type                 The type of glyph.
     * @param glyphItem            The item of the glyph.
     * @param icon                 The icon of the glyph.
     * @param characterClass       The character class the glyph applies to.
     * @param classSpecialisations The character specialisations the glyph is exclusive for.
     * @param excludes             The glyphs which usage are excluded by this glyph.
     */
    protected Glyph(long wowId, String name, GlyphType type, Item glyphItem, String icon, CharacterClass characterClass, ClassSpecialisation[] classSpecialisations, Glyph[] excludes) {
        super(wowId, name);
        this.glyphItem = glyphItem;
        this.icon = icon;
        this.characterClass = characterClass;
        if (classSpecialisations == null) {
            this.classSpecialisations = new ClassSpecialisation[0];
        } else {
            this.classSpecialisations = classSpecialisations;
        }
        if (excludes == null) {
            this.excludes = new Glyph[0];
        } else {
            this.excludes = excludes;
        }
        this.type = type;
    }

    /**
     * Get the glyph type.
     *
     * @return The glyph type.
     */
    public GlyphType getType() {
        return type;
    }

    /**
     * Get the glyph item.
     *
     * @return The glyph item.
     */
    public Item getGlyphItem() {
        return glyphItem;
    }

    /**
     * Get the name of the icon for the glyph.
     *
     * @return The name of the icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Get the character class the glyph applies to.
     *
     * @return The character class.
     */
    public CharacterClass getCharacterClass() {
        return characterClass;
    }

    /**
     * Get the class specialisations that can use the glyph.
     *
     * @return The class specialisations that are allowed to use the glyph or an empty array for all specialisations.
     */
    public ClassSpecialisation[] getClassSpecialisations() {
        return classSpecialisations;
    }

    /**
     * Get the glyphs which usage is excluded by this glyph.
     *
     * @return The excluded glyphs.
     */
    public Glyph[] getExcludes() {
        return excludes;
    }

    /**
     * Check if this glyph excludes the usage of the provided glyph.
     *
     * @param glyph The glyph to check for.
     *
     * @return {@code true} if the usage of the provided glyph is excluded by using this glyph.
     */
    public boolean isExcluding(Glyph glyph) {
        boolean excluded = false;
        if (glyph != null && !glyph.equals(this)) {
            for (Glyph g : excludes) {
                if (g.equals(glyph)) {
                    excluded = true;
                }
            }
        }
        return excluded;
    }
}
