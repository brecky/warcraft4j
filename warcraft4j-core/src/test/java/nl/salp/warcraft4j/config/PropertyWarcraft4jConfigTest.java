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
package nl.salp.warcraft4j.config;

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import org.apache.commons.configuration.Configuration;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit tests for {@link PropertyWarcraft4jConfig}.
 *
 * @author Barre Dijkstra
 */
public class PropertyWarcraft4jConfigTest {
    /** The online value returned by the configuration. */
    private static final boolean ONLINE = true;
    /** The cache value returned by the configuration. */
    private static final boolean CACHE = true;
    /** The locale value returned by the configuration. */
    private static final Locale LOCALE = Locale.EN_TW;
    /** The region value returned by the configuration. */
    private static final Region REGION = Region.SEA_AUSTRALASIA;
    /** The branch value returned by the configuration. */
    private static final Branch BRANCH = Branch.BETA;
    /** Main directory for placing directories and files in during tests. */
    private static Path testDir;
    /** The WoW installation directory. */
    private static Path wowDir;
    /** The Cache directory. */
    private static Path cacheDir;

    /** Mock configuration. */
    private Configuration configuration;

    @BeforeClass
    public static void setUpClass() throws Exception {
        testDir = Files.createTempDirectory(Paths.get(System.getProperty("java.io.tmpdir")), "w4jConfigTests");

        wowDir = testDir.resolve("wow");
        Files.createDirectory(wowDir);

        cacheDir = testDir.resolve("cache");
        Files.createDirectory(cacheDir);
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
        delete(testDir);
    }

    private static void delete(Path directory) throws Exception {
        Files.walkFileTree(directory, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
                    throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException e)
                    throws IOException {
                if (e == null) {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                } else {
                    throw e;
                }
            }
        });
    }

    @Before
    public void setUp() throws Exception {
        configuration = mock(Configuration.class);
        when(configuration.isEmpty()).thenReturn(false);
        when(configuration.getBoolean(eq(PropertyWarcraft4jConfig.ONLINE_KEY), anyBoolean())).thenReturn(ONLINE);
        when(configuration.getString(eq(PropertyWarcraft4jConfig.WOW_DIR_KEY), anyString())).thenReturn(String.valueOf(wowDir));
        when(configuration.getBoolean(eq(PropertyWarcraft4jConfig.CACHE_KEY), anyBoolean())).thenReturn(CACHE);
        when(configuration.getString(eq(PropertyWarcraft4jConfig.CACHE_DIR_KEY), anyString())).thenReturn(String.valueOf(cacheDir));
        when(configuration.getString(eq(PropertyWarcraft4jConfig.LOCALE_KEY))).thenReturn(String.valueOf(LOCALE));
        when(configuration.getString(eq(PropertyWarcraft4jConfig.REGION_KEY))).thenReturn(String.valueOf(REGION));
        when(configuration.getString(eq(PropertyWarcraft4jConfig.BRANCH_KEY))).thenReturn(String.valueOf(BRANCH));
    }

    @Test(expected = Warcraft4jConfigException.class)
    public void shouldThrowExceptionForEmptyConfig() {
        when(configuration.isEmpty()).thenReturn(true);

        new PropertyWarcraft4jConfig(configuration);
    }

    @Test(expected = Warcraft4jConfigException.class)
    public void shouldThrowExceptionForNullConfig() {
        new PropertyWarcraft4jConfig(null);
    }

    @Test
    public void shouldParseSettings() {
        PropertyWarcraft4jConfig config = new PropertyWarcraft4jConfig(configuration);

        assertEquals(ONLINE, config.isOnline());
        assertEquals(wowDir, config.getWowInstallationDirectory());
        assertEquals(CACHE, config.isCaching());
        assertEquals(cacheDir, config.getCacheDirectory());
        assertEquals(LOCALE, config.getLocale());
        assertEquals(REGION, config.getRegion());
        assertEquals(BRANCH, config.getBranch());
    }

    @Test
    public void shouldCreateCacheDirectoryIfNotExisting() throws Exception {
        Path dir = testDir.resolve("newCacheDir");
        if (Files.exists(dir)) {
            delete(dir);
        }
        assertTrue("Cache directory not deleted.", Files.notExists(dir));
        when(configuration.getString(eq(PropertyWarcraft4jConfig.CACHE_DIR_KEY), anyString())).thenReturn(String.valueOf(dir));

        PropertyWarcraft4jConfig config = new PropertyWarcraft4jConfig(configuration);

        assertTrue("Cache directory does not exist.", Files.exists(config.getCacheDirectory()));
        assertTrue("Cache directory is not a directory.", Files.isDirectory(config.getCacheDirectory()));
        assertTrue("Cache directory is not readable.", Files.isReadable(config.getCacheDirectory()));
    }

    @Test
    public void shouldNotCreateCacheDirectoryIfExisting() throws Exception {
        Files.createDirectories(cacheDir);
        assertTrue("Cache directory does not exist after creation.", Files.exists(cacheDir));
        Path testFile = Files.createTempFile(cacheDir, "testFile", "tmp");
        long creationTime = Files.getLastModifiedTime(cacheDir).toMillis();

        PropertyWarcraft4jConfig config = new PropertyWarcraft4jConfig(configuration);


        assertTrue("Cache directory does not exist.", Files.exists(config.getCacheDirectory()));
        assertTrue("Cache directory is not a directory.", Files.isDirectory(config.getCacheDirectory()));
        assertTrue("Cache directory is not readable.", Files.isReadable(config.getCacheDirectory()));
        assertEquals("Cache directory modification time changed after initialisation", creationTime, Files.getLastModifiedTime(config.getCacheDirectory()).toMillis());
        assertTrue("Test file in cache directory does not exist anymore.", Files.exists(testFile));
    }

    @Test(expected = Warcraft4jConfigException.class)
    public void shouldThrowExceptionForFileCacheDir() throws Exception {
        Path testFile = Files.createTempFile(testDir, "cacheDir", "file");

        when(configuration.getString(eq(PropertyWarcraft4jConfig.CACHE_DIR_KEY), anyString())).thenReturn(String.valueOf(testFile));

        new PropertyWarcraft4jConfig(configuration);
    }

    @Test(expected = Warcraft4jConfigException.class)
    public void shouldThrowExceptionForNonExistingWowDir() throws Exception {
        Path invalidWowDir = testDir.resolve("invalidWowDir");
        when(configuration.getString(eq(PropertyWarcraft4jConfig.WOW_DIR_KEY), anyString())).thenReturn(String.valueOf(invalidWowDir));

        new PropertyWarcraft4jConfig(configuration);
    }
}