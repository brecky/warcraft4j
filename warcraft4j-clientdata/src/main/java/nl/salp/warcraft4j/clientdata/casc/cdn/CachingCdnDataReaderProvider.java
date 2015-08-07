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
package nl.salp.warcraft4j.clientdata.casc.cdn;

import nl.salp.warcraft4j.clientdata.casc.CascConfig;
import nl.salp.warcraft4j.clientdata.casc.CascParsingException;
import nl.salp.warcraft4j.clientdata.casc.DataReaderProvider;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.file.FileDataReader;
import nl.salp.warcraft4j.io.reader.http.CachedHttpDataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.nio.file.StandardOpenOption.*;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CachingCdnDataReaderProvider implements DataReaderProvider {
    /** The logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CachingCdnDataReaderProvider.class);

    private static final int CACHE_CHUNK_SIZE = 4096;
    private final Path cacheDirectory;
    private final String cdnUrl;

    public CachingCdnDataReaderProvider(CascConfig cascConfig, Path cacheRootDirectory) {
        String versionDir = "v" + cascConfig.getVersion().replace('.', '_');
        this.cacheDirectory = cacheRootDirectory.resolve(versionDir);
        this.cdnUrl = cascConfig.getCdnUrl();
        if (Files.notExists(cacheDirectory)) {
            LOGGER.trace("Creating cache directory {}", cacheDirectory);
            try {
                Files.createDirectories(cacheDirectory);
            } catch (IOException e) {
                throw new IllegalArgumentException(format("Unable to create CDN cache directory %s", cacheDirectory), e);
            }
        } else {
            LOGGER.trace("Using existing cache directory {}", cacheDirectory);
        }
        LOGGER.debug("Created cached CDN data reader for CDN URL {}, using cache directory {}", cdnUrl, cacheDirectory);
    }

    @Override
    public Supplier<DataReader> getDataReader(String uri) throws CascParsingException {
        if (!isCached(uri)) {
            try {
                cache(uri);
            } catch (IOException e) {
                throw new CascParsingException(format("Error caching file %s", uri), e);
            }
        }
        return () -> new FileDataReader(toCacheFile(uri));
    }

    @Override
    public Supplier<DataReader> getDataReader(String uri, long offset, long length) throws CascParsingException {
        if (!isCached(uri)) {
            try {
                cache(uri);
            } catch (IOException e) {
                throw new CascParsingException(format("Error caching file %s", uri), e);
            }
        }
        return () -> new FileDataReader(toCacheFile(uri), offset, length);
    }

    private void cache(String url) throws IOException {
        Path file = toCacheFile(url);
        LOGGER.trace("Caching CDN file {} to {}", url, file);
        if (!Files.exists(file.getParent())) {
            LOGGER.trace("Creating path {} for caching file {}", file.getParent(), file);
            Files.createDirectories(file.getParent());
        }
        try (DataReader dataReader = new CachedHttpDataReader(url); ByteChannel channel = Files.newByteChannel(file, CREATE, WRITE, TRUNCATE_EXISTING)) {
            while (dataReader.hasRemaining()) {
                int chunkSize = (int) Math.min(CACHE_CHUNK_SIZE, dataReader.remaining());
                byte[] data = dataReader.readNext(DataTypeFactory.getByteArray(chunkSize));
                channel.write(ByteBuffer.wrap(data));
            }
        }
    }

    private boolean isCached(String url) {
        Path file = toCacheFile(url);
        return Files.exists(file) && Files.isRegularFile(file) && Files.isReadable(file);
    }

    private Path toCacheFile(String url) {
        if (isEmpty(url) || !url.startsWith(cdnUrl) || url.equals(cdnUrl)) {
            throw new CascParsingException(format("%s is not a cacheable url.", url));
        }
        return cacheDirectory.resolve(Paths.get(url.substring(cdnUrl.length() + 1)));
    }
}
