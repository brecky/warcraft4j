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

import nl.salp.warcraft4j.clientdata.dbc.mapping.*;
import nl.salp.warcraft4j.clientdata.io.RandomAccessDataReader;
import nl.salp.warcraft4j.clientdata.io.file.FileDataReader;

import java.io.FilenameFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Client database related utility methods.
 *
 * @author Barre Dijkstra
 */
public final class DbcUtil {
    /** Comparator for sorting {@code java.lang.reflect.Field} instances based on their dbc mapping order, placing non-mapped fields last. */
    public static final Comparator<Field> DBC_FIELD_COMPARATOR = (f1, f2) -> {
        OptionalInt o1 = getFieldMappingOrder(f1);
        OptionalInt o2 = getFieldMappingOrder(f2);
        if (o1.isPresent() && o2.isPresent()) {
            return Integer.valueOf(o1.getAsInt()).compareTo(o2.getAsInt());
        } else if (o1.isPresent()) {
            return -1;
        } else if (o2.isPresent()) {
            return 1;
        } else {
            return 0;
        }
    };
    /** Comparator for sorting {@link DbcFile} instances based on their DBC filename without path. */
    public static final Comparator<DbcFile> DBC_FILE_COMPARATOR = (f1, f2) -> {
        Optional<String> n1 = getFilename(f1);
        Optional<String> n2 = getFilename(f2);
        if (n1.isPresent() && n2.isPresent()) {
            return n1.get().compareTo(n2.get());
        } else if (n1.isPresent()) {
            return -1;
        } else if (n2.isPresent()) {
            return 1;
        } else {
            return 0;
        }
    };
    /** FilenameFilter for filtering DBC type files. */
    public static final FilenameFilter DBC_FILENAME_FILTER = (dir, name) -> name.endsWith(".db2") || name.endsWith(".dbc");

    /**
     * Private constructor to prevent instantiation.
     */
    private DbcUtil() {
    }

    /**
     * Get the {@code java.util.Optional} instance of the filename mapped by a {@link DbcFile}.
     *
     * @param dbcFile The DbcFile to get the filename for.
     *
     * @return The optional of the filename, being empty if no filename could be retrieved.
     */
    public static Optional<String> getFilename(DbcFile dbcFile) {
        return dbcFile == null ? Optional.empty() : Optional.ofNullable(dbcFile.getDbcName());
    }

    /**
     * Get the {@code java.util.Optional} instance of the DBC file mapped by the {@link DbcEntry} type.
     *
     * @param entryType    The entry mapping type.
     * @param dbcDirectory The directory with the DBC files.
     * @param <T>          The type of the DbcEntry.
     *
     * @return The optional of the mapped DbcFile, being empty when it couldn't be resolved.
     */
    public static <T extends DbcEntry> Optional<DbcFile> getDbcFile(Class<T> entryType, String dbcDirectory) {
        return getDbcFile(entryType, Paths.get(dbcDirectory));
    }

    /**
     * Get the {@code java.util.Optional} instance of the DBC file mapped by the {@link DbcEntry} type.
     *
     * @param entryType    The entry mapping type.
     * @param dbcDirectory The directory with the DBC files.
     * @param <T>          The type of the DbcEntry.
     *
     * @return The optional of the mapped DbcFile, being empty when it couldn't be resolved.
     */
    public static <T extends DbcEntry> Optional<DbcFile> getDbcFile(Class<T> entryType, Path dbcDirectory) {
        Optional<String> file = getMappedFile(entryType);
        return file.isPresent() ? getDbcFile(entryType, getFileDataReaderSupplier(dbcDirectory, file.get())) : Optional.empty();
    }

