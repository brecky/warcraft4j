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

package nl.salp.warcraft4j.model.realm;

import nl.salp.warcraft4j.model.data.Area;
import nl.salp.warcraft4j.model.data.CharacterFaction;

import java.io.Serializable;

/**
 * Status of a PvP contested area.
 *
 * @author Barre Dijkstra
 */
public class PvpAreaStatus implements Serializable {
    /** The timestamp of the status. */
    private final long timestamp;
    /** The contested area. */
    private final Area area;
    /** The currently controlling faction. */
    private final CharacterFaction controllingFaction;
    /** The current status of the area battle. */
    private final PvpBattleStatus battleStatus;
    /** The realm local time of the battle in epoch. */
    private final long timeNextBattle;

    /**
     * Create a new PvP area status.
     *
     * @param timestamp          The timestamp of the status.
     * @param area               The contested area.
     * @param controllingFaction The currently controlling faction.
     * @param battleStatus       The current status of the area battle.
     * @param timeNextBattle     The realm local time of the battle in epoch.
     */
    public PvpAreaStatus(long timestamp, Area area, CharacterFaction controllingFaction, PvpBattleStatus battleStatus, long timeNextBattle) {
        this.timestamp = timestamp;
        this.area = area;
        this.controllingFaction = controllingFaction;
        this.battleStatus = battleStatus;
        this.timeNextBattle = timeNextBattle;
    }

    /**
     * Get the area the status is for.
     *
     * @return The area.
     */
    public Area getArea() {
        return area;
    }

    /**
     * Get the faction who controls the area.
     *
     * @return The faction.
     */
    public CharacterFaction getControllingFaction() {
        return controllingFaction;
    }

    /**
     * Get the current status of the battle ground.
     *
     * @return The status.
     */
    public PvpBattleStatus getBattleStatus() {
        return battleStatus;
    }

    /**
     * Get the time of the next battle for the area.
     *
     * @return The realm time as epoch.
     */
    public long getTimeNextBattle() {
        return timeNextBattle;
    }
}
