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
package nl.salp.warcraft4j.casc.cdn.local;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.cdn.BaseCdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.CdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.DataReaderProvider;
import nl.salp.warcraft4j.casc.cdn.KeyBasedConfiguration;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.FileDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * {@link CdnCascConfig} for a local CDN based CASC.
 *
 * @author Barre Dijkstra
 * @see CdnCascConfig
 */
public class LocalCdnCascConfig extends BaseCdnCascConfig implements CdnCascConfig {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalCdnCascConfig.class);
    /** The filename of the {@code build info} file. */
    private static final String FILENAME_BUILDINFO = ".build.info";
    /** The {@code build info} key for the configuration being active. */
    private static final String KEY_BUILDINFO_ACTIVE = "Active";
    /** The {@code build info} key for {@code Armadillo} (I have no clue either... ;-)). */
    private static final String KEY_BUILDINFO_ARMADILLO = "Armadillo";
    /** The {@code build info} key for the {@code build} key. */
    private static final String KEY_BUILDINFO_BUILD = "Build Key";
    /** The {@code build info} key for the development branch. */
    private static final String KEY_BUILDINFO_BRANCH = "Branch";
    /** The {@code build info} key for the {@code cdn} key. */
    private static final String KEY_BUILDINFO_CDN = "CDN Key";
    /** The {@code build info} key for the online CDN hosts. */
    private static final String KEY_BUILDINFO_CDN_HOSTS = "CDN Hosts";
    /** The {@code build info} key for the online CDN relative path. */
    private static final String KEY_BUILDINFO_CDN_PATH = "CDN Path";
    /** The {@code build info} key for the IM size (???). */
    private static final String KEY_BUILDINFO_IM_SIZE = "IM Size";
    /** The {@code build info} key for the {@code install} key. */
    private static final String KEY_BUILDINFO_INSTALL = "Install Key";
    /** The {@code build info} key for the data when the configuration was last activated. */
    private static final String KEY_BUILDINFO_LAST_ACTIVATION = "Last Activated";
    /** The {@code build info} key for the region. */
    private static final String KEY_BUILDINFO_REGION = "Branch";
    /** The {@code build info} key for the configuration tags. */
    private static final String KEY_BUILDINFO_TAGS = "Tags";
    /** The {@code build info} key for the version. */
    private static final String KEY_BUILDINFO_VERSION = "Version";

    /** The parsed {@code build info} configuration. */
    private KeyBasedConfiguration buildInfo;

    /**
     * Create a new instance.
     *
     * @param warcraft4jConfig   The {@link Warcraft4jConfig} instance to configure the CDN CASC configuration with.
     * @param dataReaderProvider The {@link DataReaderProvider} for reading the configuration files.
     */
    public LocalCdnCascConfig(Warcraft4jConfig warcraft4jConfig, DataReaderProvider dataReaderProvider) {
        super(warcraft4jConfig, dataReaderProvider);
    }

    /**
     * Get the parsed {@code build info} configuration.
     *
     * @return The parsed {@code build info} configuration.
     */
    private KeyBasedConfiguration getBuildInfo() {
        if (buildInfo == null) {
            String path = getWarcraft4jConfig().getWowInstallationDirectory().resolve(FILENAME_BUILDINFO).toString();
            LOGGER.debug("Initialising build info from file {}", path);
            buildInfo = KeyBasedConfiguration.tableConfig(getDataReader(path));
        }
        return buildInfo;
    }

    /**
     * Get a value from the parsed {@code build info} configuration for the configuration region.
     *
     * @param key The key to get the value for.
     *
     * @return Optional containing the value if the key and region combination is available.
     */
    private Optional<String> getBuildInfoValue(String key) {
        return getIndexedValue(getBuildInfo(), key, KEY_BUILDINFO_REGION, getRegionCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getBuildConfigKey() {
        return getBuildInfoValue(KEY_BUILDINFO_BUILD);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCdnConfigKey() {
        return getBuildInfoValue(KEY_BUILDINFO_CDN);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Supplier<DataReader> getConfigDataReader(String checksum) {
        Path path = getWarcraft4jConfig().getWowInstallationDirectory().resolve(Paths.get("Data", "config", checksum.substring(0, 2), checksum.substring(2, 4), checksum));
        return () -> new FileDataReader(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableRegions() {
        return getBuildInfo().getValues(KEY_BUILDINFO_REGION)
                .orElse(Collections.emptyList());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCdnUrl() {
        String host = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN_HOSTS)
                .map(s -> s.split(" ")[0])
                .orElseThrow(() -> new CascParsingException("No CDN host(s) available."));
        String path = getBuildInfo().getFirstValue(KEY_BUILDINFO_CDN_PATH)
                .orElseThrow(() -> new CascParsingException("No CDN path available."));
        return String.format("http://%s%s", host, path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return getBuildInfo().getFirstValue(KEY_BUILDINFO_VERSION)
                .orElseThrow(() -> new CascParsingException("No version available."));
    }
}