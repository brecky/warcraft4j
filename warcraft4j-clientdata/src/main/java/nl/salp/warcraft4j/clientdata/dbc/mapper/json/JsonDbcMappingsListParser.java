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
package nl.salp.warcraft4j.clientdata.dbc.mapper.json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.lang.String.format;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
class JsonDbcMappingsListParser {
    /** The default mappings file. */
    private static final String DEFAULT_FILE = "/mappings.dbc.json";
    /** The filename of the mapping file to use. */
    private final String filename;
    /** The Jackson object mapper to use. */
    private final ObjectMapper mapper;

    /**
     * Create a new JsonDbcMappingsListParser instance from the default file.
     */
    public JsonDbcMappingsListParser() {
        this(DEFAULT_FILE);
    }

    /**
     * Create a new JsonDbcMappingsListParser instance from the provided file.
     */
    public JsonDbcMappingsListParser(String mappingFile) {
        this.filename = mappingFile;
        this.mapper = createObjectMapper();
    }

    /**
     * Parse the mappings.
     */
    public JsonDbcMappingsList parse() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(JsonDbcMappingsListParser.class.getResourceAsStream(filename)))) {
            return mapper.readValue(reader, JsonDbcMappingsList.class);
        } catch (IOException e) {
            // TODO Replace with parsing exception
            throw new RuntimeException(format("Unable to parse json dbc mappings file %s", filename), e);
        }
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }
}
