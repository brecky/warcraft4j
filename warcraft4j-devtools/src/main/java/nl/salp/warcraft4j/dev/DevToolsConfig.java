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
package nl.salp.warcraft4j.dev;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DevToolsConfig {
    /** The path of the configuration file on the classpath. */
    private static final String CONFIG_FILE = "/w4j_devtools.config";
    /** The configuration key of the directory that contains extracted DBC and DB2 files. */
    private static final String KEY_DIRECTORY_DBC = "w4j.directory.dbc";
    /** The configuration key of the World of Warcraft installation directory. */
    private static final String KEY_DIRECTORY_WOW = "w4j.directory.wow";

    /** The path to the directory that contains extracted DBC and DB2 files. */
    private final String dbcDirectory;
    /** The World of Warcarft installation directory. */
    private final String wowDirectory;

    public DevToolsConfig() throws IOException {
        this(CONFIG_FILE);
    }

    public DevToolsConfig(String configFile) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = DevToolsConfig.class.getResourceAsStream(configFile)) {
            properties.load(in);
        }
        if (!properties.containsKey(KEY_DIRECTORY_DBC)) {
            throw new IllegalArgumentException(format("No directory for extracted DBC/DB2 files (%s) specified in the configuration file (%s)", KEY_DIRECTORY_DBC, configFile));
        }
        if (!properties.containsKey(KEY_DIRECTORY_WOW)) {
            throw new IllegalArgumentException(format("No World of Warcarft installation directory (%s) specified in the configuration file (%s)", KEY_DIRECTORY_WOW, configFile));
        }
        this.dbcDirectory = new File(properties.getProperty(KEY_DIRECTORY_DBC)).getCanonicalPath();
        this.wowDirectory = new File(properties.getProperty(KEY_DIRECTORY_WOW)).getCanonicalPath();
    }

    public DevToolsConfig(String dbcDirectory, String wowDirectory) throws IOException {
        this.dbcDirectory = new File(dbcDirectory).getCanonicalPath();
        this.wowDirectory = new File(wowDirectory).getCanonicalPath();
    }

    public String getDbcDirectoryPath() {
        return dbcDirectory;
    }

    public File getDbcDirectory() {
        return new File(dbcDirectory);
    }

    public String getWowDirectoryPath() {
        return wowDirectory;
    }

    public File getWowDirectory() {
        return new File(wowDirectory);
    }
}
