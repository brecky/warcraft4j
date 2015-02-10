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

package nl.salp.warcraft4j.dbc;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcParser {
    public <T> List<T> parse(File file, Class<T> entryClass) {
        validateFile(file);
        validateEntryTypeForFile(entryClass, file);

        List<T> entries = new ArrayList<>();


        return entries;
    }

    private <T> T parseEntry(ByteBuffer data, DbcHeader header, Class<T> entryClass) throws DbcParserException {
        T entry = null;
        try {

            entry = entryClass.newInstance();
            for (Field f : entryClass.getDeclaredFields()) {
                DbcField meta = f.getAnnotation(DbcField.class);
                if (meta != null) {
                }
            }

        } catch (InstantiationException e) {
            throw new DbcParserException(e);
        } catch (IllegalAccessException e) {
            throw new DbcParserException(e);
        }
        return entry;
    }


    private void validateFile(File file) throws DbcParserException {
        if (file == null || !file.exists()) {
            throw new DbcParserException("Unable to parse non-existing dbc file " + file);
        }
        if (!file.isFile() || !file.canRead()) {
            throw new DbcParserException("Unable to read dbc file " + file.getName());
        }
    }

    private void validateEntryTypeForFile(Class<?> entryClass, File file) throws DbcParserException {
        if (entryClass == null) {
            throw new IllegalArgumentException("Unable to parse dbc file " + file.getName() + " to a null object.");
        }
        if (entryClass.getAnnotation(Dbc.class) == null) {
            throw new IllegalArgumentException("Can't parse dbc file " + file.getName() + " to " + entryClass.getName() + " since it's not a DBC model file.");
        }
    }


}
