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
package nl.salp.warcraft4j.dev.dbc.validation;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * Validate that the all entries of the DBC mapping entry are mapped and parsed.
 *
 * @param <T> The {@link DbcEntry} mapping type implementation.
 *
 * @author Barre Dijkstra
 */
public class EntryCountMappingValidation<T extends DbcEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryCountMappingValidation.class);
    /** The parsed DBC/DB2 file. */
    private final DbcFile file;
    /** The mapping type. */
    private final Class<T> type;
    /** The parsed instances of the mapping type. */
    private final Collection<T> instances;

    /**
     * Create a new validation instance.
     *
     * @param file      The parsed DBC/DB2 file.
     * @param type      The mapping type.
     * @param instances The parsed instances of the mapping type.
     */
    public EntryCountMappingValidation(DbcFile file, Class<T> type, Collection<T> instances) {
        this.file = file;
        this.type = type;
        this.instances = instances;
    }

    @Override
    public boolean isValid() {
        int fileEntries = file.getHeader().getEntryCount();
        int parsedEntries = instances.size();
        boolean valid = fileEntries == parsedEntries;
        if (valid) {
            LOGGER.debug("Successfully parsed {} instances from {}: [expected: {}, actual: {}]", type.getName(), file.getFilename(), fileEntries, parsedEntries);
        } else {
            LOGGER.warn("{} has an invalid number of parsed instances from {}: [expected: {}, actual: {}]", type.getName(), file.getFilename(), fileEntries, parsedEntries);
        }
        return valid;
    }
}