    /**
     * Get the {@code java.util.Optional} instance of the DBC file mapped by the {@link DbcEntry} type.
     *
     * @param entryType          The entry mapping type.
     * @param dataReaderSupplier Supplier for the {@link RandomAccessDataReader} to use for reading the dbc file.
     * @param <T>                The type of the DbcEntry.
     *
     * @return The optional of the mapped DbcFile, being empty when it couldn't be resolved.
     */
    public static <T extends DbcEntry> Optional<DbcFile> getDbcFile(Class<T> entryType, Supplier<RandomAccessDataReader> dataReaderSupplier) {
        Optional<String> file = getMappedFile(entryType);
        return file.isPresent() ? Optional.of(new DbcFile(file.get(), dataReaderSupplier)) : Optional.empty();
    }

    /**
     * Get the {@code java.util.Optional} instance of the {@link DbcMapping} for a entry mapping type.
     *
     * @param entryType The entry mapping type.
     * @param <T>       The type of the DbcEntry.
     *
     * @return The optional of the DbcMapping, being empty when none was present.
     */
    public static <T extends DbcEntry> Optional<DbcMapping> getMapping(Class<T> entryType) {
        return getAnnotation(entryType, DbcMapping.class);
    }

    /**
     * Get the {@code java.util.Optional} instance of the filename a entry mapping type maps.
     *
     * @param entryType The entry mapping type.
     * @param <T>       The type of the DbcEntry.
     *
     * @return The optional of the dbc filename, being empty when none was present.
     */
    public static <T extends DbcEntry> Optional<String> getMappedFile(Class<T> entryType) {
        Optional<DbcMapping> mapping = getMapping(entryType);
        return mapping.isPresent() ? Optional.ofNullable(mapping.get().file()) : Optional.empty();
    }

    /**
     * Get the number of mapped DBC fields on the entry mapping type.
     *
     * @param type The type to get the number of mapped fields for.
     * @param <T>  The type of the entry mapping type.
     *
     * @return The number of mapped DBC fields.
     */
    public static <T extends DbcEntry> int getMappedFieldCount(Class<T> type) {
        return getFieldMappingsStream(type, false)
                .map(DbcUtil::getFieldMapping)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .mapToInt(DbcFieldMapping::numberOfEntries)
                .sum();
    }

    /**
     * Get the mapped DBC fields on the entry mapping type.
     *
     * @param entryType      The type to get the number of mapped fields for.
     * @param includePadding {@code true} if padding fields should be included.
     * @param <T>            The type of the entry mapping type.
     *
     * @return The mapped DBC fields.
     */
    public static <T extends DbcEntry> List<Field> getFieldMappings(Class<T> entryType, boolean includePadding) {
        return getFieldMappingsStream(entryType, includePadding)
                .distinct()
                .sorted(DBC_FIELD_COMPARATOR)
                .collect(Collectors.toList());
    }

    /**
     * Get the mapped DBC fields on the entry mapping type, excluding the padding fields.
     *
     * @param entryType The type to get the number of mapped fields for.
     * @param <T>       The type of the entry mapping type.
     *
     * @return The mapped DBC fields, excluding the padding fields.
     */
    public static <T extends DbcEntry> List<Field> getFieldMappings(Class<T> entryType) {
        return getFieldMappings(entryType, false);
    }

    /**
     * Get the mapped DBC fields on the entry mapping type of a specific data type.
     *
     * @param entryType      The type to get the number of mapped fields for.
     * @param dataType       The DBC data type of the field mapping.
     * @param includePadding {@code true} if padding fields should be included.
     * @param <T>            The type of the entry mapping type.
     *
     * @return The mapped DBC fields.
     */
    public static <T extends DbcEntry> List<Field> getFieldMappings(Class<T> entryType, DbcDataType dataType, boolean includePadding) {
        return getFieldMappingsStream(entryType, includePadding)
                .filter(f -> getFieldMapping(f).get().dataType() == dataType)
                .distinct()
                .sorted(DBC_FIELD_COMPARATOR)
                .collect(Collectors.toList());
    }

    /**
     * Get the mapped DBC fields on the entry mapping type of a specific data type, ignoring padding fields.
     *
     * @param entryType The type to get the number of mapped fields for.
     * @param dataType  The DBC data type of the field mapping.
     * @param <T>       The type of the entry mapping type.
     *
     * @return The mapped DBC fields excluding the padding fields.
     */
    public static <T extends DbcEntry> List<Field> getFieldMappings(Class<T> entryType, DbcDataType dataType) {
        return getFieldMappings(entryType, dataType, false);
    }

