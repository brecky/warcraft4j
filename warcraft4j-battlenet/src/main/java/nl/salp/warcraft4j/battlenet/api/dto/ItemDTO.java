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

package nl.salp.warcraft4j.battlenet.api.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Map;

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
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("quality")
    private int quality;
    @JsonProperty("itemLevel")
    private int itemLevel;
    @JsonProperty("tooltipParams")
    private Map<String, Integer> tooltipParams;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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

    public Map<String, Integer> getTooltipParams() {
        return tooltipParams;
    }

    public void setTooltipParams(Map<String, Integer> tooltipParams) {
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
