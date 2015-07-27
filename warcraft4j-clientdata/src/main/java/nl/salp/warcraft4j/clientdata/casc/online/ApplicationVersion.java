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
package nl.salp.warcraft4j.clientdata.casc.online;

import nl.salp.warcraft4j.Region;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * CDN application version information.
 *
 * @author Barre Dijkstra
 */
public class ApplicationVersion {
    /** The region. */
    private final Region region;
    /** The build config. */
    private final String buildConfig;
    /** The cdn config. */
    private final String cdnConfig;
    /** The build ID. */
    private final String buildId;
    /** The version name. */
    private final String versionName;

    /**
     * Create a new CDN ApplicationVersion.
     *
     * @param region      The region.
     * @param buildConfig The build config.
     * @param cdnConfig   The CDN config.
     * @param buildId     The build ID.
     * @param versionName The version name.
     */
    public ApplicationVersion(Region region, String buildConfig, String cdnConfig, String buildId, String versionName) {
        this.region = region;
        this.buildConfig = buildConfig;
        this.cdnConfig = cdnConfig;
        this.buildId = buildId;
        this.versionName = versionName;
    }

    /**
     * Get the region the application version is applicable for.
     *
     * @return The region.
     */
    public Region getRegion() {
        return region;
    }

    /**
     * Get the build configuration for the application in the CDN.
     *
     * @return The build configuration.
     */
    public String getBuildConfig() {
        return buildConfig;
    }

    /**
     * Get the CDN configuration for the application.
     *
     * @return The CDN configuration.
     */
    public String getCdnConfig() {
        return cdnConfig;
    }

    /**
     * Get the build ID of the application in the CDN.
     *
     * @return The build ID.
     */
    public String getBuildId() {
        return buildId;
    }

    /**
     * Get the version number of the application in the CDN.
     *
     * @return The version number.
     */
    public String getVersionName() {
        return versionName;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
