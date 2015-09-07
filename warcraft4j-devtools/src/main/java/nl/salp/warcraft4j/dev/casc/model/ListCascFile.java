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
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ListCascFile implements CascFile {
    private final long hash;
    private String filename;
    private boolean calculated;
    private FileHeader fileHeader;

    public ListCascFile(long hash) {
        this(hash, null, null, false);
    }

    public ListCascFile(long hash, String filename) {
        this(hash, filename, null, false);
    }

    public ListCascFile(String filename) throws IllegalArgumentException {
        this(Optional.ofNullable(filename)
                .filter(StringUtils::isNotEmpty)
                .map(f -> f.replace('/', '\\'))
                .map(String::toUpperCase)
                .map(f -> f.getBytes(StandardCharsets.US_ASCII))
                .map(d -> JenkinsHash.hashLittle2(d, d.length))
                .orElseThrow(() -> new IllegalArgumentException("Unable to calculate a hash for an empty filename."))
                , filename, null, true);
    }

    public ListCascFile(long hash, String filename, FileHeader header, boolean calculated) {
        this.hash = hash;
        this.filename = filename;
        this.fileHeader = header;
        this.calculated = calculated;
    }

    @Override
    public long getFilenameHash() {
        return hash;
    }

    public boolean isFilenameHashCalculated() {
        return calculated;
    }

    @Override
    public Optional<String> getFilename() {
        return Optional.ofNullable(filename);
    }

    @Override
    public void setFilename(String filename) {
        this.filename = filename;

    }

    @Override
    public Optional<FileHeader> getHeader() {
        return Optional.ofNullable(fileHeader);
    }

    @Override
    public void setHeader(FileHeader header) {
        this.fileHeader = header;
    }
}
