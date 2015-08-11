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
package nl.salp.warcraft4j.data.casc.cdn;

import nl.salp.warcraft4j.Checksum;
import nl.salp.warcraft4j.config.W4jConfig;
import nl.salp.warcraft4j.data.casc.*;
import nl.salp.warcraft4j.data.casc.blte.BlteDataReader;
import nl.salp.warcraft4j.io.reader.DataReader;

import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CdnCascContext extends CascContext {
    private static final String MASK_FILES_DATA = "%s/data/%s/%s/%s";
    private CascConfig cascConfig;

    public CdnCascContext(W4jConfig w4jConfig) {
        super(w4jConfig);
    }

    @Override
    protected CascConfig getCascConfig() {
        if (cascConfig == null) {
            cascConfig = new CdnCascConfig(getW4jConfig(), getDataReaderProvider());
        }
        return cascConfig;
    }

    @Override
    protected Supplier<DataReader> getEncodingReader() {
        FileKey encodingFileChecksum = getCascConfig().getStorageEncodingFileChecksum();
        String uri = getDataFileUri(encodingFileChecksum)
                .orElseThrow(() -> new CascParsingException(format("Unable to get the URl for the encoding file with checksum %s", encodingFileChecksum.toHexString())));
        LOGGER.trace("Creating data supplier for {} byte encoding file {} from {}", getCascConfig().getStorageEncodingFileSize(), encodingFileChecksum, uri);
        return () -> new BlteDataReader(getDataReaderProvider().getDataReader(uri), getCascConfig().getStorageEncodingFileSize());
    }

    @Override
    protected Index parseIndex() throws CascParsingException {
        return new CdnIndexParser(getCascConfig(), getDataReaderProvider()).parse();
    }

    @Override
    protected DataReaderProvider getDataReaderProvider() {
        if (getW4jConfig().isCaching() && cascConfig != null) {
            return new CachingCdnDataReaderProvider(cascConfig, getW4jConfig().getCacheDirectory());
        } else {
            return new CdnDataReaderProvider();
        }
    }

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

    protected Optional<String> getDataFileUri(Checksum checksum) {
        Optional<String> uri;
        if (checksum == null || checksum.length() != 16) {
            uri = Optional.empty();
        } else {
            String keyString = checksum.toHexString();
            uri = Optional.of(format(MASK_FILES_DATA, cascConfig.getCdnUrl(), keyString.substring(0, 2), keyString.substring(2, 4), keyString));
        }
        return uri;
    }
}
