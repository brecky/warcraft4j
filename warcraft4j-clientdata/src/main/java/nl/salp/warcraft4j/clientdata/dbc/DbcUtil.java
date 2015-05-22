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

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcClasspathMappingScanner;
import nl.salp.warcraft4j.clientdata.dbc.mapping.DbcMapping;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;

/**
 * Client database related utility methods.
 *
 * @author Barre Dijkstra
 */
public final class DbcUtil {
    private DbcUtil() {
    }

    /**
     * Get the {@link DbcType} for the given {@link DbcEntry} class.
     *
     * @param mappingType The class to get the entry type from.
     *
     * @return The entry type or {@code null} if it could not be determined.
     */
    public static <T extends DbcEntry> DbcType getEntryType(Class<T> mappingType) {
        DbcType type = null;
        if (mappingType != null) {
            for (Field f : mappingType.getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers())) {
                    try {
                        boolean access = f.isAccessible();
                        f.setAccessible(true);
                        type = (DbcType) f.get(null);
                        f.setAccessible(access);
                        break;
                    } catch (Exception e) {
                        // Ignore.
                    }
                }
            }
        }
        return type;
    }

    /**
     * Get the DBC/DB2 file the entry is mapped to.
     *
     * @param mapping The mapping.
     *
     * @return The file or {@code null} when no mapped file was found.
     */
    public static <T extends DbcEntry> String getMappedFile(Class<T> mapping) {
        String file = null;
        if (mapping.isAnnotationPresent(DbcMapping.class)) {
            file = mapping.getAnnotation(DbcMapping.class).file();
        }
        return file;
    }

    /**
     * Find all entry mappings on the classpath.
     *
     * @param classLoader The class loader to use (scan will include the parent class loaders).
     *
     * @return The entry mappings on the classpath.
     */
    public static Collection<Class<? extends DbcEntry>> findMappingsOnClasspath(ClassLoader classLoader) {
        DbcClasspathMappingScanner scanner = new DbcClasspathMappingScanner(classLoader);
        return scanner.scan();
    }

    /**
     * Get the names of all DBC and DB2 files in the given directory.
     *
     * @param dbcDirectory The directory holding the files.
     *
     * @return The names of all client database files.
     *
     * @throws IOException When reading failed.
     */
    public static String[] getAllClientDatabaseFiles(String dbcDirectory) throws IOException {
        File dbcDir = new File(dbcDirectory);
        return dbcDir.list((dir, name) -> name.endsWith(".db2") || name.endsWith(".dbc"));
    }
}
