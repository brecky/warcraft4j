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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.casc.CascParsingException;
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.casc.EncodingEntry;
import nl.salp.warcraft4j.casc.FileKey;
import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.lang.String.format;
import static nl.salp.warcraft4j.hash.Hashes.MD5;
import static nl.salp.warcraft4j.util.DataTypeUtil.byteArrayToHexString;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class EncodingFileParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EncodingFileParser.class);
    private static final String MAGIC_STRING = "EN";
    private static final int SEGMENT_CHECKSUMS_SIZE = 32;
    private static final int MAGIC_STRING_SIZE = 2;
    private static final int CHECKSUM_SIZE = 0x10;
    private static final int HEADER_SIZE = 22;
    private static final int SEGMENT_ENTRY_SIZE = 22;
    private static final int SEGMENT_SIZE = 0x1000;

    public EncodingFile parse(DataReader reader, long encodingFileSize) throws DataReadingException, DataParsingException, CascParsingException {
        LOGGER.trace("Parsing {}-byte encoding file", encodingFileSize);
        if (reader.remaining() < encodingFileSize) {
            throw new CascParsingException(format("Tried to read a %d-byte encoding file from a stream with %d-bytes remaining.", encodingFileSize, reader.remaining()));
        }

        long startOffset = reader.position();
        LOGGER.trace("Parsing encoding header from offset {}", startOffset);
        EncodingFileHeader header = parseHeader(reader);
        long segmentStartPosition = startOffset + header.getSegmentOffset() + HEADER_SIZE;
        LOGGER.trace("Parsed {} byte encoding header from position {} to {}: {}", HEADER_SIZE, startOffset, reader.position(), header);
        LOGGER.trace("Encoding file segments: {}", header.getSegmentCount());
        LOGGER.trace("Encoding file segment offset: {}", header.getSegmentOffset());
        LOGGER.trace("Encoding file requires {} bytes of data with a file size of {} bytes.", getEncodingFileSize(header), encodingFileSize);
        if (reader.position() != startOffset + HEADER_SIZE) {
            throw new CascParsingException(format("Error reading header segment, ended up on offset %d instead of %d", reader.position(), startOffset + HEADER_SIZE));
        }
        if (getEncodingFileSize(header) > encodingFileSize) {
            throw new CascParsingException(format("Invalid encoding file size: %d segments require %d bytes of data with %d bytes of data provided",
                    header.getSegmentCount(), getEncodingFileSize(header), encodingFileSize));
        }

        long stringsStartPosition = reader.position();
        LOGGER.trace("Parsing encoding string segment from offset {}", (stringsStartPosition - startOffset));
        List<String> strings = parseStrings(segmentStartPosition, reader);
        LOGGER.trace("Read {} strings from position {} to {} ({} bytes)",
                strings.size(), stringsStartPosition, segmentStartPosition, segmentStartPosition - stringsStartPosition);
        if (reader.position() != segmentStartPosition) {
            throw new CascParsingException(format("Error reading string segment, ended up on offset %d instead of %d", reader.position(), segmentStartPosition));
        }

        LOGGER.trace("Parsing {} encoding file segment checksums from offset {}", header.getSegmentCount(), segmentStartPosition - startOffset);
        List<EncodingFileSegmentChecksum> segmentChecksums = parseSegmentChecksums(header.getSegmentCount(), reader);
        LOGGER.trace("Read {} {}-byte segment checksums from position {} to {} ({} bytes)",
                segmentChecksums.size(), SEGMENT_CHECKSUMS_SIZE, segmentStartPosition, reader.position(), reader.position() - segmentStartPosition);
        if (reader.position() != (segmentStartPosition + (header.getSegmentCount() * SEGMENT_CHECKSUMS_SIZE))) {
            throw new CascParsingException(format("Error reading segment checksums, ended up on offset %d instead of %d", reader.position(), segmentStartPosition));
        }

        long entryStartPosition = reader.position();
        LOGGER.trace("Parsing {} encoding file entry segments from offset {}", header.getSegmentCount(), entryStartPosition - startOffset);
        List<EncodingFileSegment> segments = parseSegments(header.getSegmentCount(), reader);
        LOGGER.trace("Parsed {} encoding file entry segments from position {} to {} ({} bytes)",
                header.getSegmentCount(), entryStartPosition, reader.position(), reader.position() - entryStartPosition);
        if (segments.size() != segmentChecksums.size()) {
            throw new CascParsingException(format("Retrieved %d encoding file segments and %d segment checksums with %d segments expected.",
                    segments.size(), segmentChecksums.size(), header.getSegmentCount()));
        }

        LOGGER.trace("Validating data integrity of {} segments", segments.size());
        List<Boolean> segmentValidity = IntStream.range(0, segments.size())
                .mapToObj(i -> validateSegment(segments.get(i), segmentChecksums.get(i)))
                .collect(Collectors.toList());
        if (segmentValidity.stream().anyMatch(valid -> valid == false)) {
            IntStream.range(0, segments.size())
                    .filter(index -> segmentValidity.get(index) == false)
                    .forEach(i -> LOGGER.trace("segment {} has checksum {} calculated with {} expected",
                                    i, byteArrayToHexString(segments.get(i).getChecksum()), byteArrayToHexString(segmentChecksums.get(i).getSegmentChecksum()))
                    );
            long invalidSegments = IntStream.range(0, segmentValidity.size()).filter(index -> segmentValidity.get(index) == false).count();
            throw new CascParsingException(format("Encoding file has %d invalid segments out of %d segments.", invalidSegments, segments.size()));
        }
        LOGGER.trace("Validated data integrity of {} segments", segments.size());

        List<EncodingEntry> entries = segments.stream()
                .map(EncodingFileSegment::getEntries)
                .flatMap(List::stream)
                .collect(Collectors.toList());
        LOGGER.trace("Successfully parsed encoding file with {} entries from {} segments", entries.size(), header.getSegmentCount());
        return new EncodingFile(entries);
    }

    private EncodingFileHeader parseHeader(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        String magicString = reader.readNext(DataTypeFactory.getFixedLengthString(2, StandardCharsets.US_ASCII));
        if (!MAGIC_STRING.equals(magicString)) {
            throw new CascParsingException(format("Encoding file starts with magic string %s instead of %s", magicString, MAGIC_STRING));
        }
        byte[] unknown1 = reader.readNext(DataTypeFactory.getByteArray(3));
        int unknown2 = reader.readNext(DataTypeFactory.getUnsignedShort(), ByteOrder.LITTLE_ENDIAN);
        int unknown3 = reader.readNext(DataTypeFactory.getUnsignedShort(), ByteOrder.LITTLE_ENDIAN);
        long segmentCount = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        long unknown4 = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        byte unknown5 = reader.readNext(DataTypeFactory.getByte());
        long segmentOffset = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        return new EncodingFileHeader(magicString, unknown1, unknown2, unknown3, segmentCount, unknown4, unknown5, segmentOffset);
    }

    private long getEncodingFileSize(EncodingFileHeader header) {
        long fileSize = 0;
        if (header != null) {
            fileSize += HEADER_SIZE;
            fileSize += header.getSegmentOffset();
            fileSize += (header.getSegmentCount() * SEGMENT_CHECKSUMS_SIZE);
            fileSize += (header.getSegmentCount() * SEGMENT_SIZE);
        }
        return fileSize;
    }

    private List<String> parseStrings(long segmentStartPosition, DataReader reader) throws DataReadingException, DataParsingException {
        List<String> strings = new ArrayList<>();
        while (reader.hasRemaining() && reader.position() < segmentStartPosition) {
            String str = reader.readNext(DataTypeFactory.getTerminatedString());
            if (isNotEmpty(str)) {
                strings.add(str);
            }
        }
        return strings;
    }

    private List<EncodingFileSegmentChecksum> parseSegmentChecksums(long segmentCount, DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        List<EncodingFileSegmentChecksum> segmentChecksums = new ArrayList<>((int) segmentCount);
        for (int i = 0; i < segmentCount; i++) {
            byte[] contentChecksum = reader.readNext(DataTypeFactory.getByteArray(CHECKSUM_SIZE));
            byte[] checksum = reader.readNext(DataTypeFactory.getByteArray(CHECKSUM_SIZE));
            segmentChecksums.add(new EncodingFileSegmentChecksum(contentChecksum, checksum));
        }
        return segmentChecksums;
    }

    private List<EncodingFileSegment> parseSegments(long segmentCount, DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        List<EncodingFileSegment> segments = new ArrayList<>((int) segmentCount);
        for (int i = 0; i < segmentCount; i++) {
            long segmentStart = reader.position();
            EncodingFileSegment segment = parseSegment(reader);
            LOGGER.trace("Parsed encoding file segment {} with checksum {} and {} entries from position {} to {} ({} bytes)",
                    i, byteArrayToHexString(segment.getChecksum()), segment.getEntries().size(), segmentStart, reader.position(), reader.position() - segmentStart);
            segments.add(segment);
        }
        return segments;
    }

    private EncodingFileSegment parseSegment(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        long segmentStart = reader.position();
        byte[] segmentChecksum = MD5.hash(reader.readNext(DataTypeFactory.getByteArray(SEGMENT_SIZE)));
        LOGGER.trace("Calculated checksum {} over {} byte segment from position {} to {}, reading entries from position {}",
                byteArrayToHexString(segmentChecksum), SEGMENT_SIZE, segmentStart, reader.position(), segmentStart);
        reader.position(segmentStart);
        List<EncodingEntry> entries = new ArrayList<>();

        int keyCount;

        while ((keyCount = reader.readNext(DataTypeFactory.getUnsignedShort(), ByteOrder.LITTLE_ENDIAN)) != 0) {
            long fileSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
            byte[] checksum = reader.readNext(DataTypeFactory.getByteArray(CHECKSUM_SIZE));

            List<FileKey> keys = new ArrayList<>();
            for (int keyIndex = 0; keyIndex < keyCount; keyIndex++) {
                byte[] key = reader.readNext(DataTypeFactory.getByteArray(CHECKSUM_SIZE));
                keys.add(new FileKey(key));
            }
            EncodingEntry entry = new CascEncodingEntry(fileSize, new ContentChecksum(checksum), keys);
            entries.add(entry);
        }
        while (reader.hasRemaining() && reader.peek(DataTypeFactory.getByte()) == 0) {
            reader.skip(1);
        }
        return new EncodingFileSegment(entries, segmentChecksum);
    }

    private boolean validateSegment(EncodingFileSegment segment, EncodingFileSegmentChecksum segmentChecksum) {
        return ((segment != null) && (segmentChecksum != null))
                && (Arrays.equals(segment.getChecksum(), segmentChecksum.getSegmentChecksum())
                && ((segment.getEntries().size() == 0) || Arrays.equals(segment.getEntries().get(0).getContentChecksum().getChecksum(), segmentChecksum.getFirstFileChecksum()))
        );
    }

    private static class EncodingFileHeader {
        private final String magicString;
        private final byte[] unknown1;
        private final int unknown2;
        private final int unknown3;
        private final long segmentCount;
        private final long unknown4;
        private final byte unknown5;
        private final long segmentOffset;

        public EncodingFileHeader(String magicString, byte[] unknown1, int unknown2, int unknown3, long segmentCount, long unknown4, byte unknown5, long segmentOffset) {
            this.magicString = magicString;
            this.unknown1 = unknown1;
            this.unknown2 = unknown2;
            this.unknown3 = unknown3;
            this.segmentCount = segmentCount;
            this.unknown4 = unknown4;
            this.unknown5 = unknown5;
            this.segmentOffset = segmentOffset;
        }

        public String getMagicString() {
            return magicString;
        }

        public byte[] getUnknown1() {
            return unknown1;
        }

        public int getUnknown2() {
            return unknown2;
        }

        public int getUnknown3() {
            return unknown3;
        }

        public long getSegmentCount() {
            return segmentCount;
        }

        public long getUnknown4() {
            return unknown4;
        }

        public byte getUnknown5() {
            return unknown5;
        }

        public long getSegmentOffset() {
            return segmentOffset;
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

    /**
     * TODO Add description.
     *
     * @author Barre Dijkstra
     */
    private static class EncodingFileSegment {
        private final byte[] checksum;
        private final List<EncodingEntry> entries;

        public EncodingFileSegment(List<EncodingEntry> entries, byte[] checksum) {
            this.entries = entries;
            this.checksum = checksum;
        }

        public byte[] getChecksum() {
            return checksum;
        }

        public List<EncodingEntry> getEntries() {
            return entries;
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

    /**
     * TODO Add description.
     *
     * @author Barre Dijkstra
     */
    private static class EncodingFileSegmentChecksum {
        private final byte[] firstFileChecksum;
        private final byte[] segmentChecksum;

        public EncodingFileSegmentChecksum(byte[] firstFileChecksum, byte[] segmentChecksum) {
            this.firstFileChecksum = firstFileChecksum;
            this.segmentChecksum = segmentChecksum;
        }

        public byte[] getFirstFileChecksum() {
            return firstFileChecksum;
        }

        public byte[] getSegmentChecksum() {
            return segmentChecksum;
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