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
import nl.salp.warcraft4j.clientdata.dbc.DbcMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.jar.Attributes;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * Scanner that scans the whole classpath for all top-level classes that implement {@link DbcEntry} and have a {@link DbcMapping} annotation present.
 *
 * @author Barre Dijkstra
 */
public class DbcClasspathMappingScanner {
    /** The logger instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbcClasspathMappingScanner.class);

    /** Class filter filters out classes that are not a client database entry. */
    private static final ClassFilter FILTER_CLIENTDATABASEENTRY = new ClassFilter() {
        @Override
        public boolean isMatching(Class<?> type) {
            return type != null && DbcEntry.class.isAssignableFrom(type) && type.isAnnotationPresent(DbcMapping.class);
        }
    };
    /** Class filter that filters out classes that are not top level classes. */
    private static final ClassFilter FILTER_TOPLEVELCLASS = new ClassFilter() {
        @Override
        public boolean isMatching(Class<?> type) {
            return type != null && type.getName().indexOf('$') == -1;
        }
    };
    /** The classloader to use. */
    private final ClassLoader classLoader;

    /**
     * Create a new instance using the class loader of the given object.
     *
     * @param object The object to use the class loader from.
     */
    public DbcClasspathMappingScanner(Object object) {
        this(object.getClass().getClassLoader());
    }

    /**
     * Create a new instance using a specific class loader.
     *
     * @param classLoader The class loader to use.
     */
    public DbcClasspathMappingScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    /**
     * Scan all packages on the classpath for client database entry classes.
     *
     * @return The client database entry classes on the classpath.
     */
    public Collection<Class<? extends DbcEntry>> scan() {
        return scan("");
    }

