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
public class ItemSpellDTO {
    @JsonProperty("spellId")
    private long spellId;
    @JsonProperty("spell")
    private SpellDTO spell;
    @JsonProperty("nCharges")
    private int nCharges;
    @JsonProperty("consumable")
    private boolean consumable;
    @JsonProperty("categoryId")
    private int categoryId;
    @JsonProperty("trigger")
    private String trigger;

    public long getSpellId() {
        return spellId;
    }

    public void setSpellId(long spellId) {
        this.spellId = spellId;
    }

    public SpellDTO getSpell() {
        return spell;
    }

    public void setSpell(SpellDTO spell) {
        this.spell = spell;
    }

    public int getnCharges() {
        return nCharges;
    }

    public void setnCharges(int nCharges) {
        this.nCharges = nCharges;
    }

    public boolean isConsumable() {
        return consumable;
    }

    public void setConsumable(boolean consumable) {
        this.consumable = consumable;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTrigger() {
        return trigger;
    }

    public void setTrigger(String trigger) {
        this.trigger = trigger;
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
