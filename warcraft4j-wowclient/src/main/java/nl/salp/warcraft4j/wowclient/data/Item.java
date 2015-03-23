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

package nl.salp.warcraft4j.wowclient.data;

import nl.salp.warcraft4j.wowclient.dbc.mapping.Dbc;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcDataType;
import nl.salp.warcraft4j.wowclient.dbc.mapping.DbcField;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
@Dbc("Item-sparse.db2")
public class Item {
    @DbcField(name = "id", column = 1, dataType = DbcDataType.INT32)
    private int id;
    @DbcField(name = "quality", column = 2, dataType = DbcDataType.INT32)
    private int quality;
    @DbcField(name = "flags", numberOfEntries = 2, column = 3, dataType = DbcDataType.INT32)
    private Integer[] flags;
    @DbcField(name = "buyPrice", column = 5, dataType = DbcDataType.INT32)
    private int buyPrice;
    @DbcField(name = "sellPrice", column = 6, dataType = DbcDataType.INT32)
    private int sellPrice;
    @DbcField(name = "inventoryType", column = 7, dataType = DbcDataType.INT32)
    private int inventoryType;
    @DbcField(name = "allowableClass", column = 8, dataType = DbcDataType.INT32)
    private int allowableClass;
    @DbcField(name = "allowableRace", column = 9, dataType = DbcDataType.INT32)
    private int allowableRace;
    @DbcField(name = "itemLevel", column = 10, dataType = DbcDataType.INT32)
    private int itemLevel;
    @DbcField(name = "requiredLevel", column = 11, dataType = DbcDataType.INT32)
    private int requiredLevel;
    @DbcField(name = "requiredSkill", column = 12, dataType = DbcDataType.INT32)
    private int requiredSkill;
    @DbcField(name = "requiredSkillRank", column = 13, dataType = DbcDataType.INT32)
    private int requiredSkillRank;
    @DbcField(name = "requiredSpell", column = 14, dataType = DbcDataType.INT32)
    private int requiredSpell;
    @DbcField(name = "requiredHonorRank", column = 15, dataType = DbcDataType.INT32)
    private int requiredHonorRank;
    @DbcField(name = "requiredCityRank", column = 16, dataType = DbcDataType.INT32)
    private int requiredCityRank;
    @DbcField(name = "requiredReputationFaction", column = 17, dataType = DbcDataType.INT32)
    private int requiredReputationFaction;
    @DbcField(name = "requiredReputationFactionRank", column = 18, dataType = DbcDataType.INT32)
    private int requiredReputationFactionRank;
    @DbcField(name = "maxCount", column = 19, dataType = DbcDataType.INT32)
    private int maxCount;
    @DbcField(name = "stackable", column = 20, dataType = DbcDataType.INT32)
    private int stackable;
    @DbcField(name = "containerSlots", column = 21, dataType = DbcDataType.INT32)
    private int containerSlots;
    @DbcField(name = "itemStatType", numberOfEntries = 10, column = 22, dataType = DbcDataType.INT32)
    private Integer[] itemStatType;
    @DbcField(name = "itemStatValue", numberOfEntries = 10, column = 32, dataType = DbcDataType.INT32)
    private Integer[] itemStatValue;
    @DbcField(name = "itemStatUnknown1", numberOfEntries = 10, column = 42, dataType = DbcDataType.INT32)
    private Integer[] itemStatUnknown1;
    @DbcField(name = "itemStatUnknown2", numberOfEntries = 10, column = 52, dataType = DbcDataType.INT32)
    private Integer[] itemStatUnknown2;
    @DbcField(name = "scalingStatDistribution", column = 62, dataType = DbcDataType.INT32)
    private int scalingStatDistribution;
    @DbcField(name = "damageType", column = 63, dataType = DbcDataType.INT32)
    private int damageType;
    @DbcField(name = "delay", column = 64, dataType = DbcDataType.INT32)
    private int delay;
    @DbcField(name = "rangedModRange", column = 65, dataType = DbcDataType.FLOAT)
    private float rangedModRange;
    @DbcField(name = "spellId", numberOfEntries = 5, column = 66, dataType = DbcDataType.INT32)
    private Integer[] spellId;
    @DbcField(name = "spellTrigger", numberOfEntries = 5, column = 71, dataType = DbcDataType.INT32)
    private Integer[] spellTrigger;
    @DbcField(name = "spellCharges", numberOfEntries = 5, column = 76, dataType = DbcDataType.INT32)
    private Integer[] spellCharges;
    @DbcField(name = "spellCooldowns", numberOfEntries = 5, column = 81, dataType = DbcDataType.INT32)
    private Integer[] spellCooldowns;
    @DbcField(name = "spellCategories", numberOfEntries = 5, column = 86, dataType = DbcDataType.INT32)
    private Integer[] spellCategories;
    @DbcField(name = "spellCategoryCooldowns", numberOfEntries = 5, column = 91, dataType = DbcDataType.INT32)
    private Integer[] spellCategoryCooldowns;
    @DbcField(name = "bonding", column = 96, dataType = DbcDataType.INT32)
    private int bonding;
    @DbcField(name = "name", column = 97, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name;
    @DbcField(name = "name2", column = 98, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name2;
    @DbcField(name = "name3", column = 99, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name3;
    @DbcField(name = "name4", column = 100, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String name4;
    @DbcField(name = "description", column = 101, dataType = DbcDataType.STRINGTABLE_REFERENCE)
    private String description;
    @DbcField(name = "pageText", column = 102, dataType = DbcDataType.INT32)
    private int pageText;
    @DbcField(name = "languageId", column = 103, dataType = DbcDataType.INT32)
    private int languageId;
    @DbcField(name = "pageMaterial", column = 104, dataType = DbcDataType.INT32)
    private int pageMaterial;
    @DbcField(name = "startQuest", column = 105, dataType = DbcDataType.INT32)
    private int startQuest;
    @DbcField(name = "lockId", column = 106, dataType = DbcDataType.INT32)
    private int lockId;
    @DbcField(name = "material", column = 107, dataType = DbcDataType.INT32)
    private int material;
    @DbcField(name = "sheath", column = 108, dataType = DbcDataType.INT32)
    private int sheath;
    @DbcField(name = "randomProperty", column = 109, dataType = DbcDataType.INT32)
    private int randomProperty;
    @DbcField(name = "randomSuffix", column = 110, dataType = DbcDataType.INT32)
    private int randomSuffix;
    @DbcField(name = "itemSet", column = 111, dataType = DbcDataType.INT32)
    private int itemSet;
    @DbcField(name = "maxDurability", column = 112, dataType = DbcDataType.INT32)
    private int maxDurability;
    @DbcField(name = "area", column = 113, dataType = DbcDataType.INT32)
    private int area;
    @DbcField(name = "map", column = 114, dataType = DbcDataType.INT32)
    private int map;
    @DbcField(name = "bagFamily", column = 115, dataType = DbcDataType.INT32)
    private int bagFamily;
    @DbcField(name = "totemCategory", column = 116, dataType = DbcDataType.INT32)
    private int totemCategory;
    @DbcField(name = "socketColors", numberOfEntries = 3, column = 117, dataType = DbcDataType.INT32)
    private Integer[] socketColors;
    @DbcField(name = "socketContents", numberOfEntries = 3, column = 120, dataType = DbcDataType.INT32)
    private Integer[] socketContents;
    @DbcField(name = "gemProperties", column = 123, dataType = DbcDataType.INT32)
    private int gemProperties;
    @DbcField(name = "armorDamageModifier", column = 124, dataType = DbcDataType.FLOAT)
    private float armorDamageModifier;
    @DbcField(name = "duration", column = 125, dataType = DbcDataType.INT32)
    private int duration;
    @DbcField(name = "itemLimitCategory", column = 126, dataType = DbcDataType.INT32)
    private int itemLimitCategory;
    @DbcField(name = "holidayId", column = 127, dataType = DbcDataType.INT32)
    private int holidayId;
    @DbcField(name = "statScalingFactor", column = 128, dataType = DbcDataType.FLOAT)
    private float statScalingFactor;
    @DbcField(name = "field130", column = 129, dataType = DbcDataType.INT32)
    private int field130;
    @DbcField(name = "field131", column = 130, dataType = DbcDataType.INT32)
    private int field131;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
