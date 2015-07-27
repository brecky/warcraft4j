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
package nl.salp.warcraft4j.clientdata.casc.info;

import nl.salp.warcraft4j.clientdata.casc.Branch;
import nl.salp.warcraft4j.clientdata.casc.CascParsingException;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
abstract class BuildInfoFileParser<T> extends InfoFileParser<T> {
    private static final String DATETIME_FORMAT = "yyyy-MM-ddTHH:mm:ssZ";
    private static final String FIELD_BRANCH = "Branch";
    private static final String FIELD_ACTIVE = "Active";
    private static final String FIELD_BUILD_KEY = "Build Key";
    private static final String FIELD_CDN_KEY = "CDN Key";
    private static final String FIELD_INSTALL_KEY = "Install Key";
    private static final String FIELD_IM_SIZE = "IM Size";
    private static final String FIELD_CDN_PATH = "CDN Path";
    private static final String FIELD_CDN_HOSTS = "CDN Hosts";
    private static final String FIELD_TAGS = "Tags";
    private static final String FIELD_ARMADILLO = "Armadillo";
    private static final String FIELD_LAST_ACTIVATED = "Last Activated";
    private static final String FIELD_VERSION = "Version";

    public static final String FILENAME = ".build.info";

    protected BuildInfoFile parseFirstEntry(Map<String, Field> fields) {
        return parseEntry(fields, 0);
    }

    protected List<BuildInfoFile> parseEntries(Map<String, Field> fields) {
        int entryCount = getEntryCount(fields);
        if (entryCount < 1) {
            throw new CascParsingException("The build info file contains no entries.");
        }
        return IntStream.range(0, getEntryCount(fields)).mapToObj(i -> parseEntry(fields, i)).collect(Collectors.toList());
    }

    protected BuildInfoFile parseEntry(Map<String, Field> fields, int entryIndex) {
        Branch branch = getEntry(FIELD_BRANCH, Branch::getForKey, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_BRANCH, entryIndex));
        boolean active = getEntry(FIELD_ACTIVE, "1"::equals, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_ACTIVE, entryIndex));
        String buildFileChecksum = getEntry(FIELD_BUILD_KEY, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_BUILD_KEY, entryIndex));
        String cdnFileChecksum = getEntry(FIELD_CDN_KEY, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_CDN_KEY, entryIndex));
        String installFileChecksum = getEntry(FIELD_INSTALL_KEY, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_INSTALL_KEY, entryIndex));
        long imSize = getEntry(FIELD_IM_SIZE, Long::parseLong, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_IM_SIZE, entryIndex));
        String cdnPath = getEntry(FIELD_CDN_PATH, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_CDN_PATH, entryIndex));
        List<String> cdnHosts = getEntry(FIELD_CDN_HOSTS, BuildInfoFileParser::parseHosts, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_CDN_HOSTS, entryIndex));
        List<String> tags = getEntry(FIELD_TAGS, BuildInfoFileParser::parseTags, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_TAGS, entryIndex));
        String armadillo = getEntry(FIELD_ARMADILLO, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_ARMADILLO, entryIndex));
        DateTime lastActivated = getEntry(FIELD_LAST_ACTIVATED, DateTime::parse, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_LAST_ACTIVATED, entryIndex));
        String version = getEntry(FIELD_VERSION, fields, entryIndex).orElseThrow(missingFieldEntry(FIELD_VERSION, entryIndex));

        return new BuildInfoFile(branch, active, buildFileChecksum, cdnFileChecksum, installFileChecksum, imSize, cdnPath, cdnHosts, tags, armadillo, lastActivated, version);
    }

    protected static int getEntryCount(Map<String, Field> fields) {
        return fields.values().stream()
                .mapToInt(Field::getDataCount)
                .min()
                .orElse(0);
    }

    protected static List<String> parseHosts(String hosts) {
        return Stream.of(hosts.split(" "))
                .filter(StringUtils::isNoneEmpty)
                .collect(Collectors.toList());
    }


    protected static List<String> parseTags(String tags) {
        return Stream.of(tags.split("\\?"))
                .filter(StringUtils::isNotEmpty)
                .map(String::trim)
                .collect(Collectors.toList());
    }


    protected static Supplier<CascParsingException> missingFieldEntry(String fieldName, int index) {
        return () -> new CascParsingException(format("Unable to find an entry for field %s with index %d", fieldName, index));
    }

    protected static Supplier<CascParsingException> missingField(String fieldName) {
        return () -> new CascParsingException(format("Unable to find field %s", fieldName));
    }
}
