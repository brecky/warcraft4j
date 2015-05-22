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
package nl.salp.warcraft4j.dev.dbc.codegen.dbcmapping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Properties;

import static java.lang.String.format;

/**
 * Configuration for {@link DbcMappingGenerator}.
 *
 * @author Barre Dijkstra
 */
public class DbcMappingGeneratorConfig {
    /** The path of the configuration file on the classpath. */
    private static final String CONFIG_FILE = "/w4j_dbcmapping.config";
    /** The path of the template file on the classpath. */
    private static final String TEMPLATE_FILE = "/templates/dbcmapping.vm";
    /** The configuration key of the directory to place the generated files in. */
    private static final String KEY_TARGET_DIRECTORY = "w4j.dbcmapping.target.directory";
    /** The configuration key of the package for the generated files. */
    private static final String KEY_TARGET_PACKAGE = "w4j.dbcmapping.target.package";
    /** The configuration key of the flag that indicates if existing generated classes should be overwritten. */
    private static final String KEY_TARGET_OVERRIDE = "w4j.dbcmapping.target.override";

    private final String templateFile;
    private final String targetDirectory;
    private final String targetPackage;
    private final boolean targetOverride;

    public DbcMappingGeneratorConfig() throws IOException {
        this(CONFIG_FILE);
    }

    public DbcMappingGeneratorConfig(String configFile) throws IOException {
        Properties properties = new Properties();
        try (InputStream in = DbcMappingGeneratorConfig.class.getResourceAsStream(configFile)) {
            properties.load(in);
        }
        if (!properties.containsKey(KEY_TARGET_DIRECTORY)) {
            throw new IllegalArgumentException(format("No target directory (%s) specified in the DBC mapping generator configuration file (%s)", KEY_TARGET_DIRECTORY, configFile));
        }
        if (!properties.containsKey(KEY_TARGET_PACKAGE)) {
            throw new IllegalArgumentException(format("No target package (%s) specified in the DBC mapping generator configuration file (%s)", KEY_TARGET_PACKAGE, configFile));
        }
        if (!properties.containsKey(KEY_TARGET_OVERRIDE)) {
            throw new IllegalArgumentException(format("No override directive (%s) specified in the DBC mapping generator configuration file (%s)", KEY_TARGET_OVERRIDE, configFile));
        }
        this.templateFile = TEMPLATE_FILE;
        this.targetDirectory = properties.getProperty(KEY_TARGET_DIRECTORY);
        this.targetPackage = properties.getProperty(KEY_TARGET_PACKAGE);
        this.targetOverride = Boolean.valueOf(properties.getProperty(KEY_TARGET_OVERRIDE));
    }


    public DbcMappingGeneratorConfig(String templateFile, String targetDirectory, String targetPackage, boolean targetOverride) {
        this.templateFile = templateFile;
        this.targetDirectory = targetDirectory;
        this.targetPackage = targetPackage;
        this.targetOverride = targetOverride;
    }

    public String getTemplateFileClasspathPath() {
        return templateFile;
    }

    public String getTemplateFilePath() throws IOException {
        return getTemplateFile().getPath();
    }

    public File getTemplateFile() throws IOException {
        try {
            return new File(DbcMappingGeneratorConfig.class.getResource(templateFile).toURI()).getCanonicalFile();
        } catch (URISyntaxException e) {
            throw new IOException(format("Unable to create an URI for template file %s", templateFile), e);
        }
    }

    public String getTargetDirectoryPath() throws IOException {
        return getTargetDirectory().getPath();
    }

    public File getTargetDirectory() throws IOException {
        return new File(targetDirectory).getCanonicalFile();
    }

    public String getTargetPackage() {
        return targetPackage;
    }

    public boolean overrideEntries() {
        return targetOverride;
    }

    public File getFullTargetDirectory() throws IOException {
        String packagePath = getTargetPackage().replace('.', File.separatorChar);
        return new File(targetDirectory, packagePath).getCanonicalFile();
    }
}
