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

package nl.salp.warcraft4j.clientdata.dbc;

import java.util.Collection;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public interface DbcService {
    /**
     * Get the meta data for the provided DBC file.
     *
     * @param file The filename.
     *
     * @return The meta data.
     */
    DbcFile getMetaData(String file);

    /**
     * Get the meta data for the DBC file mapped by the mapping type.
     *
     * @param mappingType The DBC entry mapping type.
     *
     * @return The meta data.
     */
    DbcFile getMetaData(Class<? extends DbcEntry> mappingType);

    /**
     * Get all the entries of the provided DBC entry mapping type.
     *
     * @param mappingType The mapping type.
     * @param <T>         The entry implementation type.
     *
     * @return The entries for the type.
     */
    <T extends DbcEntry> Collection<T> getAllEntries(Class<T> mappingType);

    <T extends DbcEntry> T getEntryWithId(Class<T> mappingType, int id);

    /**
     * Get the
     * @param mappingType
     * @param index
     * @param <T>
     * @return
     */
    <T extends DbcEntry> T getEntryWithIndex(Class<T> mappingType, int index);


}
