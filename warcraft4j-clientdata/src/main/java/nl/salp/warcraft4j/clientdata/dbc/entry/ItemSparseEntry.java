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

package nl.salp.warcraft4j.clientdata.dbc.entry;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "Item-sparse.db2")
public class ItemSparseEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_SPARSE;

    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int quality;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] flags;
    @DbcField(order = 4, dataType = DbcDataType.FLOAT, numberOfEntries = 2, knownMeaning = false)
    private float[] unknownFlags;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int buyCount;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int buyPrice;
    @DbcField(order = 7, dataType = DbcDataType.UINT32)
    private int sellPrice;
    @DbcField(order = 8, dataType = DbcDataType.UINT32)
    private int inventoryType;
    @DbcField(order = 9, dataType = DbcDataType.INT32)
    private int allowableClass;
    @DbcField(order = 10, dataType = DbcDataType.INT32)
    private int allowableRace;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int itemLevel;
    @DbcField(order = 12, dataType = DbcDataType.INT32)
    private int requiredLevel;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int requiredSkill;
    @DbcField(order = 14, dataType = DbcDataType.UINT32)
    private int requiredSkillRank;
    @DbcField(order = 15, dataType = DbcDataType.UINT32)
    private int requiredSpell;
    @DbcField(order = 16, dataType = DbcDataType.UINT32)
    private int requiredHonorRank;
    @DbcField(order = 17, dataType = DbcDataType.UINT32)
    private int requiredCityRank;
    @DbcField(order = 18, dataType = DbcDataType.UINT32)
    private int requiredReputationFaction;
    @DbcField(order = 19, dataType = DbcDataType.UINT32)
    private int requiredReputationRank;
    @DbcField(order = 20, dataType = DbcDataType.UINT32)
    private int maxCount;
    @DbcField(order = 21, dataType = DbcDataType.UINT32)
    private int stackable;
    @DbcField(order = 22, dataType = DbcDataType.UINT32)
    private int containerSlots;
    @DbcField(order = 23, dataType = DbcDataType.INT32, numberOfEntries = 10)
    private int[] itemStatType;
    @DbcField(order = 24, dataType = DbcDataType.INT32, numberOfEntries = 10)
    private int[] itemStatValue;
    @DbcField(order = 25, dataType = DbcDataType.INT32, numberOfEntries = 10)
    private int[] itemStatAllocation;
    @DbcField(order = 26, dataType = DbcDataType.FLOAT, numberOfEntries = 10)
    private float[] itemStatSocketCostMultiplier;
    @DbcField(order = 27, dataType = DbcDataType.UINT32)
    private int scalingStatDistribution;
    @DbcField(order = 28, dataType = DbcDataType.UINT32)
    private int damageType;
    @DbcField(order = 29, dataType = DbcDataType.UINT32)
    private int delay;
    @DbcField(order = 30, dataType = DbcDataType.FLOAT)
    private float rangedModRange;
    @DbcField(order = 31, dataType = DbcDataType.UINT32)
    private int bonding;
    @DbcField(order = 32, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(order = 33, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name2;
    @DbcField(order = 34, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name3;
    @DbcField(order = 35, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name4;
    @DbcField(order = 36, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(order = 37, dataType = DbcDataType.UINT32)
    private int pageText;
    @DbcField(order = 38, dataType = DbcDataType.UINT32)
    private int languageId;
    @DbcField(order = 39, dataType = DbcDataType.UINT32)
    private int pageMaterial;
    @DbcField(order = 40, dataType = DbcDataType.UINT32)
    private int startQuest;
    @DbcField(order = 41, dataType = DbcDataType.UINT32)
    private int lockId;
    @DbcField(order = 42, dataType = DbcDataType.INT32)
    private int material;
    @DbcField(order = 43, dataType = DbcDataType.UINT32)
    private int sheath;
    @DbcField(order = 44, dataType = DbcDataType.UINT32)
    private int randomProperty;
    @DbcField(order = 45, dataType = DbcDataType.UINT32)
    private int randomSuffix;
    @DbcField(order = 46, dataType = DbcDataType.UINT32)
    private int itemSet;
    @DbcField(order = 47, dataType = DbcDataType.UINT32)
    private int area;
    @DbcField(order = 48, dataType = DbcDataType.UINT32)
    private int map;
    @DbcField(order = 49, dataType = DbcDataType.UINT32)
    private int bagFamily;
    @DbcField(order = 50, dataType = DbcDataType.UINT32)
    private int totemCategory;
    @DbcField(order = 51, dataType = DbcDataType.UINT32, numberOfEntries = 3)
    private int[] socketColor;
    @DbcField(order = 52, dataType = DbcDataType.UINT32)
    private int socketBonus;
    @DbcField(order = 53, dataType = DbcDataType.UINT32)
    private int gemProperties;
    @DbcField(order = 54, dataType = DbcDataType.FLOAT)
    private float armorDamageModifier;
    @DbcField(order = 55, dataType = DbcDataType.UINT32)
    private int duration;
    @DbcField(order = 56, dataType = DbcDataType.UINT32)
    private int itemLimitCategory;
    @DbcField(order = 57, dataType = DbcDataType.UINT32)
    private int holidayID;
    @DbcField(order = 58, dataType = DbcDataType.FLOAT)
    private float statScalingFactor;
    @DbcField(order = 59, dataType = DbcDataType.UINT32)
    private int currencySubstitutionID;
    @DbcField(order = 60, dataType = DbcDataType.UINT32)
    private int currencySubstitutionCount;
    @DbcField(order = 61, dataType = DbcDataType.UINT32)
    private int itemNameDescriptionId;


    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getQuality() {
        return quality;
    }

    public int[] getFlags() {
        return flags;
    }

    public float[] getUnknownFlags() {
        return unknownFlags;
    }

    public int getBuyCount() {
        return buyCount;
    }

    public int getBuyPrice() {
        return buyPrice;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public int getInventoryType() {
        return inventoryType;
    }

    public int getAllowableClass() {
        return allowableClass;
    }

    public int getAllowableRace() {
        return allowableRace;
    }

    public int getItemLevel() {
        return itemLevel;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public int getRequiredSkill() {
        return requiredSkill;
    }

    public int getRequiredSkillRank() {
        return requiredSkillRank;
    }

    public int getRequiredSpell() {
        return requiredSpell;
    }

    public int getRequiredHonorRank() {
        return requiredHonorRank;
    }

    public int getRequiredCityRank() {
        return requiredCityRank;
    }

    public int getRequiredReputationFaction() {
        return requiredReputationFaction;
    }

    public int getRequiredReputationRank() {
        return requiredReputationRank;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public int getStackable() {
        return stackable;
    }

    public int getContainerSlots() {
        return containerSlots;
    }

    public int[] getItemStatType() {
        return itemStatType;
    }

    public int[] getItemStatValue() {
        return itemStatValue;
    }

    public int[] getItemStatAllocation() {
        return itemStatAllocation;
    }

    public float[] getItemStatSocketCostMultiplier() {
        return itemStatSocketCostMultiplier;
    }

    public int getScalingStatDistribution() {
        return scalingStatDistribution;
    }

    public int getDamageType() {
        return damageType;
    }

    public int getDelay() {
        return delay;
    }

    public float getRangedModRange() {
        return rangedModRange;
    }

    public int getBonding() {
        return bonding;
    }

    public String getName() {
        return name;
    }

    public String getName2() {
        return name2;
    }

    public String getName3() {
        return name3;
    }

    public String getName4() {
        return name4;
    }

    public String getDescription() {
        return description;
    }

    public int getPageText() {
        return pageText;
    }

    public int getLanguageId() {
        return languageId;
    }

    public int getPageMaterial() {
        return pageMaterial;
    }

    public int getStartQuest() {
        return startQuest;
    }

    public int getLockId() {
        return lockId;
    }

    public int getMaterial() {
        return material;
    }

    public int getSheath() {
        return sheath;
    }

    public int getRandomProperty() {
        return randomProperty;
    }

    public int getRandomSuffix() {
        return randomSuffix;
    }

    public int getItemSet() {
        return itemSet;
    }

    public int getArea() {
        return area;
    }

    public int getMap() {
        return map;
    }

    public int getBagFamily() {
        return bagFamily;
    }

    public int getTotemCategory() {
        return totemCategory;
    }

    public int[] getSocketColor() {
        return socketColor;
    }

    public int getSocketBonus() {
        return socketBonus;
    }

    public int getGemProperties() {
        return gemProperties;
    }

    public float getArmorDamageModifier() {
        return armorDamageModifier;
    }

    public int getDuration() {
        return duration;
    }

    public int getItemLimitCategory() {
        return itemLimitCategory;
    }

    public int getHolidayID() {
        return holidayID;
    }

    public float getStatScalingFactor() {
        return statScalingFactor;
    }

    public int getCurrencySubstitutionID() {
        return currencySubstitutionID;
    }

    public int getCurrencySubstitutionCount() {
        return currencySubstitutionCount;
    }

    public int getItemNameDescriptionId() {
        return itemNameDescriptionId;
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
