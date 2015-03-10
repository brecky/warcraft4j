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

package nl.salp.warcraft4j.battlenet.character;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
enum RequestDetailField {
    ACHIEVEMENTS("achievements", null),
    APPEARANCE(null, null),
    FEED(null, null),
    GUILD(null, null),
    HUNTER_PETS(null, null),
    ITEMS(null, null),
    MOUNTS(null, null),
    PETS(null, null),
    PET_SLOTS(null, null),
    PROGRESSION(null, null),
    PVP(null, null),
    QUESTS(null, null),
    REPUTATION(null, null),
    STATS(null, null),
    TALENTS(null, null),
    TITLES(null, null),
    AUDIT(null, null);

    private RequestDetailField(String fieldName, Class<?> modelClass) {

    }
}
