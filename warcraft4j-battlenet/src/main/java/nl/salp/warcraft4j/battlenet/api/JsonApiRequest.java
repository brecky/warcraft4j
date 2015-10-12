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

/**
 * Base class for JSON based service methods.
 */
public abstract class JsonApiRequest<T> implements BattlenetApiRequest<T> {
    /** The DTO class to parse the JSON to. */
    private final Class<T> dtoClass;

    /**
     * Create a new JsonServiceMethod instance.
     *
     * @param dtoClass The DTO class to parse the JSON to.
     *
     * @throws IllegalArgumentException When an invalid DTO class is provided.
     */
    protected JsonApiRequest(Class<T> dtoClass) throws IllegalArgumentException {
        if (dtoClass == null) {
            throw new IllegalArgumentException("Can't create a JsonApiRequest for a null DTO class.");
        }
        this.dtoClass = dtoClass;
    }

    @Override
    public Class<T> getResultType() {
        return dtoClass;
    }
}
