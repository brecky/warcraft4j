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
package nl.salp.warcraft4j.dev.casc.dbc;

import nl.salp.warcraft4j.casc.CascFile;
import nl.salp.warcraft4j.casc.CascService;
import nl.salp.warcraft4j.casc.FileHeader;
import nl.salp.warcraft4j.casc.RootEntry;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.cdn.CdnCascService;
import nl.salp.warcraft4j.casc.cdn.local.LocalCdnCascContext;
import nl.salp.warcraft4j.fileformat.dbc.DbcHeader;
import nl.salp.warcraft4j.dev.DevToolsConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DbcAnalyser {
    private static final Logger LOGGER = LoggerFactory.getLogger(DbcAnalyser.class);
    private final DevToolsConfig config;
    private final CdnCascContext cascContext;
    // private final ListFile listFile;
    private final CascService cascService;

    public DbcAnalyser(DevToolsConfig config) throws Exception {
        this.config = config;
        LOGGER.debug("Loading CASC context");
        cascContext = new LocalCdnCascContext(config);
        /*
        LOGGER.debug("Loading list file");
        listFile = ListFile.fromFile(config.getListFilePath());
        LOGGER.debug("Populating CASC context with list file resolved names");
        listFile.getCascFiles().stream()
                .filter(file -> file.getFilename()
                        .filter(DbcAnalyser::isDbcFile)
                        .isPresent())
                .forEach(file -> cascContext.resolve(file.getFilename().get().toUpperCase(), file.getFilenameHash()));
        */
        LOGGER.debug("Loading CASC service");
        this.cascService = new CdnCascService(cascContext);
    }

    private Set<CascFile> getDbcFiles() {
        LOGGER.debug("Getting all DBC files");
        return cascService.getAllRootEntries().stream()
                .map(RootEntry::getFilenameHash)
                .distinct()
                .map(cascService::getCascFile)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(DbcAnalyser::isDbcFile)
                .filter(cascService::isDataAvailable)
                .collect(Collectors.toSet());
    }

    private static boolean isDbcFile(CascFile cascFile) {
        boolean dbcFile = false;
        if (cascFile != null) {
            if (cascFile.getFilename().isPresent()) {
                dbcFile = cascFile.getFilename().filter(DbcAnalyser::isDbcFile).isPresent();
            } else {
                dbcFile = cascFile.getHeader().filter(DbcAnalyser::isDbcFile).isPresent();
            }
        }
        return dbcFile;
    }

    private static boolean isDbcFile(String filename) {
        return Optional.ofNullable(filename)
                .map(String::toLowerCase)
                .filter(fn -> fn.endsWith(".dbc") || fn.endsWith(".db2"))
                .isPresent();
    }

    private static boolean isDbcFile(FileHeader header) {
        return Optional.ofNullable(header)
                .map(FileHeader::getHeader)
                .filter(h -> h.length > 0)
                .map(String::new)
                .filter(magicString -> magicString.equals(DbcHeader.DBC_MAGICSTRING) || magicString.equals(DbcHeader.DB2_MAGICSTRING))
                .isPresent();
    }

    private Set<String> getCascResolvedDbcFilesByName() {
        LOGGER.debug("Getting all CASC DBC files with a resolved name.");
        return cascContext.getResolvedFilenames().stream()
                .filter(DbcAnalyser::isDbcFile)
                .distinct()
                .collect(Collectors.toSet());
    }

    private Set<String> getListFileDbcFilesByName() {
        LOGGER.debug("Getting all ListFile DBC files with a resolved name.");
        /*
        return listFile.getFilenames().stream()
                .filter(DbcAnalyser::isDbcFile)
                .map(String::toUpperCase)
                .distinct()
                .collect(Collectors.toSet());
        */
        return null;
    }

    private Set<String> getAllDbcFilesByName() {
        LOGGER.debug("Getting all DBC files with a resolved name.");
        return Stream.concat(getCascResolvedDbcFilesByName().stream(), getListFileDbcFilesByName().stream())
                .filter(StringUtils::isNotEmpty)
                .distinct()
                .collect(Collectors.toSet());
    }

    private Set<String> getKnownDbcFilesByName() {
        LOGGER.debug("Getting all known CASC DBC files with a resolved name.");
        return getAllDbcFilesByName().stream()
                .filter(cascContext::isRegisteredData)
                .collect(Collectors.toSet());
    }

    private Set<String> getInvalidDbcFilesByName() {
        LOGGER.debug("Getting all DBC files with a resolved name that are not in the CASC.");
        return getAllDbcFilesByName().stream()
                .filter(this::isNotRegistered)
                .collect(Collectors.toSet());
    }

    private Set<String> getDbcFilesWithNoDataByName() {
        LOGGER.debug("Getting all CASC registered DBC files with a resolved name that have no data in the CASC.");
        return getAllDbcFilesByName().stream()
                .filter(cascContext::isRegistered)
                .filter(this::isNotRegisteredData)
                .collect(Collectors.toSet());
    }

    private boolean isNotRegistered(String filename) {
        return !cascContext.isRegistered(filename);
    }

    private boolean isNotRegisteredData(String filename) {
        return !cascContext.isRegisteredData(filename);
    }

    public void analyse() {
        /*
        Set<String> knownDbcFileNames = getKnownDbcFilesByName();
        Set<String> invalidDbcFileNames = getInvalidDbcFilesByName();
        Set<String> noDataDbcFileNames = getDbcFilesWithNoDataByName();
        System.out.println(format("------------------------[  KNOWN DBC FILES (%d)  ]------------------------", knownDbcFileNames.size()));
        System.out.println(format("------------------------[ INVALID DBC FILES (%d) ]------------------------", invalidDbcFileNames.size()));
        invalidDbcFileNames.stream().forEach(System.out::println);
        System.out.println(format("------------------------[ NO DATA DBC FILES (%d) ]------------------------", noDataDbcFileNames.size()));
        noDataDbcFileNames.stream().forEach(System.out::println);
        */
        // Force CASC loading.
        final Set<Long> knownHashes = cascContext.getHashes();
        final int maxChars = 20;
        System.out.println(format("Brute forcing names with up to %d characters", maxChars));
        final AtomicLong count = new AtomicLong(0);
        final Map<String, Long> resolvedNames = new HashMap<>();
        new DbcFilenameGenerator(maxChars, () -> (filename) -> {
            long hash = CdnCascContext.hashFilename(filename);
            if (knownHashes.contains(hash)) {
                resolvedNames.put(filename, hash);
                LOGGER.debug("Resolved filename {} to CASC known hash {}", filename, hash);
            }
            count.incrementAndGet();
        }).execute();
        LOGGER.info("Attempted hashing resolution on {} filenames against {} known hashes, resulting in {} resolved CASC hashes.", count.get(), knownHashes.size(), resolvedNames.size());

        /*
        getDbcFiles().stream()
                .forEach(f -> System.out.println(format("DbcFile [hash: %d, filename: %s, header: %s]",
                        f.getFilenameHash(),
                        f.getFilename()
                                .orElse("<unknown>"),
                        f.getHeader()
                                .map(FileHeader::getHeader)
                                .map(String::new)
                                .orElse(""))));
        */
    }

    public static void main(String... args) throws Exception {
        DbcAnalyser analyser = new DbcAnalyser(DevToolsConfig.fromFile("w4j_devtools.config"));
        analyser.analyse();
    }
}
