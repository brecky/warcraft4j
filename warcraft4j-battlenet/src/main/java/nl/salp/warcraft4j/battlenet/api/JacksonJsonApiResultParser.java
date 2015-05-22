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

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class JacksonJsonApiResultParser implements JsonApiResultParser {
    /** The initialised Jackson object mapper instance for the implementations to use. */
    private ObjectMapper objectMapper;

    public JacksonJsonApiResultParser() {
        this(false);
    }

    public JacksonJsonApiResultParser(boolean failOnUnknownProperty) {
        objectMapper = new ObjectMapper();
        if (failOnUnknownProperty) {
            objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        }
    }

    @Override
    public <T> T parse(String json, Class<T> resultType) throws BattlenetApiParsingException {
        try {
            return objectMapper.readValue(json, resultType);
        } catch (JsonMappingException | JsonParseException e) {
            throw new BattlenetApiParsingException(format("Error parsing JSON result to %s", resultType.getName()), e);
        } catch (IOException e) {
            throw new BattlenetApiParsingException("Error reading JSON result", e);
        }
    }

    /**
     * Create the mapper to use.
     * <p/>
     * TODO Move to a different Guice managed class for easy switching of implementations/testing.
     *
     * @return The mapper.
     */
    private static ObjectMapper createMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        // TODO Add any needed mapping logic
        return mapper;
    }
}