    /**
     * Get the field order of the field mapping for a field.
     *
     * @param field The field.
     *
     * @return Optional int which is empty when no mapping order was specified.
     */
    public static OptionalInt getFieldMappingOrder(Field field) {
        Optional<DbcFieldMapping> mapping = getFieldMapping(field);
        return mapping.isPresent() ? OptionalInt.of(mapping.get().order()) : OptionalInt.empty();
    }

    /**
     * Get the total size mapped by the entry type.
     *
     * @param entryType The mapping entry type.
     * @param <T>       The type of the mapping entry type.
     *
     * @return The total size mapped in bytes.
     */
    public static <T extends DbcEntry> int getMappedEntrySize(Class<T> entryType) {
        return getFieldMappings(entryType, true).stream()
                .distinct()
                .mapToInt(DbcUtil::getDbcDataSize)
                .sum();
    }

    /**
     * Get the mapping information for a field.
     *
     * @param field The field to get the mapping information from.
     *
     * @return The optional {@link DbcFieldMapping} with the mapping information.
     */
    public static Optional<DbcFieldMapping> getFieldMapping(Field field) {
        return getAnnotation(field, DbcFieldMapping.class);
    }

    /**
     * Get the DBC data size of a field.
     *
     * @param field The field.
     *
     * @return The data size in bytes.
     */
    public static int getDbcDataSize(Field field) {
        Optional<DbcFieldMapping> fieldMapping = getFieldMapping(field);
        int size = 0;
        if (fieldMapping.isPresent()) {
            size = new DbcFieldType(fieldMapping.get()).getDataSize();
        }
        return size;
    }

    /**
     * Get a supplier for a {@link RandomAccessDataReader} for a file.
     *
     * @param path The path of the file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> getFileDataReaderSupplier(String path) {
        return getFileDataReaderSupplier(Paths.get(path));
    }

    /**
     * Get a supplier for a {@link RandomAccessDataReader} for a file.
     *
     * @param directory The directory the file is located in.
     * @param filename  The name of the file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> getFileDataReaderSupplier(String directory, String filename) {
        return getFileDataReaderSupplier(Paths.get(directory).resolve(filename));
    }

    /**
     * Get a supplier for a {@link RandomAccessDataReader} for a file.
     *
     * @param directory The directory the file is located in.
     * @param filename  The name of the file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> getFileDataReaderSupplier(Path directory, String filename) {
        return getFileDataReaderSupplier(directory.resolve(filename));
    }

    /**
     * Get a supplier for a {@link RandomAccessDataReader} for a file.
     *
     * @param directory The directory the file is located in.
     * @param file  The file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> getFileDataReaderSupplier(Path directory, Path file) {
        return getFileDataReaderSupplier(directory.resolve(file));
    }

    /**
     * Get a supplier for a {@link RandomAccessDataReader} for a file.
     *
     * @param file The file.
     *
     * @return The supplier.
     */
    public static Supplier<RandomAccessDataReader> getFileDataReaderSupplier(Path file) {
        return () -> new FileDataReader(file);
    }


    /**
     * Get the {@link DbcType} for the given {@link DbcEntry} class if there is a static field with the DbcType present.
     *
     * @param mappingType The class to get the entry type from.
     *
     * @return The entry type or {@code null} if it could not be determined.
     */
    public static <T extends DbcEntry> Optional<DbcType> getDbcType(Class<T> mappingType) {
        if (mappingType == null || !mappingType.isAnnotationPresent(DbcMapping.class)) {
            return Optional.empty();
        }
        return getFieldsStream(mappingType)
                .filter(f -> Modifier.isStatic(f.getModifiers()))
                .filter(f -> f.getType() == DbcType.class)
                .limit(1)
                .map(f -> getFieldValue(f, DbcType.class, null))
                .findFirst()
                .orElse(Optional.empty());
    }

