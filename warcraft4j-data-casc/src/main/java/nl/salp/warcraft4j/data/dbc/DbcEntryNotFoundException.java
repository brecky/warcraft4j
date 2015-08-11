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

package nl.salp.warcraft4j.data.dbc;

import nl.salp.warcraft4j.data.dbc.mapping.DbcMapping;

import static java.lang.String.format;

/**
 * Exception for when a requested dbc entry could not be found.
 *
 * @author Barre Dijkstra
 */
public class DbcEntryNotFoundException extends RuntimeException {
    /** The exception mesasge to use. */
    private static final String MESSAGE = "Could not find entry %d in dbc file %s with mapping type %s";
    /** The id of the entry that was not found. */
    private final int id;
    /** The dbc entry mapping type of the entry. */
    private final Class<? extends DbcEntry> mappingType;
    /** The dbc file that was used to parse the entry from. */
    private final String dbcFile;

    /**
     * Create a new exception instance.
     *
     * @param id          The id of the entry that was not found.
     * @param mappingType The dbc entry mapping type of the entry.
     */
    public DbcEntryNotFoundException(int id, Class<? extends DbcEntry> mappingType) {
        super(getExceptionMessage(id, mappingType));
        this.id = id;
        this.mappingType = mappingType;
        if (mappingType == null || !mappingType.isAnnotationPresent(DbcMapping.class)) {
            dbcFile = null;
        } else {
            dbcFile = mappingType.getAnnotation(DbcMapping.class).file();
        }
    }


    /**
     * Get the id of the entry that was not found.
     *
     * @return The entry id.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the dbc entry mapping type of the entry.
     *
     * @return The dbc entry mapping type.
     */
    public Class<? extends DbcEntry> getMappingType() {
        return mappingType;
    }

    /**
     * Get the dbc file that was used to parse the entry from.
     *
     * @return The dbc file.
     */
    public String getDbcFile() {
        return dbcFile;
    }

    /**
     * Create the exception message.
     *
     * @param id          The id of the entry that was not found.
     * @param mappingType The dbc entry mapping type.
     *
     * @return The exception message.
     */
    private static String getExceptionMessage(int id, Class<? extends DbcEntry> mappingType) {
        String file = (mappingType != null && mappingType.isAnnotationPresent(DbcMapping.class)) ? mappingType.getAnnotation(DbcMapping.class).file() : null;
        return format(MESSAGE, id, mappingType, file);
    }
}
