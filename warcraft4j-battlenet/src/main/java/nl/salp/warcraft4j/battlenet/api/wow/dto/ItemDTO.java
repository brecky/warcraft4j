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
public class ItemDTO {
    @JsonProperty("id")
    private int id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("quality")
    private int quality;
    @JsonProperty("allowableClasses")
    private int[] allowableClasses;
    @JsonProperty("itemLevel")
    private int itemLevel;
    @JsonProperty("tooltipParams")
    private ItemTooltipParamsDTO tooltipParams;
    @JsonProperty("stats")
    private StatAmountDTO[] stats;
    @JsonProperty("weaponInfo")
    private WeaponInfoDTO weaponInfo;
    @JsonProperty("armor")
    private int armor;
    @JsonProperty("context")
    private String context;
    @JsonProperty("bonusLists")
    private int[] bonusLists;
    @JsonProperty("stackable")
    private int stackable;
    @JsonProperty("itemBind")
    private int itemBind;
    @JsonProperty("bonusStats")
    private StatAmountDTO[] bonusStats;
    @JsonProperty("buyPrice")
    private long buyPrice;
    @JsonProperty("itemClass")
    private int itemClass;
    @JsonProperty("itemSubClass")
    private int itemSubClass;
    @JsonProperty("containerSlots")
    private int containerSlots;
    @JsonProperty("inventoryType")
    private int inventoryType;
    @JsonProperty("equippable")
    private boolean equippable;
    @JsonProperty("maxCount")
    private int maxCount;
    @JsonProperty("maxDurability")
    private int maxDurability;
    @JsonProperty("minFactionId")
    private int minFactionId;
    @JsonProperty("minReputation")
    private int minReputation;
    @JsonProperty("sellPrice")
    private long sellPrice;
    @JsonProperty("requiredSkill")
    private int requiredSkill;
    @JsonProperty("requiredLevel")
    private int requiredLevel;
    @JsonProperty("requiredSkillRank")
    private int requiredSkillRank;
    @JsonProperty("itemSource")
    private ItemSourceDTO itemSource;
    @JsonProperty("baseArmor")
    private int baseArmor;
    @JsonProperty("hasSockets")
    private boolean hasSockets;
    @JsonProperty("isAuctionable")
    private boolean isAuctionable;
    @JsonProperty("displayInfoId")
    private int displayInfoId;
    @JsonProperty("nameDescription")
    private String nameDescription;
    @JsonProperty("nameDescriptionColor")
    private String nameDescriptionColor;
    @JsonProperty("upgradable")
    private boolean upgradable;
    @JsonProperty("heroicTooltip")
    private boolean heroicTooltip;
    @JsonProperty("availableContexts")
    private String[] availableContexts;
    @JsonProperty("itemSet")
    private ItemSetDTO itemSet;
    @JsonProperty("itemSpells")
    private ItemSpellDTO[] itemSpells;
    @JsonProperty("socketInfo")
    private ItemSocketDTO socketInfo;
    @JsonProperty("disenchantingSkillRank")
    private int disenchantingSkillRank;
    private ItemBonusSummaryDTO bonusSummary;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int[] getAllowableClasses() {
        return allowableClasses;
    }

