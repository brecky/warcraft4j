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
package nl.salp.warcraft4j.casc.cdn.local;

import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.cdn.CascIndexEntry;

/**
 * {@link CascIndexEntry} implementation for a local CDN CASC.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.cdn.CascIndexEntry
 */
public class LocalIndexEntry extends CascIndexEntry {
    /** The high bits of the index file information. */
    private final short indexInfoHigh;
    /** The low bits of the index file information. */
    private final long indexInfoLow;

    /**
     * Create a new instance.
     *
     * @param fileKey       The file key.
     * @param indexInfoHigh The high bits of the index file information.
     * @param indexInfoLow  The low bits of the index file information.
     * @param fileSize      The size of the referenced file.
     */
    public LocalIndexEntry(FileKey fileKey, short indexInfoHigh, long indexInfoLow, long fileSize) {
        super(fileKey, getFileNumber(indexInfoHigh, indexInfoLow), getDataFileOffset(indexInfoHigh, indexInfoLow), fileSize);
        this.indexInfoHigh = indexInfoHigh;
        this.indexInfoLow = indexInfoLow;
    }

    /**
     * Get the high bits of the index file information.
     *
     * @return The high bits of the index file information.
     */
    public short getIndexInfoHigh() {
        return indexInfoHigh;
    }

    /**
     * Get the low bits of the index file information.
     *
     * @return The low bits of the index file information.
     */
    public long getIndexInfoLow() {
        return indexInfoLow;
    }

    /**
     * Get the file number.
     *
     * @param indexInfoHigh The high bits of the index file information.
     * @param indexInfoLow  The low bits of the index file information.
     *
     * @return The file number for the index entry.
     */
    private static int getFileNumber(short indexInfoHigh, long indexInfoLow) {
        return ((byte) (indexInfoHigh << 2) | (((int) indexInfoLow & 0xC0000000) >>> 30));
    }

    /**
     * Get the data file offset.
     *
     * @param indexInfoHigh The high bits of the index file information.
     * @param indexInfoLow  The low bits of the index file information.
     *
     * @return The data file offset.
     */
    private static int getDataFileOffset(short indexInfoHigh, long indexInfoLow) {
        return (int) (indexInfoLow & 0x3FFFFFFF);
    }
}
