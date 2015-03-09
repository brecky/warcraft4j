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

package nl.salp.warcraft4j.wowclient.cdn;

/**
 * Application available in a CDN.
 *
 * @author Barre Dijkstra
 */
public enum Application {
    /** World of Warcraft (live). */
    WORLD_OF_WARCRAFT_LIVE("wow"),
    /** World of Warcraft (PTR). */
    WORLD_OF_WARCRAFT_PTR("wowt"),
    /** World of Warcraft (beta). */
    WORLD_OF_WARCRAFT_BETA("wow_beta"),
    /** Heroes of the Storm (live, though currently only available as beta). */
    HEROES_OF_THE_STORM("storm");

    /**
     * The CDN key for the application.
     */
    private final String key;

    /**
     * Create a new Application instance.
     *
     * @param key The CDN key for the application.
     */
    private Application(String key) {
        this.key = key;
    }

    /**
     * Get the CDN key for the application.
     *
     * @return The CDN key.
     */
    public String getKey() {
        return key;
    }
}
