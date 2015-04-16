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

package nl.salp.warcraft4j.model.lazy;

import nl.salp.warcraft4j.model.Entity;

/**
 * Resolver for loading an instance of a referable entity.
 *
 * @author Barre Dijkstra
 */
public interface EntityResolver {
    /**
     * Resolve an instance with a specific id of a referable entity.
     *
     * @param entityType The class of the referable entity to resolve.
     * @param id         The id.
     * @param <T>        The type of the entity.
     *
     * @return The instance.
     *
     * @throws EntityNotFoundException When the entity could not be resolved.
     */
    <T extends Entity> T resolve(Class<T> entityType, int id) throws EntityNotFoundException;
}
