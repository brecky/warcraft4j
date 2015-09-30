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

package nl.salp.warcraft4j.battlenet;

import nl.salp.warcraft4j.Region;

/**
 * Battle.NET API region.
 * <p/>
 * Note: there is no API region for China.
 *
 * @author Barre Dijkstra
 */
public enum BattlenetRegion {
    /** Americas. */
    AMERICAS(Region.AMERICAS, "us", "us"),
    /** Europe. */
    EUROPE(Region.EUROPE, "eu", "eu"),
    /** Korea. */
    KOREA(Region.KOREA, "kr", "kr"),
    /** Taiwan. */
    TAIWAN(Region.TAIWAN, "tw", "tw"),
    /** South-East asia. */
    SEA(Region.SEA_AUSTRALASIA, "us", "us");

    /** The Battle.NET API URI prefix. */
    private final String apiUri;
    /** The region. */
    private final Region region;
    /** The key for the region. */
    private final String key;

    /**
     * Create a new Battle.NET region.
     *
     * @param region The region.
     * @param apiUri The Battle.NET API URI prefix.
     * @param key    The key for the region.
     */
    private BattlenetRegion(Region region, String apiUri, String key) {
        this.region = region;
        this.apiUri = apiUri;
        this.key = key;
    }

    /**
     * Get the region.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Get the key for the region.
     *
     * @return The key.
     */
    public String getKey() {
        return key;
    }

    /**
     * Get the Battle.NET API URI prefix.
     *
     * @return The Battle.NET API URI prefix.
     */
    public String getApiUri() {
        return apiUri;
    }

    /**
     * Get the Battle.NET region for the given key.
     *
     * @param key The key.
     *
     * @return The region.
     */
    public static BattlenetRegion getRegionForKey(String key) {
        BattlenetRegion region = null;
        for (BattlenetRegion r : BattlenetRegion.values()) {
            if (r.getKey().equalsIgnoreCase(key)) {
                region = r;
                break;
            }
        }
        return region;
    }

    /**
     * Get the Battle.NET region for the given region.
     *
     * @param region The region.
     *
     * @return The region.
     */
    public static BattlenetRegion getRegionForKey(Region region) {
        BattlenetRegion bnetRegion = null;
        for (BattlenetRegion r : BattlenetRegion.values()) {
            if (r.getRegion() == region) {
                bnetRegion = r;
                break;
            }
        }
        return bnetRegion;
    }

}
