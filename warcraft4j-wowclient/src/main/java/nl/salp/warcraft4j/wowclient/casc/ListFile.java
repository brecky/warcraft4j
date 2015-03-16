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

package nl.salp.warcraft4j.wowclient.casc;

import org.apache.commons.codec.digest.DigestUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class ListFile {
    private Map<String, String> fileHashes;

    public ListFile(String file) throws IOException {
        fileHashes = parseListFile(new File(file));
    }

    public ListFile(File file) throws IOException {
        fileHashes = parseListFile(file);
    }

    private Map<String, String> parseListFile(File file) throws IOException {
        if (file == null || !file.exists() || !file.isFile() || !file.canRead()) {
            throw new IllegalArgumentException(format("Unable to read the list file '%s'", file));
        }

        Map<String, String> files = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                files.put(line, DigestUtils.md5Hex(line));
            }
        }

        return files;
    }

    public Collection<String> getFileNames() {
        return fileHashes.keySet();
    }

    public String getHash(String fileName) {
        String hash = null;
        if (isNotEmpty(fileName) && fileHashes.containsKey(fileName)) {
            hash = fileHashes.get(fileName);
        }
        return hash;
    }

    public String getFile(String hash) {
        String file = null;
        if (isNotEmpty(hash)) {
            for (Map.Entry<String, String> e : fileHashes.entrySet()) {
                if (hash.equals(e.getValue())) {
                    file = e.getKey();
                    break;
                }
            }
        }
        return file;
    }
}
