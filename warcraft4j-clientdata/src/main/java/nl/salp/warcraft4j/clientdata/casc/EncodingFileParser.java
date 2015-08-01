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
package nl.salp.warcraft4j.clientdata.casc;

import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.util.DataTypeUtil;
import nl.salp.warcraft4j.io.parser.DataParser;
import nl.salp.warcraft4j.io.parser.DataParsingException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static nl.salp.warcraft4j.hash.Hashes.MD5;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class EncodingFileParser implements DataParser<EncodingFile> {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingFileParser.class);
    private static final int CHECKSUM_BLOCK_SIZE = 32;
    private static final int CHECKSUM_SIZE = 16;

    @Override
    public EncodingFile parse(DataReader reader) throws IOException, DataParsingException {
        LOGGER.trace("Parsing encoding file");
        byte[] locale = reader.readNext(DataTypeFactory.getByteArray(2));
        byte[] unknown1 = reader.readNext(DataTypeFactory.getByteArray(3));
        int unknown2 = reader.readNext(DataTypeFactory.getUnsignedShort(), ByteOrder.LITTLE_ENDIAN);
        int unknown3 = reader.readNext(DataTypeFactory.getUnsignedShort(), ByteOrder.LITTLE_ENDIAN);
        long entryCount = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        long unknown4 = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        byte unknown5 = reader.readNext(DataTypeFactory.getByte());
        long entriesOffset = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);

        // List<String> strings = parseStrings(entriesOffset, reader);
        long posStr = reader.position();
        reader.skip(entriesOffset);
        LOGGER.trace("Skipped strings from position {} to {} with offset {}", posStr, reader.position(), entriesOffset);

        // LOGGER.trace("Parsing {} encoding file block checksums", entryCount);
        // List<EncodingFileBlockChecksum> blockChecksums = parseBlocks(entryCount, EncodingFileParser::parseChecksum, reader);
        long posCsm = reader.position();
        reader.skip(entryCount * CHECKSUM_BLOCK_SIZE);
        LOGGER.trace("Skipped checksums from position {} to {} with offset {}", posCsm, reader.position(), entryCount * CHECKSUM_BLOCK_SIZE);

        LOGGER.trace("Parsing {} encoding file entry blocks for locale {} at offset {}", entryCount, locale, reader.position());
        /*
        List<EncodingFileBlock> blocks = parseBlocks(entryCount, EncodingFileParser::parseBlock, reader);

        List<EncodingEntry> entries = blocks.stream()
                .map(EncodingFileBlock::getEntries)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        */
        List<EncodingEntry> entries = parseEntries(entryCount, reader);
        LOGGER.trace("Successfully parsed encoding file for locale {} with {} entries from {} blocks", locale, entries.size(), entryCount);
        return new EncodingFile(locale, entries);
    }

    private List<EncodingEntry> parseEntries(long entryCount, DataReader reader) throws IOException {
        List<EncodingEntry> entries = new ArrayList<>();
        for (int i = 0; i < entryCount; i++) {
            int blockStartCount = entries.size();
            int keysCount;
            while (reader.remaining() > 2 && (keysCount = reader.readNext(DataTypeFactory.getUnsignedShort())) != 0 && reader.remaining() >= (20 + (keysCount * 16))) {
                long fileSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
                byte[] md5 = reader.readNext(DataTypeFactory.getByteArray(16));

                /*
                List<Checksum> keys = new ArrayList<>();
                for (int ki = 0; ki < keysCount && reader.remaining() >= 16; ++ki) {
                    byte[] key = reader.readNext(DataTypeFactory.getByteArray(16));
                    keys.add(new Checksum(key));
                }
                entries.add(new EncodingEntry(fileSize, new Checksum(md5), keys));
                LOGGER.trace("Added encoding entry {} with {} keys of {} expected keys", DataTypeUtil.byteArrayToHexString(md5), keys.size(), keysCount);
                */

                byte[] key = reader.readNext(DataTypeFactory.getByteArray(16));
                entries.add(new EncodingEntry(fileSize, new Checksum(md5), Arrays.asList(new Checksum(key))));
            }

            while (reader.hasRemaining() && peek(DataTypeFactory.getByte(), reader) == 0) {
                reader.skip(1);
            }
            LOGGER.trace("Added encoding block with {} entries", entries.size() - blockStartCount);
        }
        return entries;
    }

    private <T> T peek(DataType<T> dataType, DataReader reader) throws IOException {
        long position = reader.position();
        T value = reader.readNext(dataType);
        reader.position(position);
        return value;
    }


    private <T> List<T> parseBlocks(long entryCount, Function<DataReader, T> parser, DataReader reader) throws IOException {
        List<T> blocks = new ArrayList<>((int) entryCount);
        for (int i = 0; i < entryCount; i++) {
            blocks.add(parser.apply(reader));
        }
        return blocks;
    }

    @Override
    public int getInstanceDataSize() {
        return 0;
    }


    private static boolean isDataValid(EncodingFileBlockChecksum checksum, EncodingFileBlock block) {
        boolean valid = false;
        if (block != null && checksum != null) {
            valid = Arrays.equals(block.getChecksum(), checksum.getBlockChecksum());
            valid = valid && !block.getEntries().isEmpty();
            valid = valid && checksum.getFirstFileChecksum().equals(block.getEntries().get(0).getContentChecksum());
        }
        return valid;
    }

    private static List<String> parseStrings(long entriesOffset, DataReader reader) throws IOException {
        List<String> strings = new ArrayList<>();
        long entriesStart = reader.position() + entriesOffset;
        while (reader.hasRemaining() && reader.position() < entriesStart) {
            String str = reader.readNext(DataTypeFactory.getTerminatedString());
            if (isNotEmpty(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    private static EncodingFileBlockChecksum parseChecksum(DataReader reader) {
        try {
            byte[] contentChecksum = reader.readNext(DataTypeFactory.getByteArray(CHECKSUM_SIZE));
            byte[] checksum = reader.readNext(DataTypeFactory.getByteArray(CHECKSUM_SIZE));
            return new EncodingFileBlockChecksum(contentChecksum, checksum);
        } catch (IOException e) {
            throw new CascParsingException(e);
        }
    }

    private static EncodingFileBlock parseBlock(DataReader reader) {
        try (ByteArrayOutputStream blockDataStream = new ByteArrayOutputStream()) {
            List<EncodingEntry> entries = new ArrayList<>();
            EncodingEntry entry;
            byte[] checksum;
            while (reader.hasRemaining() && (entry = parseEntry(reader, blockDataStream)) != null) {
                entries.add(entry);
            }
            checksum = MD5.hash(blockDataStream.toByteArray());
            LOGGER.trace("Successfully parsed encoding file block with {} entries and MD5 checksum {}", entries.size(), DataTypeUtil.byteArrayToHexString(checksum));
            return new EncodingFileBlock(entries, checksum);
        } catch (IOException e) {
            throw new CascParsingException(e);
        }
    }

    private static EncodingEntry parseEntry(DataReader reader, ByteArrayOutputStream blockDataStream) throws IOException {
        EncodingEntry entry;
        byte[] marker = reader.readNext(DataTypeFactory.getByteArray(2));
        blockDataStream.write(marker);
        if (marker[0] == 0x0 && marker[1] == 0x0) {
            blockDataStream.write(reader.readNext(DataTypeFactory.getByteArray(26)));
            entry = null;
        } else {
            long fileSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
            byte[] fileContentChecksum = reader.readNext(DataTypeFactory.getByteArray(16));
            byte[] fileChecksum = reader.readNext(DataTypeFactory.getByteArray(16));
            entry = new EncodingEntry(fileSize, new Checksum(fileContentChecksum), Arrays.asList(new Checksum(fileChecksum)));
        }
        return entry;
    }

    /**
     * TODO Add description.
     *
     * @author Barre Dijkstra
     */
    private static class EncodingFileBlock {
        private final byte[] checksum;
        private final List<EncodingEntry> entries;

        public EncodingFileBlock(List<EncodingEntry> entries, byte[] checksum) {
            this.entries = entries;
            this.checksum = checksum;
        }

        public byte[] getChecksum() {
            return checksum;
        }

        public List<EncodingEntry> getEntries() {
            return entries;
        }
    }

    /**
     * TODO Add description.
     *
     * @author Barre Dijkstra
     */
    private static class EncodingFileBlockChecksum {
        private byte[] firstFileChecksum;
        private byte[] blockChecksum;

        public EncodingFileBlockChecksum(byte[] firstFileChecksum, byte[] blockChecksum) {
            this.firstFileChecksum = firstFileChecksum;
            this.blockChecksum = blockChecksum;
        }

        public byte[] getFirstFileChecksum() {
            return firstFileChecksum;
        }

        public byte[] getBlockChecksum() {
            return blockChecksum;
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }
    }
}