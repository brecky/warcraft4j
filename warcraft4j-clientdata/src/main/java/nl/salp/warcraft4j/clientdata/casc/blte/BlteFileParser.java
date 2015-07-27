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
package nl.salp.warcraft4j.clientdata.casc.blte;

import nl.salp.warcraft4j.clientdata.casc.CascParsingException;
import nl.salp.warcraft4j.clientdata.casc.IndexEntry;
import nl.salp.warcraft4j.clientdata.io.DataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.clientdata.io.parser.DataParser;
import nl.salp.warcraft4j.clientdata.io.parser.DataParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.List;

import static java.lang.String.format;
import static java.nio.charset.StandardCharsets.US_ASCII;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class BlteFileParser implements DataParser<BlteFile> {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlteFileParser.class);
    private static final String MAGIC_STRING = "BLTE";
    private final IndexEntry entry;
    private final long fileSize;

    public BlteFileParser(IndexEntry entry) {
        this.entry = entry;
        this.fileSize = entry.getFileSize();
    }

    public BlteFileParser(long fileSize) {
        this.entry= null;
        this.fileSize = fileSize;
    }

    @Override
    public BlteFile parse(DataReader reader) throws IOException, DataParsingException {
        LOGGER.trace("Parsing {}B BLTE file from data file at offset {}", fileSize, reader.position());
        long readerPos = reader.position();
        String magicString = reader.readNext(DataTypeFactory.getFixedLengthString(MAGIC_STRING.length(), US_ASCII));
        LOGGER.trace("Parsed {}B magic string {}", reader.position() - readerPos, magicString);
        if (!MAGIC_STRING.equals(magicString)) {
            throw new DataParsingException(format("BLTE file has the magic string '%s' while '%s' was required.", magicString, MAGIC_STRING));
        }
        long dataOffset = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        boolean multiChunk = (dataOffset != 0);
        BlteChunkParser parser;
        int chunkCount;
        if (multiChunk) {
            chunkCount = parseMultiChunkCount(reader);
            LOGGER.trace("Parsing {} byte BLTE multi-chunk file with {} chunks", fileSize, chunkCount);
            parser = new BlteMultiChunkParser(chunkCount);
        } else {
            chunkCount = 1;
            LOGGER.trace("Parsing {} byte BLTE single chunk file", fileSize );
            parser = new BlteSingleChunkParser(fileSize);
        }

        List<BlteChunk> chunks = reader.readNext(parser);
        if (chunks.size() != chunkCount) {
            throw new CascParsingException(format("Parsed %d chunks of data from BLTE file while %d chunks were expected.", chunks.size(), chunkCount));
        }
        LOGGER.debug("Successfully parsed {} bytes file from BLTE file with {} bytes of data from {} chunk(s)",
                chunks.stream().mapToLong(BlteChunk::getCompressedSize).sum(),
                chunks.stream().mapToLong(BlteChunk::getDecompressedSize).sum(),
                chunkCount);
        return new BlteFile(chunks);
    }

    private int parseMultiChunkCount(DataReader reader) throws IOException, DataParsingException {
        byte leadingByte = reader.readNext(DataTypeFactory.getByte());
        if (leadingByte != 0x0F) {
            throw new DataParsingException("Blte chunk count value is not lead by 0x0F");
        }
        byte b1 = reader.readNext(DataTypeFactory.getByte());
        byte b2 = reader.readNext(DataTypeFactory.getByte());
        byte b3 = reader.readNext(DataTypeFactory.getByte());
        return b1 << 16 | b2 << 8 | b3 << 0;
    }
}
