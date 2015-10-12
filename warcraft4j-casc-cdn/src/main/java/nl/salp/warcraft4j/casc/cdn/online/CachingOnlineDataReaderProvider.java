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
import nl.salp.warcraft4j.casc.cdn.CdnCascConfig;
import nl.salp.warcraft4j.casc.cdn.DataReaderProvider;
import nl.salp.warcraft4j.io.CachedHttpDataReader;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.FileDataReader;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
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
 * {@link DataReaderProvider} for reading files over HTTP, caching the files prior per version of the CASC the file is in.
 * <p>
 * This implementation has no cache-expiration or cleanup functionality and can consume quite a bit of drive space over time.
 * </p>
 *
 * @author Barre Dijkstra
 */
public class CachingOnlineDataReaderProvider implements DataReaderProvider {
    /** The logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CachingOnlineDataReaderProvider.class);
    /** The size of chunks to use reading (cached) files. */
    private static final int CACHE_CHUNK_SIZE = 4096;
    /** The cache directory. */
    private final Path cacheDirectory;
    /** The CDN url. */
    private final String cdnUrl;

    /**
     * Create a new instance.
     *
     * @param cdnCascConfig      The {@link CdnCascConfig} to use.
     * @param cacheRootDirectory The path of the directory to cache all files in.
     *
     * @throws IllegalArgumentException When the cache directory is not available and could not be created.
     */
    public CachingOnlineDataReaderProvider(CdnCascConfig cdnCascConfig, Path cacheRootDirectory) throws IllegalArgumentException {
        String versionDir = "v" + cdnCascConfig.getVersion().replace('.', '_');
        this.cacheDirectory = cacheRootDirectory.resolve(versionDir);
        this.cdnUrl = cdnCascConfig.getCdnUrl();
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
        LOGGER.trace("Created cached CDN data reader for CDN URL {}, using cache directory {}", cdnUrl, cacheDirectory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Supplier<DataReader> getDataReader(String uri) throws CascParsingException {
        return () -> new FileDataReader(getFile(uri));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Supplier<DataReader> getDataReader(String uri, long offset, long length) throws CascParsingException {
        return () -> new FileDataReader(getFile(uri), offset, length);
    }

    /**
     * Get the path of the cached version of a file, caching it when required.
     *
     * @param url The URL of the file.
     *
     * @return The path of the cached file.
     *
     * @throws CascParsingException When caching the file failed or the URL is invalid.
     */
    private Path getFile(String url) throws CascParsingException {
        Path filePath;
        if (isCached(url)) {
            filePath = toCacheFile(url);
        } else {
            try {
                filePath = cache(url);
            } catch (IOException e) {
                throw new CascParsingException(format("Error caching file %s", url), e);
            }
        }
        return filePath;
    }

    /**
     * Cache a file, overwriting the previous version if existing.
     *
     * @param url The URL of the file to cache.
     *
     * @return The path of the cached file.
     *
     * @throws IOException When reading the file or writing the cached version of the file fails.
     */
    private Path cache(String url) throws IOException {
        Path file = toCacheFile(url);
        LOGGER.trace("Caching CDN file {} to {}", url, file);
        if (!Files.exists(file.getParent())) {
            LOGGER.trace("Creating directory structure {} to cache file {}", file.getParent(), file);
            Files.createDirectories(file.getParent());
        }
        try (DataReader fileDataReader = new CachedHttpDataReader(url);
             ByteChannel cachedFileChannel = Files.newByteChannel(file, CREATE, WRITE, TRUNCATE_EXISTING)) {
            long fileSize = 0;
            while (fileDataReader.hasRemaining()) {
                int chunkSize = (int) Math.min(CACHE_CHUNK_SIZE, fileDataReader.remaining());
                ByteBuffer dataBuffer = ByteBuffer.wrap(fileDataReader.readNext(DataTypeFactory.getByteArray(chunkSize)));
                while (dataBuffer.hasRemaining()) {
                    fileSize += cachedFileChannel.write(dataBuffer);
                }
            }
            LOGGER.trace("Cached {} byte CDN file {} to {}", fileSize, url, file);
        }
        return file;
    }

    /**
     * Check if a file is cached.
     *
     * @param url The URL of the file.
     *
     * @return {@code true} if the file is cached.
     *
     * @throws CascParsingException When the provided URL is invalid or does not point to a valid file.
     */
    private boolean isCached(String url) throws CascParsingException {
        Path file = toCacheFile(url);
        return Files.exists(file) && Files.isRegularFile(file) && Files.isReadable(file);
    }

    /**
     * Convert the URL of a file to the path of the cached version (which might not exist).
     *
     * @param url The URL of the file.
     *
     * @return The path to the cached version of the file.
     *
     * @throws CascParsingException When the provided URL is invalid or does not point to a valid file.
     */
    private Path toCacheFile(String url) throws CascParsingException {
        if (isEmpty(url) || !url.startsWith(cdnUrl) || url.equals(cdnUrl)) {
            throw new CascParsingException(format("%s is not a cacheable url.", url));
        }
        return cacheDirectory.resolve(Paths.get(url.substring(cdnUrl.length() + 1)));
    }
}
