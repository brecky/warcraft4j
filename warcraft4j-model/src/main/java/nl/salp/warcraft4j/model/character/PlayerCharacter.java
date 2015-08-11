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

package nl.salp.warcraft4j.model.character;

import nl.salp.warcraft4j.model.data.CharacterFaction;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.model.data.CharacterClass;
import nl.salp.warcraft4j.model.data.CharacterRace;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Model of a player character, including all related character specific, static and calculated data.
 *
 * @author Barre Dijkstra
 */
public class PlayerCharacter implements Serializable {
    /** The region the character is in. */
    private final Region region;
    /** The realm the character is residing on. */
    private final String realm;
    /** The character name. */
    private final String characterName;
    /** The character identifier. */
    private final Identifier id;
    /** The character level. */
    private final int level;
    /** The character faction. */
    private final CharacterFaction faction;
    /** The character race. */
    private final CharacterRace race;
    /** The class of the character. */
    private final CharacterClass characterClass;

    public PlayerCharacter(Region region, String realm, String characterName, int level, CharacterFaction faction, CharacterRace race, CharacterClass characterClass) {
        this.region = region;
        this.realm = realm;
        this.characterName = characterName;
        this.id = new Identifier(region, realm, characterName);
        this.level = level;
        this.faction = faction;
        this.race = race;
        this.characterClass = characterClass;
    }


    /**
     * The unique identifier for a player character, consisting of the region, realm and character name.
     */
    public static class Identifier implements Serializable {
        /** The region the character is in. */
        private final Region region;
        /** The realm the character is residing on. */
        private final String realm;
        /** The character name. */
        private final String characterName;

        /**
         * Create a new instance.
         *
         * @param region        The region the character is in.
         * @param realm         The realm the character is residing on.
         * @param characterName The character name.
         *
         * @throws IllegalArgumentException When the provided information is invalid or incomplete.
         */
        public Identifier(Region region, String realm, String characterName) throws IllegalArgumentException {
            if (region == null || isEmpty(realm) || isEmpty(characterName)) {
                throw new IllegalArgumentException(format("Can't create a character identifier with empty values (region='%s', realm='%s', characterName='%s'", region, realm, characterName));
            }
            this.region = region;
            this.realm = realm;
            this.characterName = characterName;
        }

        /**
         * Get the region the character is in.
         *
         * @return The region.
         */
        public Region getRegion() {
            return region;
        }

        /**
         * Get the realm the character is residing on.
         *
         * @return The realm.
         */
        public String getRealm() {
            return realm;
        }

        /**
         * Get the character name.
         *
         * @return The character name.
         */
        public String getCharacterName() {
            return characterName;
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
}
