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
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFileParser;
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;

/**
 * Utility for finding all DBC and DB2 files from a directory tree that have no ClientDatabaseEntry mapping associated.
 *
 * @author Barre Dijkstra
 */
public class MissingDbcMappingFinder {

    /**
     * Find all files with no corresponding mapping.
     *
     * @param dbcDirectory The directory holding the DBC and DB2 files.
     *
     * @return The files for which no mapping is present.
     *
     * @throws IOException When the files reading failed.
     */
    public Collection<DbcFile> findAllMissingMappings(String dbcDirectory) throws IOException {
        Set<String> dbcFiles = new HashSet<>(Arrays.asList(getAllClientDatabaseFiles(dbcDirectory)));
        Set<String> mappedFiles = new HashSet<>();
        for (Class<? extends DbcEntry> type : getAllClientDatabaseEntryMappings()) {
            DbcMapping f = type.getAnnotation(DbcMapping.class);
            if (f != null) {
                mappedFiles.add(f.file());
            }
        }
        dbcFiles.removeAll(mappedFiles);

        SortedSet<DbcFile> missingMappings = new TreeSet<>(new Comparator<DbcFile>() {

            @Override
            public int compare(DbcFile o1, DbcFile o2) {
                return o1.getFilename().compareToIgnoreCase(o2.getFilename());
            }
        });
        for (String mf : dbcFiles) {
            missingMappings.add(parse(mf, dbcDirectory));
        }

        return missingMappings;
    }

    /**
     * Parse a DBC/DB2 file.
     *
     * @param filename     The file name.
     * @param dbcDirectory The directory the file is in.
     *
     * @return The parsed file.
     *
     * @throws IOException When parsing failed.
     */
    private DbcFile parse(String filename, String dbcDirectory) throws IOException {
        DbcFileParser parser = new DbcFileParser();
        return parser.parseFile(filename, dbcDirectory);
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
        return dbcDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".db2") || name.endsWith(".dbc");
            }
        });
    }

    /**
     * Program entry point.
     *
     * @param args The command line arguments.
     *
     * @throws Exception When finding the missing mappings failed.
     */
    public static void main(String... args) throws Exception {
        if (args.length == 1) {
            MissingDbcMappingFinder finder = new MissingDbcMappingFinder();
            print(finder.findAllMissingMappings(args[0]));
        } else {
            printHelp();
        }
    }


    /**
     * Print out the missing mappings to the {@code System.out}.
     *
     * @param missingMappings The missing mappings.
     */
    private static void print(Collection<DbcFile> missingMappings) {
        if (missingMappings.isEmpty()) {
            System.out.println(format("All found DBC and DB2 files are mapped."));
        } else {
            System.out.println(format("Missing mappings for the following %d files", missingMappings.size()));
            for (DbcFile m : missingMappings) {
                System.out.println(format("    - %s (fields:%d, entrySize:%d stringBlock:%s, entries:%d)", m.getFilename(), m.getHeader().getEntryFieldCount(), m.getHeader().getEntrySize(), m.getStringTable().getNumberOfEntries() > 0, m.getHeader().getEntryCount()));
            }
        }
    }

    /**
     * Print out the help message.
     */
    private static void printHelp() {
        System.out.println(format("Didn't receive all required parameters."));
        System.out.println(format("Usage:"));
        System.out.println(format("    MissingDbcMappingFinder <dbc directory>"));
        System.out.println(format(""));
        System.out.println(format("Parameters:"));
        System.out.println(format("    <dbc directory> The directory where the DBC and DB2 files are located"));
    }

}
