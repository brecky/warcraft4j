package nl.salp.warcraft4j.clientdata.dbc.util;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;

import java.io.File;
import java.io.FilenameFilter;
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
        if (mapping.isAnnotationPresent(DbcFile.class)) {
            file = mapping.getAnnotation(DbcFile.class).file();
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
        return dbcDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".db2") || name.endsWith(".dbc");
            }
        });
    }
}
