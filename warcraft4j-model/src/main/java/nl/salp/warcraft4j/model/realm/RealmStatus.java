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
import nl.salp.warcraft4j.model.data.Realm;
import nl.salp.warcraft4j.model.data.RealmPopulation;

import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The status of a realm.
 *
 * @author Barre Dijkstra
 */
public class RealmStatus implements Serializable {
    /** The timestamp of the realm status. */
    private final long timestamp;
    /** The realm. */
    private final Realm realm;
    /** Flag indicating if the realm is active. */
    private final boolean realmActive;
    /** The realm population. */
    private final RealmPopulation population;
    /** Flag indicating if there is a queue for the realm. */
    private final boolean queue;
    /** The status of the PvP contested areas on the realm. */
    private final Map<Area, PvpAreaStatus> pvpAreas;

    /**
     * Create a new realm status.
     *
     * @param timestamp       The timestamp of the status.
     * @param realm           The realm.
     * @param realmActive     Flag indicating if the realm is active ({@code true}).
     * @param population      The population of the realm.
     * @param queue           Flag indicating if there is a queue for the realm ({@code true}).
     * @param pvpAreaStatuses The statuses of the PvP contested areas.
     */
    public RealmStatus(long timestamp, Realm realm, boolean realmActive, RealmPopulation population, boolean queue, PvpAreaStatus[] pvpAreaStatuses) {
        this.timestamp = timestamp;
        this.realm = realm;
        this.realmActive = realmActive;
        this.population = population;
        this.queue = queue;
        this.pvpAreas = new HashMap<>();
        if (pvpAreaStatuses != null) {
            for (PvpAreaStatus pas : pvpAreaStatuses) {
                this.pvpAreas.put(pas.getArea(), pas);
            }
        }
    }

    /**
     * Get the timestamp of the realm status.
     *
     * @return The timestamp (realm time in epoch).
     */
    public long getTimestamp() {
        return timestamp;
    }

    /**
     * Get the realm the status is for.
     *
     * @return The realm.
     */
    public Realm getRealm() {
        return realm;
    }

    /**
     * Check if the realm is active.
     *
     * @return {@code true} if the realm is active.
     */
    public boolean isRealmActive() {
        return realmActive;
    }

    /**
     * Get the population for the realm.
     *
     * @return The population.
     */
    public RealmPopulation getPopulation() {
        return population;
    }

    /**
     * Check if there is a queue for the realm.
     *
     * @return {@code true} if there is a queue.
     */
    public boolean isQueue() {
        return queue;
    }

    /**
     * Get the status of the PvP contested areas.
     *
     * @return Status of the PvP contested areas, indexed by area, as an unmodifiable map.
     */
    public Map<Area, PvpAreaStatus> getPvpAreas() {
        return Collections.unmodifiableMap(pvpAreas);
    }

    /**
     * Get the PvP contested areas where a status is available for.
     *
     * @return The areas.
     */
    public Collection<Area> getPvpContestedAreas() {
        return Collections.unmodifiableSet(pvpAreas.keySet());
    }

    /**
     * Get the status for a PvP contested area.
     *
     * @param area The area.
     *
     * @return The status for the area or {@code null} if no status is available.
     */
    public PvpAreaStatus getPvpAreaStatus(Area area) {
        PvpAreaStatus status = null;
        if (area != null) {
            status = pvpAreas.get(area);
        }
        return status;
    }
}
