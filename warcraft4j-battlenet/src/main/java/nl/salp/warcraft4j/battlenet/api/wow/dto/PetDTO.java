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
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class PetDTO {
    @JsonProperty("name")
    private String name;
    @JsonProperty("spellId")
    private int spellId;
    @JsonProperty("creatureId")
    private int creatureId;
    @JsonProperty("itemId")
    private int itemId;
    @JsonProperty("qualityId")
    private int qualityId;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("stats")
    private PetStatsDTO stats;
    @JsonProperty("battlePetGuid")
    private String battlePetGuid;
    @JsonProperty("isFavorite")
    private boolean isFavorite;
    @JsonProperty("isFirstAbilitySlotSelected")
    private boolean isFirstAbilitySlotSelected;
    @JsonProperty("isSecondAbilitySlotSelected")
    private boolean isSecondAbilitySlotSelected;
    @JsonProperty("isThirdAbilitySlotSelected")
    private boolean isThirdAbilitySlotSelected;
    @JsonProperty("creatureName")
    private String creatureName;
    @JsonProperty("canBattle")
    private boolean canBattle;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSpellId() {
        return spellId;
    }

    public void setSpellId(int spellId) {
        this.spellId = spellId;
    }

    public int getCreatureId() {
        return creatureId;
    }

    public void setCreatureId(int creatureId) {
        this.creatureId = creatureId;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getQualityId() {
        return qualityId;
    }

    public void setQualityId(int qualityId) {
        this.qualityId = qualityId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public PetStatsDTO getStats() {
        return stats;
    }

    public void setStats(PetStatsDTO stats) {
        this.stats = stats;
    }

    public String getBattlePetGuid() {
        return battlePetGuid;
    }

    public void setBattlePetGuid(String battlePetGuid) {
        this.battlePetGuid = battlePetGuid;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public boolean isFirstAbilitySlotSelected() {
        return isFirstAbilitySlotSelected;
    }

    public void setFirstAbilitySlotSelected(boolean isFirstAbilitySlotSelected) {
        this.isFirstAbilitySlotSelected = isFirstAbilitySlotSelected;
    }

    public boolean isSecondAbilitySlotSelected() {
        return isSecondAbilitySlotSelected;
    }

    public void setSecondAbilitySlotSelected(boolean isSecondAbilitySlotSelected) {
        this.isSecondAbilitySlotSelected = isSecondAbilitySlotSelected;
    }

    public boolean isThirdAbilitySlotSelected() {
        return isThirdAbilitySlotSelected;
    }

    public void setThirdAbilitySlotSelected(boolean isThirdAbilitySlotSelected) {
        this.isThirdAbilitySlotSelected = isThirdAbilitySlotSelected;
    }

    public String getCreatureName() {
        return creatureName;
    }

    public void setCreatureName(String creatureName) {
        this.creatureName = creatureName;
    }

    public boolean isCanBattle() {
        return canBattle;
    }

    public void setCanBattle(boolean canBattle) {
        this.canBattle = canBattle;
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
