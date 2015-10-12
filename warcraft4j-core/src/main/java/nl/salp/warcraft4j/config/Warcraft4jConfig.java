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
package nl.salp.warcraft4j.config;

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;

import java.nio.file.Path;

/**
 * General Warcraft4J configuration.
 *
 * @author Barre Dijkstra
 */
public interface Warcraft4jConfig {
    /**
     * Get the path to the local World of Warcraft installation directory.
     *
     * @return The path to the World of Warcraft installation, may be {@code null} if Warcraft4J operates online.
     */
    Path getWowInstallationDirectory();

    /**
     * Check if Warcraft4J can operate online.
     *
     * @return {@code true} if Warcraft4J can operate online.
     */
    boolean isOnline();

    /**
     * Check if Warcraft4J should cache online files.
     *
     * @return {@code true} if online files should be cached.
     */
    boolean isCaching();

    /**
     * Get the path to the directory to cache online files in.
     *
     * @return The path to the cache directory, may be {@code null} if caching is disabled.
     */
    Path getCacheDirectory();

    /**
     * Get the preferred locale for data.
     *
     * @return The preferred locale.
     */
    Locale getLocale();

    /**
     * Get the preferred region for data.
     *
     * @return The preferred region.
     */
    Region getRegion();

    /**
     * Get the preferred development branch for data.
     *
     * @return The preferred development branch.
     */
    Branch getBranch();
}
