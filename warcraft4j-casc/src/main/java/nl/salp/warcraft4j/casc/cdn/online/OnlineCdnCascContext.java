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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.salp.warcraft4j.casc.cdn.*;
import nl.salp.warcraft4j.util.Checksum;
import nl.salp.warcraft4j.casc.*;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.io.DataReader;

import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * {@link CdnCascContext} implementation for an online CDN.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.cdn.CdnCascContext
 */
@Singleton
public class OnlineCdnCascContext extends CdnCascContext {
    /** The URI mask for data files. */
    private static final String MASK_FILES_DATA = "%s/data/%s/%s/%s";
    /** The {@link CdnCascConfig}. */
    private CdnCascConfig cdnCascConfig;

    /**
     * Create a new context instance.
     *
     * @param warcraft4jConfig The {@link Warcraft4jConfig}.
     */
    @Inject
    public OnlineCdnCascContext(Warcraft4jConfig warcraft4jConfig) {
        super(warcraft4jConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CdnCascConfig getCdnCascConfig() {
        if (cdnCascConfig == null) {
            cdnCascConfig = new OnlineCdnCascConfig(getWarcraft4jConfig(), getDataReaderProvider());
        }
        return cdnCascConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Supplier<DataReader> getEncodingReader() {
        FileKey encodingFileChecksum = getCdnCascConfig().getStorageEncodingFileChecksum();
        String uri = getDataFileUri(encodingFileChecksum)
                .orElseThrow(() -> new CascParsingException(format("Unable to get the URl for the encoding file with checksum %s", encodingFileChecksum.toHexString())));
        LOGGER.trace("Creating data supplier for {} byte encoding file {} from {}", getCdnCascConfig().getStorageEncodingFileSize(), encodingFileChecksum, uri);
        return () -> new BlteDataReader(getDataReaderProvider().getDataReader(uri), getCdnCascConfig().getStorageEncodingFileSize());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Index parseIndex() throws CascParsingException {
        return new OnlineIndexParser(getCdnCascConfig(), getDataReaderProvider()).parse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataReaderProvider getDataReaderProvider() {
        if (getWarcraft4jConfig().isCaching() && cdnCascConfig != null) {
            return new CachingOnlineDataReaderProvider(cdnCascConfig, getWarcraft4jConfig().getCacheDirectory());
        } else {
            return new OnlineDataReaderProvider();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<String> getDataFileUri(IndexEntry entry) {
        Optional<String> uri;
        if (entry == null) {
            uri = Optional.empty();
        } else {
            uri = getDataFileUri(entry.getFileKey());
        }
        return uri;
    }

    /**
     * Get the URI of the data file containing the file for a checksum.
     *
     * @param checksum The checksum of the file to retrieve the data file URI for.
     *
     * @return Optional with the data file URI if present.
     */
    protected Optional<String> getDataFileUri(Checksum checksum) {
        Optional<String> uri;
        if (checksum == null || checksum.length() != 16) {
            uri = Optional.empty();
        } else {
            String keyString = checksum.toHexString();
            uri = Optional.of(format(MASK_FILES_DATA, cdnCascConfig.getCdnUrl(), keyString.substring(0, 2), keyString.substring(2, 4), keyString));
        }
        return uri;
    }
}