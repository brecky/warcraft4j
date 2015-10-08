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
package nl.salp.warcraft4j.battlenet.guice;

import com.google.inject.Provider;
import com.google.inject.ProvisionException;
import nl.salp.warcraft4j.battlenet.BattlenetApiConfig;
import nl.salp.warcraft4j.config.Warcraft4jConfigException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Guice provider for the {@link BattlenetApiConfig Battle.NET API Configuration}
 *
 * @author Barre Dijkstra
 * @see BattlenetApiConfig
 */
public class BattlenetApiConfigFileProvider implements Provider<BattlenetApiConfig> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BattlenetApiConfigFileProvider.class);
    /** The name of the configuration file. */
    private final String configFile;

    /**
     * Create a new provider that creates the config based on the default file.
     */
    public BattlenetApiConfigFileProvider() {
        this(BattlenetApiConfig.CONFIG_FILENAME_DEFAULT);
    }

    /**
     * Create a new provider that creates the config based on the provided file.
     *
     * @param file The file.
     */
    public BattlenetApiConfigFileProvider(String file) {
        if (isEmpty(file)) {
            this.configFile = BattlenetApiConfig.CONFIG_FILENAME_DEFAULT;
        } else {
            this.configFile = file;
        }
    }

    /**
     * Load the Battle.NET API configuration.
     *
     * @return The loaded configuration.
     *
     * @throws ProvisionException When loading failed.
     */
    @Override
    public BattlenetApiConfig get() throws ProvisionException {
        BattlenetApiConfig config;
        try {
            config = BattlenetApiConfig.fromConfigFile(configFile);
        } catch (IOException e) {
            String message = format("I/O Error loading Battle.NET API configuration from %s", configFile);
            LOGGER.error(message, e);
            throw new ProvisionException(message, e);
        } catch (Warcraft4jConfigException e) {
            String message = format("Invalid Battle.NET API configuration file %s", configFile);
            LOGGER.error(message, e);
            throw new ProvisionException(message, e);
        }
        return config;
    }
}
