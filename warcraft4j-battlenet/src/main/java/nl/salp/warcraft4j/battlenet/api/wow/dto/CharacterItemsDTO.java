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
public class CharacterItemsDTO {
    @JsonProperty("averageItemLevel")
    private int averageItemLevel;
    @JsonProperty("averageItemLevelEquipped")
    private int averageItemLevelEquipped;
    @JsonProperty("head")
    private ItemDTO head;
    @JsonProperty("neck")
    private ItemDTO neck;
    @JsonProperty("shoulder")
    private ItemDTO shoulder;
    @JsonProperty("back")
    private ItemDTO back;
    @JsonProperty("chest")
    private ItemDTO chest;
    @JsonProperty("shirt")
    private ItemDTO shirt;
    @JsonProperty("wrist")
    private ItemDTO wrist;
    @JsonProperty("hands")
    private ItemDTO hands;
    @JsonProperty("waist")
    private ItemDTO waist;
    @JsonProperty("legs")
    private ItemDTO legs;
    @JsonProperty("feet")
    private ItemDTO feet;
    @JsonProperty("finger1")
    private ItemDTO finger1;
    @JsonProperty("finger2")
    private ItemDTO finger2;
    @JsonProperty("trinket1")
    private ItemDTO trinket1;
    @JsonProperty("trinket2")
    private ItemDTO trinket2;
    @JsonProperty("mainHand")
    private ItemDTO mainHand;
    @JsonProperty("offHand")
    private ItemDTO offHand;
    @JsonProperty("tabard")
    private ItemDTO tabard;

    public int getAverageItemLevel() {
        return averageItemLevel;
    }

    public void setAverageItemLevel(int averageItemLevel) {
        this.averageItemLevel = averageItemLevel;
    }

    public int getAverageItemLevelEquipped() {
        return averageItemLevelEquipped;
    }

    public void setAverageItemLevelEquipped(int averageItemLevelEquipped) {
        this.averageItemLevelEquipped = averageItemLevelEquipped;
    }

    public ItemDTO getHead() {
        return head;
    }

    public void setHead(ItemDTO head) {
        this.head = head;
    }

    public ItemDTO getNeck() {
        return neck;
    }

    public void setNeck(ItemDTO neck) {
        this.neck = neck;
    }

    public ItemDTO getShoulder() {
        return shoulder;
    }

    public void setShoulder(ItemDTO shoulder) {
        this.shoulder = shoulder;
    }

    public ItemDTO getBack() {
        return back;
    }

    public void setBack(ItemDTO back) {
        this.back = back;
    }

    public ItemDTO getChest() {
        return chest;
    }

    public void setChest(ItemDTO chest) {
        this.chest = chest;
    }

    public ItemDTO getShirt() {
        return shirt;
    }

    public void setShirt(ItemDTO shirt) {
        this.shirt = shirt;
    }

    public ItemDTO getWrist() {
        return wrist;
    }

    public void setWrist(ItemDTO wrist) {
        this.wrist = wrist;
    }

    public ItemDTO getHands() {
        return hands;
    }

    public void setHands(ItemDTO hands) {
        this.hands = hands;
    }

    public ItemDTO getWaist() {
        return waist;
    }

    public void setWaist(ItemDTO waist) {
        this.waist = waist;
    }

    public ItemDTO getLegs() {
        return legs;
    }

    public void setLegs(ItemDTO legs) {
        this.legs = legs;
    }

    public ItemDTO getFeet() {
        return feet;
    }

    public void setFeet(ItemDTO feet) {
        this.feet = feet;
    }

    public ItemDTO getFinger1() {
        return finger1;
    }

    public void setFinger1(ItemDTO finger1) {
        this.finger1 = finger1;
    }

    public ItemDTO getFinger2() {
        return finger2;
    }

    public void setFinger2(ItemDTO finger2) {
        this.finger2 = finger2;
    }

    public ItemDTO getTrinket1() {
        return trinket1;
    }

    public void setTrinket1(ItemDTO trinket1) {
        this.trinket1 = trinket1;
    }

    public ItemDTO getTrinket2() {
        return trinket2;
    }

    public void setTrinket2(ItemDTO trinket2) {
        this.trinket2 = trinket2;
    }

    public ItemDTO getMainHand() {
        return mainHand;
    }

    public void setMainHand(ItemDTO mainHand) {
        this.mainHand = mainHand;
    }

    public ItemDTO getOffHand() {
        return offHand;
    }

    public void setOffHand(ItemDTO offHand) {
        this.offHand = offHand;
    }

    public ItemDTO getTabard() {
        return tabard;
    }

    public void setTabard(ItemDTO tabard) {
        this.tabard = tabard;
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
