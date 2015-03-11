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

package nl.salp.warcraft4j.battlenet.character.dto;

import com.owlike.genson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class CharacterItemsDTO {
    /*
    "items": {
        "averageItemLevel": int,
        "averageItemLevelEquipped": int,
        string<slot>: EquippedItemDTO...
    }
     */
    @JsonProperty("averageItemLevel")
    private int averageItemLevel;
    @JsonProperty("averageItemLevelEquipped")
    private int averageItemLevelEquipped;
    @JsonProperty("head")
    private EquippedItemDTO head;
    @JsonProperty("neck")
    private EquippedItemDTO neck;
    @JsonProperty("shoulder")
    private EquippedItemDTO shoulder;
    @JsonProperty("back")
    private EquippedItemDTO back;
    @JsonProperty("chest")
    private EquippedItemDTO chest;
    @JsonProperty("shirt")
    private EquippedItemDTO shirt;
    @JsonProperty("wrist")
    private EquippedItemDTO wrist;
    @JsonProperty("hands")
    private EquippedItemDTO hands;
    @JsonProperty("waist")
    private EquippedItemDTO waist;
    @JsonProperty("legs")
    private EquippedItemDTO legs;
    @JsonProperty("feet")
    private EquippedItemDTO feet;
    @JsonProperty("finger1")
    private EquippedItemDTO finger1;
    @JsonProperty("finger2")
    private EquippedItemDTO finger2;
    @JsonProperty("trinket1")
    private EquippedItemDTO trinket1;
    @JsonProperty("trinket2")
    private EquippedItemDTO trinket2;
    @JsonProperty("mainHand")
    private EquippedItemDTO mainHand;
    @JsonProperty("offHand")
    private EquippedItemDTO offHand;

    public int getAverageItemLevel() {
        return averageItemLevel;
    }

    public int getAverageItemLevelEquipped() {
        return averageItemLevelEquipped;
    }

    public EquippedItemDTO getHead() {
        return head;
    }

    public EquippedItemDTO getNeck() {
        return neck;
    }

    public EquippedItemDTO getShoulder() {
        return shoulder;
    }

    public EquippedItemDTO getBack() {
        return back;
    }

    public EquippedItemDTO getChest() {
        return chest;
    }

    public EquippedItemDTO getShirt() {
        return shirt;
    }

    public EquippedItemDTO getWrist() {
        return wrist;
    }

    public EquippedItemDTO getHands() {
        return hands;
    }

    public EquippedItemDTO getWaist() {
        return waist;
    }

    public EquippedItemDTO getLegs() {
        return legs;
    }

    public EquippedItemDTO getFeet() {
        return feet;
    }

    public EquippedItemDTO getFinger1() {
        return finger1;
    }

    public EquippedItemDTO getFinger2() {
        return finger2;
    }

    public EquippedItemDTO getTrinket1() {
        return trinket1;
    }

    public EquippedItemDTO getTrinket2() {
        return trinket2;
    }

    public EquippedItemDTO getMainHand() {
        return mainHand;
    }

    public EquippedItemDTO getOffHand() {
        return offHand;
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
