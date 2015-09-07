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
package nl.salp.warcraft4j;

/**
 * World of Warcraft version.
 *
 * @author Barre Dijkstra
 */
public interface WowVersion {
    /**
     * Get the version.
     *
     * @return The version.
     */
    String getVersion();

    /**
     * Check if a {@link WowVersion} has the same version.
     *
     * @param version The version to check.
     *
     * @return {@link true} if both World of Warcraft versions have the same version.
     *
     * @see #getVersion()
     */
    default boolean isSameVersion(WowVersion version) {
        return (version != null) && (getVersion().equals(version.getVersion()));
    }

    /**
     * Get the branch for the version is for.
     *
     * @return The branch.
     */
    Branch getBranch();

    /**
     * Check if a {@link WowVersion} has the same branch.
     *
     * @param version The version to check.
     *
     * @return {@link true} if both World of Warcraft versions have the same branch.
     *
     * @see #getBranch()
     */
    default boolean isSameBranch(WowVersion version) {
        return (version != null) && (getBranch().equals(version.getBranch()));
    }

    /**
     * Get the region for the version is for.
     *
     * @return The region.
     */
    Region getRegion();

    /**
     * Check if a {@link WowVersion} has the same region.
     *
     * @param version The version to check.
     *
     * @return {@link true} if both World of Warcraft versions have the same region.
     *
     * @see #getRegion()
     */
    default boolean isSameRegion(WowVersion version) {
        return (version != null) && (getRegion().equals(version.getRegion()));
    }
}
