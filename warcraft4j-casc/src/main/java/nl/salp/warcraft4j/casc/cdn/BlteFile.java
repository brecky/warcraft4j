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
import nl.salp.warcraft4j.io.ByteArrayDataReader;
import nl.salp.warcraft4j.io.DataReader;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class BlteFile {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlteFile.class);
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

    public Supplier<DataReader> getDataReader() {
        return () -> new ByteArrayDataReader(decompress());
    }

    public Supplier<DataReader> getDataReader(long offset, long length) {
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
        try (ByteArrayOutputStream out = new ByteArrayOutputStream(size)) {
            for (int i = 0; i < chunks.size(); i++) {
                BlteChunk chunk = chunks.get(i);
                byte[] chunkData = chunk.getData();
                LOGGER.trace("Decompressed chunk {} with {} bytes of data to {} bytes", i, chunk.getCompressedSize(), chunkData.length);
                if (!chunk.isDecompressedSizeVariable() && chunkData.length != chunk.getDecompressedSize()) {
                    throw new CascParsingException(format("BLTE file chunk %d was decompressed to %d bytes of data while %d were expected.", i, chunkData.length, chunk
                            .getDecompressedSize()));
                }
                out.write(chunkData);
            }
            byte[] data = out.toByteArray();
            LOGGER.trace("Decompressed BLTE file to {} bytes of data in {} chunks to {} of data.", getCompressedSize(), chunks.size(), data.length);
            return data;
        } catch (IOException e) {
            throw new CascParsingException(format("Error compressing %d bytes BLTE file with %d chunks", decompressedSize, chunks.size()), e);
        }
    }
}
