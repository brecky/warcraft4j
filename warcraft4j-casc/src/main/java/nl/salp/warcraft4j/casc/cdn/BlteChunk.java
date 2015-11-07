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

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class BlteChunk {
    public static final long VARIABLE_SIZE = 0;
    private final long compressedSize;
    private final long decompressedSize;
    private final DataDecompressor decompressorSupplier;
    private byte[] data;
    private boolean compressed;

    public BlteChunk(long compressedSize, byte[] data, DataDecompressor decompressorSupplier) {
        this(compressedSize, VARIABLE_SIZE, data, decompressorSupplier);
    }

    public BlteChunk(long compressedSize, long decompressedSize, byte[] data, DataDecompressor decompressorSupplier) {
        if (data.length != compressedSize) {
            throw new CascParsingException(format("Error creating BLTE chunk, compressed data is %d bytes with %d bytes expected", data.length, compressedSize));
        }
        this.compressedSize = compressedSize;
        this.decompressedSize = decompressedSize;
        this.data = data;
        this.compressed = true;
        this.decompressorSupplier = decompressorSupplier;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public long getDecompressedSize() {
        return decompressedSize;
    }

    public boolean isDecompressedSizeVariable() {
        return decompressedSize == VARIABLE_SIZE;
    }

    public byte[] getData() {
        if (compressed) {
            data = decompressorSupplier.decompress(data, 0, data.length, decompressedSize);
            if (!isDecompressedSizeVariable() && data.length != decompressedSize) {
                throw new CascParsingException(format("Error decompressing BLTE chunk, decompressed data is %d bytes with %d bytes expected", data.length, decompressedSize));
            }
            compressed = false;
        }
        return data;
    }
}
