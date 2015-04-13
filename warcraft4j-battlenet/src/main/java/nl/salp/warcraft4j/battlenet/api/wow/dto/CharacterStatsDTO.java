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
public class CharacterStatsDTO {
    @JsonProperty("health")
    private int health;
    @JsonProperty("powerType")
    private String powerType;
    @JsonProperty("power")
    private int power;
    @JsonProperty("str")
    private int strength;
    @JsonProperty("agi")
    private int agility;
    @JsonProperty("int")
    private int intellect;
    @JsonProperty("sta")
    private int stamina;
    @JsonProperty("speedRating")
    private double speedRating;
    @JsonProperty("speedRatingBonus")
    private double speedRatingBonus;
    @JsonProperty("crit")
    private double crit;
    @JsonProperty("critRating")
    private int critRating;
    @JsonProperty("haste")
    private double haste;
    @JsonProperty("hasteRating")
    private int hasteRating;
    @JsonProperty("hasteRatingPercent")
    private double hasteRatingPercent;
    @JsonProperty("mastery")
    private double mastery;
    @JsonProperty("masteryRating")
    private int masteryRating;
    @JsonProperty("spr")
    private int spirit;
    @JsonProperty("bonusArmor")
    private int bonusArmor;
    @JsonProperty("multistrike")
    private double multistrike;
    @JsonProperty("multistrikeRating")
    private double multistrikeRating;
    @JsonProperty("multistrikeRatingBonus")
    private double multistrikeRatingBonus;
    @JsonProperty("leech")
    private double leech;
    @JsonProperty("leechRating")
    private double leechRating;
    @JsonProperty("leechRatingBonus")
    private double leechRatingBonus;
    @JsonProperty("versatility")
    private double versatility;
    @JsonProperty("versatilityDamageDoneBonus")
    private double versatilityDamageDoneBonus;
    @JsonProperty("versatilityHealingDoneBonus")
    private double versatilityHealingDoneBonus;
    @JsonProperty("versatilityDamageTakenBonus")
    private double versatilityDamageTakenBonus;
    @JsonProperty("avoidanceRating")
    private double avoidanceRating;
    @JsonProperty("avoidanceRatingBonus")
    private double avoidanceRatingBonus;
    @JsonProperty("spellPower")
    private int spellPower;
    @JsonProperty("spellPen")
    private int spellPen;
    @JsonProperty("spellCrit")
    private double spellCrit;
    @JsonProperty("spellCritRating")
    private int spellCritRating;
    @JsonProperty("mana5")
    private double mana5;
    @JsonProperty("mana5Combat")
    private double mana5Combat;
    @JsonProperty("armor")
    private int armor;
    @JsonProperty("dodge")
    private double dodge;
    @JsonProperty("dodgeRating")
    private int dodgeRating;
    @JsonProperty("parry")
    private double parry;
    @JsonProperty("parryRating")
    private int parryRating;
    @JsonProperty("block")
    private double block;
    @JsonProperty("blockRating")
    private int blockRating;
    @JsonProperty("mainHandDmgMin")
    private double mainHandDmgMin;
    @JsonProperty("mainHandDmgMax")
    private double mainHandDmgMax;
    @JsonProperty("mainHandSpeed")
    private double mainHandSpeed;
    @JsonProperty("mainHandDps")
    private double mainHandDps;
    @JsonProperty("offHandDmgMin")
    private double offHandDmgMin;
    @JsonProperty("offHandDmgMax")
    private double offHandDmgMax;
    @JsonProperty("offHandSpeed")
    private double offHandSpeed;
    @JsonProperty("offHandDps")
    private double offHandDps;
    @JsonProperty("rangedDmgMin")
    private double rangedDmgMin;
    @JsonProperty("rangedDmgMax")
    private double rangedDmgMax;
    @JsonProperty("rangedSpeed")
    private double rangedSpeed;
    @JsonProperty("rangedDps")
    private double rangedDps;
    @JsonProperty("attackPower")
    private int attackPower;
    @JsonProperty("rangedAttackPower")
    private int rangedAttackPower;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public String getPowerType() {
        return powerType;
    }

