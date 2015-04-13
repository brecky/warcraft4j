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
 * Character achievement DTO, which is an optional snippet of the {@link CharacterDTO}.
 *
 * @author Barre Dijkstra
 */
public class CharacterAchievementDTO {
    @JsonProperty("achievementsCompleted")
    private int[] achievementsCompleted;
    @JsonProperty("achievementsCompletedTimestamp")
    private long[] achievementsCompletedTimestamp;
    @JsonProperty("criteria")
    private int[] criteria;
    @JsonProperty("criteriaQuantity")
    private int[] criteriaQuantity;
    @JsonProperty("criteriaTimestamp")
    private long[] criteriaTimestamp;
    @JsonProperty("criteriaCreated")
    private long[] criteriaCreated;

    public int[] getAchievementsCompleted() {
        return achievementsCompleted;
    }

    public void setAchievementsCompleted(int[] achievementsCompleted) {
        this.achievementsCompleted = achievementsCompleted;
    }

    public long[] getAchievementsCompletedTimestamp() {
        return achievementsCompletedTimestamp;
    }

    public void setAchievementsCompletedTimestamp(long[] achievementsCompletedTimestamp) {
        this.achievementsCompletedTimestamp = achievementsCompletedTimestamp;
    }

    public int[] getCriteria() {
        return criteria;
    }

    public void setCriteria(int[] criteria) {
        this.criteria = criteria;
    }

    public int[] getCriteriaQuantity() {
        return criteriaQuantity;
    }

    public void setCriteriaQuantity(int[] criteriaQuantity) {
        this.criteriaQuantity = criteriaQuantity;
    }

    public long[] getCriteriaTimestamp() {
        return criteriaTimestamp;
    }

    public void setCriteriaTimestamp(long[] criteriaTimestamp) {
        this.criteriaTimestamp = criteriaTimestamp;
    }

    public long[] getCriteriaCreated() {
        return criteriaCreated;
    }

    public void setCriteriaCreated(long[] criteriaCreated) {
        this.criteriaCreated = criteriaCreated;
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