    /**
     * Find all entry mappings on the classpath of the class loader of the DbcUtil class.
     *
     * @return The entry mappings on the classpath.
     */
    public static Collection<Class<? extends DbcEntry>> findMappingsOnClasspath() {
        return findMappingsOnClasspath(DbcUtil.class.getClassLoader());
    }


    /**
     * Find all entry mappings on the classpath of the provided class loader.
     *
     * @param classLoader The class loader to use (scan will include the parent class loaders).
     *
     * @return The entry mappings on the classpath.
     */
    public static Collection<Class<? extends DbcEntry>> findMappingsOnClasspath(ClassLoader classLoader) {
        return new DbcClasspathMappingScanner(classLoader).scan();
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
        Path path = Paths.get(dbcDirectory);
        List<String> files = new ArrayList<>();
        try (DirectoryStream<Path> stream = path.getFileSystem().provider().newDirectoryStream(path, p -> isReadableFile(p) && isDbcFile(p))) {
            stream.forEach(p -> files.add(String.valueOf(p)));
        }
        return files.toArray(new String[files.size()]);
    }

    private static boolean isReadableFile(Path path) {
        return path != null && Files.exists(path) && Files.isRegularFile(path) && Files.isReadable(path);
    }

    private static boolean isDbcFile(Path path) {
        return path != null && (String.valueOf(path.getFileName()).endsWith("dbc") || String.valueOf(path.getFileName()).endsWith("db2"));
    }

    /**
     * Get a {@code java.util.Optional} instance for an annotation on an element.
     *
     * @param element    The annotated element.
     * @param annotation The class of the annotation to retrieve.
     * @param <T>        The type of the annotation.
     *
     * @return The optional of the annotation instance, being empty when the annotation was not present.
     */
    public static <T extends Annotation> Optional<T> getAnnotation(AnnotatedElement element, Class<T> annotation) {
        return element == null || annotation == null ? Optional.empty() : Optional.ofNullable(element.getAnnotation(annotation));
    }

    /**
     * Get a the value of a field on the provided instance.
     *
     * @param field    The field to get the value of.
     * @param type     The type of the value.
     * @param instance The instance or {@code null} for a static field.
     * @param <T>      The type of the value.
     *
     * @return Optional of the value, where every error situation results an a Optional.empty.
     */
    public static <T> Optional<T> getFieldValue(Field field, Class<T> type, Object instance) {
        if (field == null || type == null || field.getType() != type || (instance != null && instance.getClass() != type)) {
            return Optional.empty();
        }
        boolean access = field.isAccessible();
        try {
            field.setAccessible(true);
            Object value = field.get(instance);
            return value == null ? Optional.empty() : Optional.of((T) value);
        } catch (IllegalAccessException e) {
            return Optional.empty();
        } finally {
            field.setAccessible(access);
        }
    }

    /**
     * Get all the DBC mapped fields on a type as a stream.
     *
     * @param type           The type.
     * @param includePadding {@code true} if DBC padding fields should be included.
     *
     * @return The stream of the mapped fields.
     */
    public static Stream<Field> getFieldMappingsStream(Class<?> type, boolean includePadding) {
        if (type == null || Object.class == type) {
            return Stream.empty();
        }
        return getFieldsStream(type)
                .filter(f -> getFieldMapping(f).isPresent())
                .filter(f -> includePadding || !getFieldMapping(f).get().padding());
    }

    /**
     * Get all fields (with any modifier) defined on a type or its parent types as a stream.
     *
     * @param type The type to get the fields for.
     *
     * @return The fields.
     */
    public static Stream<Field> getFieldsStream(Class<?> type) {
        if (type == null || Object.class == type) {
            return Stream.empty();
        }
        return Stream.concat(Stream.concat(Stream.of(type.getFields()), Stream.of(type.getDeclaredFields())), getFieldsStream(type.getEnclosingClass()))
                .filter(f -> f != null)
                .distinct();
    }
}
