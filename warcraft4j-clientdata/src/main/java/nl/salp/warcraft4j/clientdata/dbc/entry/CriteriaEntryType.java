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

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public enum CriteriaEntryType {
    ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE(0),
    ACHIEVEMENT_CRITERIA_TYPE_WIN_BG(1),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_ARCHAEOLOGY_PROJECTS(3), // struct { uint32 itemCount; }
    ACHIEVEMENT_CRITERIA_TYPE_REACH_LEVEL(5),
    ACHIEVEMENT_CRITERIA_TYPE_REACH_SKILL_LEVEL(7),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_ACHIEVEMENT(8),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUEST_COUNT(9),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_DAILY_QUEST_DAILY(10), // you have to complete a daily quest x times in a row
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUESTS_IN_ZONE(11),
    ACHIEVEMENT_CRITERIA_TYPE_CURRENCY(12),
    ACHIEVEMENT_CRITERIA_TYPE_DAMAGE_DONE(13),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_DAILY_QUEST(14),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_BATTLEGROUND(15),
    ACHIEVEMENT_CRITERIA_TYPE_DEATH_AT_MAP(16),
    ACHIEVEMENT_CRITERIA_TYPE_DEATH(17),
    ACHIEVEMENT_CRITERIA_TYPE_DEATH_IN_DUNGEON(18),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_RAID(19),
    ACHIEVEMENT_CRITERIA_TYPE_KILLED_BY_CREATURE(20),
    ACHIEVEMENT_CRITERIA_TYPE_KILLED_BY_PLAYER(23),
    ACHIEVEMENT_CRITERIA_TYPE_FALL_WITHOUT_DYING(24),
    ACHIEVEMENT_CRITERIA_TYPE_DEATHS_FROM(26),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUEST(27),
    ACHIEVEMENT_CRITERIA_TYPE_BE_SPELL_TARGET(28),
    ACHIEVEMENT_CRITERIA_TYPE_CAST_SPELL(29),
    ACHIEVEMENT_CRITERIA_TYPE_BG_OBJECTIVE_CAPTURE(30),
    ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILL_AT_AREA(31),
    ACHIEVEMENT_CRITERIA_TYPE_WIN_ARENA(32),
    ACHIEVEMENT_CRITERIA_TYPE_PLAY_ARENA(33),
    ACHIEVEMENT_CRITERIA_TYPE_LEARN_SPELL(34),
    ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILL(35),
    ACHIEVEMENT_CRITERIA_TYPE_OWN_ITEM(36),
    ACHIEVEMENT_CRITERIA_TYPE_WIN_RATED_ARENA(37),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_TEAM_RATING(38),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_PERSONAL_RATING(39),
    ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILL_LEVEL(40),
    ACHIEVEMENT_CRITERIA_TYPE_USE_ITEM(41),
    ACHIEVEMENT_CRITERIA_TYPE_LOOT_ITEM(42),
    ACHIEVEMENT_CRITERIA_TYPE_EXPLORE_AREA(43),
    ACHIEVEMENT_CRITERIA_TYPE_OWN_RANK(44),
    ACHIEVEMENT_CRITERIA_TYPE_BUY_BANK_SLOT(45),
    ACHIEVEMENT_CRITERIA_TYPE_GAIN_REPUTATION(46),
    ACHIEVEMENT_CRITERIA_TYPE_GAIN_EXALTED_REPUTATION(47),
    ACHIEVEMENT_CRITERIA_TYPE_VISIT_BARBER_SHOP(48),
    ACHIEVEMENT_CRITERIA_TYPE_EQUIP_EPIC_ITEM(49),
    ACHIEVEMENT_CRITERIA_TYPE_ROLL_NEED_ON_LOOT(50), /// @todo itemlevel is mentioned in text but not present in dbc
    ACHIEVEMENT_CRITERIA_TYPE_ROLL_GREED_ON_LOOT(51),
    ACHIEVEMENT_CRITERIA_TYPE_HK_CLASS(52),
    ACHIEVEMENT_CRITERIA_TYPE_HK_RACE(53),
    ACHIEVEMENT_CRITERIA_TYPE_DO_EMOTE(54),
    ACHIEVEMENT_CRITERIA_TYPE_HEALING_DONE(55),
    ACHIEVEMENT_CRITERIA_TYPE_GET_KILLING_BLOWS(56), /// @todo in some cases map not present), and in some cases need do without die
    ACHIEVEMENT_CRITERIA_TYPE_EQUIP_ITEM(57),
    ACHIEVEMENT_CRITERIA_TYPE_MONEY_FROM_VENDORS(59),
    ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_FOR_TALENTS(60),
    ACHIEVEMENT_CRITERIA_TYPE_NUMBER_OF_TALENT_RESETS(61),
    ACHIEVEMENT_CRITERIA_TYPE_MONEY_FROM_QUEST_REWARD(62),
    ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_FOR_TRAVELLING(63),
    ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_AT_BARBER(65),
    ACHIEVEMENT_CRITERIA_TYPE_GOLD_SPENT_FOR_MAIL(66),
    ACHIEVEMENT_CRITERIA_TYPE_LOOT_MONEY(67),
    ACHIEVEMENT_CRITERIA_TYPE_USE_GAMEOBJECT(68),
    ACHIEVEMENT_CRITERIA_TYPE_BE_SPELL_TARGET2(69),
    ACHIEVEMENT_CRITERIA_TYPE_SPECIAL_PVP_KILL(70),
    ACHIEVEMENT_CRITERIA_TYPE_FISH_IN_GAMEOBJECT(72),
    /// @todo 73: Achievements 1515), 1241), 1103 (Name: Mal'Ganis)
    ACHIEVEMENT_CRITERIA_TYPE_ON_LOGIN(74),
    ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILLLINE_SPELLS(75),
    ACHIEVEMENT_CRITERIA_TYPE_WIN_DUEL(76),
    ACHIEVEMENT_CRITERIA_TYPE_LOSE_DUEL(77),
    ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE_TYPE(78),
    ACHIEVEMENT_CRITERIA_TYPE_GOLD_EARNED_BY_AUCTIONS(80),
    ACHIEVEMENT_CRITERIA_TYPE_CREATE_AUCTION(82),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_AUCTION_BID(83),
    ACHIEVEMENT_CRITERIA_TYPE_WON_AUCTIONS(84),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_AUCTION_SOLD(85),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_GOLD_VALUE_OWNED(86),
    ACHIEVEMENT_CRITERIA_TYPE_GAIN_REVERED_REPUTATION(87),
    ACHIEVEMENT_CRITERIA_TYPE_GAIN_HONORED_REPUTATION(88),
    ACHIEVEMENT_CRITERIA_TYPE_KNOWN_FACTIONS(89),
    ACHIEVEMENT_CRITERIA_TYPE_LOOT_EPIC_ITEM(90),
    ACHIEVEMENT_CRITERIA_TYPE_RECEIVE_EPIC_ITEM(91),
    ACHIEVEMENT_CRITERIA_TYPE_ROLL_NEED(93),
    ACHIEVEMENT_CRITERIA_TYPE_ROLL_GREED(94),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HIT_DEALT(101),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HIT_RECEIVED(102),
    ACHIEVEMENT_CRITERIA_TYPE_TOTAL_DAMAGE_RECEIVED(103),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HEAL_CAST(104),
    ACHIEVEMENT_CRITERIA_TYPE_TOTAL_HEALING_RECEIVED(105),
    ACHIEVEMENT_CRITERIA_TYPE_HIGHEST_HEALING_RECEIVED(106),
    ACHIEVEMENT_CRITERIA_TYPE_QUEST_ABANDONED(107),
    ACHIEVEMENT_CRITERIA_TYPE_FLIGHT_PATHS_TAKEN(108),
    ACHIEVEMENT_CRITERIA_TYPE_LOOT_TYPE(109),
    ACHIEVEMENT_CRITERIA_TYPE_CAST_SPELL2(110), /// @todo target entry is missing
    ACHIEVEMENT_CRITERIA_TYPE_LEARN_SKILL_LINE(112),
    ACHIEVEMENT_CRITERIA_TYPE_EARN_HONORABLE_KILL(113),
    ACHIEVEMENT_CRITERIA_TYPE_ACCEPTED_SUMMONINGS(114),
    ACHIEVEMENT_CRITERIA_TYPE_EARN_ACHIEVEMENT_POINTS(115),
    ACHIEVEMENT_CRITERIA_TYPE_USE_LFD_TO_GROUP_WITH_PLAYERS(119),
    ACHIEVEMENT_CRITERIA_TYPE_SPENT_GOLD_GUILD_REPAIRS(124),
    ACHIEVEMENT_CRITERIA_TYPE_REACH_GUILD_LEVEL(125),
    ACHIEVEMENT_CRITERIA_TYPE_CRAFT_ITEMS_GUILD(126),
    ACHIEVEMENT_CRITERIA_TYPE_CATCH_FROM_POOL(127),
    ACHIEVEMENT_CRITERIA_TYPE_BUY_GUILD_BANK_SLOTS(128),
    ACHIEVEMENT_CRITERIA_TYPE_EARN_GUILD_ACHIEVEMENT_POINTS(129),
    ACHIEVEMENT_CRITERIA_TYPE_WIN_RATED_BATTLEGROUND(130),
    ACHIEVEMENT_CRITERIA_TYPE_REACH_BG_RATING(132),
    ACHIEVEMENT_CRITERIA_TYPE_BUY_GUILD_TABARD(133),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_QUESTS_GUILD(134),
    ACHIEVEMENT_CRITERIA_TYPE_HONORABLE_KILLS_GUILD(135),
    ACHIEVEMENT_CRITERIA_TYPE_KILL_CREATURE_TYPE_GUILD(136),
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_GUILD_CHALLENGE_TYPE(138), //struct { Flag flag; uint32 count; } 1: Guild Dungeon), 2:Guild Challenge), 3:Guild battlefield
    ACHIEVEMENT_CRITERIA_TYPE_COMPLETE_GUILD_CHALLENGE(139);  //struct { uint32 count; } Guild Challenge

    private final int id;

    private CriteriaEntryType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static CriteriaEntryType getType(int id) {
        CriteriaEntryType type = null;
        for (CriteriaEntryType t : CriteriaEntryType.values()) {
            if (t.getId() == id) {
                type = t;
                break;
            }
        }
        return type;
    }

    public static CriteriaEntryType getType(CriteriaEntry entry) {
        CriteriaEntryType type = null;
        if (entry != null) {
            for (CriteriaEntryType t : CriteriaEntryType.values()) {
                if (t.getId() == entry.getType()) {
                    type = t;
                    break;
                }
            }
        }
        return type;
    }


}