    public void setPowerType(String powerType) {
        this.powerType = powerType;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getIntellect() {
        return intellect;
    }

    public void setIntellect(int intellect) {
        this.intellect = intellect;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getSpirit() {
        return spirit;
    }

    public void setSpirit(int spirit) {
        this.spirit = spirit;
    }

    public double getSpeedRating() {
        return speedRating;
    }

    public void setSpeedRating(double speedRating) {
        this.speedRating = speedRating;
    }

    public double getSpeedRatingBonus() {
        return speedRatingBonus;
    }

    public void setSpeedRatingBonus(double speedRatingBonus) {
        this.speedRatingBonus = speedRatingBonus;
    }

    public double getCrit() {
        return crit;
    }

    public void setCrit(double crit) {
        this.crit = crit;
    }

    public int getCritRating() {
        return critRating;
    }

    public void setCritRating(int critRating) {
        this.critRating = critRating;
    }

    public double getHaste() {
        return haste;
    }

    public void setHaste(double haste) {
        this.haste = haste;
    }

    public int getHasteRating() {
        return hasteRating;
    }

    public void setHasteRating(int hasteRating) {
        this.hasteRating = hasteRating;
    }

    public double getHasteRatingPercent() {
        return hasteRatingPercent;
    }

    public void setHasteRatingPercent(double hasteRatingPercent) {
        this.hasteRatingPercent = hasteRatingPercent;
    }

    public double getMastery() {
        return mastery;
    }

    public void setMastery(double mastery) {
        this.mastery = mastery;
    }

    public int getMasteryRating() {
        return masteryRating;
    }

    public void setMasteryRating(int masteryRating) {
        this.masteryRating = masteryRating;
    }

    public int getBonusArmor() {
        return bonusArmor;
    }

    public void setBonusArmor(int bonusArmor) {
        this.bonusArmor = bonusArmor;
    }

    public double getMultistrike() {
        return multistrike;
    }

    public void setMultistrike(double multistrike) {
        this.multistrike = multistrike;
    }

    public double getMultistrikeRating() {
        return multistrikeRating;
    }

    public void setMultistrikeRating(double multistrikeRating) {
        this.multistrikeRating = multistrikeRating;
    }

    public double getMultistrikeRatingBonus() {
        return multistrikeRatingBonus;
    }

    public void setMultistrikeRatingBonus(double multistrikeRatingBonus) {
        this.multistrikeRatingBonus = multistrikeRatingBonus;
    }

    public double getLeech() {
        return leech;
    }

    public void setLeech(double leech) {
        this.leech = leech;
    }

    public double getLeechRating() {
        return leechRating;
    }

    public void setLeechRating(double leechRating) {
        this.leechRating = leechRating;
    }

    public double getLeechRatingBonus() {
        return leechRatingBonus;
    }

    public void setLeechRatingBonus(double leechRatingBonus) {
        this.leechRatingBonus = leechRatingBonus;
    }

    public double getVersatility() {
        return versatility;
    }

    public void setVersatility(double versatility) {
        this.versatility = versatility;
    }

    public double getVersatilityDamageDoneBonus() {
        return versatilityDamageDoneBonus;
    }

    public void setVersatilityDamageDoneBonus(double versatilityDamageDoneBonus) {
        this.versatilityDamageDoneBonus = versatilityDamageDoneBonus;
    }

    public double getVersatilityHealingDoneBonus() {
        return versatilityHealingDoneBonus;
    }

    public void setVersatilityHealingDoneBonus(double versatilityHealingDoneBonus) {
        this.versatilityHealingDoneBonus = versatilityHealingDoneBonus;
    }

    public double getVersatilityDamageTakenBonus() {
        return versatilityDamageTakenBonus;
    }

    public void setVersatilityDamageTakenBonus(double versatilityDamageTakenBonus) {
        this.versatilityDamageTakenBonus = versatilityDamageTakenBonus;
    }

    public double getAvoidanceRating() {
        return avoidanceRating;
    }

    public void setAvoidanceRating(double avoidanceRating) {
        this.avoidanceRating = avoidanceRating;
    }

    public double getAvoidanceRatingBonus() {
        return avoidanceRatingBonus;
    }

    public void setAvoidanceRatingBonus(double avoidanceRatingBonus) {
        this.avoidanceRatingBonus = avoidanceRatingBonus;
    }

    public int getSpellPower() {
        return spellPower;
    }

    public void setSpellPower(int spellPower) {
        this.spellPower = spellPower;
    }

    public int getSpellPen() {
        return spellPen;
    }

    public void setSpellPen(int spellPen) {
        this.spellPen = spellPen;
    }

    public double getSpellCrit() {
        return spellCrit;
    }

    public void setSpellCrit(double spellCrit) {
        this.spellCrit = spellCrit;
    }

    public int getSpellCritRating() {
        return spellCritRating;
    }

    public void setSpellCritRating(int spellCritRating) {
        this.spellCritRating = spellCritRating;
    }

    public double getMana5() {
        return mana5;
    }

    public void setMana5(double mana5) {
        this.mana5 = mana5;
    }

    public double getMana5Combat() {
        return mana5Combat;
    }

    public void setMana5Combat(double mana5Combat) {
        this.mana5Combat = mana5Combat;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public double getDodge() {
        return dodge;
    }

    public void setDodge(double dodge) {
        this.dodge = dodge;
    }

    public int getDodgeRating() {
        return dodgeRating;
    }

    public void setDodgeRating(int dodgeRating) {
        this.dodgeRating = dodgeRating;
    }

    public double getParry() {
        return parry;
    }

    public void setParry(double parry) {
        this.parry = parry;
    }

    public int getParryRating() {
        return parryRating;
    }

    public void setParryRating(int parryRating) {
        this.parryRating = parryRating;
    }

    public double getBlock() {
        return block;
    }

    public void setBlock(double block) {
        this.block = block;
    }

    public int getBlockRating() {
        return blockRating;
    }

    public void setBlockRating(int blockRating) {
        this.blockRating = blockRating;
    }

    public double getMainHandDmgMin() {
        return mainHandDmgMin;
    }

    public void setMainHandDmgMin(double mainHandDmgMin) {
        this.mainHandDmgMin = mainHandDmgMin;
    }

    public double getMainHandDmgMax() {
        return mainHandDmgMax;
    }

    public void setMainHandDmgMax(double mainHandDmgMax) {
        this.mainHandDmgMax = mainHandDmgMax;
    }

    public double getMainHandSpeed() {
        return mainHandSpeed;
    }

    public void setMainHandSpeed(double mainHandSpeed) {
        this.mainHandSpeed = mainHandSpeed;
    }

    public double getMainHandDps() {
        return mainHandDps;
    }

    public void setMainHandDps(double mainHandDps) {
        this.mainHandDps = mainHandDps;
    }

    public double getOffHandDmgMin() {
        return offHandDmgMin;
    }

    public void setOffHandDmgMin(double offHandDmgMin) {
        this.offHandDmgMin = offHandDmgMin;
    }

    public double getOffHandDmgMax() {
        return offHandDmgMax;
    }

    public void setOffHandDmgMax(double offHandDmgMax) {
        this.offHandDmgMax = offHandDmgMax;
    }

    public double getOffHandSpeed() {
        return offHandSpeed;
    }

    public void setOffHandSpeed(double offHandSpeed) {
        this.offHandSpeed = offHandSpeed;
    }

    public double getOffHandDps() {
        return offHandDps;
    }

    public void setOffHandDps(double offHandDps) {
        this.offHandDps = offHandDps;
    }

    public double getRangedDmgMin() {
        return rangedDmgMin;
    }

    public void setRangedDmgMin(double rangedDmgMin) {
        this.rangedDmgMin = rangedDmgMin;
    }

    public double getRangedDmgMax() {
        return rangedDmgMax;
    }

    public void setRangedDmgMax(double rangedDmgMax) {
        this.rangedDmgMax = rangedDmgMax;
    }

    public double getRangedSpeed() {
        return rangedSpeed;
    }

    public void setRangedSpeed(double rangedSpeed) {
        this.rangedSpeed = rangedSpeed;
    }

    public double getRangedDps() {
        return rangedDps;
    }

    public void setRangedDps(double rangedDps) {
        this.rangedDps = rangedDps;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getRangedAttackPower() {
        return rangedAttackPower;
    }

    public void setRangedAttackPower(int rangedAttackPower) {
        this.rangedAttackPower = rangedAttackPower;
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