    /**
     * Scan a specific package (including sub-packages) on the classpath for client database entry classes.
     *
     * @param basePackage The package to start scanning from.
     *
     * @return The found client database entry classes.
     */
    public Collection<Class<? extends DbcEntry>> scan(String basePackage) {
        LOGGER.debug(format("Scanning package [%s] for ClientDatabaseEntry implementations", basePackage));
        FileClasspathScanner scanner = new FileClasspathScanner(basePackage, FILTER_CLIENTDATABASEENTRY, FILTER_TOPLEVELCLASS);
        for (Map.Entry<URI, ClassLoader> entry : getResources(classLoader).entrySet()) {
            try {
                scanner.scan(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                LOGGER.error(format("Error scanning classpath URI %s: %s)", entry.getKey(), e.getMessage()), e);
            }
        }

        Collection<Class<? extends DbcEntry>> entries = new HashSet<>();
        for (Class<?> c : scanner.getClasses()) {
            if (DbcEntry.class.isAssignableFrom(c)) {
                entries.add((Class<DbcEntry>) c);
            } else {
                LOGGER.debug(format("Ignoring non ClientDatabaseEntry class %s", c.getName()));
            }
        }
        return entries;
    }

    /**
     * Get all path resources for the given class loader (including the parent class loaders).
     *
     * @param classLoader The class loader to get the path resources from.
     *
     * @return The path resource URIs with their corresponding class loader.
     */
    private Map<URI, ClassLoader> getResources(ClassLoader classLoader) {
        LinkedHashMap<URI, ClassLoader> resources = new LinkedHashMap<>();
        ClassLoader parent = classLoader.getParent();
        if (parent != null) {
            resources.putAll(getResources(parent));
        }
        if (URLClassLoader.class.isAssignableFrom(classLoader.getClass())) {
            URLClassLoader cl = (URLClassLoader) classLoader;
            for (URL url : cl.getURLs()) {
                URI uri;
                try {
                    uri = url.toURI();
                } catch (URISyntaxException e) {
                    throw new IllegalArgumentException(e);
                }
                if (!resources.containsKey(uri)) {
                    resources.put(uri, classLoader);
                }
            }
        }
        return resources;
    }


    /**
     * Filter for filtering out non-matching classes.
     */
    private interface ClassFilter {
        /**
         * Check if a class matches the filter.
         *
         * @param type The class.
         *
         * @return {@code true} if the class matches the filter.
         */
        boolean isMatching(Class<?> type);
    }

    /**
     * File based classpath scanner.
     */
    private static class FileClasspathScanner {
        /** The resolved classes. */
        private final Set<Class<?>> classes;
        /** The filters the resolved classes should confirm to. */
        private final ClassFilter[] filters;
        /** The URI's that have been scanned. */
        private final Set<URI> scannedUris;
        /** The base package to look in. */
        private final String basePackage;

        /**
         * Create a new instance.
         *
         * @param basePackage The package to check in (including subpackages).
         * @param filters     The filters that all resolved classes should abide to.
         */
        public FileClasspathScanner(String basePackage, ClassFilter... filters) {
            this.basePackage = basePackage;
            this.filters = filters;
            this.classes = new HashSet<>();
            this.scannedUris = new HashSet<>();
        }

        /**
         * Get the found classes.
         *
         * @return The classes.
         */
        public Set<Class<?>> getClasses() {
            return Collections.unmodifiableSet(classes);
        }

        /**
         * Scan the URI for matching classes using the given class loader.
         *
         * @param uri         the URI (either a directory or jar-file) to scan.
         * @param classLoader The class loader to use.
         *
         * @throws IOException When scanning failed.
         */
        public void scan(URI uri, ClassLoader classLoader) throws IOException {
            if ("file".equals(uri.getScheme()) && scannedUris.add(uri)) {
                File file = new File(uri);
                if (!file.exists()) {
                    LOGGER.warn(format("Tried to scan non-existing file %s", file.getCanonicalPath()));
                    // Shouldn't happen....
                } else if (file.isDirectory()) {
                    LOGGER.debug(format("Scanning directory %s", file.getCanonicalPath()));
                    scanDirectory(file, classLoader);
                } else {
                    LOGGER.debug(format("Scanning JAR file %s", file.getCanonicalPath()));
                    scanJarFile(file, classLoader);
                }
            }
        }

        /**
         * Scan a directory for matching classes.
         *
         * @param directory   The directory to scan.
         * @param classLoader The class loader to use.
         *
         * @throws IOException When scanning fails.
         */
        private void scanDirectory(File directory, ClassLoader classLoader) throws IOException {
            scanDirectory(directory, "", Collections.<File>emptySet(), classLoader);
        }

        /**
         * Scan a directory for matching classes.
         *
         * @param directory       The name of the directory to scan.
         * @param directoryPrefix The directory name prefix (e.g. preceding directories, etc.)
         * @param parents         The parent files.
         * @param classLoader     The class loader to use.
         *
         * @throws IOException When scanning fails.
         */
        private void scanDirectory(File directory, String directoryPrefix, Set<File> parents, ClassLoader classLoader) throws IOException {
            File canonical = directory.getCanonicalFile();
            if (!parents.contains(canonical)) {
                File[] files = directory.listFiles();
                if (files != null) {
                    Set<File> newParents = new HashSet(parents);
                    newParents.add(canonical);
                    for (File f : files) {
                        String name = f.getName();
                        if (f.isDirectory()) {
                            scanDirectory(f, directoryPrefix + name + "/", newParents, classLoader);
                        } else {
                            String fileName = directoryPrefix + name;
                            loadFile(fileName, classLoader);
                        }
                    }
                }
            }
        }


        /**
         * Scan a JAR file for matching classes.
         *
         * @param file        The jar file.
         * @param classLoader The class loader to use.
         *
         * @throws IOException When scanning fails.
         */
        private void scanJarFile(File file, ClassLoader classLoader) throws IOException {
            try (JarFile jarFile = new JarFile(file)) {
                for (URI uri : getClasspathFromJarManifest(file, jarFile.getManifest())) {
                    scan(uri, classLoader);
                }
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    if (!entry.isDirectory()) {
                        loadFile(entry.getName(), classLoader);
                    }
                }
            }
        }

        /**
         * Get the class path from the manifest of a JAR file.
         *
         * @param jarFile  The JAR file.
         * @param manifest The manifest file (may be {@code null}).
         *
         * @return The resources from the manifest classpath.
         */
        private Set<URI> getClasspathFromJarManifest(File jarFile, Manifest manifest) {
            Set<URI> entries;
            if (manifest == null) {
                entries = Collections.emptySet();
            } else {
                entries = new HashSet<>();
                String classPathAttribute = manifest.getMainAttributes().getValue(Attributes.Name.CLASS_PATH.toString());
                if (classPathAttribute != null) {
                    StringTokenizer tokenizer = new StringTokenizer(classPathAttribute, " ");
                    while (tokenizer.hasMoreElements()) {
                        String resource = tokenizer.nextToken().trim().toLowerCase();
                        if (isNotEmpty(resource)) {
                            try {
                                URI uri = getClassPathEntry(jarFile, resource);
                                entries.add(uri);
                            } catch (URISyntaxException e) {
                                // Ignore.
                            }
                        }
                    }
                }
            }
            return entries;
        }

        /**
         * Load class file.
         * <p/>
         * Class files are only loaded when they are in the matching appropriate package, are not a Java core class and are a top-level class.
         *
         * @param filename    The filename.
         * @param classLoader The class loader to use.
         */
        private void loadFile(String filename, ClassLoader classLoader) {
            if (isTopLevelClassName(filename) && !isJavaCore(filename) && isMatchingPackage(filename)) {
                Class<?> type = load(filename, classLoader);
                if (isMatchingFilters(type)) {
                    classes.add(type);
                }
            }
        }

        /**
         * Check if the file name is matching a class in the base package.
         *
         * @param filename The file name.
         *
         * @return {@code true} if the class is in the package.
         */
        private boolean isMatchingPackage(String filename) {
            return filename != null && getClassName(filename).startsWith(basePackage);
        }

        /**
         * Check if the filename is that of a top-level class.
         *
         * @param name The filename.
         *
         * @return {@code true} if the filename matches that of a top-level class.
         */
        private boolean isTopLevelClassName(String name) {
            return name != null && name.toLowerCase().endsWith(".class") && name.indexOf('$') == -1;
        }

        /**
         * Check if the class is a Java core class.
         *
         * @param name The name of the class.
         *
         * @return {@code true} if the class is a Java core class.
         */
        private boolean isJavaCore(String name) {
            boolean javaCore = false;
            if (name != null) {
                String className = getClassName(name);
                javaCore = javaCore || className.startsWith("com.sun");
                javaCore = javaCore || className.startsWith("sun");
                javaCore = javaCore || className.startsWith("javax");
                javaCore = javaCore || className.startsWith("java");
                javaCore = javaCore || className.startsWith("jdk");
            }
            return javaCore;
        }


        /**
         * Check if the class matches the scanner filters.
         *
         * @param type The class.
         *
         * @return {@code true} if the class matches all filters.
         */
        private boolean isMatchingFilters(Class<?> type) {
            boolean matching = true;
            if (type == null) {
                matching = false;
            } else {
                for (ClassFilter filter : filters) {
                    matching = matching && filter.isMatching(type);
                }
            }
            return matching;
        }


    }

