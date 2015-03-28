package nl.salp.warcraft4j.files.clientdatabase.util;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.parser.ClientDatabaseFile;
import nl.salp.warcraft4j.files.clientdatabase.parser.ClientDatabaseFileParser;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcFile;

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
    public Map<ClientDatabaseFile, Class<? extends ClientDatabaseEntry>> findAllIncompleteMappings(String dbcDirectory) throws IOException {
        final SortedMap<ClientDatabaseFile, Class<? extends ClientDatabaseEntry>> incompleteMappings = new TreeMap<>(new Comparator<ClientDatabaseFile>() {
            @Override
            public int compare(ClientDatabaseFile o1, ClientDatabaseFile o2) {
                return o1.getFilename().compareToIgnoreCase(o2.getFilename());
            }
        });
        for (Class<? extends ClientDatabaseEntry> type : getAllClientDatabaseEntryMappings()) {
            ClientDatabaseFile file = parse(type, dbcDirectory);
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
     * Find all files with no corresponding mapping.
     *
     * @param dbcDirectory The directory holding the DBC and DB2 files.
     *
     * @return The files for which no mapping is present.
     *
     * @throws IOException When the files reading failed.
     */
    public Collection<ClientDatabaseFile> findAllMissingMappings(String dbcDirectory) throws IOException {
        Set<String> dbcFiles = new HashSet<>(Arrays.asList(getAllClientDatabaseFiles(dbcDirectory)));
        Set<String> mappedFiles = new HashSet<>();
        for (Class<? extends ClientDatabaseEntry> type : getAllClientDatabaseEntryMappings()) {
            DbcFile f = type.getAnnotation(DbcFile.class);
            if (f != null) {
                mappedFiles.add(f.file());
            }
        }
        dbcFiles.removeAll(mappedFiles);

        SortedSet<ClientDatabaseFile> missingMappings = new TreeSet<>(new Comparator<ClientDatabaseFile>() {

            @Override
            public int compare(ClientDatabaseFile o1, ClientDatabaseFile o2) {
                return o1.getFilename().compareToIgnoreCase(o2.getFilename());
            }
        });
        for (String mf : dbcFiles) {
            missingMappings.add(parse(mf, dbcDirectory));
        }

        return missingMappings;
    }

    private ClientDatabaseFile parse(String filename, String dbcDirectory) throws IOException {
        ClientDatabaseFileParser parser = new ClientDatabaseFileParser();
        return parser.parseFile(filename, dbcDirectory);
    }

    private ClientDatabaseFile parse(Class<? extends ClientDatabaseEntry> type, String dbcDirectory) throws IOException {
        ClientDatabaseFileParser parser = new ClientDatabaseFileParser();
        return parser.parseFile(type.getAnnotation(DbcFile.class).file(), dbcDirectory);
    }

    private static int getUnknownFieldCount(Class<? extends ClientDatabaseEntry> type) {
        int fieldCount = 0;
        if (type != null && type.isAnnotationPresent(DbcFile.class)) {
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
    private static int getFieldCount(ClientDatabaseFile file) {
        int fieldCount = 0;
        if (file != null && file.getHeader() != null) {
            fieldCount = file.getHeader().getFieldCount();
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
    private static int getFieldCount(Class<? extends ClientDatabaseEntry> type) {
        int fieldCount = 0;
        if (type != null && type.isAnnotationPresent(DbcFile.class)) {
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
    private Collection<Class<? extends ClientDatabaseEntry>> getAllClientDatabaseEntryMappings() throws IOException {
        ClientDatabaseEntryClasspathScanner scanner = new ClientDatabaseEntryClasspathScanner(this);
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

    public static void main(String... args) throws Exception {
        if (args.length != 2) {
            printHelp();
        } else if ("missing".equalsIgnoreCase(args[0])) {
            IncompleteDbcMappingFinder finder = new IncompleteDbcMappingFinder();
            print(finder.findAllMissingMappings(args[1]));
        } else if ("incomplete".equalsIgnoreCase(args[0])) {
            IncompleteDbcMappingFinder finder = new IncompleteDbcMappingFinder();
            print(finder.findAllIncompleteMappings(args[1]));
        } else {
            printHelp();
        }

    }

    private static void print(Map<ClientDatabaseFile, Class<? extends ClientDatabaseEntry>> incompleteMappings) {
        if (incompleteMappings.isEmpty()) {
            System.out.println(format("All mapped files are complete"));
        } else {
            System.out.println(format("Incomplete mappings for the following %d files", incompleteMappings.size()));
            for (Map.Entry<ClientDatabaseFile, Class<? extends ClientDatabaseEntry>> m : incompleteMappings.entrySet()) {
                String fileName = m.getKey().getFilename();
                int fileFields = getFieldCount(m.getKey());
                int entrySize = m.getKey().getHeader().getRecordSize();
                String typeName = m.getValue().getName();
                int typeFields = getFieldCount(m.getValue());
                int typeUnknownFields = getUnknownFieldCount(m.getValue());
                System.out.println(format("    - %s has %d fields (%d bytes per entry) with mapping type %s having %d fields with %d being unknown.", fileName, fileFields, entrySize, typeName, typeFields, typeUnknownFields));
            }
        }
    }

    private static void print(Collection<ClientDatabaseFile> missingMappings) {
        if (missingMappings.isEmpty()) {
            System.out.println(format("All found DBC and DB2 files are mapped."));
        } else {
            System.out.println(format("Missing mappings for the following %d files", missingMappings.size()));
            for (ClientDatabaseFile m : missingMappings) {
                System.out.println(format("    - %s (fields:%d, entrySize:%d stringBlock:%s, entries:%d)", m.getFilename(), m.getHeader().getFieldCount(), m.getHeader().getRecordSize(), !m.getStringBlock().getAvailablePositions().isEmpty(), m.getHeader().getRecordCount()));
            }
        }
    }

    private static void printHelp() {
        System.out.println(format("Didn't receive all required parameters."));
        System.out.println(format("Usage:"));
        System.out.println(format("    IncompleteDbcMappingFinder <method> <dbc directory>"));
        System.out.println(format(""));
        System.out.println(format("Parameters:"));
        System.out.println(format("    <method> The method to scan for, being either 'incomplete' or 'missing' which scans for either incomplete mappings or missing mappings"));
        System.out.println(format("    <dbc directory> The directory where the DBC and DB2 files are located"));
    }
}
