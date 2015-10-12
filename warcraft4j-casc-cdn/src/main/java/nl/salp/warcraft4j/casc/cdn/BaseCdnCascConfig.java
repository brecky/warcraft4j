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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.util.DataTypeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Base {@link CdnCascConfig} implementation containing all generic configuration logic.
 *
 * @author Barre Dijkstra
 * @see CdnCascConfig
 */
public abstract class BaseCdnCascConfig implements CdnCascConfig {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCdnCascConfig.class);
    /** The {@code build} file key for the {@code download} field. */
    protected static final String KEY_BUILD_DOWNLOAD = "download";
    /** The {@code build} file key for the {@code encoding} field. */
    protected static final String KEY_BUILD_ENCODING = "encoding";
    /** The {@code build} file key for the {@code encoding size} field. */
    protected static final String KEY_BUILD_ENCODING_SIZE = "encoding-size";
    /** The {@code build} file key for the {@code install} field. */
    protected static final String KEY_BUILD_INSTALL = "install";
    /** The {@code build} file key for the {@code installer} field. */
    protected static final String KEY_BUILD_INSTALLER = "buildConfig-playbuild-installer";
    /** The {@code build} file key for the {@code name} field. */
    protected static final String KEY_BUILD_NAME = "buildConfig-name";
    /** The {@code build} file key for the {@code patch} field. */
    protected static final String KEY_BUILD_PATCH = "patch";
    /** The {@code build} file key for the {@code patch config} field. */
    protected static final String KEY_BUILD_PATCH_CONFIG = "patch-config";
    /** The {@code build} file key for the {@code patch size} field. */
    protected static final String KEY_BUILD_PATCH_SIZE = "patch-size";
    /** The {@code build} file key for the {@code product} field. */
    protected static final String KEY_BUILD_PRODUCT = "buildConfig-product";
    /** The {@code build} file key for the {@code root} field. */
    protected static final String KEY_BUILD_ROOT = "root";
    /** The {@code build} file key for the {@code UID} field. */
    protected static final String KEY_BUILD_UID = "buildConfig-uid";
    /** The {@code CDN} file key for the {@code builds} field. */
    protected static final String KEY_CDN_BUILDS = "builds";
    /** The {@code CDN} file key for the {@code archives} field. */
    protected static final String KEY_CDN_ARCHIVES = "archives";
    /** The {@code CDN} file key for the {@code archive group} field. */
    protected static final String KEY_CDN_ARCHIVE_GROUP = "archive-group";
    /** The {@code CDN} file key for the {@code patch archives} field. */
    protected static final String KEY_CDN_PATCH_ARCHIVES = "patch-archives";
    /** The {@code CDN} file key for the {@code patch archive group} field. */
    protected static final String KEY_CDN_PATCH_ARCHIVE_GROUP = "patch-archive-group";
    /** The {@link DataReaderProvider} for reading the configuration files. */
    private final DataReaderProvider dataReaderProvider;
    /** The {@link Warcraft4jConfig} instance. */
    private final Warcraft4jConfig warcraft4jConfig;
    /** The parsed build configuration file. */
    private KeyBasedConfiguration buildConfig;
    /** The parsed CDN configuration file. */
    private KeyBasedConfiguration cdnConfig;

    protected BaseCdnCascConfig(Warcraft4jConfig warcraft4jConfig, DataReaderProvider dataReaderProvider) {
        LOGGER.trace("Created {} config instance for installation {}, region {}, branch {} and locale {} with data reader provider {}",
                getClass().getName(), warcraft4jConfig.getWowInstallationDirectory(), warcraft4jConfig.getRegion(),
                warcraft4jConfig.getBranch(), warcraft4jConfig.getLocale(), dataReaderProvider.getClass().getName());
        this.warcraft4jConfig = warcraft4jConfig;
        this.dataReaderProvider = dataReaderProvider;
    }

    /**
     * Get a supplier for a data reader based on an URI.
     *
     * @param uri The URI to get the data reader for.
     *
     * @return The supplier for the data reader.
     */
    protected final Supplier<DataReader> getDataReader(String uri) {
        return dataReaderProvider.getDataReader(uri);
    }

    /**
     * Get a supplier for a data reader based for a file based on a checksum.
     *
     * @param checksum The checksum of the file.
     *
     * @return The supplier for the data reader.
     */
    protected abstract Supplier<DataReader> getConfigDataReader(String checksum);

    /**
     * Get the parsed build configuration.
     *
     * @return The build configuration.
     */
    protected final KeyBasedConfiguration getBuildConfig() {
        if (buildConfig == null) {
            String buildConfigKey = getBuildConfigKey()
                    .orElseThrow(() -> new CascParsingException("No build configuration file checksum available."));
            LOGGER.debug("Initialising build config with key {}", buildConfigKey);
            buildConfig = KeyBasedConfiguration.keyValueConfig(getConfigDataReader(buildConfigKey));
        }
        return buildConfig;
    }

    /**
     * Get the parsed CDN configuration.
     *
     * @return The CDN configuration.
     */
    protected final KeyBasedConfiguration getCdnConfig() {
        if (cdnConfig == null) {
            String cdnConfigKey = getCdnConfigKey()
                    .orElseThrow(() -> new CascParsingException("No CDN configuration file checksum available."));
            LOGGER.debug("Initialising CDN config with key {}", cdnConfigKey);
            cdnConfig = KeyBasedConfiguration.keyValueConfig(getConfigDataReader(cdnConfigKey));
        }
        return cdnConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ContentChecksum getRootContentChecksum() {
        ContentChecksum checksum = getBuildConfig().getLastValue(KEY_BUILD_ROOT, (s) -> new ContentChecksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No root content checksum available."));
        LOGGER.trace("Retrieved root file content checksum {}", checksum.toHexString());
        return checksum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileKey getStorageEncodingFileChecksum() {
        FileKey fileKey = getBuildConfig().getLastValue(KEY_BUILD_ENCODING, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No storage encoding file checksum available."));
        LOGGER.trace("Retrieved storage encoding file checksum {}", fileKey.toHexString());
        return fileKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getStorageEncodingFileSize() {
        long size = getBuildConfig().getLastValue(KEY_BUILD_ENCODING_SIZE, Long::valueOf)
                .orElseThrow(() -> new CascParsingException("No storage encoding file size available."));
        LOGGER.trace("Retrieved storage encoding file size {}", size);
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getExtractedEncodingFileSize() {
        long size = getBuildConfig().getFirstValue(KEY_BUILD_ENCODING_SIZE, Long::valueOf)
                .orElseThrow(() -> new CascParsingException("No extracted encoding file size available."));
        LOGGER.trace("Retrieved extracted encoding file size {}", size);
        return size;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileKey getExtractedEncodingFileChecksum() {
        FileKey fileKey = getBuildConfig().getFirstValue(KEY_BUILD_ENCODING, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No extracted encoding file checksum available."));
        LOGGER.trace("Retrieved extracted encoding file checksum {}", fileKey.toHexString());
        return fileKey;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileKey> getArchiveChecksums() {
        List<FileKey> checksums = getCdnConfig().getValues(KEY_CDN_ARCHIVES, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No archive file checksums available."));
        LOGGER.trace("Retrieved {} archive checksums {}", checksums.size(), checksums);
        return checksums;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileKey getArchiveGroupChecksum() {
        FileKey checksum = getCdnConfig().getLastValue(KEY_CDN_ARCHIVE_GROUP, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No archive group file checksum available."));
        LOGGER.trace("Retrieved archive group checksum {}", checksum);
        return checksum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<FileKey> getPatchArchiveChecksums() {
        List<FileKey> checksums = getCdnConfig().getValues(KEY_CDN_PATCH_ARCHIVES, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No patch archive file checksums available."));
        LOGGER.trace("Retrieved {} patch archive checksums {}", checksums.size(), checksums);
        return checksums;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FileKey getPatchArchiveGroupChecksum() {
        FileKey checksum = getCdnConfig().getLastValue(KEY_CDN_PATCH_ARCHIVE_GROUP, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No patch archive group file checksum available."));
        LOGGER.trace("Retrieved patch archive group checksum {}", checksum);
        return checksum;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public abstract String getCdnUrl();

    /**
     * Get the {@link Warcraft4jConfig} the configuration is initialised with.
     *
     * @return The {@link Warcraft4jConfig} instance.
     */
    protected final Warcraft4jConfig getWarcraft4jConfig() {
        return warcraft4jConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Region getRegion() {
        return getWarcraft4jConfig().getRegion();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String getRegionCode() {
        String region;
        switch (getRegion()) {
            case AMERICAS:
                region = "us";
                break;
            case CHINA:
                region = "cn";
                break;
            case EUROPE:
                region = "eu";
                break;
            case KOREA:
                region = "kr";
                break;
            case SEA_AUSTRALASIA:
                region = "sg";
                break;
            case TAIWAN:
                region = "tw";
                break;
            default:
                region = "xx";
                break;
        }
        return region;
    }

    /**
     * Get a value from a config based on the value of another column in the config.
     *
     * @param config     The config to get the value from.
     * @param key        The key of the column to get the value for.
     * @param indexKey   The key of the column to use as filter.
     * @param indexValue The value of the column to filter on.
     *
     * @return Optional containing the value if available.
     */
    protected final Optional<String> getIndexedValue(KeyBasedConfiguration config, String key, String indexKey, String indexValue) {
        return getIndexedValue(config, key, 0, indexKey, indexValue);
    }

    /**
     * Get a entry from a multi-entry value from a config based on the value of another column in the config.
     *
     * @param config     The config to get the value from.
     * @param key        The key of the column to get the value for.
     * @param idx        The index of the value from the multi-entry value to get.
     * @param indexKey   The key of the column to use as filter.
     * @param indexValue The value of the column to filter on.
     *
     * @return Optional containing the value if available.
     */
    protected final Optional<String> getIndexedValue(KeyBasedConfiguration config, String key, int idx, String indexKey, String indexValue) {
        return config.getValue(key, indexKey, indexValue)
                .flatMap(s -> {
                    if (s.contains(" ")) {
                        String[] values = s.split(" ");
                        if (idx > values.length) {
                            return Optional.empty();
                        } else {
                            return Optional.of(s.split(" ")[idx].trim());
                        }
                    } else {
                        return Optional.ofNullable(s);
                    }
                });
    }
}