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
package nl.salp.warcraft4j.clientdata.dbc.util;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.parser.FullDbcFileParser;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcMapping;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

import static java.lang.String.format;

/**
 * Utility for finding client database entry mappings for DBC/DB2 files that are incomplete.
 *
 * @author Barre Dijkstra
 */
public class IncompleteDbcMappingFinder {
    /**
     * Find all client database mappings that have incomplete mappings (e.g. having a different number of mapped fields or unknown fields).
     *
     * @param dbcDirectory The directory holding the DBC and DB2 files.
     *
     * @return The incomplete mapping entries, indexed by the parsed client database file.
     *
     * @throws IOException When reading the files failed.
     */
    public Map<DbcFile, Class<? extends DbcEntry>> findAllIncompleteMappings(String dbcDirectory) throws IOException {
        final SortedMap<DbcFile, Class<? extends DbcEntry>> incompleteMappings = new TreeMap<>(new Comparator<DbcFile>() {
            @Override
            public int compare(DbcFile o1, DbcFile o2) {
                return o1.getFilename().compareToIgnoreCase(o2.getFilename());
            }
        });
        for (Class<? extends DbcEntry> type : getAllClientDatabaseEntryMappings()) {
            DbcFile file = parse(type, dbcDirectory);
            if (getFieldCount(file) != getFieldCount(type)) {
                incompleteMappings.put(file, type);
            }
            if (getUnknownFieldCount(type) > 0) {
                incompleteMappings.put(file, type);
            }
        }
        return incompleteMappings;
    }

    /**
     * Parse the entry mapping.
     *
     * @param type         The type to parse to.
     * @param dbcDirectory The directory containing the DBC/DB2 files.
     *
     * @return The parsed file.
     *
     * @throws IOException When parsing failed.
     */
    private DbcFile parse(Class<? extends DbcEntry> type, String dbcDirectory) throws IOException {
        FullDbcFileParser parser = new FullDbcFileParser(dbcDirectory);
        return parser.parseMetaData(type);
    }

    /**
     * Get the number of fields in the type that are marked as having an unknown meaning.
     *
     * @param type The type to parse.
     *
     * @return The number of fields that have an unknown meaning.
     */
    private static int getUnknownFieldCount(Class<? extends DbcEntry> type) {
        int fieldCount = 0;
        if (type != null && type.isAnnotationPresent(DbcMapping.class)) {
            for (Field field : type.getDeclaredFields()) {
                DbcField f = field.getAnnotation(DbcField.class);
                if (f != null && !f.knownMeaning()) {
                    fieldCount += f.numberOfEntries();
                }
            }
        }
        return fieldCount;
    }

    /**
     * Get the number of fields in the client database file.
     *
     * @param file The file.
     *
     * @return The number of fields.
     */
    private static int getFieldCount(DbcFile file) {
        int fieldCount = 0;
        if (file != null && file.getHeader() != null) {
            fieldCount = file.getHeader().getEntryFieldCount();
        }
        return fieldCount;
    }

    /**
     * Get the number of mapped fields for the entry.
     *
     * @param type The entry type.
     *
     * @return The number of mapped fields.
     */
    private static int getFieldCount(Class<? extends DbcEntry> type) {
        int fieldCount = 0;
        if (type != null && type.isAnnotationPresent(DbcMapping.class)) {
            for (Field field : type.getDeclaredFields()) {
                DbcField f = field.getAnnotation(DbcField.class);
                if (f != null && !f.padding()) {
                    fieldCount += f.numberOfEntries();
                }
            }
        }
        return fieldCount;
    }

    /**
     * Get all client database entry classes that have mapping information.
     *
     * @return The mappings.
     *
     * @throws IOException When reading the entries failed.
     */
    private Collection<Class<? extends DbcEntry>> getAllClientDatabaseEntryMappings() throws IOException {
        DbcClasspathMappingScanner scanner = new DbcClasspathMappingScanner(this);
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
    private String[] getAllClientDatabaseFiles(String dbcDirectory) throws IOException {
        File dbcDir = new File(dbcDirectory);
        return dbcDir.list((dir, name) -> name.endsWith(".db2") || name.endsWith(".dbc"));
    }

    /**
     * Program entry point.
     *
     * @param args The command line arguments.
     *
     * @throws Exception When determining mappnig completeness failed.
     */
    public static void main(String... args) throws Exception {
        if (args.length == 1) {
            IncompleteDbcMappingFinder finder = new IncompleteDbcMappingFinder();
            print(finder.findAllIncompleteMappings(args[0]));
        } else {
            printHelp();
        }

    }

    /**
     * Print the details for incomplete mappings to the {@code System.out}.
     *
     * @param incompleteMappings The incomplete mappings.
     */
    private static void print(Map<DbcFile, Class<? extends DbcEntry>> incompleteMappings) {
        if (incompleteMappings.isEmpty()) {
            System.out.println(format("All mapped files are complete"));
        } else {
            System.out.println(format("Incomplete mappings for the following %d files", incompleteMappings.size()));
            for (Map.Entry<DbcFile, Class<? extends DbcEntry>> m : incompleteMappings.entrySet()) {
                String fileName = m.getKey().getFilename();
                int fileFields = getFieldCount(m.getKey());
                int entrySize = m.getKey().getHeader().getEntrySize();
                String typeName = m.getValue().getName();
                int typeFields = getFieldCount(m.getValue());
                int typeUnknownFields = getUnknownFieldCount(m.getValue());
                System.out.println(format("    - %s has %d fields (%d bytes per entry) with mapping type %s having %d fields with %d being unknown.", fileName, fileFields, entrySize, typeName, typeFields, typeUnknownFields));
            }
        }
    }

    /**
     * Print usage/help message.
     */
    private static void printHelp() {
        System.out.println(format("Didn't receive all required parameters."));
        System.out.println(format("Usage:"));
        System.out.println(format("    IncompleteDbcMappingFinder <dbc directory>"));
        System.out.println(format(""));
        System.out.println(format("Parameters:"));
        System.out.println(format("    <dbc directory> The directory where the DBC and DB2 files are located"));
    }
}
