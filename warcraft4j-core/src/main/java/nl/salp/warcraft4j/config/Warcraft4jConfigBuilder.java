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
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import java.nio.file.Path;

/**
 * Builder for creating a {@link PropertyWarcraft4jConfig} instance.
 *
 * @author Barre Dijkstra
 * @see Warcraft4jConfig
 */
public class Warcraft4jConfigBuilder {
    /** The online flag. */
    private boolean online;
    /** The local World of Warcraft installation directory. */
    private String wowDir;
    /** The cache flag. */
    private boolean caching;
    /** The local cache directory. */
    private String cacheDir;
    /** The preferred locale. */
    private String locale;
    /** The preferred region. */
    private String region;
    /** The preferred branch. */
    private String branch;

    /**
     * Set the online flag.
     *
     * @param online The online flag.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#isOnline()
     */
    public Warcraft4jConfigBuilder online(boolean online) {
        this.online = online;
        return this;
    }

    /**
     * Set the local World of Warcraft installation directory.
     *
     * @param wowDir The installation directory.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getWowInstallationDirectory()
     */
    public Warcraft4jConfigBuilder withWowDir(String wowDir) {
        this.wowDir = wowDir;
        return this;
    }

    /**
     * Set the local World of Warcraft installation directory.
     *
     * @param wowDir The installation directory.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getWowInstallationDirectory()
     */
    public Warcraft4jConfigBuilder withWowDir(Path wowDir) {
        this.wowDir = wowDir == null ? null : wowDir.toString();
        return this;
    }

    /**
     * Set the caching flag.
     *
     * @param caching The caching flag.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#isCaching()
     */
    public Warcraft4jConfigBuilder caching(boolean caching) {
        this.caching = caching;
        return this;
    }

    /**
     * Set the local cache directory.
     *
     * @param cacheDir The cache directory.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getCacheDirectory()
     */
    public Warcraft4jConfigBuilder withCacheDir(String cacheDir) {
        this.cacheDir = cacheDir;
        return this;
    }

    /**
     * Set the local cache directory.
     *
     * @param cacheDir The cache directory.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getCacheDirectory()
     */
    public Warcraft4jConfigBuilder withCacheDir(Path cacheDir) {
        this.cacheDir = cacheDir == null ? null : cacheDir.toString();
        return this;
    }


    /**
     * Set the preferred locale.
     *
     * @param locale The locale.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getLocale()
     */
    public Warcraft4jConfigBuilder withLocale(String locale) {
        this.locale = locale;
        return this;
    }

    /**
     * Set the preferred locale.
     *
     * @param locale The locale.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getLocale()
     */
    public Warcraft4jConfigBuilder withLocale(Locale locale) {
        this.locale = locale != null ? locale.name() : null;
        return this;
    }

    /**
     * Set the preferred region.
     *
     * @param region The region.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getRegion()
     */
    public Warcraft4jConfigBuilder withRegion(String region) {
        this.region = region;
        return this;
    }

    /**
     * Set the preferred region.
     *
     * @param region The region.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getRegion()
     */
    public Warcraft4jConfigBuilder withRegion(Region region) {
        this.region = region != null ? region.name() : null;
        return this;
    }

    /**
     * Set the preferred development branch.
     *
     * @param branch The branch.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getBranch()
     */
    public Warcraft4jConfigBuilder withBranch(String branch) {
        this.branch = branch;
        return this;
    }

    /**
     * Set the preferred development branch.
     *
     * @param branch The branch.
     *
     * @return The builder instance.
     *
     * @see PropertyWarcraft4jConfig#getBranch()
     */
    public Warcraft4jConfigBuilder withBranch(Branch branch) {
        this.branch = branch != null ? branch.name() : null;
        return this;
    }

    /**
     * Build a PropertyWarcraft4jConfig instance from the builder values.
     *
     * @return The PropertyWarcraft4jConfig instance.
     *
     * @throws Warcraft4jConfigException When the configuration values were invalid.
     */
    public PropertyWarcraft4jConfig build() throws Warcraft4jConfigException {
        Configuration config = new BaseConfiguration();
        config.addProperty(PropertyWarcraft4jConfig.ONLINE_KEY, online);
        config.addProperty(PropertyWarcraft4jConfig.WOW_DIR_KEY, wowDir);
        config.addProperty(PropertyWarcraft4jConfig.CACHE_KEY, caching);
        config.addProperty(PropertyWarcraft4jConfig.CACHE_DIR_KEY, cacheDir);
        config.addProperty(PropertyWarcraft4jConfig.LOCALE_KEY, locale);
        config.addProperty(PropertyWarcraft4jConfig.REGION_KEY, region);
        config.addProperty(PropertyWarcraft4jConfig.BRANCH_KEY, branch);
        return new PropertyWarcraft4jConfig(config);
    }
}
