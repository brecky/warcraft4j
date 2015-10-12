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
package nl.salp.warcraft4j.dev.casc;

import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.dev.casc.model.CascEntry;
import nl.salp.warcraft4j.dev.casc.model.FileHeader;
import nl.salp.warcraft4j.dev.casc.model.ListFile;
import nl.salp.warcraft4j.dev.casc.model.MultiMap;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.unmodifiableSet;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class EntryStore {
    private final Map<Long, CascEntry> entries;
    private final Map<Long, String> hashFilenames;
    private final Map<String, Long> filenameHashes;
    private final MultiMap<FileHeader, String> headerExtensions;
    private final MultiMap<String, String> extensionPaths;


    public EntryStore() {
        this(null, null);
    }

    public EntryStore(ListFile listFile, CdnCascContext cascContext) {
        this.hashFilenames = new HashMap<>();
        this.filenameHashes = new HashMap<>();
        this.headerExtensions = new MultiMap<>();
        this.extensionPaths = new MultiMap<>();
        this.entries = new HashMap<>();
        if (listFile != null && cascContext != null) {
            populate(listFile, cascContext);
        }
    }

    public void populate(ListFile listFile, CdnCascContext cascContext) {
        listFile.getFilenames().stream()
                .map(EntryStore::cleanFilename)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .forEach(filename -> populate(filename, cascContext));
    }

    public void populate(long hashcode, CdnCascContext cascContext) {
        CascEntry entry = new CascEntry(hashcode, cascContext);
        populate(entry);
    }

    public void populate(String filename, CdnCascContext cascContext) {
        CascEntry entry = new CascEntry(filename, cascContext);
        populate(entry);
    }

    public void populate(CascEntry entry) {
        if (entry != null) {
            long hashCode = entry.getHashCode();
            Optional<String> filename = entry.getFilename();
            Optional<String> extension = entry.getExtension();
            Optional<FileHeader> header = entry.getFileHeader();
            Optional<String> path = entry.getFilePath();

            this.entries.putIfAbsent(hashCode, entry);
            if (filename.isPresent()) {
                this.filenameHashes.putIfAbsent(filename.get(), hashCode);
                this.hashFilenames.putIfAbsent(hashCode, filename.get());
            }
            if (extension.isPresent() && header.isPresent()) {
                headerExtensions.add(header.get(), extension.get());
            }
            if (extension.isPresent() && path.isPresent()) {
                extensionPaths.add(extension.get(), path.get());
            }
        }
    }

    public Optional<CascEntry> getEntry(long hash) {
        return Optional.ofNullable(entries.get(hash));
    }

    public Set<CascEntry> getEntries() {
        return entries.values().stream()
                .filter(e -> e != null)
                .distinct()
                .collect(Collectors.toSet());
    }

    public Optional<String> getFilename(long hash) {
        return Optional.ofNullable(hashFilenames.get(hash));
    }

    public Optional<Long> getHash(String filename) {
        return Optional.ofNullable(filenameHashes.get(cleanFilename(filename)));
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

    public int getHeaderCount(FileHeader header) {
        return (int) headerExtensions.values().stream()
                .map(headerExtensions::getKeys)
                .flatMap(Set::stream)
                .filter(h -> h.equals(header))
                .count();
    }

    public Set<String> getExtensions() {
        return unmodifiableSet(headerExtensions.values());
    }

    public int getExtensionCount() {
        return headerExtensions.valueSize();
    }

    public int getExtensionCount(String extension) {
        return (int) headerExtensions.values().stream()
                .map(headerExtensions::getKeys)
                .flatMap(Set::stream)
                .filter(ext -> ext.equals(extension))
                .count();
    }

    public Set<String> getPaths() {
        return unmodifiableSet(extensionPaths.values());
    }

    public int getPathCount() {
        return extensionPaths.valueSize();
    }

    public Set<FileHeader> getHeadersForExtension(String extension) {
        return cleanFilename(extension)
                .map(headerExtensions::getKeys)
                .orElse(Collections.emptySet());
    }

    public Set<String> getExtensionsForHeader(FileHeader fileHeader) {
        return Optional.ofNullable(fileHeader)
                .map(headerExtensions::getValues)
                .orElse(Collections.emptySet());
    }

    public Set<String> getPathsForExtension(String extension) {
        return cleanFilename(extension)
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
        return cleanFilename(path)
                .map(extensionPaths::getKeys)
                .orElse(Collections.emptySet());
    }

    public Set<FileHeader> getHeadersForPath(String path) {
        return cleanFilename(path)
                .map(extensionPaths::getKeys)
                .map(Set::stream)
                .map(stream -> stream
                        .map(this::getHeadersForExtension)
                        .flatMap(Set::stream)
                        .distinct()
                        .collect(Collectors.toSet()))
                .orElse(Collections.emptySet());
    }

    public static Optional<String> cleanFilename(String filename) {
        return Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .map(f -> f.replace('/', '\\'))
                .map(String::toUpperCase);
    }

    public static Optional<String> getExtension(String filename) {
        return Optional.ofNullable(filename)
                .flatMap(EntryStore::cleanFilename)
                .filter(f -> f.contains("."))
                .map(f -> f.substring(f.lastIndexOf('.')));
    }

    public static Optional<String> getPath(String filename) {
        return Optional.ofNullable(filename)
                .flatMap(EntryStore::cleanFilename)
                .map(f -> f.replace('/', '\\'))
                .filter(f -> f.contains("\\"))
                .map(f -> f.substring(0, f.lastIndexOf('\\')));
    }
}
