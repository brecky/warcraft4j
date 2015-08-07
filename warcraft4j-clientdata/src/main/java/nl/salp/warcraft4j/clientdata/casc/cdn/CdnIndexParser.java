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
package nl.salp.warcraft4j.clientdata.casc.cdn;

import nl.salp.warcraft4j.clientdata.casc.*;
import nl.salp.warcraft4j.io.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CdnIndexParser {
    /** The logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(CascContext.class);
    private static final String MASK_FILES_INDEX = "%s/data/%s/%s/%s.index";
    private final CascConfig cascConfig;
    private final DataReaderProvider dataReaderProvider;

    public CdnIndexParser(CascConfig cascConfig, DataReaderProvider dataReaderProvider) {
        this.cascConfig = cascConfig;
        this.dataReaderProvider = dataReaderProvider;
    }

    public Index parse() throws CascParsingException {
        List<CdnIndexFile> indices = new ArrayList<>();
        List<FileKey> indexFileKeys = cascConfig.getArchiveChecksums();
        LOGGER.trace("Parsing index entries from {} index files.", indexFileKeys.size());
        for (int i = 0; i < indexFileKeys.size(); i++) {
            indices.add(parseIndexFile(i, indexFileKeys.get(i)));
        }
        Set<IndexEntry> indexEntries = indices.stream()
                .map(CdnIndexFile::getIndexEntries)
                .flatMap(Collection::stream)
                .distinct()
                .collect(Collectors.toSet());
        LOGGER.trace("Parsed {} index entries from {} index files.", indexEntries.size(), indices.size());
        return new Index(indexEntries);
    }

    private CdnIndexFile parseIndexFile(int index, FileKey checksum) {
        CdnIndexFile indexFile;
        String url = getUrl(checksum);
        try (DataReader dataReader = dataReaderProvider.getDataReader(url).get()) {
            indexFile = dataReader.readNext(new CdnIndexFileParser(index, checksum));
        } catch (IOException e) {
            throw new CascParsingException(format("Error parsing CDN index file %d with key %s", index, checksum.toHexString()), e);
        }
        LOGGER.trace("Parsed index file {} with checksum {} from {} resulting in {} entries", index, checksum, url, indexFile.getIndexEntries().size());
        return indexFile;
    }

    private String getUrl(FileKey fileKey) {
        if (fileKey == null) {
            throw new CascParsingException("Unable to parse a CDN index file with a null key.");
        }
        if (fileKey.length() != 16) {
            throw new CascParsingException(format("Unable to parse a CDN index file with a %d byte file key '%s', a 16 byte key is required.",
                    fileKey.length(), fileKey.toHexString()));
        }
        String keyString = fileKey.toHexString();
        return format(MASK_FILES_INDEX, cascConfig.getCdnUrl(), keyString.substring(0, 2), keyString.substring(2, 4), keyString);
    }
}
