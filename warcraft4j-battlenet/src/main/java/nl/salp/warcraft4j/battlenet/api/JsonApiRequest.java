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

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.JsonBindingException;
import com.owlike.genson.stream.JsonStreamException;

/**
 * Base class for JSON based service methods.
 */
public abstract class JsonApiRequest<T> implements BattlenetApiRequest<T> {
    /** The initialised Genson instance for the implementations to use. */
    private static Genson gensonInstance = new GensonBuilder().failOnMissingProperty(true).create();
    /** The DTO class to parse the JSON to. */
    private final Class<T> dtoClass;

    /**
     * Create a new JsonServiceMethod instance.
     *
     * @param dtoClass The DTO class to parse the JSON to.
     */
    protected JsonApiRequest(Class<T> dtoClass) {
        this.dtoClass = dtoClass;
    }

    @Override
    public T parse(String json) throws BattlenetApiParsingException {
        try {
            return gensonInstance.deserialize(json, dtoClass);
        } catch (JsonStreamException | JsonBindingException e) {
            throw new BattlenetApiParsingException("Error parsing JSON", e);
        }
    }
}
