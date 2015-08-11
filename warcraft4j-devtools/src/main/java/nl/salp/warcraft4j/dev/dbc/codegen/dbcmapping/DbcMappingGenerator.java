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

import nl.salp.warcraft4j.data.dbc.DbcFile;
import nl.salp.warcraft4j.data.dbc.DbcStringTable;
import nl.salp.warcraft4j.data.dbc.DbcUtil;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import nl.salp.warcraft4j.dev.dbc.codegen.VelocityGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * Simple velocity template based file generator for generating DBC mapping entry classes ({@link nl.salp.warcraft4j.data.dbc.DbcEntry}).
 *
 * @author Barre Dijkstra
 */
public class DbcMappingGenerator extends VelocityGenerator<DbcFile> {
    /** The logging instance. */
    private static final Logger LOGGER = LoggerFactory.getLogger(DbcMappingGenerator.class);
    /** The velocity dbc information template parameter. */
    private static final String PARAM_DBC_INFORMATION = "dbc";
    /** The velocity author template parameter. */
    private static final String PARAM_AUTHOR = "author";
    /** TODO Move to the configuration file. */
    private static final String VALUE_AUTHOR = "Barre Dijkstra";
    /** The velocity entry information template parameter. */
    private static final String PARAM_ENTRY_INFORMATION = "entry";

    /** The dbc mapping generator configuration file. */
    private final DbcMappingGeneratorConfig config;
    /** The general development tools configuration file. */
    private final DevToolsConfig generalConfig;


    /**
     * Create a new DbcMappingGenerator using the configuration files.
     *
     * @throws IOException              When the configuration file could not be read.
     * @throws IllegalArgumentException When the generator could not be initialised.
     */
    public DbcMappingGenerator() throws IOException, IllegalArgumentException {
        this(new DevToolsConfig(), new DbcMappingGeneratorConfig());
    }

    /**
     * Create a new DbcMappingGenerator using a specific configuration.
     *
     * @param generalConfig The general development tools configuration file.
     * @param config        The dbc mapping generator configuration file.
     *
     * @throws IllegalArgumentException When the generator could not be initialised.
     */
    public DbcMappingGenerator(DevToolsConfig generalConfig, DbcMappingGeneratorConfig config) throws IllegalArgumentException {
        super(config.getTemplateFileClasspathPath(), config.overrideEntries());
        this.config = config;
        this.generalConfig = generalConfig;
        try {
            validateConfigurationPaths();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating a new DbcMappingGenerator instance", e);
        }
    }

    public void generate() throws IOException {
        File targetDir = config.getFullTargetDirectory();
        if (!targetDir.exists() && !targetDir.mkdirs()) {
            throw new IllegalArgumentException(format("Unable to create output directory %s", targetDir.getPath()));
        }
        Collection<DbcFile> files = Stream.of(DbcUtil.getAllClientDatabaseFiles(generalConfig.getDbcDirectoryPath()))
                .map(f -> new DbcFile(f, DbcUtil.getFileDataReaderSupplier(generalConfig.getDbcDirectoryPath(), f)))
                .collect(Collectors.toSet());
        LOGGER.debug("Attempting to generate {} mappings (overriding: {}) to directory {}", files.size(), config.overrideEntries(), targetDir.getPath());
        generate(files);
    }


    @Override
    protected File getOutputFile(DbcFile dbcFile) throws IOException {
        String fileName = getEntryClassName(dbcFile.getDbcName()) + ".java";
        return new File(config.getFullTargetDirectory(), fileName);
    }

    @Override
    protected Map<String, Object> getParameters(DbcFile dbcFile) {
        Map<String, Object> params = new HashMap<>();
        params.put(PARAM_AUTHOR, VALUE_AUTHOR);
        params.put(PARAM_DBC_INFORMATION, getDbcInformation(dbcFile));
        params.put(PARAM_ENTRY_INFORMATION, getEntryInformation(dbcFile));
        return params;
    }

    private DbcInformation getDbcInformation(DbcFile dbcFile) {
        DbcEntryInformation entryInformation = new DbcEntryInformation(dbcFile.getHeader().getEntryCount(), dbcFile.getHeader().getEntryFieldCount(), dbcFile.getHeader().getEntrySize());
        DbcStringTable stringTable = dbcFile.parseStringTable();
        DbcStringTableInformation stringTableInformation = new DbcStringTableInformation(stringTable.getNumberOfEntries(), dbcFile.getHeader().getStringTableBlockSize(), stringTable.getNumberOfEntries() > 0);
        return new DbcInformation(dbcFile.getDbcName(), dbcFile.getHeader().getMagicString(), entryInformation, stringTableInformation);
    }

    private EntryInformation getEntryInformation(DbcFile dbcFile) {
        String targetPackage = config.getTargetPackage();
        String className = getEntryClassName(dbcFile.getDbcName());
        String dbcType = getDbcTypeName(dbcFile.getDbcName());
        int entrySize = dbcFile.getHeader().getEntryFieldCount() * 4;
        int sizeDifference = (entrySize - dbcFile.getHeader().getEntrySize());
        boolean invalidSize = (sizeDifference != 0);
        return new EntryInformation(targetPackage, className, dbcType, invalidSize, sizeDifference, entrySize);
    }

    private String getEntryClassName(String dbcFileName) {
        String className = dbcFileName.substring(0, dbcFileName.lastIndexOf('.'));
        if (Character.isLowerCase(className.charAt(0))) {
            className = Character.toUpperCase(className.charAt(0)) + className.substring(1);
        }
        className = className.replace("_", "");
        return className;
    }

    private String getDbcTypeName(String dbcFileName) {
        StringBuilder typeName = new StringBuilder();
        boolean first = true;
        for (char c : getEntryClassName(dbcFileName).toCharArray()) {
            if (Character.isLetter(c) && Character.isUpperCase(c)) {
                if (first) {
                    first = false;
                } else {
                    typeName.append('_');
                }
            }
            typeName.append(Character.toUpperCase(c));
        }
        return typeName.toString();
    }

    @Override
    protected Logger getLogger() {
        return LOGGER;
    }

    /**
     * Validate the configuration paths.
     *
     * @throws IOException When a configured path is invalid.
     */
    private void validateConfigurationPaths() throws IOException {
        LOGGER.debug("Generating entries from .DBC and .DB2 files in {} based on template {}.",
                generalConfig.getDbcDirectoryPath(),
                config.getTemplateFilePath()
        );
        LOGGER.debug("Placing generated entries in directory {} and package {} ({}),{} overriding existing file.",
                config.getTargetDirectoryPath(),
                config.getTargetPackage(),
                config.getFullTargetDirectory().getCanonicalPath(),
                config.overrideEntries() ? "" : " not"
        );
    }

    /**
     * Stand-alone program entry point which generates the DBC mapping entry classes based on the configuration file on the classpath.
     *
     * @param args The command line arguments.
     *
     * @throws Exception When execution failed.
     */
    public static void main(String... args) throws Exception {
        new DbcMappingGenerator().generate();
    }
}
