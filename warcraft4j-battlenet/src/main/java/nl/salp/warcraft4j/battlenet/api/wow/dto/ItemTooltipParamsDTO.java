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
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ItemTooltipParamsDTO {
    @JsonProperty("tinker")
    private int tinker;
    @JsonProperty("transmogItem")
    private long transmogItem;
    @JsonProperty("set")
    private int[] set;
    @JsonProperty("enchant")
    private long enchant;
    @JsonProperty("gem0")
    private long gem0;
    @JsonProperty("gem1")
    private long gem1;
    @JsonProperty("gem2")
    private long gem2;
    @JsonProperty("upgrade")
    private ItemUpgradeDTO upgrade;

    public int getTinker() {
        return tinker;
    }

    public void setTinker(int tinker) {
        this.tinker = tinker;
    }

    public long getTransmogItem() {
        return transmogItem;
    }

    public void setTransmogItem(long transmogItem) {
        this.transmogItem = transmogItem;
    }

    public int[] getSet() {
        return set;
    }

    public void setSet(int[] set) {
        this.set = set;
    }

    public long getEnchant() {
        return enchant;
    }

    public void setEnchant(long enchant) {
        this.enchant = enchant;
    }

    public long getGem0() {
        return gem0;
    }

    public void setGem0(long gem0) {
        this.gem0 = gem0;
    }

    public long getGem1() {
        return gem1;
    }

    public void setGem1(long gem1) {
        this.gem1 = gem1;
    }

    public long getGem2() {
        return gem2;
    }

    public void setGem2(long gem2) {
        this.gem2 = gem2;
    }

    public ItemUpgradeDTO getUpgrade() {
        return upgrade;
    }

    public void setUpgrade(ItemUpgradeDTO upgrade) {
        this.upgrade = upgrade;
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
