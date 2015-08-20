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
package nl.salp.warcraft4j.data.casc;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CascIndexEntry implements IndexEntry {
    private final FileKey fileKey;
    private final int fileNumber;
    private final int dataFileOffset;
    private final long fileSize;
    private final int hash;

    public CascIndexEntry(FileKey fileKey, int fileNumber, int dataFileOffset, long fileSize) {
        this.fileKey = fileKey;
        this.fileSize = fileSize;
        this.fileNumber = fileNumber;
        this.dataFileOffset = dataFileOffset;
        this.hash = fileKey.hashCode();
    }

    @Override
    public FileKey getFileKey() {
        return fileKey;
    }

    @Override
    public long getFileSize() {
        return fileSize;
    }

    @Override
    public int getFileNumber() {
        return fileNumber;
    }

    @Override
    public int getDataFileOffset() {
        return dataFileOffset;
    }

    @Override
    public int hashCode() {
        return hash;
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