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
package nl.salp.warcraft4j.dev.dbc;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.parser.FullDbcFileParser;
import nl.salp.warcraft4j.clientdata.dbc.util.DbcClasspathMappingScanner;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * DBC and mapping related utility methods.
 *
 * @author Barre Dijkstra
 */
public final class DbcUtils {
    /**
     * Private constructor to prevent instantiation.
     */
    private DbcUtils() {
    }

    /**
     * Parse a DBC/DB2 to the mapped entry instances.
     *
     * @param mappingType  The class of the dbc mapping entry to map to.
     * @param dbcDirectory Directory with unpacked DBC/DB2 files.
     * @param <T>          The type of the dbc mapping entry.
     *
     * @return The parsed entries.
     *
     * @throws IOException When reading the data file failed.
     */
    public static <T extends DbcEntry> Set<T> parse(Class<T> mappingType, String dbcDirectory) throws IOException {
        FullDbcFileParser parser = new FullDbcFileParser(dbcDirectory);
        return parser.parse(mappingType);
    }

    /**
     * Get the parsed {@link DbcFile} meta-data for a DBC file.
     *
     * @param filename     The file name.
     * @param dbcDirectory Directory with unpacked DBC/DB2 files.
     *
     * @return The parsed file.
     *
     * @throws IOException When parsing failed.
     */
    public static DbcFile getDbcFile(String filename, String dbcDirectory) throws IOException {
        FullDbcFileParser parser = new FullDbcFileParser(dbcDirectory);
        return parser.parseFile(filename, dbcDirectory);
    }

    /**
     * Get the parsed {@link DbcFile} meta-data for a dbc mapping type.
     *
     * @param mappingType  The dbc mapping type class to get the meta-data for.
     * @param dbcDirectory Directory with unpacked DBC/DB2 files.
     * @param <T>          The dbc mapping type.
     *
     * @return The parsed file.
     *
     * @throws IOException              When parsing failed.
     * @throws IllegalArgumentException When the mapping type does not point refer to a file.
     */
    public static <T extends DbcEntry> DbcFile getDbcFile(Class<T> mappingType, String dbcDirectory) throws IOException {
        if (mappingType.isAnnotationPresent(DbcMapping.class) || isEmpty(mappingType.getAnnotation(DbcMapping.class).file())) {
            throw new IllegalArgumentException(format("Can not get the DbcFile meta data for entry %s with no @DbcMapping annotation or no file set.", mappingType.getName()));
        }
        String fileName = mappingType.getAnnotation(DbcMapping.class).file();
        return getDbcFile(fileName, dbcDirectory);
    }

    /**
     * Get the mapping entry types that map to the given file.
     *
     * @param file The DbcFile to get the mappings for.
     *
     * @return The mapping entry types.
     */
    public static Collection<Class<? extends DbcEntry>> getMappings(DbcFile file) throws IOException {
        Collection<Class<? extends DbcEntry>> mappings = new HashSet<>();
        if (file != null && isNotEmpty(file.getFilename())) {
            for (Class<? extends DbcEntry> mapping : getAllClientDatabaseEntryMappings()) {
                if (file.getFilename().equals(getMappedFile(mapping))) {
                    mappings.add(mapping);
                }
            }
        }
        return mappings;
    }

