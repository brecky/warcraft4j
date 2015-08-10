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
import nl.salp.warcraft4j.io.reader.ByteArrayDataReader;
import nl.salp.warcraft4j.io.reader.RandomAccessDataReader;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class BlteFile {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlteFileParser.class);
    private final List<BlteChunk> chunks;

    public BlteFile(List<BlteChunk> chunks) {
        this.chunks = chunks;
    }

    public long getCompressedSize() {
        return chunks.stream()
                .mapToLong(BlteChunk::getCompressedSize)
                .sum();
    }

    public long getDecompressedSize() {
        return chunks.stream()
                .mapToLong(BlteChunk::getDecompressedSize)
                .sum();
    }

    public int getChunkCount() {
        return chunks.size();
    }

    public byte[] getCompressedData() {
        return merge((chunk) -> chunk.getCompressedData());
    }

    public Supplier<RandomAccessDataReader> getDataReader() {
        return () -> new ByteArrayDataReader(decompress());
    }

    public Supplier<RandomAccessDataReader> getDataReader(long offset, long length) {
        return () -> {
            byte[] data = decompress();
            if (offset + length > data.length) {
                throw new CascParsingException(format("Unable to create a reader from offset %d and data length %d for a blte file with %d bytes of data.", offset, length, data.length));
            }
            return new ByteArrayDataReader(ArrayUtils.subarray(data, (int) offset, (int) length));
        };
    }

    public byte[] decompress() {
        long decompressedSize = getDecompressedSize();
        if (decompressedSize > Integer.MAX_VALUE) {
            throw new CascParsingException(format("Unable to decompress %d bytes of BLTE data into a single byte array.", decompressedSize));
        }
        int size = (int) decompressedSize;
        ByteArrayOutputStream out = new ByteArrayOutputStream(size);
        for (int i = 0; i < chunks.size(); i++) {
            try {
                BlteChunk chunk = chunks.get(i);
                byte[] chunkData = chunk.decompress();
                LOGGER.trace("Writing chunk {} with {} bytes of decompressed data ({} bytes expected, {} bytes compressed)", i, chunkData.length,
                        chunk.getDecompressedSize(), chunk.getCompressedSize());
                if (chunkData.length != chunk.getDecompressedSize()) {
//                    throw new CascParsingException(format("BLTE file chunk %d was decompressed to %d bytes of data while %d were expected.", i, chunkData.length, chunk.getDecompressedSize()));
                }
                out.write(chunkData);
            } catch (IOException e) {
                throw new CascParsingException(format("Unable to concatenate BLTE chunk data for chunk %d", i), e);
            }
        }
        byte[] data = out.toByteArray();
        LOGGER.trace("Decompressed BLTE file to {} bytes of data ({} bytes expected, {} bytes compressed)", data.length, getDecompressedSize(), getCompressedSize());
        return data;
    }

    private byte[] merge(Function<BlteChunk, byte[]> dataFunction) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        for (int i = 0; i < chunks.size(); i++) {
            writeChunk(i, chunks.get(i), out, dataFunction);
        }
        return out.toByteArray();
    }

    private static void writeChunk(int index, BlteChunk chunk, ByteArrayOutputStream out, Function<BlteChunk, byte[]> dataFunction) {
        byte[] data = dataFunction.apply(chunk);
        LOGGER.trace("Writing chunk {} with {} bytes of decompressed data ({} bytes expected, {} bytes compressed)", index + 1, data.length,
                chunk.getDecompressedSize(), chunk.getCompressedSize());
        writeByteArray(data, out);

    }

    private static void writeByteArray(byte[] byteArray, ByteArrayOutputStream out) {
        try {
            out.write(byteArray);
            out.flush();
        } catch (IOException e) {
            throw new CascParsingException("Unable to concatenate chunk data", e);
        }
    }
}