    /**
     * Load a class without instantiating it.
     *
     * @param name        The name of the class.
     * @param classLoader The class loader to use.
     *
     * @return The class instance or {@code null} if there was a problem loading the class.
     */
    private static Class<?> load(String name, ClassLoader classLoader) {
        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass(getClassName(name));
        } catch (NoClassDefFoundError e) {
            // Ignore.
        } catch (ClassNotFoundException e) {
            // Ignore.
        }
        return clazz;
    }

    /**
     * Get the URI for a manifest classpath entry.
     *
     * @param jarFile The JAR file the manifest is from.
     * @param path    The classpath entry.
     *
     * @return The URI.
     *
     * @throws URISyntaxException When the constructed URI resulted in an invalid URI.
     */
    private static URI getClassPathEntry(File jarFile, String path)
            throws URISyntaxException {
        URI uri = new URI(path);
        if (uri.isAbsolute()) {
            return uri;
        } else {
            return new File(jarFile.getParentFile(), path.replace('/', File.separatorChar)).toURI();
        }
    }

    /**
     * Get the class name from a file name.
     *
     * @param filename The file name.
     *
     * @return The class name.
     */
    private static String getClassName(String filename) {
        String name = filename;
        if (filename.endsWith(".class")) {
            int classNameEnd = filename.length() - ".class".length();
            name = filename.substring(0, classNameEnd);
        }
        return name.replace('/', '.');
    }
}
