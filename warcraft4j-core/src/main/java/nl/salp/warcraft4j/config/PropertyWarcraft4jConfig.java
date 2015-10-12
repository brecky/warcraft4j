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
import org.apache.commons.configuration.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

/**
 * {@link Warcraft4jConfig} implementation for property based configuration.
 *
 * @author Barre Dijkstra
 * @see Warcraft4jConfig
 */
public class PropertyWarcraft4jConfig implements Warcraft4jConfig {
    /** The property key for the value of {@link Warcraft4jConfig#isOnline()}. */
    public static final String ONLINE_KEY = "w4j.online";
    /** The property key for the value of {@link Warcraft4jConfig#isOnline()}. */
    public static final boolean ONLINE_DEFAULT = false;
    /** The property key for the value of {@link Warcraft4jConfig#getWowInstallationDirectory()}. */
    public static final String WOW_DIR_KEY = "w4j.wow.directory";
    /** The dfeault value for {@link Warcraft4jConfig#getWowInstallationDirectory()}} ({@code C:\Program Files (x86)\World of Warcraft}). */
    public static final String WOW_DIR_DEFAULT = "C:\\Program Files (x86)\\World of Warcraft";
    /** The property key for the value of {@link Warcraft4jConfig#isCaching()}. */
    public static final String CACHE_KEY = "w4j.data.cache";
    /** The dfeault value for {@link Warcraft4jConfig#isCaching()} ({@code true}). */
    public static final boolean CACHE_DEFAULT = true;
    /** The property key for the value of {@link Warcraft4jConfig#getCacheDirectory()}. */
    public static final String CACHE_DIR_KEY = "w4j.data.cache.directory";
    /** The default value for {@link Warcraft4jConfig#getCacheDirectory()} ({@code ${USER.HOME}\w4j\data\cache}). */
    public static final String CACHE_DIR_DEFAULT = System.getProperty("user.home") + "\\w4j\\data\\cache";
    /** The property key for the value of {@link Warcraft4jConfig#getLocale()}. */
    public static final String LOCALE_KEY = "w4j.data.locale";
    /** The property key for the value of {@link Warcraft4jConfig#getRegion()}. */
    public static final String REGION_KEY = "w4j.data.region";
    /** The property key for the value of {@link Warcraft4jConfig#getBranch()}. */
    public static final String BRANCH_KEY = "w4j.data.branch";

    /** Flag indicating if Warcraft4J is allowed to operate online. */
    private boolean online;
    /** The local World of Warcraft installation directory. */
    private Path wowDir;
    /** Flag indicating if Warcraft4J is allowed to cache data locally. */
    private boolean cache;
    /** The path to the directory to cache data if allowed. */
    private Path cacheDir;
    /** The preferred locale for World of Warcraft data. */
    private Locale locale;
    /** The preferred region for World of Warcraft data. */
    private Region region;
    /** The preferred branch for World of Warcraft data. */
    private Branch branch;

    /**
     * Create a new instance from a configuration.
     *
     * @param configuration The configuration
     *
     * @throws Warcraft4jConfigException When the configuration is invalid.
     */
    public PropertyWarcraft4jConfig(Configuration configuration) throws Warcraft4jConfigException {
        initialise(configuration);
    }

    /**
     * Initialise the instance from a configuration.
     *
     * @param configuration The configuration.
     *
     * @throws Warcraft4jConfigException When the configuration is invalid.
     */

    private void initialise(Configuration configuration) throws Warcraft4jConfigException {
        if (configuration == null || configuration.isEmpty()) {
            throw new Warcraft4jConfigException("Can't create a Warcraft4J configuration from an empty configuration.");
        }
        online = configuration.getBoolean(ONLINE_KEY, ONLINE_DEFAULT);

        wowDir = Paths.get(configuration.getString(WOW_DIR_KEY, WOW_DIR_DEFAULT));
        if (Files.notExists(wowDir) || !Files.isDirectory(wowDir) || !Files.isReadable(wowDir)) {
            throw new Warcraft4jConfigException(format("WoW installation directory %s does not exist or can't be read.", wowDir));
        }

        cache = configuration.getBoolean(CACHE_KEY, CACHE_DEFAULT);
        if (cache) {
            cacheDir = Paths.get(configuration.getString(CACHE_DIR_KEY, CACHE_DIR_DEFAULT));
            if (Files.notExists(cacheDir)) {
                try {
                    cacheDir = Files.createDirectories(cacheDir);
                } catch (IOException e) {
                    throw new Warcraft4jConfigException(format("Unable to create cache directory %s.", cacheDir), e);
                }
            } else if (!Files.isDirectory(cacheDir)) {
                throw new Warcraft4jConfigException(format("Cache directory %s is not a directory.", cacheDir));
            } else if (!Files.isReadable(cacheDir) || !Files.isWritable(cacheDir)) {
                throw new Warcraft4jConfigException(format("Cache directory %s is not accessible.", cacheDir));
            }
        }
        locale = Locale.getLocale(configuration.getString(LOCALE_KEY))
                .orElseThrow(() -> new Warcraft4jConfigException(format("Locale %s is not a valid locale.", configuration.getString(LOCALE_KEY))));
        region = Region.getRegion(configuration.getString(REGION_KEY))
                .orElseThrow(() -> new Warcraft4jConfigException(format("Region %s is not a valid region.", configuration.getString(REGION_KEY))));
        branch = Branch.getBranch(configuration.getString(BRANCH_KEY))
                .orElseThrow(() -> new Warcraft4jConfigException(format("Branch %s is not a valid branch.", configuration.getString(BRANCH_KEY))));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOnline() {
        return online;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getWowInstallationDirectory() {
        return wowDir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCaching() {
        return cache;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Path getCacheDirectory() {
        return cacheDir;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Locale getLocale() {
        return locale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Region getRegion() {
        return region;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Branch getBranch() {
        return branch;
    }
}
