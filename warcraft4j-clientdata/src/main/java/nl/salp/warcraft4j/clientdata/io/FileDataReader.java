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

package nl.salp.warcraft4j.clientdata.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * {@WowReader} implementation for sequentially reading files.
 *
 * @author Barre Dijkstra
 */
public class FileDataReader extends InputStreamDataReader {
    /** The path of the file that the reader is created for. */
    private final String filePath;
    /** The canonical path of the file that the reader is created for. */
    private final String canonicalFilePath;

    /**
     * Create a new FileDataReader instance for the given file.
     *
     * @param file The file.
     *
     * @throws IOException When the file could not be read.
     */
    public FileDataReader(File file) throws IOException {
        super(new BufferedInputStream(new FileInputStream(file)));
        this.filePath = file.getPath();
        this.canonicalFilePath = file.getCanonicalPath();
    }

    /**
     * Get the path of the file that the reader is created for.
     *
     * @return The file path.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Get the canonical path of the file that the reader is created for.
     *
     * @return The canonical file path.
     */
    public String getCanonicalFilePath() {
        return canonicalFilePath;
    }
}