    /**
     * Get the DBC file the mapping entry type is mapping from.
     *
     * @param mappingType The mapping entry type.
     *
     * @return The DBC file name or {@code null} if the mapping entry type is not mapping to a file.
     */
    public static String getMappedFile(Class<? extends DbcEntry> mappingType) {
        String mappedFile = null;
        if (mappingType != null && mappingType.isAnnotationPresent(DbcMapping.class)) {
            mappedFile = mappingType.getAnnotation(DbcMapping.class).file();
        }
        return mappedFile;
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
     * Get all dbc mapping entry classes that have mapping information.
     *
     * @return The dbc mapping entry classes.
     */
    public static Collection<Class<? extends DbcEntry>> getAllClientDatabaseEntryMappings() {
        DbcClasspathMappingScanner scanner = new DbcClasspathMappingScanner(DbcUtils.class.getClassLoader());
        return scanner.scan();
    }

    /**
     * Get the paths of all DBC and DB2 files in the DBC directory.
     *
     * @param dbcDirectory Directory with unpacked DBC/DB2 files.
     *
     * @return The names of all client database files in the directory.
     *
     * @throws IOException When reading the directory failed.
     */
    public static String[] getAllDbcFilePaths(String dbcDirectory) throws IOException {
        File dbcDir = new File(dbcDirectory);
        return dbcDir.list(getDbcFilenameFilter());
    }

    /**
     * Get all DBC and DB2 files in the configured DBC directory.
     *
     * @param dbcDirectory Directory with unpacked DBC/DB2 files.
     *
     * @return All client database files in the directory.
     *
     * @throws IOException When reading the directory failed.
     */
    public static File[] getAllDbcFiles(String dbcDirectory) throws IOException {
        File dbcDir = new File(dbcDirectory);
        return dbcDir.listFiles(getDbcFilenameFilter());
    }


    /**
     * Parse all DBC and DB2 files in the configured DBC directory.
     *
     * @param dbcDirectory Directory with unpacked DBC/DB2 files.
     *
     * @return All parsed files.
     *
     * @throws IOException When reading the files failed.
     */
    public static Set<DbcFile> parseDbcFiles(String dbcDirectory) throws IOException {
        Set<DbcFile> parsedFiles = new HashSet<>();
        for (String file : getAllDbcFilePaths(dbcDirectory)) {
            parsedFiles.add(getDbcFile(file, dbcDirectory));
        }
        return parsedFiles;
    }

    /**
     * Get a Comparator implementation for DbcFile instances.
     *
     * @return The comparator.
     */
    public static Comparator<DbcFile> getDbcFileComparator() {
        return DbcFileComparator.INSTANCE;
    }

    /**
     * Get all mapped fields for the dbc mapping entry type.
     *
     * @param mappingType The mapping entry type.
     *
     * @return The mapped fields, ordered by mapping order.
     */
    public static Collection<Field> getMappedFields(Class<? extends DbcEntry> mappingType) {
        Set<Field> mappedFields = new TreeSet<>(getFieldOrderComparator());
        mappedFields.addAll(getDbcFields(mappingType, true));
        return mappedFields;
    }

    /**
     * Get all mapped fields, excluding padding fields, for the dbc mapping entry type.
     *
     * @param mappingType The mapping entry type.
     *
     * @return The mapped fields, ordered by mapping order.
     */
    public static Collection<Field> getMappedFieldsWithoutPadding(Class<? extends DbcEntry> mappingType) {
        Set<Field> mappedFields = new TreeSet<>(getFieldOrderComparator());
        mappedFields.addAll(getDbcFields(mappingType, false));
        return mappedFields;
    }

    /**
     * Get the size of the mapped fields for the dbc mapping entry type in bytes.
     *
     * @param mappingType The mapping entry type.
     *
     * @return The size in bytes.
     */
    public static int getSize(Class<? extends DbcEntry> mappingType) {
        return getSize(getMappedFields(mappingType));
    }

    /**
     * Get the size of the mapped fields, excluding padding fields, for the dbc mapping entry type in bytes.
     *
     * @param mappingType The mapping entry type.
     *
     * @return The size in bytes.
     */
    public static int getSizeWithoutPadding(Class<? extends DbcEntry> mappingType) {
        return getSize(getMappedFieldsWithoutPadding(mappingType));
    }

    /**
     * Get the size of the DbcField annotated fields in bytes.
     *
     * @param fields The fields.
     *
     * @return The size in bytes.
     */
    private static int getSize(Collection<Field> fields) {
        int size = 0;
        for (Field field : fields) {
            DbcField f = field.getAnnotation(DbcField.class);
            if (f.length() > 0) {
                size = size + f.length();
            } else {
                size = size + (f.dataType().getDataType(f).getLength() * f.numberOfEntries());
            }
        }
        return size;
    }

    /**
     * Get all {@link DbcField} annotated fields for the provided class and all super classes.
     *
     * @param type           The class to get the fields for.
     * @param includePadding {@code true} if padding fields should be included or {@code false} if not.
     *
     * @return The fields.
     */
    private static Collection<Field> getDbcFields(Class<?> type, boolean includePadding) {
        Set<Field> fields = new HashSet<>();
        if (type != null) {
            for (Field f : type.getFields()) {
                if (f.isAnnotationPresent(DbcField.class)) {
                    if (includePadding || !f.getAnnotation(DbcField.class).padding()) {
                        fields.add(f);
                    }
                }
            }
            for (Field f : type.getDeclaredFields()) {
                if (f.isAnnotationPresent(DbcField.class)) {
                    if (includePadding || !f.getAnnotation(DbcField.class).padding()) {
                        fields.add(f);
                    }
                }
            }
            if (type.getEnclosingClass() != null && Object.class == type.getEnclosingClass()) {
                fields.addAll(getDbcFields(type.getEnclosingClass(), includePadding));
            }
        }
        return fields;
    }

    /**
     * Get a filename filter for getting DBC files.
     *
     * @return The filter.
     */
    public static FilenameFilter getDbcFilenameFilter() {
        return DbcFilenameFilter.INSTANCE;
    }

    /**
     * Get a comparator for fields, that orders on the mapping order.
     *
     * @return The comparator.
     */
    public static Comparator<Field> getFieldOrderComparator() {
        return FieldOrderComparator.INSTANCE;
    }

    /**
     * Filename filter for filtering DBC files.
     */
    private static class DbcFilenameFilter implements FilenameFilter {
        /** Singleton instance of the filter. */
        public static final DbcFilenameFilter INSTANCE = new DbcFilenameFilter();

        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".db2") || name.endsWith(".dbc");
        }
    }

    /**
     * Comparator that compares 2 DbcField annotated fields based on their DbcField#order() value.
     */
    private static class FieldOrderComparator implements Comparator<Field> {
        /** Singleton instance of the comparator. */
        public static final FieldOrderComparator INSTANCE = new FieldOrderComparator();

        @Override
        public int compare(Field o1, Field o2) {
            int cmp;
            if (o1 == null && o2 == null) {
                cmp = 0;
            } else if (o1 == null) {
                cmp = 1;
            } else if (o2 == null) {
                cmp = -1;
            } else if (!o1.isAnnotationPresent(DbcField.class) && !o2.isAnnotationPresent(DbcField.class)) {
                cmp = 0;
            } else if (!o1.isAnnotationPresent(DbcField.class)) {
                cmp = 1;
            } else if (!o2.isAnnotationPresent(DbcField.class)) {
                cmp = -1;
            } else {
                cmp = Integer.valueOf(o1.getAnnotation(DbcField.class).order()).compareTo(o2.getAnnotation(DbcField.class).order());
            }
            return cmp;
        }
    }

    /**
     * Comparator for comparing DbcFile instances (based on filename).
     */
    private static class DbcFileComparator implements Comparator<DbcFile> {
        /** Singleton instance of the comparator. */
        public static final DbcFileComparator INSTANCE = new DbcFileComparator();

        @Override
        public int compare(DbcFile o1, DbcFile o2) {
            int cmp;
            if (o1 == null && o2 == null) {
                cmp = 0;
            } else if (o1 == null) {
                cmp = 1;
            } else if (o2 == null) {
                cmp = -1;
            } else {
                cmp = o1.getFilename().compareToIgnoreCase(o2.getFilename());
            }
            return cmp;
        }
    }
}
