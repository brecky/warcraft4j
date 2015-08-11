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
package nl.salp.warcraft4j.analysis.clientdata.casc.listfile;

import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.file.FileDataReader;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.*;
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

    private ListFile(Map<String, Long> files) throws IOException {
        this.files = files;
        this.hashes = files.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    public Optional<Long> getHash(String filename) {
        return Optional.ofNullable(files.get(filename));
    }

    public Optional<String> getFilename(long hash) {
        return Optional.ofNullable(hashes.get(hash));
    }

    public Set<String> getFilename() {
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

    public static ListFile empty() throws IOException {
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