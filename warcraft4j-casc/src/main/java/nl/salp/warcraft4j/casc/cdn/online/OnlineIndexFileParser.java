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
package nl.salp.warcraft4j.casc.cdn.online;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import nl.salp.warcraft4j.casc.cdn.CascIndexEntry;
import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class OnlineIndexFileParser {
    private static final Logger LOGGER = LoggerFactory.getLogger(OnlineIndexFileParser.class);
    private static final byte[] EMPTY_CHECKSUM = createEmptyChecksum();

    private final int fileNumber;
    private final FileKey fileKey;

    public OnlineIndexFileParser(int fileNumber, FileKey fileKey) {
        this.fileNumber = fileNumber;
        this.fileKey = fileKey;
    }

    public OnlineIndexFile parse(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        long start = reader.position();
        reader.position(reader.remaining() - 12);
        int count = reader.readNext(DataTypeFactory.getInteger(), ByteOrder.LITTLE_ENDIAN);
        reader.position(start);
        List<IndexEntry> entries = new ArrayList<>(count);
        for (int i = 0; i < count; i++) {
            byte[] checksum = reader.readNext(DataTypeFactory.getByteArray(16));
            if (Arrays.equals(checksum, EMPTY_CHECKSUM)) {
                checksum = reader.readNext(DataTypeFactory.getByteArray(16));
            }
            if (Arrays.equals(checksum, EMPTY_CHECKSUM)) {
                throw new CascParsingException(format("Encountered empty checksum for entry %d in index file %d with file key %s.", i, fileNumber, fileKey.toHexString()));
            }
            int size = reader.readNext(DataTypeFactory.getInteger(), ByteOrder.BIG_ENDIAN);
            if (size < 0) {
                throw new CascParsingException(format("CDN index file %d (%s) has a negative size of %d for entry %d", fileNumber, fileKey.toHexString(), size, i));
            }
            int offset = reader.readNext(DataTypeFactory.getInteger(), ByteOrder.BIG_ENDIAN);
            if (offset < 0) {
                throw new CascParsingException(format("CDN index file %d (%s) has a negative offset of %d for entry %d", fileNumber, fileKey.toHexString(), offset, i));
            }

            IndexEntry entry = new CascIndexEntry(new FileKey(checksum), fileNumber, size, offset);
            entries.add(entry);
        }
        return new OnlineIndexFile(fileNumber, fileKey, entries);
    }

    private static byte[] createEmptyChecksum() {
        byte[] checksum = new byte[16];
        Arrays.fill(checksum, (byte) 0);
        return checksum;
    }
}
