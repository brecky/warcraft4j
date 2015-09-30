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

package nl.salp.warcraft4j.battlenet.api;

import nl.salp.warcraft4j.battlenet.BattlenetRegion;
import nl.salp.warcraft4j.battlenet.BattlenetLocale;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class BattlenetFileApi extends BattlenetApi {
    private Map<String, String> jsonFiles;

    public BattlenetFileApi(Map<String, String> jsonFiles) {
        this.jsonFiles = jsonFiles;
    }

    @Override
    protected <T> String execute(BattlenetRegion region, BattlenetLocale locale, BattlenetApiRequest<T> method) throws IOException {
        return getFileContents(method);
    }

    private <T> String getFileContents(BattlenetApiRequest<T> method) throws IOException {
        String fileName = jsonFiles.get(method.getRequestMethodBaseUri());
        StringBuilder result = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(BattlenetFileApi.class.getResourceAsStream(fileName)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
