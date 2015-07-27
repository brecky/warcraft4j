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
package nl.salp.warcraft4j.clientdata.casc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class EncodingEntry {
    private long fileSize; // BE
    private Checksum contentChecksum; // 16 Byte content MD5
    private List<Checksum> fileChecksums; // 16 Byte

    public EncodingEntry(long fileSize, Checksum contentChecksum, List<Checksum> fileChecksums) {
        this.fileSize = fileSize;
        this.contentChecksum = Optional.ofNullable(contentChecksum).orElseThrow(() -> new IllegalArgumentException("Can't create an encoding file entry with no content checksum"));
        this.fileChecksums = Optional.ofNullable(fileChecksums)
                .filter(f -> !f.isEmpty())
                .orElseThrow(() -> new IllegalArgumentException("Can't create an encoding file entry with no file checksums"));
    }

    public long getFileSize() {
        return fileSize;
    }

    public Checksum getContentChecksum() {
        return contentChecksum;
    }

    public Checksum getFileChecksum() {
        return fileChecksums.get(0);
    }

    public List<Checksum> getFileChecksums() {
        return fileChecksums;
    }

    public Checksum getFileKey() {
        Checksum cs = getFileChecksum();
        if (cs.length() > 9) {
            cs = cs.trim(9);
        }
        return cs;
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