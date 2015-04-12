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

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BuildConfigurationParser {

    public BuildConfiguration readFile(String fileNameHash, CascFileReader reader, CascConfig cascConfig) throws IOException {
        return parse(reader.read("config", fileNameHash, cascConfig));
        // TODO Use a generic file reader.
    }

    public BuildConfiguration parse(byte[] file) throws IOException {
        Map<String, String> values = parseValues(file);

        String[] encoding = values.get("encoding").split(" ");
        String[] encodingSizes = values.get("encoding-size").split(" ");
        long[] encodingSize = new long[encodingSizes.length];
        for (int i = 0; i < encodingSizes.length; i++) {
            encodingSize[i] = Long.parseLong(encodingSizes[i]);
        }
        long patchSize = Long.parseLong(values.get("patch-size"));
        return new BuildConfiguration(
                values.get("root"),
                values.get("download"),
                values.get("install"),
                encoding,
                encodingSize,
                values.get("build-name"),
                values.get("build-playbuild-installer"),
                values.get("build-product"),
                values.get("build-uid"),
                values.get("patch"),
                patchSize,
                values.get("patch-config")
        );
    }

    private Map<String, String> parseValues(byte[] file) throws IOException {
        Map<String, String> values = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(file)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] l = line.split("=");
                if (l.length == 2) {
                    String key = l[0].trim();
                    String value = l[1].trim();
                    values.put(key, value);
                }
            }
        }
        return values;
    }
}
