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
import nl.salp.warcraft4j.clientdata.io.ByteArrayDataReader;
import nl.salp.warcraft4j.clientdata.io.RandomAccessDataReader;
import org.apache.commons.lang3.ArrayUtils;

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
        return merge((chunk) -> chunk.decompress());
    }

    private byte[] merge(Function<BlteChunk, byte[]> dataFunction) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        chunks.stream()
                .map(dataFunction)
                .forEachOrdered(data -> writeByteArray(data, out));
        return out.toByteArray();
    }

    private static void writeByteArray(byte[] byteArray, ByteArrayOutputStream out) {
        try {
            out.write(byteArray);
        } catch (IOException e) {
            throw new CascParsingException("Unable to concatenate chunk data", e);
        }
    }
}
