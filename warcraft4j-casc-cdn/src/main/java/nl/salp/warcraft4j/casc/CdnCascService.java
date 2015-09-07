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

import com.google.inject.Inject;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.WowVersion;
import nl.salp.warcraft4j.io.reader.DataReader;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Collections.emptyList;

/**
 * {@link CascService} implementation for CDN (either online CDN or local installation) based data.
 *
 * @author Barre Dijkstra
 * @see CascService
 */
public class CdnCascService implements CascService {
    /** The {@link CascContext} to use. */
    private final CascContext cascContext;
    /** The World of Warcraft version of the CASC context. */
    private final CascWowVersion version;
    /** The locale of the CASC context or {@code null} if not available. */
    private final Locale locale;

    /**
     * Create a new instance from a {@link CascContext}.
     *
     * @param cascContext The CASC context to use.
     *
     * @throws IllegalArgumentException When the CASC context is invalid.
     */
    @Inject
    public CdnCascService(CascContext cascContext) {
        if (cascContext == null) {
            throw new IllegalArgumentException("Unable to create a CDN based CASC service for a null casc context.");
        }
        this.cascContext = cascContext;
        this.version = new CascWowVersion(cascContext.getVersion(), cascContext.getBranch(), cascContext.getRegion());
        this.locale = cascContext.getLocale();
    }

    @Override
    public WowVersion getVersion() {
        return version;
    }

    @Override
    public Optional<Locale> getLocale() {
        return Optional.ofNullable(locale);
    }

    @Override
    public boolean isCascFileAvailable(ContentChecksum contentChecksum) {
        return Optional.ofNullable(contentChecksum)
                .map(cascContext::getEncodingEntry)
                .map(Optional::isPresent)
                .orElse(false);
    }

    @Override
    public boolean isCascFileAvailable(long filenameHash) {
        return cascContext.isRegistered(filenameHash);
    }

    @Override
    public boolean isCascFileAvailable(String filename) {
        return cascContext.isRegistered(filename);
    }

    @Override
    public Optional<CascFile> getCascFile(ContentChecksum contentChecksum) {
        // TODO Implement me!
        return Optional.empty();
    }

    @Override
    public Optional<CascFile> getCascFile(long filenameHash) {
        Optional<CascFile> file;
        if (cascContext.isRegistered(filenameHash)) {
            file = Optional.empty();
        } else {
            file = Optional.of(cascContext.getFilename(filenameHash)
                    .map(name -> new CdnCascFile(filenameHash, name, cascContext))
                    .orElse(new CdnCascFile(filenameHash, cascContext)));
        }
        return file;
    }

    @Override
    public Optional<CascFile> getCascFile(String filename) {
        return Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .map(cascContext::getHash)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .map(hash -> new CdnCascFile(hash, filename, cascContext));
    }

    @Override
    public Set<CascFile> getAllCascFiles() {
        return cascContext.getResolvedHashes().stream()
                .distinct()
                .map(hash -> cascContext.getFilename(hash)
                                .filter(StringUtils::isNotEmpty)
                                .map(name -> new CdnCascFile(hash, name, cascContext))
                                .orElse(new CdnCascFile(hash, cascContext))
                )
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isRootEntryAvailable(CascFile cascFile) {
        return getRootEntries(cascFile).size() > 0;
    }

    @Override
    public List<RootEntry> getRootEntries(CascFile cascFile) {
        return Optional.ofNullable(cascFile)
                .map(CascFile::getFilenameHash)
                .map(cascContext::getRootEntries)
                .orElse(emptyList());
    }

    @Override
    public Set<RootEntry> getAllRootEntries() {
        return new HashSet<>(cascContext.getRootEntries());
    }

    @Override
    public boolean isEncodingEntryAvailable(RootEntry rootEntry) {
        return getEncodingEntry(rootEntry).isPresent();
    }

    @Override
    public Optional<EncodingEntry> getEncodingEntry(RootEntry rootEntry) {
        return Optional.ofNullable(rootEntry)
                .flatMap(cascContext::getEncodingEntry);
    }

    @Override
    public Set<EncodingEntry> getAllEncodingEntries() {
        return new HashSet<>(cascContext.getEncodingEntries());
    }

    @Override
    public boolean isAtleastOneIndexEntryAvailable(EncodingEntry encodingEntry) {
        return getIndexEntries(encodingEntry).size() > 0;
    }

    @Override
    public boolean isIndexEntryAvailable(EncodingEntry encodingEntry) {
        return getIndexEntries(encodingEntry).size() == encodingEntry.getBlockCount();
    }

    @Override
    public List<IndexEntry> getIndexEntries(EncodingEntry encodingEntry) {
        return Optional.ofNullable(encodingEntry)
                .map(cascContext::getIndexEntries)
                .orElse(emptyList());
    }

    @Override
    public Set<IndexEntry> getAllIndexEntries() {
        return new HashSet<>(cascContext.getIndexEntries());
    }

    @Override
    public boolean isDataAvailable(CascFile cascFile) {
        return Optional.ofNullable(cascFile)
                .map(CascFile::getFilenameHash)
                .map(cascContext::isRegisteredData)
                .orElse(false);
    }

    @Override
    public boolean isDataAvailable(IndexEntry indexEntry) {
        return Optional.ofNullable(indexEntry)
                .map(IndexEntry::getFileKey)
                .flatMap(cascContext::getIndexEntry)
                .map(e -> e != null)
                .orElse(false);
    }

    @Override
    public DataReader getDataReader(CascFile cascFile) throws CascEntryNotFoundException {
        return Optional.ofNullable(cascFile)
                .map(CascFile::getFilenameHash)
                .filter(cascContext::isRegisteredData)
                .map(cascContext::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No data reader found for casc file with hash %d%s",
                        cascFile.getFilenameHash(), cascFile.getFilename().map(n -> " and name" + n).orElse(""))));
    }

    @Override
    public DataReader getDataReader(IndexEntry indexEntry) throws CascEntryNotFoundException {
        return Optional.ofNullable(indexEntry)
                .filter(this::isDataAvailable)
                .map(cascContext::getFileDataReader)
                .orElseThrow(() -> new CascEntryNotFoundException(format("No data reader found for index entry with file key %d and file number %d",
                        indexEntry.getFileKey().toHexString(), indexEntry.getFileNumber())));
    }
}