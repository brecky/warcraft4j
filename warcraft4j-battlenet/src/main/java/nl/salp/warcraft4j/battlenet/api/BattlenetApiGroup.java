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

package nl.salp.warcraft4j.battlenet.api;

/**
 * Battle.NET API.
 *
 * @author Barre Dijkstra
 */
public enum BattlenetApiGroup {
    /** Account API. */
    ACCOUNT("account"),
    /** Diablo 3 API. */
    DIABLO_3("d3"),
    /** Starcraft 2 API. */
    STARCRAFT_2("sc2"),
    /** World of Warcraft API. */
    WORLD_OF_WARCRAFT("wow");

    /** The URI of the API. */
    private final String apiUri;

    /**
     * Create a new BattlenetApi instance.
     *
     * @param apiUri The URI of the API.
     */
    private BattlenetApiGroup(String apiUri) {
        this.apiUri = apiUri;
    }

    /**
     * Get the URI of the API.
     *
     * @return The URI of the API.
     */
    public String getApiUri() {
        return apiUri;
    }
}
