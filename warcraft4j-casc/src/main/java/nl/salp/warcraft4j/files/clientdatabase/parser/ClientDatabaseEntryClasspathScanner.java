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

package nl.salp.warcraft4j.files.clientdatabase.parser;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;

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

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseEntryClasspathScanner {
    private static final ClassFilter FILTER_CLIENTDATABASEENTRY = new ClassFilter() {
        @Override
        public boolean isMatching(Class<?> type) {
            return type != null && ClientDatabaseEntry.class.isAssignableFrom(type) && type.isAnnotationPresent(DbcFile.class);
        }
    };
    private static final ClassFilter FILTER_TOPLEVELCLASS = new ClassFilter() {
        @Override
        public boolean isMatching(Class<?> type) {
            return type != null && type.getName().indexOf('$') == -1;
        }
    };

    private final ClassLoader classLoader;

    public ClientDatabaseEntryClasspathScanner(Object object) {
        this(object.getClass().getClassLoader());
    }

    public ClientDatabaseEntryClasspathScanner(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public Collection<Class<? extends ClientDatabaseEntry>> scan() {
        return scan("");
    }

    public Collection<Class<? extends ClientDatabaseEntry>> scan(String basePackage) {
        FileClasspathScanner scanner = new FileClasspathScanner(basePackage, FILTER_CLIENTDATABASEENTRY, FILTER_TOPLEVELCLASS);
        for (Map.Entry<URI, ClassLoader> entry : getResources(classLoader).entrySet()) {
            try {
                scanner.scan(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                System.out.println("Error scanning classpath URI " + entry.getKey() + ": " + e.getMessage());
                e.printStackTrace();
            }
        }

        Collection<Class<? extends ClientDatabaseEntry>> entries = new HashSet<>();
        for (Class<?> c : scanner.getClasses()) {
            if (ClientDatabaseEntry.class.isAssignableFrom(c)) {
                entries.add((Class<ClientDatabaseEntry>) c);
            }
        }
        return entries;
    }

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


    private static interface ClassFilter {
        boolean isMatching(Class<?> type);
    }

    private static class FileClasspathScanner {
        private final Set<Class<?>> classes;
        private final ClassFilter[] filters;
        private final Set<URI> scannedUris;
        private final String basePackage;

        public FileClasspathScanner(String basePackage, ClassFilter... filters) {
            this.basePackage = basePackage;
            this.filters = filters;
            this.classes = new HashSet<>();
            this.scannedUris = new HashSet<>();
        }

        public Set<Class<?>> getClasses() {
            return Collections.unmodifiableSet(classes);
        }

        public void scan(URI uri, ClassLoader classLoader) throws IOException {
            if ("file".equals(uri.getScheme()) && scannedUris.add(uri)) {
                File file = new File(uri);
                if (!file.exists()) {
                    // Shouldn't happen....
                } else if (file.isDirectory()) {
                    System.out.println("Scanning directory " + file.getCanonicalPath());
                    scanDirectory(file, classLoader);
                } else {
                    scanJarFile(file, classLoader);
                }
            }
        }

        private void scanDirectory(File directory, ClassLoader classLoader) throws IOException {
            scanDirectory(directory, "", Collections.<File>emptySet(), classLoader);
        }

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


        private void scanJarFile(File file, ClassLoader classLoader) throws IOException {
            try (JarFile jarFile = new JarFile(file)) {
                System.out.println("Scanning jar file " + file);
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

        private void loadFile(String filename, ClassLoader classLoader) {
            if (isTopLevelClassName(filename) && !isJavaCore(filename) && isMatchingPackage(filename)) {
                Class<?> type = load(filename, classLoader);
                if (isMatchingFilters(type)) {
                    classes.add(type);
                }
            }
        }

        private boolean isMatchingPackage(String filename) {
            return filename != null && getClassName(filename).startsWith(basePackage);
        }

        private boolean isTopLevelClassName(String name) {
            return name != null && name.toLowerCase().endsWith(".class") && name.indexOf('$') == -1;
        }

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

    private static URI getClassPathEntry(File jarFile, String path)
            throws URISyntaxException {
        URI uri = new URI(path);
        if (uri.isAbsolute()) {
            return uri;
        } else {
            return new File(jarFile.getParentFile(), path.replace('/', File.separatorChar)).toURI();
        }
    }

    private static String getClassName(String filename) {
        String name = filename;
        if (filename.endsWith(".class")) {
            int classNameEnd = filename.length() - ".class".length();
            name = filename.substring(0, classNameEnd);
        }
        return name.replace('/', '.');
    }
}
