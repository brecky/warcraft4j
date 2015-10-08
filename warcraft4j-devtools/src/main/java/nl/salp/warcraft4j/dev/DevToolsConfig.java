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

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.config.Warcraft4jConfigException;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DevToolsConfig implements Warcraft4jConfig {
    private static final String WOW_REGION = "w4j.wow.region";
    private static final Region WOW_REGION_DEFAULT = Region.EUROPE;
    private static final String WOW_LOCALE = "w4j.wow.locale";
    private static final Locale WOW_LOCALE_DEFAULT = Locale.EN_US;
    private static final String WOW_BRANCH = "w4j.wow.branch";
    private static final Branch WOW_BRANCH_DEFAULT = Branch.LIVE;
    private static final String W4J_DIR = "w4j.path";
    private static final String WOW_DIR = "w4j.wow.path";
    private static final String DATA_EXTRACTED = "w4j.data.extracted";
    private static final String LISTFILE = "w4j.data.listfile";
    private static final String LISTFILE_DEFAULT = "${w4j.path}/data/listfile.txt";
    private static final String USE_CDN = "w4j.data.cdn";
    private static final boolean USE_CDN_DEFAULT = false;
    private static final String CDN_CACHE = "w4j.data.cdn.cache";
    private static final boolean CDN_CACHE_DEFAULT = false;
    private static final String CDN_CACHE_PATH = "w4j.data.cdn.cache.path";
    private static final String CDN_CACHE_PATH_DEFAULT = "${w4j.path}/data/cdncache";
    private static final String NEO4J_DATA_PATH = "w4j.data.neo4j.data.path";
    private static final String NEO4J_EXT_URI = "w4j.data.neo4j.ext.uri";
    private static final String NEO4J_EXT_USER = "w4j.data.neo4j.ext.uri";
    private static final String NEO4J_EXT_PASSWORD = "w4j.data.neo4j.ext.uri";
    private static final String MONGODB_URI = "w4j.data.mongdb.uri";
    private static final String MONGODB_USER = "w4j.data.mongdb.user";
    private static final String MONGODB_PASSWORD = "w4j.data.mongdb.password";

    private boolean online;
    private Path wowDir;
    private boolean cache;
    private Path cacheDir;
    private Locale locale;
    private Region region;
    private Branch branch;
    private Path w4jDir;
    private Path extractDataDir;
    private Path listFile;
    private Path neo4jDataPath;
    private String neo4jExtUri;
    private String neo4jExtUser;
    private String neo4jExtPassword;
    private String mongodbUri;
    private String mongodbUser;
    private String mongodbPassword;

    public DevToolsConfig(Configuration configuration) {
        initialise(configuration);
    }

    private void initialise(Configuration configuration) {
        if (configuration == null || configuration.isEmpty()) {
            throw new Warcraft4jConfigException("Can't create a Warcraft4J configuration from an empty configuration.");
        }
        online = configuration.getBoolean(USE_CDN, USE_CDN_DEFAULT);
        if (!configuration.containsKey(WOW_DIR)) {
            throw new Warcraft4jConfigException("WoW installation directory was not configured.");
        }
        wowDir = Paths.get(resolve(configuration.getString(WOW_DIR), configuration));
        if (Files.notExists(wowDir) || !Files.isDirectory(wowDir) || !Files.isReadable(wowDir)) {
            throw new Warcraft4jConfigException(format("WoW installation directory %s does not exist or can't be read.", wowDir));
        }
        w4jDir = Paths.get(resolve(configuration.getString(W4J_DIR), configuration));
        if (Files.notExists(w4jDir)) {
            try {
                Files.createDirectories(w4jDir);
            } catch (IOException e) {
                throw new Warcraft4jConfigException(format("Unable to create Warcraft4J working directory %s.", w4jDir), e);
            }
        } else if (!Files.isDirectory(w4jDir) || !Files.isReadable(w4jDir) || !Files.isWritable(w4jDir)) {
            throw new Warcraft4jConfigException(format("Warcraft4J working directory %s is either not a directory or not accessible.", w4jDir));
        }

        listFile = Paths.get(resolve(configuration.getString(LISTFILE, LISTFILE_DEFAULT), configuration));
        if (Files.notExists(listFile) || !Files.isRegularFile(listFile) || !Files.isReadable(listFile)) {
            listFile = null;
        }

        if (configuration.containsKey(DATA_EXTRACTED)) {
            extractDataDir = Paths.get(resolve(configuration.getString(DATA_EXTRACTED), configuration));
            if (Files.notExists(extractDataDir)) {
                try {
                    Files.createDirectories(extractDataDir);
                } catch (IOException e) {
                    throw new Warcraft4jConfigException(format("Unable to create extracted directory %s.", w4jDir), e);
                }
            } else if (!Files.isDirectory(w4jDir) || !Files.isReadable(w4jDir) || !Files.isWritable(w4jDir)) {
                throw new Warcraft4jConfigException(format("Extracted data directory %s is either not a directory or not accessible.", w4jDir));
            }
        }

        cache = configuration.getBoolean(CDN_CACHE, CDN_CACHE_DEFAULT);
        if (cache) {
            cacheDir = Paths.get(resolve(configuration.getString(CDN_CACHE_PATH, CDN_CACHE_PATH_DEFAULT), configuration));
            if (Files.notExists(cacheDir)) {
                try {
                    Files.createDirectories(cacheDir);
                } catch (IOException e) {
                    throw new Warcraft4jConfigException(format("Unable to create cache directory %s.", cacheDir), e);
                }
            } else if (!Files.isDirectory(cacheDir) || !Files.isReadable(cacheDir) || !Files.isWritable(cacheDir)) {
                throw new Warcraft4jConfigException(format("Cache directory %s is either not a directory or not accessible.", cacheDir));
            }
        }
        locale = Locale.getLocale(resolve(configuration.getString(WOW_LOCALE, valueOf(WOW_LOCALE_DEFAULT)), configuration))
                .orElseThrow(() -> new Warcraft4jConfigException(format("Locale %s is not a valid locale.", resolve(configuration.getString(WOW_LOCALE), configuration))));
        region = Region.getRegion(resolve(configuration.getString(WOW_REGION, valueOf(WOW_REGION_DEFAULT)), configuration))
                .orElseThrow(() -> new Warcraft4jConfigException(format("Region %s is not a valid region.", resolve(configuration.getString(WOW_REGION), configuration))));
        branch = Branch.getBranch(resolve(configuration.getString(WOW_BRANCH, valueOf(WOW_BRANCH_DEFAULT)), configuration))
                .orElseThrow(() -> new Warcraft4jConfigException(format("Branch %s is not a valid branch.", resolve(configuration.getString(WOW_BRANCH), configuration))));
        mongodbUri = configuration.getString(MONGODB_URI, null);
        mongodbUser = configuration.getString(MONGODB_USER, null);
        mongodbPassword = configuration.getString(MONGODB_PASSWORD, null);
        if (configuration.containsKey(NEO4J_DATA_PATH)) {
            neo4jDataPath = Paths.get(resolve(configuration.getString(NEO4J_DATA_PATH), configuration));
            if (Files.notExists(neo4jDataPath)) {
                try {
                    Files.createDirectories(neo4jDataPath);
                } catch (IOException e) {
                    throw new Warcraft4jConfigException(format("Unable to create Neo4J data directory %s.", neo4jDataPath), e);
                }
            } else if (!Files.isDirectory(neo4jDataPath) || !Files.isReadable(neo4jDataPath) || !Files.isWritable(neo4jDataPath)) {
                throw new Warcraft4jConfigException(format("Neo4J data directory %s is either not a directory or not accessible.", neo4jDataPath));
            }
        }
        neo4jExtUri = configuration.getString(NEO4J_EXT_URI, null);
        neo4jExtUser = configuration.getString(NEO4J_EXT_USER, null);
        neo4jExtPassword = configuration.getString(NEO4J_EXT_PASSWORD, null);
    }

    private String resolve(String value, Configuration configuration) {
        String resolvedValue = value;
        if (isNotEmpty(value)) {
            int searchIndex = resolvedValue.indexOf("${");
            while (searchIndex < resolvedValue.length() - 2 && searchIndex > -1) {
                int end = resolvedValue.indexOf("}", searchIndex) + 1;
                if (end > -1) {
                    String replaceKey = resolvedValue.substring(searchIndex + 2, end - 1);
                    if (configuration.containsKey(replaceKey)) {
                        resolvedValue = resolvedValue.substring(0, searchIndex) + configuration.getString(replaceKey) + resolvedValue.substring(end);
                        searchIndex = resolvedValue.indexOf("${", searchIndex);
                    } else {
                        searchIndex = resolvedValue.indexOf("${", end);
                    }
                } else {
                    searchIndex = resolvedValue.indexOf("${", searchIndex + 2);
                }
            }
        }
        return value;
    }

    @Override
    public boolean isOnline() {
        return online;
    }

    @Override
    public Path getWowInstallationDirectory() {
        return wowDir;
    }

    @Override
    public boolean isCaching() {
        return cache;
    }

    @Override
    public Path getCacheDirectory() {
        return cacheDir;
    }

    @Override
    public Locale getLocale() {
        return locale;
    }

    @Override
    public Region getRegion() {
        return region;
    }

    @Override
    public Branch getBranch() {
        return branch;
    }

    public Path getWorkingDirectory() {
        return w4jDir;
    }

    public boolean isExtractDataAvailable() {
        return extractDataDir != null;
    }

    public Path getExtractDataDirectory() {
        return extractDataDir;
    }

    public boolean isListFileAvailable() {
        return listFile != null;
    }

    public Path getListFilePath() {
        return listFile;
    }

    public boolean isNeo4jConfigured() {
        return isNeo4jEmbedded() || isNeo4jExternal();
    }

    public boolean isNeo4jEmbedded() {
        return neo4jDataPath != null;
    }

    public Path getNeo4jDataPath() {
        return neo4jDataPath;
    }

    public boolean isNeo4jExternal() {
        return !isNeo4jEmbedded() && isNotEmpty(neo4jExtUri);
    }

    public String getNeo4jExtPassword() {
        return neo4jExtPassword;
    }

    public String getNeo4jExtUri() {
        return neo4jExtUri;
    }

    public String getNeo4jExtUser() {
        return neo4jExtUser;
    }

    public boolean isMongodbConfigured() {
        return isNotEmpty(mongodbUri);
    }

    public String getMongodbPassword() {
        return mongodbPassword;
    }

    public String getMongodbUri() {
        return mongodbUri;
    }

    public String getMongodbUser() {
        return mongodbUser;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static DevToolsConfig fromFile(String configFile) throws Warcraft4jConfigException {
        try {

            return new DevToolsConfig(new PropertiesConfiguration(configFile));
        } catch (org.apache.commons.configuration.ConfigurationException e) {
            throw new Warcraft4jConfigException(format("Error creating Warcraft4J development tools configuration from %s", configFile), e);
        }
    }
}
