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

package nl.salp.warcraft4j.files.clientdatabase.util;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.util.ref.EntryTypeReferenceInfo;
import nl.salp.warcraft4j.files.clientdatabase.util.ref.FieldReference;
import nl.salp.warcraft4j.files.clientdatabase.util.ref.FieldReferenceInfo;

import java.util.*;

import static java.lang.String.format;

/**
 * Utility to find and score all potential references between entries.
 *
 * @author Barre Dijkstra
 */
public class EntryReferenceFinder {

    public Map<Class<? extends ClientDatabaseEntry>, EntryTypeReferenceInfo> calculateReferences(String dbcDirectory) throws Exception {
        ClientDatabase clientDatabase = new ClientDatabase();
        clientDatabase.addFromClasspath(dbcDirectory);

        Map<Class<? extends ClientDatabaseEntry>, EntryTypeReferenceInfo> references = new HashMap<>();

        for (Class<? extends ClientDatabaseEntry> type : clientDatabase.getRegisteredTypes()) {
            EntryTypeReferenceInfo typeRef = new EntryTypeReferenceInfo(type);
            typeRef.scoreFieldReferences(clientDatabase);
            if (!typeRef.getFields().isEmpty()) {
                references.put(type, typeRef);
            }
        }
        return references;
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
            EntryReferenceFinder finder = new EntryReferenceFinder();
            print(finder.calculateReferences(args[0]));
        } else {
            printHelp();
        }

    }

    /**
     * Print the details for scored reference to the {@code System.out}.
     *
     * @param references The scored references indexed by their class.
     */
    private static void print(Map<Class<? extends ClientDatabaseEntry>, EntryTypeReferenceInfo> references) {
        if (references.isEmpty()) {
            System.out.println(format("No references found."));
        } else {
            System.out.println(format("Calculated references for %d files", references.size()));
            for (Map.Entry<Class<? extends ClientDatabaseEntry>, EntryTypeReferenceInfo> r : references.entrySet()) {
                Class<? extends ClientDatabaseEntry> type = r.getKey();
                EntryTypeReferenceInfo typeRef = r.getValue();
                System.out.println(format("=[ %s (%d fields) ]=", type.getName(), typeRef.getFields().size()));
                for (FieldReferenceInfo fieldRef : typeRef.getFields()) {
                    System.out.println(format("    [%s.%s: %d entries, %d unique entries (%.2f%%)]",
                            type.getName(),
                            fieldRef.getField().getName(),
                            fieldRef.getEntryCount(),
                            fieldRef.getUniqueValueCount(),
                            (fieldRef.getEntriesWithValuePercentage() * 100)
                    ));
                    for (FieldReference ref : filterTopReferences(fieldRef, typeRef, 5)) {
                        print(ref);
                    }
                }
            }
        }
    }

    private static void print(FieldReference ref) {
        if (ref != null) {
            System.out.println(format("        -> [%s] %d entries (%.2f%% referenced), %d hits (%.2f%%), %d misses (%.2f%%), %d unique hits",
                    ref.getTargetType().getName(),
                    ref.getTargetEntries(),
                    (ref.getReferencedEntryPercentage() * 100),
                    ref.getTargetHitsTotal(),
                    (ref.getHitPercentage() * 100),
                    ref.getTargetMisses(),
                    (ref.getMissPercentage() * 100),
                    ref.getTargetHitsUnique()
            ));
        }
    }

    /**
     * Filter the top references.
     *
     * @param field The field that is referencing the type.
     * @param type  The type the field is on.
     * @param top   The number of top references to filter.
     *
     * @return The referenced references.
     */
    public static Collection<FieldReference> filterTopReferences(FieldReferenceInfo field, EntryTypeReferenceInfo type, int top) {
        List<FieldReference> references = new ArrayList<>(type.getReferences(field));
        references.sort(new ReferenceProbabilityComparator());
        return references.subList(0, Math.min(references.size(), top));
    }

    /**
     * Comparator that sorts references on reference probability.
     * <p/>
     * Sorting priority: {@code miss % &gt; referenced entry % &gt; hit %}.
     */
    public static class ReferenceProbabilityComparator implements Comparator<FieldReference> {

        @Override
        public int compare(FieldReference r1, FieldReference r2) {
            int cmp;
            if (r1 == null && r2 == null) {
                cmp = 0;
            } else if (r1 == null) {
                cmp = -1;
            } else if (r2 == null) {
                cmp = 1;
            } else if (r1.getMissPercentage() > r2.getMissPercentage()) {
                cmp = 1;
            } else if (r1.getMissPercentage() < r2.getMissPercentage()) {
                cmp = -1;
            } else if (r1.getReferencedEntryPercentage() > r2.getReferencedEntryPercentage()) {
                cmp = 1;
            } else if (r1.getReferencedEntryPercentage() < r2.getReferencedEntryPercentage()) {
                cmp = -1;
            } else if (r1.getHitPercentage() > r2.getHitPercentage()) {
                cmp = 1;
            } else if (r1.getHitPercentage() < r2.getHitPercentage()) {
                cmp = -1;
            } else {
                cmp = 0;
            }

            return cmp;
        }

    }

    /**
     * Print usage/help message.
     */
    private static void printHelp() {
        System.out.println(format("Didn't receive all required parameters."));
        System.out.println(format("Usage:"));
        System.out.println(format("    EntryReferenceFinder <dbc directory>"));
        System.out.println(format(""));
        System.out.println(format("Parameters:"));
        System.out.println(format("    <dbc directory> The directory where the DBC and DB2 files are located"));
    }
}
