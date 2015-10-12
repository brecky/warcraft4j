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

import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.dev.casc.EntryStore;
import nl.salp.warcraft4j.hash.JenkinsHash;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CascEntry {
    private transient CdnCascContext cascContext;
    private final long hashCode;
    private String filename;
    private boolean inCasc;
    private boolean cascReferenceValid;
    private FileHeader fileHeader;
    private List<ContentChecksum> contentChecksums;
    private List<FileKey> fileKeys;

    public CascEntry(long hashCode, CdnCascContext cascContext) throws IllegalArgumentException {
        if (hashCode == 0 || hashCode == JenkinsHash.HASH_EMPTY_VALUE_HASHLITTLE2) {
            throw new IllegalArgumentException("Unable to create a CascEntry for a hashcode for an empty value.");
        }
        if (cascContext == null) {
            throw new IllegalArgumentException("Unable to create a CascEntry from a null CdnCascContext.");
        }
        this.hashCode = hashCode;
        this.cascContext = cascContext;
        this.inCasc = cascContext.isRegistered(hashCode);
        this.cascReferenceValid = cascContext.isRegisteredData(hashCode);
    }

    public CascEntry(String filename, CdnCascContext cascContext) throws IllegalArgumentException {
        this(Optional.ofNullable(filename)
                        .filter(StringUtils::isNotEmpty)
                        .map(CdnCascContext::hashFilename)
                        .orElseThrow(() -> new IllegalArgumentException("Unable to create a CascEntry for an empty filename")),
                cascContext);
        this.filename = EntryStore.cleanFilename(filename).orElse(null);
    }

    protected CdnCascContext getCascContext() throws IllegalStateException {
        return cascContext;
    }

    public long getHashCode() {
        return hashCode;
    }

    public Optional<String> getFilename() {
        if (isEmpty(filename)) {
            this.filename = cascContext.getFilename(hashCode)
                    .flatMap(EntryStore::cleanFilename)
                    .orElse(null);
        }

        return Optional.of(filename);
    }

    public Optional<String> getExtension() {
        return getFilename()
                .flatMap(EntryStore::getExtension);
    }

    public Optional<String> getFilePath() {
        return getFilename()
                .flatMap(EntryStore::getPath);
    }

    public boolean isInCasc() {
        return inCasc;
    }

    public boolean isCascReferenceValid() {
        return cascReferenceValid;
    }

    public Optional<FileHeader> getFileHeader() {
        if (fileHeader == null && isCascReferenceValid()) {
            fileHeader = FileHeader.parse(() -> getCascContext().getFileDataReader(hashCode));
        }
        return Optional.ofNullable(fileHeader);
    }

    public List<ContentChecksum> getContentChecksums() {
        List<ContentChecksum> checksums;
        if (contentChecksums == null && isCascReferenceValid()) {
            contentChecksums = getCascContext().getContentChecksums(hashCode);
            checksums = contentChecksums;
        } else if (contentChecksums == null) {
            checksums = Collections.emptyList();
        } else {
            checksums = contentChecksums;
        }
        return checksums;
    }

    public int getChecksumCount() {
        return getContentChecksums().size();
    }

    public List<FileKey> getFileKeys() {
        List<FileKey> keys;
        if (fileKeys == null && isCascReferenceValid()) {
            fileKeys = getContentChecksums().stream()
                    .map(getCascContext()::getFileKey)
                    .map(Optional::get)
                    .collect(Collectors.toList());
            keys = fileKeys;
        } else if (contentChecksums == null) {
            keys = Collections.emptyList();
        } else {
            keys = fileKeys;
        }
        return keys;
    }

    public int getFileKeyCount() {
        return getFileKeys().size();
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
