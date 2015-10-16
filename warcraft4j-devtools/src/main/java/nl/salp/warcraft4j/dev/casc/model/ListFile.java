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
package nl.salp.warcraft4j.dev.casc.model;

import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.FileDataReader;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class ListFile {
    private final Map<String, Long> files;
    private final Map<Long, String> hashes;
    private final Set<Long> calculated;

    private ListFile(Map<String, Long> files) {
        this.files = files;
        this.hashes = files.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
        this.calculated = new HashSet<>();
    }

    public Set<ListCascFile> getCascFiles() {
        return hashes.keySet().stream()
                .map(hash -> new ListCascFile(hash, getFilenames(hash).orElse(null), null, calculated.contains(hash)))
                .collect(Collectors.toSet());
    }

    public long getHash(String filename) {
        return Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .map(f -> files.getOrDefault(f, ((Supplier<Long>) () -> {
                    byte[] data = f.replace('/', '\\').toUpperCase().getBytes(StandardCharsets.US_ASCII);
                    long hash = JenkinsHash.hashLittle2(data, data.length);
                    files.put(f, hash);
                    hashes.put(hash, f);
                    calculated.add(hash);
                    return hash;
                }).get()))
                .orElse(0L);
    }

    public boolean isFileKnown(String filename) {
        long hash = getHash(filename);
        return hash != 0 || calculated.contains(hash);
    }

    public boolean isFileKnown(long hash) {
        return hashes.containsKey(hash);
    }

    public Optional<String> getFilenames(long hash) {
        return Optional.ofNullable(hashes.get(hash));
    }

    public Set<String> getFilenames() {
        return Collections.unmodifiableSet(files.keySet());
    }

    public int getFilenameCount() {
        return files.size();
    }

    public Set<Long> getHashes() {
        return Collections.unmodifiableSet(hashes.keySet());
    }

    public int getHashCount() {
        return hashes.size();
    }

    public static ListFile empty() {
        return new ListFile(Collections.emptyMap());
    }

    public static ListFile fromFile(Path listFile) throws IOException {
        return new ListFile(parseFile(listFile));
    }

    private static Map<String, Long> parseFile(Path listFile) throws IOException {
        Map<String, Long> listfile = new HashMap<>();
        try (DataReader reader = new FileDataReader(listFile)) {
            while (reader.hasRemaining()) {
                String line = reader.readNext(DataTypeFactory.getStringLine()).trim();
                if (isNotEmpty(line)) {
                    byte[] data = line.replace('/', '\\').toUpperCase().getBytes(StandardCharsets.US_ASCII);
                    long hash = JenkinsHash.hashLittle2(data, data.length);
                    listfile.put(line, hash);
                }
            }
        }
        return listfile;
    }
}