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

package nl.salp.warcraft4j.clientdata.dbc.service;

import java.util.Collection;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public interface DbcModelService {
    /**
     * Get all available instances for the given class.
     *
     * @param instanceClass The class to get the instances for.
     * @param <T>           The type of the class.
     *
     * @return The available instances.
     *
     * @throws DbcMappingException   When the data couldn't be mapped to the instances.
     * @throws DbcRetrievalException When the data could not be retrieved.
     */
    <T> Collection<T> getAllInstances(Class<T> instanceClass) throws DbcMappingException, DbcRetrievalException;

    /**
     * Get a single instance for the given class.
     *
     * @param instanceClass The class to get the instances for.
     * @param id            The id of the instance to get.
     * @param <T>           The type of the class.
     *
     * @return The instance.
     *
     * @throws DbcMappingException   When the data couldn't be mapped to the instances.
     * @throws DbcRetrievalException When the data could not be retrieved.
     */
    <T> T getInstance(Class<T> instanceClass, int id) throws DbcMappingException, DbcRetrievalException;
}
