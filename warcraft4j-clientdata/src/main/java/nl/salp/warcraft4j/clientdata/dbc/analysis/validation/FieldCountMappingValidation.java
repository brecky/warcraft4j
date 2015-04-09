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
package nl.salp.warcraft4j.clientdata.dbc.analysis.validation;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;

import static java.lang.String.format;

/**
 * Validate that all the fields in the data file are mapped.
 *
 * @param <T> The {@link DbcEntry} mapping type implementation.
 *
 * @author Barre Dijkstra
 */
public class FieldCountMappingValidation<T extends DbcEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldCountMappingValidation.class);
    /** The parsed DBC/DB2 file. */
    private final DbcFile file;
    /** The mapping type. */
    private final Class<T> type;

    /**
     * Create a new instance.
     *
     * @param file The parsed DBC/DB2 file.
     * @param type The mapping type.
     */
    public FieldCountMappingValidation(DbcFile file, Class<T> type) {
        this.file = file;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        int fileFields = file.getHeader().getEntryFieldCount();
        int mappedFields = getMappedFieldCount(getMappedFields(type, false));
        boolean valid = fileFields == mappedFields;
        if (valid) {
            LOGGER.debug(format("Successfully mapped %s fields from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileFields, mappedFields));
        } else {
            LOGGER.warn(format("%s has an invalid number of fields mapped from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileFields, mappedFields));
        }
        return valid;
    }


    private int getMappedFieldCount(Collection<Field> fields) {
        int mappedFields = 0;
        for (Field field : fields) {
            DbcField f = field.getAnnotation(DbcField.class);
            if (f != null) {
                mappedFields += f.numberOfEntries();
            }
        }
        return mappedFields;
    }
}