    public void setAllowableClasses(int[] allowableClasses) {
        this.allowableClasses = allowableClasses;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public void setItemLevel(int itemLevel) {
        this.itemLevel = itemLevel;
    }

    public ItemTooltipParamsDTO getTooltipParams() {
        return tooltipParams;
    }

    public void setTooltipParams(ItemTooltipParamsDTO tooltipParams) {
        this.tooltipParams = tooltipParams;
    }

    public StatAmountDTO[] getStats() {
        return stats;
    }

    public void setStats(StatAmountDTO[] stats) {
        this.stats = stats;
    }

    public WeaponInfoDTO getWeaponInfo() {
        return weaponInfo;
    }

    public void setWeaponInfo(WeaponInfoDTO weaponInfo) {
        this.weaponInfo = weaponInfo;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public int[] getBonusLists() {
        return bonusLists;
    }

    public void setBonusLists(int[] bonusLists) {
        this.bonusLists = bonusLists;
    }

    public int getStackable() {
        return stackable;
    }

    public void setStackable(int stackable) {
        this.stackable = stackable;
    }

    public int getItemBind() {
        return itemBind;
    }

    public void setItemBind(int itemBind) {
        this.itemBind = itemBind;
    }

    public StatAmountDTO[] getBonusStats() {
        return bonusStats;
    }

    public void setBonusStats(StatAmountDTO[] bonusStats) {
        this.bonusStats = bonusStats;
    }

    public long getBuyPrice() {
        return buyPrice;
    }

    public void setBuyPrice(long buyPrice) {
        this.buyPrice = buyPrice;
    }

    public int getItemClass() {
        return itemClass;
    }

    public void setItemClass(int itemClass) {
        this.itemClass = itemClass;
    }

    public int getItemSubClass() {
        return itemSubClass;
    }

    public void setItemSubClass(int itemSubClass) {
        this.itemSubClass = itemSubClass;
    }

    public int getContainerSlots() {
        return containerSlots;
    }

    public void setContainerSlots(int containerSlots) {
        this.containerSlots = containerSlots;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public void setInventoryType(int inventoryType) {
        this.inventoryType = inventoryType;
    }

    public boolean isEquippable() {
        return equippable;
    }

    public void setEquippable(boolean equippable) {
        this.equippable = equippable;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
    }

    public int getMaxDurability() {
        return maxDurability;
    }

    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public int getMinFactionId() {
        return minFactionId;
    }

    public void setMinFactionId(int minFactionId) {
        this.minFactionId = minFactionId;
    }

    public int getMinReputation() {
        return minReputation;
    }

    public void setMinReputation(int minReputation) {
        this.minReputation = minReputation;
    }

    public long getSellPrice() {
        return sellPrice;
    }

    public void setSellPrice(long sellPrice) {
        this.sellPrice = sellPrice;
    }

    public int getRequiredSkill() {
        return requiredSkill;
    }

    public void setRequiredSkill(int requiredSkill) {
        this.requiredSkill = requiredSkill;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public void setRequiredLevel(int requiredLevel) {
        this.requiredLevel = requiredLevel;
    }

    public int getRequiredSkillRank() {
        return requiredSkillRank;
    }

    public void setRequiredSkillRank(int requiredSkillRank) {
        this.requiredSkillRank = requiredSkillRank;
    }

    public ItemSourceDTO getItemSource() {
        return itemSource;
    }

    public void setItemSource(ItemSourceDTO itemSource) {
        this.itemSource = itemSource;
    }

    public int getBaseArmor() {
        return baseArmor;
    }

    public void setBaseArmor(int baseArmor) {
        this.baseArmor = baseArmor;
    }

    public boolean isHasSockets() {
        return hasSockets;
    }

    public void setHasSockets(boolean hasSockets) {
        this.hasSockets = hasSockets;
    }

    public boolean isAuctionable() {
        return isAuctionable;
    }

    public void setAuctionable(boolean isAuctionable) {
        this.isAuctionable = isAuctionable;
    }

    public int getDisplayInfoId() {
        return displayInfoId;
    }

    public void setDisplayInfoId(int displayInfoId) {
        this.displayInfoId = displayInfoId;
    }

    public String getNameDescription() {
        return nameDescription;
    }

    public void setNameDescription(String nameDescription) {
        this.nameDescription = nameDescription;
    }

    public String getNameDescriptionColor() {
        return nameDescriptionColor;
    }

    public void setNameDescriptionColor(String nameDescriptionColor) {
        this.nameDescriptionColor = nameDescriptionColor;
    }

    public boolean isUpgradable() {
        return upgradable;
    }

    public void setUpgradable(boolean upgradable) {
        this.upgradable = upgradable;
    }

    public boolean isHeroicTooltip() {
        return heroicTooltip;
    }

    public void setHeroicTooltip(boolean heroicTooltip) {
        this.heroicTooltip = heroicTooltip;
    }

    public String[] getAvailableContexts() {
        return availableContexts;
    }

    public void setAvailableContexts(String[] availableContexts) {
        this.availableContexts = availableContexts;
    }

    public ItemBonusSummaryDTO getBonusSummary() {
        return bonusSummary;
    }

    public void setBonusSummary(ItemBonusSummaryDTO bonusSummary) {
        this.bonusSummary = bonusSummary;
    }

    public ItemSetDTO getItemSet() {
        return itemSet;
    }

    public void setItemSet(ItemSetDTO itemSet) {
        this.itemSet = itemSet;
    }

    public ItemSocketDTO getSocketInfo() {
        return socketInfo;
    }

    public void setSocketInfo(ItemSocketDTO socketInfo) {
        this.socketInfo = socketInfo;
    }

    public int getDisenchantingSkillRank() {
        return disenchantingSkillRank;
    }

    public void setDisenchantingSkillRank(int disenchantingSkillRank) {
        this.disenchantingSkillRank = disenchantingSkillRank;
    }

    public ItemSpellDTO[] getItemSpells() {
        return itemSpells;
    }

    public void setItemSpells(ItemSpellDTO[] itemSpells) {
        this.itemSpells = itemSpells;
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
