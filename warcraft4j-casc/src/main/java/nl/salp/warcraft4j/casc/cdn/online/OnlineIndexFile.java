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
package nl.salp.warcraft4j.casc.cdn.online;

import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;

import java.util.Collection;
import java.util.Collections;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class OnlineIndexFile {
    private final int index;
    private final FileKey fileKey;
    private final Collection<IndexEntry> indexEntries;

    public OnlineIndexFile(int index, FileKey fileKey, Collection<IndexEntry> indexEntries) {
        this.index = index;
        this.fileKey = fileKey;
        this.indexEntries = indexEntries;
    }

    public int getIndex() {
        return index;
    }

    public FileKey getFileKey() {
        return fileKey;
    }

    public Collection<IndexEntry> getIndexEntries() {
        return Collections.unmodifiableCollection(indexEntries);
    }
}
