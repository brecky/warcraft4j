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
package nl.salp.warcraft4j.data.casc;

import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.parser.DataParser;
import nl.salp.warcraft4j.io.parser.DataParsingException;
import nl.salp.warcraft4j.io.reader.DataReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.String.format;
import static java.nio.ByteOrder.LITTLE_ENDIAN;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class RootFileParser implements DataParser<Root> {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RootFileParser.class);

    public RootFileParser() {
    }

    @Override
    public Root parse(DataReader reader) throws IOException, DataParsingException {
        LOGGER.trace("Parsing root file");
        Map<Long, List<RootEntry>> entries = parseEntryBlocks(reader);
        LOGGER.trace("Parsed root with {} entries", entries.size());
        return new Root(entries);
    }

    private Map<Long, List<RootEntry>> parseEntryBlocks(DataReader reader) throws IOException, DataParsingException {
        Map<Long, List<RootEntry>> entries = new HashMap<>();
        long totalEntries = 0;
        long totalReadEntries = 0;
        LOGGER.trace("Reading entries from position {} ({} bytes remaining)", reader.position(), reader.remaining());
        while (reader.remaining() > 4) {
            long entryCount = reader.readNext(DataTypeFactory.getUnsignedInteger(), LITTLE_ENDIAN);
            if (entryCount > 0) {
                LOGGER.trace("Reading {} entries from position {} ({} bytes remaining)", entryCount, reader.position(), reader.remaining());
                totalEntries += entryCount;
                long blockUnknown = reader.readNext(DataTypeFactory.getUnsignedInteger(), LITTLE_ENDIAN);
                long blockFlags = reader.readNext(DataTypeFactory.getUnsignedInteger(), LITTLE_ENDIAN);
                CascLocale.getLocale(blockFlags).orElseThrow(() -> new CascParsingException(format("Unable to find a locale for flag %d", blockFlags)));

                List<Long> entryUnknown = new ArrayList<>();
                for (long i = 0; i < entryCount; i++) {
                    entryUnknown.add(reader.readNext(DataTypeFactory.getUnsignedInteger(), LITTLE_ENDIAN));
                }
                for (long i = 0; i < entryCount; i++) {
                    byte[] contentChecksum = reader.readNext(DataTypeFactory.getByteArray(16));
                    long filenameHash = reader.readNext(DataTypeFactory.getLong(), LITTLE_ENDIAN);
                    if (!entries.containsKey(filenameHash)) {
                        entries.put(filenameHash, new ArrayList<>());
                    }
                    entries.get(filenameHash).add(new RootEntry(filenameHash, new ContentChecksum(contentChecksum), blockFlags, blockUnknown, entryUnknown.get((int) i)));
                    totalReadEntries++;
                }
                LOGGER.trace("Read {} entries from {} calculated, on position {}", totalReadEntries, totalEntries, reader.position());
            }
        }
        return entries;
    }
}
