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
package nl.salp.warcraft4j.casc.cdn.online;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.cdn.BaseCdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.CdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.DataReaderProvider;
import nl.salp.warcraft4j.casc.cdn.KeyBasedConfiguration;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.io.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * {@link CdnCascConfig} implementation for an online CDN based CASC.
 *
 * @author Barre Dijkstra
 */
public class OnlineCdnCascConfig extends BaseCdnCascConfig {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineCdnCascConfig.class);
    /** The {@code versions} config key for the region. */
    private static final String KEY_VERSIONS_REGION = "Region";
    /** The {@code versions} config key for the build id. */
    private static final String KEY_VERSIONS_BUILD_ID = "BuildId";
    /** The {@code versions} config key for the version. */
    private static final String KEY_VERSIONS_VERSION = "VersionsName";
    /** The {@code versions} config key for the build config. */
    private static final String KEY_VERSIONS_BUILD_CONFIG = "BuildConfig";
    /** The {@code versions} config key for the CDN config. */
    private static final String KEY_VERSIONS_CDN_CONFIG = "CDNConfig";
    /** The {@code CDNs} config key for the name. */
    private static final String KEY_CDNS_NAME = "Name";
    /** The {@code CDNs} config key for the path. */
    private static final String KEY_CDNS_PATH = "Path";
    /** The {@code CDNs} config key for the hosts. */
    private static final String KEY_CDNS_HOSTS = "Hosts";
    /** The URL mask for a file. */
    private static final String URL_MASK = "http://us.patch.battle.net/%s/%s";
    /** The filename of the {@code CDNs} config. */
    private static final String FILE_CDNS = "cdns";
    /** The filename of the {@code versions} config. */
    private static final String FILE_VERSIONS = "versions";
    /** The {@link OnlineVersionConfig} (or {@link nl.salp.warcraft4j.Branch} to use). */
    private final OnlineVersionConfig onlineVersionConfig;
    /** The parsed {@code CDNs} config. */
    private KeyBasedConfiguration cdns;
    /** The parsed {@code versions} config. */
    private KeyBasedConfiguration versions;

    /**
     * Create a new instance.
     *
     * @param warcraft4jConfig   The {@link Warcraft4jConfig} instance to configure the CDN CASC configuration with.
     * @param dataReaderProvider The {@link DataReaderProvider} for reading the configuration files.
     */
    public OnlineCdnCascConfig(Warcraft4jConfig warcraft4jConfig, DataReaderProvider dataReaderProvider) {
        super(warcraft4jConfig, dataReaderProvider);
        this.onlineVersionConfig = OnlineVersionConfig.getFrom(warcraft4jConfig.getBranch());
    }

    /**
     * Get a direct (non-hash based) URL for a file.
     *
     * @param file The name of the file.
     *
     * @return The URL for the file.
     */
    private String getDirectUrl(String file) {
        return format(URL_MASK, onlineVersionConfig.getProductCode(), file);
    }

    /**
     * Get the {@code CDNs} file config, reading it when not available.
     *
     * @return The {@code CDNs} config.
     */
    private KeyBasedConfiguration getCdns() {
        if (cdns == null) {
            String uri = getDirectUrl(FILE_CDNS);
            LOGGER.trace("Initialising CDNs config from URI {}", uri);
            cdns = KeyBasedConfiguration.tableConfig(getDataReader(uri));
        }
        return cdns;
    }

    /**
     * Get the value value of a {@code CDNs} configuration field if available using the configured region.
     *
     * @param key The key of the value.
     *
     * @return Optional of the value if it's available for the configured region.
     *
     * @see #getCdns()
     */
    private Optional<String> getCdnValue(String key) {
        return getIndexedValue(getCdns(), key, KEY_CDNS_NAME, getRegionCode());
    }

    /**
     * Get the {@code versions} file config, reading it when not available.
     *
     * @return The {@code versions} config.
     */
    private KeyBasedConfiguration getVersions() {
        if (versions == null) {
            String uri = getDirectUrl(FILE_VERSIONS);
            LOGGER.trace("Initialising versions config from URI {}", uri);
            versions = KeyBasedConfiguration.tableConfig(getDataReader(uri));
        }
        return versions;
    }

    /**
     * Get the value value of a {@code versions} configuration field if available using the configured region.
     *
     * @param key The key of the value.
     *
     * @return Optional of the value if it's available for the configured region.
     *
     * @see #getVersions()
     */
    private Optional<String> getVersionValue(String key) {
        return getIndexedValue(getVersions(), key, KEY_VERSIONS_REGION, getRegionCode());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getBuildConfigKey() {
        return getVersionValue(KEY_VERSIONS_BUILD_CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getCdnConfigKey() {
        return getVersionValue(KEY_VERSIONS_CDN_CONFIG);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Supplier<DataReader> getConfigDataReader(String checksum) {
        String uri = format("%s/config/%s/%s/%s", getCdnUrl(), checksum.substring(0, 2), checksum.substring(2, 4), checksum);
        LOGGER.trace("Mapped checksum {} to URL {}", checksum, uri);
        return getDataReader(uri);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getAvailableRegions() {
        List<String> cdnNames = getCdns().getValues(KEY_CDNS_NAME)
                .orElseThrow(() -> new CascParsingException("No CDN names found."));
        List<String> versionRegions = getVersions().getValues(KEY_VERSIONS_REGION)
                .orElseThrow(() -> new CascParsingException("No version regions found."));

        List<String> regions = new ArrayList<>(cdnNames);
        regions.retainAll(versionRegions);
        LOGGER.trace("Retrieved available regions {}", regions);
        return regions;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCdnUrl() {
        String host = getCdnValue(KEY_CDNS_HOSTS)
                .orElseThrow(() -> new CascParsingException(format("No CDN url found for region %s.", getRegion())));
        String path = getCdnValue(KEY_CDNS_PATH)
                .orElseThrow(() -> new CascParsingException(format("No CDN path found for region %s.", getRegion())));
        String url = format("http://%s/%s", host, path);
        return url;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        String version = getVersionValue(KEY_VERSIONS_VERSION)
                .orElseThrow(() -> new CascParsingException(format("No version information found for region %s.", getRegion())));
        LOGGER.trace("Retrieved version {}", version);
        return version;
    }
}