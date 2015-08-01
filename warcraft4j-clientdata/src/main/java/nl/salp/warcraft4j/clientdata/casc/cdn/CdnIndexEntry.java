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
package nl.salp.warcraft4j.clientdata.casc.cdn;

import nl.salp.warcraft4j.clientdata.casc.Checksum;
import nl.salp.warcraft4j.clientdata.casc.IndexEntry;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CdnIndexEntry implements IndexEntry {
    private final Checksum fileKey;
    private final long fileSize;
    private final int fileNumber;
    private final int fileOffset;

    public CdnIndexEntry(Checksum fileKey, int fileNumber, int fileOffset, long fileSize) {
        this.fileKey = fileKey;
        this.fileNumber = fileNumber;
        this.fileOffset = fileOffset;
        this.fileSize = fileSize;
    }

    @Override
    public Checksum getFileKey() {
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
        return fileOffset;
    }
}
