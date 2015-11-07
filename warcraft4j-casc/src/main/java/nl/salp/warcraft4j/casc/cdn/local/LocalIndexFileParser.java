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
package nl.salp.warcraft4j.casc.cdn.local;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.casc.IndexEntry;
import nl.salp.warcraft4j.hash.JenkinsHash;
import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.util.Checksum;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

import static java.lang.String.format;

/**
 * Parser for parsing a {@link LocalIndexFile}.
 *
 * @author Barre Dijkstra
 */
public class LocalIndexFileParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(LocalIndexFileParser.class);
    /** The size of an entry in bytes. */
    public static final int ENTRY_SIZE = 18;
    /** The path of the index file. */
    private final Path file;
    /** Function to get the the index file number from the file path. */
    private final Function<Path, Integer> fileNumberFunction;
    /** Function get the the index file version from the file path. */
    private final Function<Path, Integer> fileVersionFunction;

    /**
     * Create a new instance.
     *
     * @param file                The path of the index file to parse.
     * @param fileNumberFunction  The function to get the the index file number from the file path.
     * @param fileVersionFunction The function to get the the index file version from the file path.
     */
    public LocalIndexFileParser(Path file, Function<Path, Integer> fileNumberFunction, Function<Path, Integer> fileVersionFunction) {
        this.file = file;
        this.fileNumberFunction = fileNumberFunction;
        this.fileVersionFunction = fileVersionFunction;
    }

    /**
     * Read and parse a {@link LocalIndexFile}.
     *
     * @param reader The reader for the file.
     *
     * @return The parsed index file.
     *
     * @throws CascParsingException When the file is invalid.
     * @throws DataReadingException When reading the entry data failed.
     * @throws DataParsingException When parsing the entry data failed.
     */
    public LocalIndexFile parse(DataReader reader) throws DataReadingException, DataParsingException {
        LOGGER.trace("Parsing index file {}", file);
        validateHeader(reader);
        int dataHeaderLength = reader.readNext(DataTypeFactory.getInteger(), ByteOrder.LITTLE_ENDIAN);
        Checksum dataHeaderChecksum = new Checksum(reader.readNext(DataTypeFactory.getByteArray(4))); // ByteOrder.LITTLE_ENDIAN
        // TODO Validate header based on the checksum.
        IndexHeaderV2 header = parseHeader(reader);
        LOGGER.trace("Parsed header {}", header);
        reader.position((8 + dataHeaderLength + 0x0F) & 0xFFFFFFF0);

        int dataLength = reader.readNext(DataTypeFactory.getInteger(), ByteOrder.LITTLE_ENDIAN);
        Checksum dataChecksum = new Checksum(reader.readNext(DataTypeFactory.getByteArray(4))); // ByteOrder.LITTLE_ENDIAN
        // TODO Validate data based on the checksum.
        int entryCount = (dataLength / ENTRY_SIZE);
        LOGGER.trace("Parsing {} index file entries from {} bytes at position {}", entryCount, dataLength, reader.position());
        List<IndexEntry> entries = parseEntries(reader, entryCount);

        int fileNumber = fileNumberFunction.apply(file);
        int fileVersion = fileVersionFunction.apply(file);
        LOGGER.trace("Parsed index file {}, version {} with {} entries, expecting {} entries", fileNumber, fileVersion, entries.size(), entryCount);
        return new LocalIndexFile(file, fileNumber, fileVersion, entries);
    }

    /**
     * Validate the index file header.
     *
     * @param reader The reader to read the header data from.
     *
     * @throws CascParsingException When the header is invalid.
     * @throws DataReadingException When reading the entry data failed.
     * @throws DataParsingException When parsing the entry data failed.
     */
    private void validateHeader(DataReader reader) throws CascParsingException, DataReadingException, DataParsingException {
        long position = reader.position();

        int headerLen = (int) reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.LITTLE_ENDIAN).longValue();
        int hash = reader.readNext(DataTypeFactory.getInteger(), ByteOrder.LITTLE_ENDIAN);

        byte[] data = reader.readNext(DataTypeFactory.getByteArray(headerLen));

        int h2 = JenkinsHash.hashLittle2b(data, headerLen);
        if (hash != h2) {
            throw new CascParsingException(format("Invalid index header hash %X -> %X", hash, h2));
        }

        reader.position(position);
    }

    /**
     * Read and parse the index file header.
     *
     * @param reader The reader to read the header data from.
     *
     * @return The parsed index file header.
     *
     * @throws CascParsingException When the header is invalid.
     * @throws DataReadingException When reading the entry data failed.
     * @throws DataParsingException When parsing the entry data failed.
     */
    private IndexHeaderV2 parseHeader(DataReader reader) throws CascParsingException, DataReadingException, DataParsingException {
        int indexVersion = reader.readNext(DataTypeFactory.getUnsignedShort(), ByteOrder.LITTLE_ENDIAN);
        if (indexVersion != 0x07) {
            throw new CascParsingException(format("Invalid index file header version 0x%02X, requires 0x07", indexVersion));
        }
        byte keyIndex = reader.readNext(DataTypeFactory.getByte());
        byte extraBytes = reader.readNext(DataTypeFactory.getByte());
        byte spanSizeBytes = reader.readNext(DataTypeFactory.getByte());
        byte spanOffsetBytes = reader.readNext(DataTypeFactory.getByte());
        byte keyBytes = reader.readNext(DataTypeFactory.getByte());
        byte segmentBits = reader.readNext(DataTypeFactory.getByte());
        long maxFileOffset = reader.readNext(DataTypeFactory.getLong(), ByteOrder.BIG_ENDIAN);
        if (extraBytes != 0x00 || spanSizeBytes != 0x04 || spanOffsetBytes != 0x05 || keyBytes != 0x09) {
            throw new CascParsingException("Invalid index file header");
        }
        return new IndexHeaderV2(indexVersion, keyIndex, extraBytes, spanSizeBytes, spanOffsetBytes, keyBytes, segmentBits, maxFileOffset);
    }

    /**
     * Read an parse all available {@link IndexEntry} instances.
     *
     * @param reader     The reader to read the entry data from.
     * @param entryCount The number of entries avialable.
     *
     * @return The parsed entries.
     *
     * @throws DataReadingException When reading the entry data failed.
     * @throws DataParsingException When parsing the entry data failed.
     */
    private List<IndexEntry> parseEntries(DataReader reader, int entryCount) throws DataReadingException, DataParsingException {
        Set<Checksum> keys = new HashSet<>();
        List<IndexEntry> entries = new ArrayList<>();
        for (int i = 0; i < entryCount; i++) {
            IndexEntry entry = parseEntry(reader);
            if (entry != null && entry.getFileKey() != null && keys.add(entry.getFileKey())) {
                entries.add(entry);
            } else if (entry != null && entry.getFileKey() != null) {
                LOGGER.trace("Skipping index entry for duplicate file key {}", entry.getFileKey());
            }
        }
        return entries;
    }

    /**
     * Read and parse an {@link IndexEntry}.
     *
     * @param reader The reader to read the entry data from.
     *
     * @return The parsed entry.
     *
     * @throws DataReadingException When reading the entry data failed.
     * @throws DataParsingException When parsing the entry data failed.
     */
    private IndexEntry parseEntry(DataReader reader) throws DataReadingException, DataParsingException {
        byte[] fileKey = reader.readNext(DataTypeFactory.getByteArray(9));
        short indexInfoHigh = reader.readNext(DataTypeFactory.getUnsignedByte());
        long indexInfoLow = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        long fileSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.LITTLE_ENDIAN);
        IndexEntry entry = new LocalIndexEntry(new FileKey(fileKey), indexInfoHigh, indexInfoLow, fileSize);
        return entry;
    }

    /**
     * Header of the index file (version 2).
     */
    private static class IndexHeaderV2 {
        /** The index version, must be 0x07 for CASCv2. */
        private final int indexVersion;
        /** The file key index. */
        private final byte keyIndex;
        /** Empty byte */
        private final byte unknown1;
        /** The size of the field with the file size in bytes. */
        private final byte spanSizeBytes;
        /** The size of the field with the file offset in bytes. */
        private final byte spanOffsetBytes;
        /** The size of the file key in bytes. */
        private final byte keyBytes;
        /** Number of bits for the file offset (rest is archive index). */
        private final byte segmentBits;
        /** The maximum file offset. */
        private final long maxFileOffset;

        /**
         * @param indexVersion    The index version.
         * @param keyIndex        The file key index.
         * @param unknown1        Unknown byte.
         * @param spanSizeBytes   The size of the field with the file size in bytes.
         * @param spanOffsetBytes The size of the field with the file offset in bytes.
         * @param keyBytes        The size of the file key in bytes.
         * @param segmentBits     The number of bits for the file offset (rest is archive index).
         * @param maxFileOffset   The maximum file offset.
         */
        public IndexHeaderV2(int indexVersion, byte keyIndex, byte unknown1, byte spanSizeBytes, byte spanOffsetBytes, byte keyBytes, byte segmentBits, long maxFileOffset) {
            this.indexVersion = indexVersion;
            this.keyIndex = keyIndex;
            this.unknown1 = unknown1;
            this.spanSizeBytes = spanSizeBytes;
            this.spanOffsetBytes = spanOffsetBytes;
            this.keyBytes = keyBytes;
            this.segmentBits = segmentBits;
            this.maxFileOffset = maxFileOffset;
        }

        /**
         * Get the index version.
         *
         * @return The index version.
         */
        public int getIndexVersion() {
            return indexVersion;
        }

        /**
         * Get the file key index.
         *
         * @return The file key index.
         */
        public byte getKeyIndex() {
            return keyIndex;
        }

        /**
         * Get the first unknown data.
         *
         * @return The first unknown data.
         */
        public byte getUnknown1() {
            return unknown1;
        }

        /**
         * Get the size of the field with the file size in bytes.
         *
         * @return The size of the field with the file size in bytes.
         */
        public byte getSpanSizeBytes() {
            return spanSizeBytes;
        }

        /**
         * Get the size of the field with the file offset in bytes.
         *
         * @return The size of the field with the file offset in bytes.
         */
        public byte getSpanOffsetBytes() {
            return spanOffsetBytes;
        }

        /**
         * Get the size of the file key in bytes.
         *
         * @return The size of the file key in bytes.
         */
        public byte getKeyBytes() {
            return keyBytes;
        }

        /**
         * Get the number of bits for the file offset (rest is archive index).
         *
         * @return The number of bits.
         */
        public byte getSegmentBits() {
            return segmentBits;
        }

        /**
         * Get the maximum file offset.
         *
         * @return The maximum file offset.
         */
        public long getMaxFileOffset() {
            return maxFileOffset;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}
