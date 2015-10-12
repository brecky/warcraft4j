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

import com.google.inject.Inject;
import com.google.inject.Singleton;
import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import nl.salp.warcraft4j.casc.cdn.CdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.cdn.DataReaderProvider;
import nl.salp.warcraft4j.casc.cdn.Index;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.io.DataReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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
public class LocalCdnCascContext extends CdnCascContext {
    /** The CASC configuration. */
    private CdnCascConfig cdnCascConfig;

    /**
     * Create a new instance.
     *
     * @param warcraft4jConfig The configuration.
     */
    @Inject
    public LocalCdnCascContext(Warcraft4jConfig warcraft4jConfig) {
        super(warcraft4jConfig);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CdnCascConfig getCdnCascConfig() {
        if (cdnCascConfig == null) {
            cdnCascConfig = new LocalCdnCascConfig(getWarcraft4jConfig(), getDataReaderProvider());
        }
        return cdnCascConfig;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Supplier<DataReader> getEncodingReader() {
        FileKey encodingFileChecksum = getCdnCascConfig().getStorageEncodingFileChecksum();
        IndexEntry indexEntry = Optional.ofNullable(encodingFileChecksum)
                .flatMap(this::getIndexEntry)
                .orElseThrow(() -> new CascParsingException(format("No entry found for encoding file entry %s", encodingFileChecksum.toHexString())));
        LOGGER.trace("Creating data supplier for encoding file {} from {} bytes of data in data.{} at offset {}",
                encodingFileChecksum,
                indexEntry.getFileSize(),
                format("%03d", indexEntry.getFileNumber()),
                indexEntry.getDataFileOffset()
        );
        return getFileDataReaderSupplier(indexEntry);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected DataReaderProvider getDataReaderProvider() {
        return new FileDataReaderProvider();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Supplier<DataReader> getDataReader(String dataFile, long dataFileOffset, long fileSize) throws CascParsingException {
        LOGGER.trace("Getting local file data reader for {} (offset: {}, size: {})", dataFile, dataFileOffset + 30, fileSize - 30);
        return getDataReaderProvider().getDataReader(dataFile, dataFileOffset + 30, fileSize - 30);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Index parseIndex() throws CascParsingException {
        return new LocalIndexParser(getWarcraft4jConfig().getWowInstallationDirectory()).parse();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected Optional<String> getDataFileUri(IndexEntry entry) {
        Optional<String> uri;
        if (entry != null) {
            String filename = format("data.%03d", entry.getFileNumber());
            Path file = getWarcraft4jConfig().getWowInstallationDirectory().resolve(Paths.get("Data", "data", filename));
            if (Files.exists(file) && Files.isReadable(file) && Files.isRegularFile(file)) {
                uri = Optional.of(file.toString());
            } else {
                uri = Optional.empty();
            }
        } else {
            uri = Optional.empty();
        }
        return uri;
    }
}