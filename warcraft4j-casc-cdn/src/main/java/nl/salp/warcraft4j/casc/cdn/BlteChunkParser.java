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
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
abstract class BlteChunkParser {
    private final int chunkCount;

    protected BlteChunkParser(int chunkCount) {
        this.chunkCount = chunkCount;
    }

    protected final int getChunkCount() {
        return chunkCount;
    }

    protected abstract Logger logger();


    public final List<BlteChunk> parse(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        List<BlteChunkHeader> headers = parseHeaders(reader);
        List<BlteChunk> chunks = parseChunks(headers, reader);
        return chunks;
    }

    private List<BlteChunkHeader> parseHeaders(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        List<BlteChunkHeader> headers = new ArrayList<>();
        for (int i = 0; i < chunkCount; i++) {
            logger().trace("Parsing chunk header {} out of {}", i + 1, chunkCount);
            headers.add(parseChunkHeader(reader));
            logger().trace("Successfully parsed header {} [checksum: {}]", i + 1, headers.get(i).getChecksum());
        }
        return headers;
    }

    protected abstract BlteChunkHeader parseChunkHeader(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException;

    private List<BlteChunk> parseChunks(List<BlteChunkHeader> headers, DataReader reader) throws DataParsingException {
        List<BlteChunk> chunks = new ArrayList<>();
        for (int i = 0; i < chunkCount; i++) {
            BlteChunk chunk = parseChunk(reader, headers.get(i));
            chunks.add(chunk);
            logger().trace("Parsed BLTE file chunk {} ({} bytes of chunk data, {} bytes uncompressed)", i,
                    chunk.getCompressedSize(), chunk.getDecompressedSize());
        }
        return chunks;
    }

    protected abstract BlteChunk parseChunk(DataReader reader, BlteChunkHeader header) throws DataReadingException, DataParsingException, CascParsingException;

    protected static DataDecompressor getDecompressor(char type) {
        DataDecompressor decompressor;
        switch (type) {
            case 'N':
                decompressor = new RawDataDecompressor();
                break;
            case 'Z':
                decompressor = new ZlibDataDecompressor();
                break;
            default:
                throw new CascParsingException(format("Unable to determine the BLTE chunk decompression strategy for type '%s'", type));
        }
        return decompressor;
    }
}
