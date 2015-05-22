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
 * Achievement data DTO.
 *
 * @author Barre Dijkstra
 */
public class AchievementDTO {
    @JsonProperty("id")
    private long id;
    @JsonProperty("title")
    private String title;
    @JsonProperty("points")
    private int points;
    @JsonProperty("description")
    private String description;
    @JsonProperty("reward")
    private String reward;
    @JsonProperty("rewardItems")
    private ItemDTO[] rewardItems;
    @JsonProperty("icon")
    private String icon;
    @JsonProperty("criteria")
    private AchievementCriteriaDTO[] criteria;
    @JsonProperty("accountWide")
    private boolean accountWide;
    @JsonProperty("factionId")
    private int factionId;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReward() {
        return reward;
    }

    public void setReward(String reward) {
        this.reward = reward;
    }

    public ItemDTO[] getRewardItems() {
        return rewardItems;
    }

    public void setRewardItems(ItemDTO[] rewardItems) {
        this.rewardItems = rewardItems;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public AchievementCriteriaDTO[] getCriteria() {
        return criteria;
    }

    public void setCriteria(AchievementCriteriaDTO[] criteria) {
        this.criteria = criteria;
    }

    public boolean isAccountWide() {
        return accountWide;
    }

    public void setAccountWide(boolean accountWide) {
        this.accountWide = accountWide;
    }

    public int getFactionId() {
        return factionId;
    }

    public void setFactionId(int factionId) {
        this.factionId = factionId;
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
