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
import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;
import java.util.List;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BlteFileParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlteFileParser.class);
    /** The BLTE file magic String. */
    private static final String MAGIC_STRING = "BLTE";

    /**
     * Read a BLTE file from a {@link DataReader}.
     *
     * @param reader   The reader to read the data from.
     * @param fileSize The size of the BLTE file.
     *
     * @return The parsed BLTE file.
     *
     * @throws DataReadingException When reading the data failed.
     * @throws DataParsingException When parsing the data failed.
     */
    public BlteFile parse(DataReader reader, long fileSize) throws DataReadingException, DataParsingException {
        if (reader == null) {
            throw new IllegalArgumentException("Unable to read a BLTE file from a null reader.");
        }
        if (fileSize < 0) {
            throw new IllegalArgumentException(format("Unable to parse a %d byte BLTE file."));
        }
        LOGGER.trace("Parsing {}-byte BLTE file from data file at offset {}", fileSize, reader.position());
        long readerPos = reader.position();
        String magicString = reader.readNext(DataTypeFactory.getFixedLengthString(MAGIC_STRING.length(), US_ASCII));
        LOGGER.trace("Parsed {}-byte magic string {}", reader.position() - readerPos, magicString);
        if (!MAGIC_STRING.equals(magicString)) {
            throw new CascParsingException(format("BLTE file has the magic string '%s' while '%s' was required.", magicString, MAGIC_STRING));
        }
        long dataOffset = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        boolean multiChunk = (dataOffset != 0);
        BlteChunkParser parser;
        int chunkCount;
        if (multiChunk) {
            chunkCount = readMultiChunkCount(reader);
            LOGGER.trace("Parsing {}-byte BLTE multi-chunk file with {} chunks", fileSize, chunkCount);
            parser = new BlteMultiChunkParser(chunkCount);
        } else {
            chunkCount = 1;
            LOGGER.trace("Parsing {}-byte BLTE single chunk file", fileSize);
            parser = new BlteSingleChunkParser(fileSize);
        }

        List<BlteChunk> chunks = parser.parse(reader);
        if (chunks.size() != chunkCount) {
            throw new DataParsingException(format("Parsed %d chunks of data from BLTE file while %d chunks were expected.", chunks.size(), chunkCount));
        }
        LOGGER.trace("Successfully parsed {} bytes file from BLTE file with {} bytes of data from {} chunk(s)",
                chunks.stream().mapToLong(BlteChunk::getCompressedSize).sum(),
                chunks.stream().mapToLong(BlteChunk::getDecompressedSize).sum(),
                chunkCount);
        return new BlteFile(chunks);
    }

    /**
     * Read the number of chunks from a reader.
     *
     * @param reader The reader.
     *
     * @return The number of chunks.
     *
     * @throws DataReadingException When reading from the reader failed.
     * @throws DataParsingException When parsing the chunk count failed.
     */
    private int readMultiChunkCount(DataReader reader) throws DataReadingException, DataParsingException {
        short leadingByte = reader.readNext(DataTypeFactory.getUnsignedByte());
        if (leadingByte != 0x0F) {
            throw new DataParsingException("Blte chunk count value is not lead by 0x0F");
        }
        short b1 = reader.readNext(DataTypeFactory.getUnsignedByte());
        short b2 = reader.readNext(DataTypeFactory.getUnsignedByte());
        short b3 = reader.readNext(DataTypeFactory.getUnsignedByte());
        return b1 << 16 | b2 << 8 | b3;
    }
}
