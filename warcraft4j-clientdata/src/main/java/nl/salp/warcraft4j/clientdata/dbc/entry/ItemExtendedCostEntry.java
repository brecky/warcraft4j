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
import nl.salp.warcraft4j.clientdata.dbc.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
@DbcMapping(file = "ItemExtendedCost.db2")
public class ItemExtendedCostEntry implements DbcEntry {
    private static final DbcType ENTRY_TYPE = DbcType.ITEM_EXTENDED_COST;
    // TODO Implement me!
    @DbcField(order = 1, dataType = DbcDataType.UINT32)
    private int id;
    @DbcField(order = 2, dataType = DbcDataType.UINT32)
    private int requiredArenaSlot;
    @DbcField(order = 3, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredItem;
    @DbcField(order = 4, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredItemCount;
    @DbcField(order = 5, dataType = DbcDataType.UINT32)
    private int requiredPersonalArenaRating;
    @DbcField(order = 6, dataType = DbcDataType.UINT32)
    private int itemPurchaseGroup;
    @DbcField(order = 7, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredCurrency;
    @DbcField(order = 8, dataType = DbcDataType.UINT32, numberOfEntries = 5)
    private int[] requiredCurrencyCount;
    @DbcField(order = 9, dataType = DbcDataType.UINT32)
    private int requiredFactionId;
    @DbcField(order = 10, dataType = DbcDataType.UINT32)
    private int requiredFactionStanding;
    @DbcField(order = 11, dataType = DbcDataType.UINT32)
    private int requirementsFlag;
    @DbcField(order = 12, dataType = DbcDataType.UINT32)
    private int requiredAchievementId;
    @DbcField(order = 13, dataType = DbcDataType.UINT32)
    private int requiredMoney;


    @Override
    public DbcType getEntryType() {
        return ENTRY_TYPE;
    }

    @Override
    public int getId() {
        return id;
    }

    public int getRequiredArenaSlot() {
        return requiredArenaSlot;
    }

    public int[] getRequiredItem() {
        return requiredItem;
    }

    public int[] getRequiredItemCount() {
        return requiredItemCount;
    }

    public int getItemPurchaseGroup() {
        return itemPurchaseGroup;
    }

    public int[] getRequiredCurrency() {
        return requiredCurrency;
    }

    public int[] getRequiredCurrencyCount() {
        return requiredCurrencyCount;
    }

    public int getRequiredFactionId() {
        return requiredFactionId;
    }

    public int getRequiredFactionStanding() {
        return requiredFactionStanding;
    }

    public int getRequirementsFlag() {
        return requirementsFlag;
    }

    public int getRequiredAchievementId() {
        return requiredAchievementId;
    }

    public int getRequiredMoney() {
        return requiredMoney;
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
