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
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import nl.salp.warcraft4j.clientdata.dbc.mapper.DbcFileMapping;

import static java.lang.String.format;

/**
 * TODO Document!
 *
 * @author Barre Dijkstra
 */
public class JsonMappingParser {
    /** The mapping files. */
    private final String[] mappingFiles;
    /** The Jackson object mapper to use. */
    private final ObjectMapper mapper;

    public JsonMappingParser(String[] mappingFiles) {
        this.mappingFiles = mappingFiles;
        mapper = createObjectMapper();
    }

    public JsonMappingParser(String basePath, String[] mappingFiles) {
        this.mappingFiles = getFilePaths(basePath, mappingFiles);
        mapper = createObjectMapper();
    }

    public JsonMappingParser(Supplier<JsonDbcMappingsList> mappingsSupplier) {
        JsonDbcMappingsList mappings = mappingsSupplier.get();
        this.mappingFiles = getFilePaths(mappings.getMappingFolder(), mappings.getMappings());
        mapper = createObjectMapper();
    }

    public String[] getMappingFiles() {
        return mappingFiles;
    }

    public DbcFileMapping[] parseMappingFiles() {
        return Stream.of(mappingFiles).map(this::parseMappingFile).toArray(JsonDbcFileMapping[]::new);
    }

    private JsonDbcFileMapping parseMappingFile(String file) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(JsonMappingParser.class.getResourceAsStream(file)))) {
            return mapper.readValue(reader, JsonDbcFileMapping.class);
        } catch (IOException e) {
            // TODO Replace with parsing exception
            throw new RuntimeException(format("Unable to parse json dbc mappings file %s", file), e);
        }
    }

    private static String[] getFilePaths(String basePath, String[] files) {
        return Stream.of(files).map(file -> getFilePath(basePath, file)).toArray(String[]::new);
    }

    private static String getFilePath(String basePath, String path) {
        String filePath;
        if (basePath.endsWith("/")) {
            filePath = basePath + path;
        } else {
            filePath = basePath + "/" + path;
        }
        return filePath;
    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        return mapper;
    }

    public static void main(String... args) {
        JsonMappingParser parser = new JsonMappingParser(() -> new JsonDbcMappingsListParser().parse());
        Stream.of(parser.parseMappingFiles()).forEach(System.out::println);
    }
}
