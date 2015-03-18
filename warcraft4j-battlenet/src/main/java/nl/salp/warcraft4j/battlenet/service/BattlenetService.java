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

package nl.salp.warcraft4j.battlenet.service;

import com.google.inject.Inject;
import nl.salp.warcraft4j.battlenet.BattlenetApiConfig;

/**
 * Base class for Battle.NET service implementations.
 *
 * @author Barre Dijkstra
 */
abstract class BattlenetService {
    /** The Battle.NET API configuration. */
    @Inject
    private BattlenetApiConfig battlenetConfig;

    /**
     * Create a new BattlenetService, relying on Guice injection for the {@link nl.salp.warcraft4j.battlenet.BattlenetApiConfig configuration}.
     */
    protected BattlenetService() {
    }

    /**
     * Create a new BattlenetService instance with the provided {@link nl.salp.warcraft4j.battlenet.BattlenetApiConfig configuration}.
     *
     * @param battlenetConfig The configuration.
     *
     * @throws IllegalArgumentException When a {@code null} configuration was provided.
     */
    protected BattlenetService(BattlenetApiConfig battlenetConfig) throws IllegalArgumentException {
        if (battlenetConfig == null) {
            throw new IllegalArgumentException("Unable to create a Battle.NET service without a configuration.");
        }
        this.battlenetConfig = battlenetConfig;
    }

    /**
     * Get the {@link nl.salp.warcraft4j.battlenet.BattlenetApiConfig Battle.NET API configuration}.
     *
     * @return The configuration.
     */
    protected final BattlenetApiConfig getConfiguration() {
        return battlenetConfig;
    }
}
