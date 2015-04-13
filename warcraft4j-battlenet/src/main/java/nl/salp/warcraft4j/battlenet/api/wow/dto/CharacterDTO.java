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

package nl.salp.warcraft4j.battlenet.api.wow.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Battle.NET general character response DTO.
 *
 * @author Barre Dijkstra
 */
public class CharacterDTO {
    @JsonProperty("lastModified")
    private long lastModified;
    @JsonProperty("name")
    private String name;
    @JsonProperty("realm")
    private String realm;
    @JsonProperty("battlegroup")
    private String battlegroup;
    @JsonProperty("class")
    private int characterClass;
    @JsonProperty("race")
    private int race;
    @JsonProperty("level")
    private int level;
    @JsonProperty("gender")
    private int gender;
    @JsonProperty("achievementPoints")
    private int achievementPoints;
    @JsonProperty("thumbnail")
    private String thumbnail;
    @JsonProperty("calcClass")
    private String calcClass;
    @JsonProperty("totalHonorableKills")
    private int totalHonorableKills;
    @JsonProperty("guild")
    private CharacterGuildDTO guild;
    @JsonProperty("items")
    private CharacterItemsDTO items;
    @JsonProperty("appearance")
    private CharacterAppearanceDTO appearance;
    @JsonProperty("mounts")
    private CharacterMountsDTO mounts;
    @JsonProperty("pets")
    private CharacterPetsDTO pets;
    @JsonProperty("petSlots")
    private CharacterPetSlotDTO[] petSlots;
    @JsonProperty("stats")
    private CharacterStatsDTO stats;

    public long getLastModified() {
        return lastModified;
    }

    public void setLastModified(long lastModified) {
        this.lastModified = lastModified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealm() {
        return realm;
    }

    public void setRealm(String realm) {
        this.realm = realm;
    }

    public String getBattlegroup() {
        return battlegroup;
    }

    public void setBattlegroup(String battlegroup) {
        this.battlegroup = battlegroup;
    }

    public int getCharacterClass() {
        return characterClass;
    }

    public void setCharacterClass(int characterClass) {
        this.characterClass = characterClass;
    }

    public int getRace() {
        return race;
    }

    public void setRace(int race) {
        this.race = race;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAchievementPoints() {
        return achievementPoints;
    }

    public void setAchievementPoints(int achievementPoints) {
        this.achievementPoints = achievementPoints;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getCalcClass() {
        return calcClass;
    }

    public void setCalcClass(String calcClass) {
        this.calcClass = calcClass;
    }

    public int getTotalHonorableKills() {
        return totalHonorableKills;
    }

    public void setTotalHonorableKills(int totalHonorableKills) {
        this.totalHonorableKills = totalHonorableKills;
    }

    public CharacterGuildDTO getGuild() {
        return guild;
    }

    public void setGuild(CharacterGuildDTO guild) {
        this.guild = guild;
    }

    public CharacterItemsDTO getItems() {
        return items;
    }

    public void setItems(CharacterItemsDTO items) {
        this.items = items;
    }

    public CharacterAppearanceDTO getAppearance() {
        return appearance;
    }

    public void setAppearance(CharacterAppearanceDTO appearance) {
        this.appearance = appearance;
    }

    public CharacterMountsDTO getMounts() {
        return mounts;
    }

    public void setMounts(CharacterMountsDTO mounts) {
        this.mounts = mounts;
    }

    public CharacterPetsDTO getPets() {
        return pets;
    }

    public void setPets(CharacterPetsDTO pets) {
        this.pets = pets;
    }

    public CharacterPetSlotDTO[] getPetSlots() {
        return petSlots;
    }

    public void setPetSlots(CharacterPetSlotDTO[] petSlots) {
        this.petSlots = petSlots;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public CharacterStatsDTO getStats() {
        return stats;
    }

    public void setStats(CharacterStatsDTO stats) {
        this.stats = stats;
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
