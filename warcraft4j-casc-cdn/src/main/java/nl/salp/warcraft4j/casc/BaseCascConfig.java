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
package nl.salp.warcraft4j.casc;

import nl.salp.warcraft4j.util.DataTypeUtil;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.io.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public abstract class BaseCascConfig implements CascConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseCascConfig.class);
    protected static final String KEY_BUILD_DOWNLOAD = "download";
    protected static final String KEY_BUILD_ENCODING = "encoding";
    protected static final String KEY_BUILD_ENCODING_SIZE = "encoding-size";
    protected static final String KEY_BUILD_INSTALL = "install";
    protected static final String KEY_BUILD_INSTALLER = "buildConfig-playbuild-installer";
    protected static final String KEY_BUILD_NAME = "buildConfig-name";
    protected static final String KEY_BUILD_PATCH = "patch";
    protected static final String KEY_BUILD_PATCH_CONFIG = "patch-config";
    protected static final String KEY_BUILD_PATCH_SIZE = "patch-size";
    protected static final String KEY_BUILD_PRODUCT = "buildConfig-product";
    protected static final String KEY_BUILD_ROOT = "root";
    protected static final String KEY_BUILD_UID = "buildConfig-uid";

    protected static final String KEY_CDN_BUILDS = "builds";
    protected static final String KEY_CDN_ARCHIVES = "archives";
    protected static final String KEY_CDN_ARCHIVE_GROUP = "archive-group";
    protected static final String KEY_CDN_PATCH_ARCHIVES = "patch-archives";
    protected static final String KEY_CDN_PATCH_ARCHIVE_GROUP = "patch-archive-group";

    private final DataReaderProvider dataReaderProvider;
    private final Warcraft4jConfig warcraft4jConfig;
    private Config buildConfig;
    private Config cdnConfig;

    protected BaseCascConfig(Warcraft4jConfig warcraft4jConfig, DataReaderProvider dataReaderProvider) {
        LOGGER.trace("Created {} config instance for installation {}, region {}, branch {} and locale {} with data reader provider {}",
                getClass().getName(), warcraft4jConfig.getWowInstallationDirectory(), warcraft4jConfig.getRegion(),
                warcraft4jConfig.getBranch(), warcraft4jConfig.getLocale(), dataReaderProvider.getClass().getName());
        this.warcraft4jConfig = warcraft4jConfig;
        this.dataReaderProvider = dataReaderProvider;
    }

    protected final Supplier<DataReader> getDataReader(String uri) {
        return dataReaderProvider.getDataReader(uri);
    }

    protected abstract Supplier<DataReader> getConfigDataReader(String checksum);

    protected final Config getBuildConfig() {
        if (buildConfig == null) {
            String buildConfigKey = getBuildConfigKey()
                    .orElseThrow(() -> new CascParsingException("No build configuration file checksum available."));
            LOGGER.debug("Initialising build config with key {}", buildConfigKey);
            buildConfig = Config.keyValueConfig(getConfigDataReader(buildConfigKey));
        }
        return buildConfig;
    }

    protected final Config getCdnConfig() {
        if (cdnConfig == null) {
            String cdnConfigKey = getCdnConfigKey()
                    .orElseThrow(() -> new CascParsingException("No CDN configuration file checksum available."));
            LOGGER.debug("Initialising CDN config with key {}", cdnConfigKey);
            cdnConfig = Config.keyValueConfig(getConfigDataReader(cdnConfigKey));
        }
        return cdnConfig;
    }

    @Override
    public ContentChecksum getRootContentChecksum() {
        ContentChecksum checksum = getBuildConfig().getLastValue(KEY_BUILD_ROOT, (s) -> new ContentChecksum(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No root content checksum available."));
        LOGGER.trace("Retrieved root file content checksum {}", checksum.toHexString());
        return checksum;
    }

    @Override
    public FileKey getStorageEncodingFileChecksum() {
        FileKey fileKey = getBuildConfig().getLastValue(KEY_BUILD_ENCODING, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No storage encoding file checksum available."));
        LOGGER.trace("Retrieved storage encoding file checksum {}", fileKey.toHexString());
        return fileKey;
    }

    @Override
    public long getStorageEncodingFileSize() {
        long size = getBuildConfig().getLastValue(KEY_BUILD_ENCODING_SIZE, Long::valueOf)
                .orElseThrow(() -> new CascParsingException("No storage encoding file size available."));
        LOGGER.trace("Retrieved storage encoding file size {}", size);
        return size;
    }

    @Override
    public long getExtractedEncodingFileSize() {
        long size = getBuildConfig().getFirstValue(KEY_BUILD_ENCODING_SIZE, Long::valueOf)
                .orElseThrow(() -> new CascParsingException("No extracted encoding file size available."));
        LOGGER.trace("Retrieved extracted encoding file size {}", size);
        return size;
    }

    @Override
    public FileKey getExtractedEncodingFileChecksum() {
        FileKey fileKey = getBuildConfig().getFirstValue(KEY_BUILD_ENCODING, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No extracted encoding file checksum available."));
        LOGGER.trace("Retrieved extracted encoding file checksum {}", fileKey.toHexString());
        return fileKey;
    }

    @Override
    public List<FileKey> getArchiveChecksums() {
        List<FileKey> checksums = getCdnConfig().getValues(KEY_CDN_ARCHIVES, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No archive file checksums available."));
        LOGGER.trace("Retrieved {} archive checksums {}", checksums.size(), checksums);
        return checksums;
    }

    @Override
    public FileKey getArchiveGroupChecksum() {
        FileKey checksum = getCdnConfig().getLastValue(KEY_CDN_ARCHIVE_GROUP, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No archive group file checksum available."));
        LOGGER.trace("Retrieved archive group checksum {}", checksum);
        return checksum;
    }

    @Override
    public List<FileKey> getPatchArchiveChecksums() {
        List<FileKey> checksums = getCdnConfig().getValues(KEY_CDN_PATCH_ARCHIVES, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No patch archive file checksums available."));
        LOGGER.trace("Retrieved {} patch archive checksums {}", checksums.size(), checksums);
        return checksums;
    }

    @Override
    public FileKey getPatchArchiveGroupChecksum() {
        FileKey checksum = getCdnConfig().getLastValue(KEY_CDN_PATCH_ARCHIVE_GROUP, (s) -> new FileKey(DataTypeUtil.hexStringToByteArray(s)))
                .orElseThrow(() -> new CascParsingException("No patch archive group file checksum available."));
        LOGGER.trace("Retrieved patch archive group checksum {}", checksum);
        return checksum;
    }

    @Override
    public abstract String getCdnUrl();

    protected final Warcraft4jConfig getWarcraft4jConfig() {
        return warcraft4jConfig;
    }

    @Override
    public final Region getRegion() {
        return getWarcraft4jConfig().getRegion();
    }

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

    protected final Optional<String> getIndexedValue(Config config, String key, String indexKey, String indexValue) {
        return getIndexedValue(config, key, 0, indexKey, indexValue);
    }

    protected final Optional<String> getIndexedValue(Config config, String key, int idx, String indexKey, String indexValue) {
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
