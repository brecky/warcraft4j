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
package nl.salp.warcraft4j.clientdata.casc.file;

import nl.salp.warcraft4j.clientdata.casc.CascConfig;
import nl.salp.warcraft4j.clientdata.casc.CascFileReader;
import nl.salp.warcraft4j.clientdata.util.hash.Hashes;
import org.apache.commons.io.IOUtils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FileCascFileReader implements CascFileReader {
    @Override
    public String getCascFileName(String fileName) {
        return Hashes.LOOKUP3_32BIT_LE.hashHexString(fileName);
    }

    @Override
    public String getRelativeDirectory(String fileNameHash) {
        return fileNameHash.substring(0, 2) + File.separator + fileNameHash.substring(2, 4);
    }

    @Override
    public byte[] read(String type, String cascFileName, CascConfig cascConfig) throws IOException {
        File directory = new File(cascConfig.getInstallationDirectory(), "Data");
        directory = new File(directory, type);
        directory = new File(directory, getRelativeDirectory(cascFileName));
        File file = new File(directory, cascFileName);
        byte[] data;
        try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
            data = IOUtils.toByteArray(in);
        }
        return data;
    }
}
