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
package nl.salp.warcraft4j.clientdata;

import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.DefaultConfigurationBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class DefaultClientDataConfiguration implements ClientDataConfiguration {
    private static final String ONLINE_KEY = "w4j.casc.online";
    private static final boolean ONLINE_DEFAULT = false;
    private static final String WOW_DIR_KEY = "w4j.wow.directory";
    private static final String WOW_DIR_DEFAULT = "C:\\Program Files (x86)\\World of Warcraft";
    private static final String CACHE_KEY = "w4j.data.cache";
    private static final boolean CACHE_DEFAULT = true;
    private static final String CACHE_DIR_KEY = "w4j.data.cache.directory";
    private static final String CACHE_DIR_DEFAULT = "C:\\temp\\w4j\\data\\cache";
    private static final String LOCALE_KEY = "w4j.wow.locale";
    private static final String REGION_KEY = "w4j.wow.region";
    private static final String BRANCH_KEY = "w4j.wow.branch";

    private boolean online;
    private Path wowDir;
    private boolean cache;
    private Path cacheDir;
    private Locale locale;
    private Region region;
    private Branch branch;

    public DefaultClientDataConfiguration(Configuration configuration) {
        initialise(configuration);
    }

    private void initialise(Configuration configuration) {
        if (configuration == null || configuration.isEmpty()) {
            throw new ClientDataConfigurationException("Can't create a client data configuration from an empty configuration.");
        }
        online = configuration.getBoolean(ONLINE_KEY, ONLINE_DEFAULT);

        wowDir = Paths.get(configuration.getString(WOW_DIR_KEY, WOW_DIR_DEFAULT));
        if (Files.notExists(wowDir) || !Files.isDirectory(wowDir) || !Files.isReadable(wowDir)) {
            throw new ClientDataConfigurationException(format("WoW installation directory %s does not exist or can't be read.", wowDir));
        }

        cache = configuration.getBoolean(CACHE_KEY, CACHE_DEFAULT);
        if (cache) {
            cacheDir = Paths.get(configuration.getString(CACHE_DIR_KEY, CACHE_DIR_DEFAULT));
            if (Files.notExists(cacheDir)) {
                try {
                    Files.createDirectories(cacheDir);
                } catch (IOException e) {
                    throw new ClientDataConfigurationException(format("Unable to create cache directory %s.", cacheDir), e);
                }
            } else if (!Files.isDirectory(cacheDir) || !Files.isReadable(cacheDir) || !Files.isWritable(cacheDir)) {
                throw new ClientDataConfigurationException(format("Cache directory %s is either not a directory or not accessible.", cacheDir));
            }
        }

        locale = Locale.getLocale(configuration.getString(LOCALE_KEY))
                .orElseThrow(() -> new ClientDataConfigurationException(format("Locale %s is not a valid locale.", configuration.getString(LOCALE_KEY))));
        region = Region.getRegion(configuration.getString(REGION_KEY))
                .orElseThrow(() -> new ClientDataConfigurationException(format("Region %s is not a valid region.", configuration.getString(REGION_KEY))));
        branch = Branch.getBranch(configuration.getString(BRANCH_KEY))
                .orElseThrow(() -> new ClientDataConfigurationException(format("Branch %s is not a valid branch.", configuration.getString(BRANCH_KEY))));
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

    public static DefaultClientDataConfiguration fromFile(String configFile) throws ClientDataConfigurationException {
        try {
            return new DefaultClientDataConfiguration(new DefaultConfigurationBuilder(configFile).getConfiguration());
        } catch (ConfigurationException e) {
            throw new ClientDataConfigurationException(format("Error creating client data configuration from %s", configFile), e);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private boolean online;
        private String wowDir;
        private boolean caching;
        private String cacheDir;
        private String locale;
        private String region;
        private String branch;

        public Builder online(boolean online) {
            this.online = online;
            return this;
        }

        public Builder withWowDir(String wowDir) {
            this.wowDir = wowDir;
            return this;
        }

        public Builder withWowDir(Path wowDir) {
            this.wowDir = wowDir == null ? null : wowDir.toString();
            return this;
        }

        public Builder caching(boolean caching) {
            this.caching = caching;
            return this;
        }

        public Builder withCacheDir(String cacheDir) {
            this.cacheDir = cacheDir;
            return this;
        }

        public Builder withCacheDir(Path cacheDir) {
            this.cacheDir = cacheDir == null ? null : cacheDir.toString();
            return this;
        }


        public Builder withLocale(String locale) {
            this.locale = locale;
            return this;
        }

        public Builder withLocale(Locale locale) {
            this.locale = locale != null ? locale.name() : null;
            return this;
        }

        public Builder withRegion(String region) {
            this.region = region;
            return this;
        }

        public Builder withRegion(Region region) {
            this.region = region != null ? region.name() : null;
            return this;
        }

        public Builder withBranch(String branch) {
            this.branch = branch;
            return this;
        }

        public Builder withBranch(Branch branch) {
            this.branch = branch != null ? branch.name() : null;
            return this;
        }

        public DefaultClientDataConfiguration build() {
            Configuration config = new BaseConfiguration();
            config.addProperty(ONLINE_KEY, online);
            config.addProperty(WOW_DIR_KEY, wowDir);
            config.addProperty(CACHE_KEY, caching);
            config.addProperty(CACHE_DIR_KEY, cacheDir);
            config.addProperty(LOCALE_KEY, locale);
            config.addProperty(REGION_KEY, region);
            config.addProperty(BRANCH_KEY, branch);
            return new DefaultClientDataConfiguration(config);
        }
    }
}
