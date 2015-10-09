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
package nl.salp.warcraft4j.dev.casc.listfile;

import nl.salp.warcraft4j.Branch;
import nl.salp.warcraft4j.Locale;
import nl.salp.warcraft4j.Region;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.cdn.local.LocalCdnCascContext;
import nl.salp.warcraft4j.config.Warcraft4jConfig;
import nl.salp.warcraft4j.casc.cdn.online.OnlineCdnCascContext;
import nl.salp.warcraft4j.config.Warcraft4jConfigBuilder;
import nl.salp.warcraft4j.dev.casc.EntryStore;
import nl.salp.warcraft4j.dev.casc.model.ListFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class ListFileGenerator {
    private static final Logger LOGGER = LoggerFactory.getLogger(ListFileGenerator.class);
    private static final String FILE_IN_LISTFILE = "listfile-wow6.txt";
    private static final String FILE_OUT_CASC_MISSING = "w4j-casc-missing-%s.txt";
    private static final String FILE_OUT_LISTFILE_INVALID = "w4j-listfile-invalid-%s.txt";
    private static final String FILE_OUT_CASC_INVALID = "w4j-casc-invalid-%s.txt";
    private static final String FILE_OUT_LISTFILE = "w4j-listfile-%s.txt";
    private final Warcraft4jConfig warcraft4jConfig;
    private final Path outputPath;
    private final Path fileListfile;
    private final Path fileCascInvalidEntries;
    private final Path fileCascUnknownEntries;
    private final Path fileListfileInvalidEntries;
    private final CdnCascContext cascContext;
    private final ListFile listFile;
    private final EntryStore entryStore;

    public ListFileGenerator(Path wowPath, Path outputPath, Path cachePath) throws IOException {
        LOGGER.debug("Initialising listfile generator for installation path {}, cache path {} and using ouput directory {}", wowPath, outputPath,
                cachePath);
        this.warcraft4jConfig = initialiseConfig(wowPath, cachePath);
        LOGGER.debug("Initialising casc context (branch: {}, region: {}, locale: {}, online: {}, caching: {})",
                warcraft4jConfig.getBranch(), warcraft4jConfig.getRegion(), warcraft4jConfig.getLocale(), warcraft4jConfig.isOnline(), warcraft4jConfig.isCaching());
        this.cascContext = initialiseCascContext(warcraft4jConfig);

        this.outputPath = outputPath;
        String version = cascContext.getVersion().replace('.', '_');
        LOGGER.debug("Initialising listfile from output path {} and output files with version {}", outputPath, version);
        this.fileListfile = outputPath.resolve(format(FILE_OUT_LISTFILE, version));
        this.fileListfileInvalidEntries = outputPath.resolve(format(FILE_OUT_LISTFILE_INVALID, version));
        this.fileCascInvalidEntries = outputPath.resolve(format(FILE_OUT_CASC_INVALID, version));
        this.fileCascUnknownEntries = outputPath.resolve(format(FILE_OUT_CASC_MISSING, version));
        this.listFile = initialiseFiles(outputPath);
        LOGGER.debug("Initialising entry store from CASC and listfile entries");
        this.entryStore = new EntryStore(listFile, cascContext);
        LOGGER.debug("Listfile generator initialised");
    }

    private Warcraft4jConfig initialiseConfig(Path wowPath, Path cachePath) {
        boolean online = wowPath == null;
        boolean caching = cachePath != null;
        Region region = Region.EUROPE;
        Branch branch = Branch.LIVE;
        Locale locale = Locale.EN_US;
        Warcraft4jConfigBuilder builder = new Warcraft4jConfigBuilder()
                .online(online)
                .caching(caching)
                .withRegion(region)
                .withBranch(branch)
                .withLocale(locale);
        if (wowPath != null) {
            builder.withWowDir(wowPath);
        }
        if (caching) {
            builder.withCacheDir(cachePath);
        }
        return builder.build();

    }

    private CdnCascContext initialiseCascContext(Warcraft4jConfig warcraft4jConfig) throws IOException, IllegalArgumentException {
        if (Files.exists(warcraft4jConfig.getWowInstallationDirectory())
                && Files.isDirectory(warcraft4jConfig.getWowInstallationDirectory()) && Files.isReadable(warcraft4jConfig.getWowInstallationDirectory())) {
            CdnCascContext cascContext;
            if (warcraft4jConfig.isOnline()) {
                cascContext = new OnlineCdnCascContext(warcraft4jConfig);
            } else {
                cascContext = new LocalCdnCascContext(warcraft4jConfig);
            }
            return cascContext;
        } else {
            throw new IllegalArgumentException(format("WoW installation directory %s is either not a directory or not readable and writable.",
                    warcraft4jConfig.getWowInstallationDirectory()));
        }
    }

    private ListFile initialiseFiles(Path outputDirectory) throws IOException, IllegalArgumentException {
        ListFile listFile = ListFile.empty();
        if (Files.exists(outputDirectory) && Files.isDirectory(outputDirectory) && Files.isReadable(outputDirectory) && Files.isWritable(outputDirectory)) {
            Path listFileIn = outputDirectory.resolve(FILE_IN_LISTFILE);
            if (Files.exists(listFileIn) && Files.isReadable(listFileIn) && Files.isRegularFile(listFileIn)) {
                long startTime = System.currentTimeMillis();
                listFile = ListFile.fromFile(listFileIn);
                LOGGER.debug("Parsed list file {} with {} files and {} hashes in {} ms",
                        listFileIn, listFile.getFilenameCount(), listFile.getHashCount(), (System.currentTimeMillis() - startTime));
            } else {
                LOGGER.debug("Couldn't find or read list file {}, using an empty input.", listFileIn);
            }
        } else if (Files.notExists(outputDirectory)) {
            Files.createDirectories(outputDirectory);
            LOGGER.debug("Output directory {} does not exist, created it and using empty list file", outputDirectory);
        } else {
            throw new IllegalArgumentException(format("Output directory %s is either not a directory or not readable and writable.", outputDirectory));
        }
        return listFile;
    }

    private Predicate<Long> filterCascValidHash() {
        return (hash) -> cascContext.isRegisteredData(hash);
    }

    private Predicate<String> filterCascValidFilename() {
        return (filename) -> cascContext.isRegisteredData(filename);
    }

    private Predicate<Long> filterCascKnownHash() {
        return (hash) -> cascContext.isRegistered(hash);
    }

    private Predicate<String> filterCascKnownFilename() {
        return (filename) -> cascContext.isRegistered(filename);
    }

    private Predicate<Long> filterListfileKnownHash() {
        return (hash) -> listFile.getFilenames(hash).isPresent();
    }

    private Predicate<String> filterListfileKnownFilename() {
        return listFile::isFileKnown;
    }

    protected List<String> toSortedFilenames(Collection<Long> hashes, String unknownValue) {
        return hashes.stream()
                .filter(h -> h != null)
                .filter(h -> h != 0)
                .map(entryStore::getFilename)
                .map(f -> f.orElse(unknownValue))
                .sorted()
                .collect(Collectors.toList());
    }

    protected Set<Long> findCascValidEntries() {
        LOGGER.trace("Collecting all valid CASC entries from {} CASC entries.", cascContext.getHashes().size());
        Stream<Long> entriesStream = cascContext.getHashes().stream()
                .filter(filterCascValidHash());
        Set<Long> entries = entriesStream.collect(Collectors.toSet());
        LOGGER.trace("Done collecting valid CASC entries, resulting in {} entries.", entries.size());
        return entries;
    }

    protected Set<Long> findCascInvalidEntries() {
        LOGGER.trace("Collecting all invalid CASC entries from {} CASC entries.", cascContext.getHashes().size());
        Stream<Long> entriesStream = cascContext.getHashes().stream()
                .filter(filterCascValidHash().negate());
        Set<Long> entries = entriesStream.collect(Collectors.toSet());
        LOGGER.trace("Done collecting invalid CASC entries, resulting in {} entries.", entries.size());
        return entries;
    }

    protected Set<Long> findListfileKnownEntries() {
        LOGGER.trace("Collecting all known listfile entries from {} listfile entries and {} CASC entries.", listFile.getHashCount(), cascContext.getHashes().size());
        Stream<Long> entriesStream = listFile.getHashes().stream()
                .filter(filterCascKnownHash());
        Set<Long> entries = entriesStream.collect(Collectors.toSet());
        LOGGER.trace("Done collecting unknown listfile entries, resulting in {} entries.", entries.size());
        return entries;
    }

    protected Set<Long> findListfileUnknownEntries() {
        LOGGER.trace("Collecting all unknown listfile entries from {} listfile entries and {} CASC entries.", listFile.getHashCount(), cascContext.getHashes().size());
        Stream<Long> entriesStream = listFile.getHashes().stream()
                .filter(filterCascKnownHash().negate());
        Set<Long> entries = entriesStream.collect(Collectors.toSet());
        LOGGER.trace("Done collecting unknown listfile entries, resulting in {} entries.", entries.size());
        return entries;
    }

    protected Set<Long> findListfileValidEntries() {
        LOGGER.trace("Collecting all valid listfile entries from {} listfile entries and {} CASC entries.", listFile.getHashCount(), cascContext.getHashes().size());
        Stream<Long> entriesStream = listFile.getHashes().stream()
                .filter(filterCascValidHash());
        Set<Long> entries = entriesStream.collect(Collectors.toSet());
        LOGGER.trace("Done collecting valid listfile entries, resulting in {} entries.", entries.size());
        return entries;
    }

    protected Set<Long> findListfileInvalidEntries() {
        LOGGER.trace("Collecting all invalid listfile entries from {} listfile entries and {} CASC entries.", listFile.getHashCount(), cascContext.getHashes().size());
        Stream<Long> entriesStream = listFile.getHashes().stream()
                .filter(filterCascKnownHash())
                .filter(filterCascValidHash().negate());
        Set<Long> entries = entriesStream.collect(Collectors.toSet());
        LOGGER.trace("Done collecting invalid listfile entries, resulting in {} entries.", entries.size());
        return entries;
    }

    private void write(Path file, Collection<?> entries) throws IOException {
        final String lineMask = "%s\n";
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            for (Object entry : entries) {
                writer.write(format(lineMask, String.valueOf(entry)));
            }
            writer.flush();
        }
    }

    public void generate() throws IOException {
        LOGGER.debug("Writing lists to file based on {} CASC entries and {} listfile entries.", cascContext.getHashes().size(), listFile.getHashCount());
        Set<Long> listfileKnowndEntries = findListfileKnownEntries();
        Set<Long> listfileUnknownEntries = findListfileUnknownEntries();
        Set<Long> listfileValidEntries = findListfileValidEntries();
        Set<Long> listfileInvalidEntries = findListfileInvalidEntries();
        Set<Long> cascInvalidEntries = findCascInvalidEntries();
        Set<Long> cascValidEntries = findCascValidEntries();

        // TODO Implement me!
    }

    public void print() throws IOException {
        entryStore.getExtensions().stream()
                .forEach(ext -> System.out.println(format(" - [%s] : %d", ext, entryStore.getExtensionCount(ext))));
    }

    public static void main(String... args) throws IOException {
        ListFileGenerator generator = new ListFileGenerator(Paths.get(args[0]), Paths.get(args[1]), Paths.get(args[2]));
        generator.print();
    }
}
