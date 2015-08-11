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
package nl.salp.warcraft4j.dev.casc.listfile;

import nl.salp.warcraft4j.clientdata.casc.CascContext;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class EntryStore {
    private static final String EXTENSION_EMPTY = "NO_EXT";
    private static final String PATH_ROOT = "\\";
    private final Map<Long, String> hashFilenames;
    private final Map<String, Long> filenameHashes;
    private final MultiMap<FileHeader, String> headerExtensions;
    private final MultiMap<String, String> extensionPaths;

    public EntryStore(ListFile listFile, CascContext cascContext) {
        this.hashFilenames = new HashMap<>();
        this.filenameHashes = new HashMap<>();
        this.headerExtensions = new MultiMap<>();
        this.extensionPaths = new MultiMap<>();
        populate(listFile, cascContext);
    }

    private void populate(ListFile listFile, CascContext cascContext) {
        listFile.getFilename().stream()
                .map(EntryStore::clean)
                .filter(cascContext::isRegisteredData)
                .forEach(filename -> populate(filename, cascContext));
    }

    private boolean populate(String filename, CascContext cascContext) {
        Optional<Long> optionalHash = cascContext.getHash(filename);
        boolean populate = optionalHash.isPresent();
        if (populate) {
            long hash = optionalHash.get();
            FileHeader header = FileHeader.parse(() -> cascContext.getFileDataReader(filename));
            String extension = getExtension(filename);
            String path = getPath(filename);
            this.filenameHashes.put(filename, hash);
            this.hashFilenames.put(hash, filename);
            this.headerExtensions.add(header, extension);
            this.extensionPaths.add(extension, path);
        }
        return populate;
    }

    public Optional<String> resolve(long hash) {
        return Optional.ofNullable(hashFilenames.get(hash));
    }

    public Optional<Long> resolve(String filename) {
        return Optional.ofNullable(filenameHashes.get(clean(filename)));
    }

    public Set<Long> getHashes() {
        return unmodifiableSet(hashFilenames.keySet());
    }

    public int getHashCount() {
        return hashFilenames.size();
    }

    public Set<String> getFilenames() {
        return unmodifiableSet(filenameHashes.keySet());
    }

    public int getFilenameCount() {
        return filenameHashes.size();
    }

    public Set<FileHeader> getHeaders() {
        return unmodifiableSet(headerExtensions.keySet());
    }

    public int getHeaderCount() {
        return headerExtensions.size();
    }

    public Set<FileHeader> getHeadersForExtension(String extension) {
        return Optional.ofNullable(extension)
                .map(EntryStore::clean)
                .map(headerExtensions::getKeys)
                .orElse(Collections.emptySet());
    }

    public Set<String> getExtensions() {
        return unmodifiableSet(headerExtensions.values());
    }

    public int getExtensionCount() {
        return headerExtensions.valueSize();
    }

    public Set<String> getExtensionsForHeader(FileHeader fileHeader) {
        return Optional.ofNullable(fileHeader)
                .map(headerExtensions::getValues)
                .orElse(Collections.emptySet());
    }

    public Set<String> getPaths() {
        return unmodifiableSet(extensionPaths.values());
    }

    public int getPathCount() {
        return extensionPaths.valueSize();
    }

    public Set<String> getPathsForExtension(String extension) {
        return Optional.ofNullable(extension)
                .map(EntryStore::clean)
                .map(extensionPaths::getValues)
                .orElse(Collections.emptySet());
    }

    public Set<String> getPathsForHeader(FileHeader header) {
        return headerExtensions.getValues(header).stream()
                .map(this::getPathsForExtension)
                .flatMap(Set::stream)
                .distinct()
                .collect(Collectors.toSet());
    }

    public Set<String> getExtensionsForPath(String path) {
        return Optional.ofNullable(path)
                .map(EntryStore::clean)
                .map(extensionPaths::getKeys)
                .orElse(Collections.emptySet());
    }

    public Set<FileHeader> getHeadersForPath(String path) {
        return extensionPaths.getKeys(clean(path)).stream()
                .map(this::getHeadersForExtension)
                .flatMap(Set::stream)
                .distinct()
                .collect(Collectors.toSet());
    }

    protected static String clean(String filename) {
        return Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .map(f -> f.replace('/', '\\'))
                .map(String::toUpperCase)
                .orElse("");
    }

    protected static String getExtension(String filename) {
        return Optional.ofNullable(filename)
                .map(EntryStore::clean)
                .filter(f -> f.contains("."))
                .orElse(EXTENSION_EMPTY);
    }

    protected static String getPath(String filename) {
        return Optional.ofNullable(filename)
                .map(EntryStore::clean)
                .map(f -> f.replace('/', '\\'))
                .filter(f -> f.contains("\\"))
                .map(f -> f.substring(0, f.lastIndexOf('\\')))
                .orElse(PATH_ROOT);

    }
}