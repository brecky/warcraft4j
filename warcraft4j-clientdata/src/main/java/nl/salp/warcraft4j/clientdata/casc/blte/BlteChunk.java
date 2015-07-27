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

import nl.salp.warcraft4j.clientdata.casc.Checksum;

import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class BlteChunk {
    private final long compressedSize;
    private final long decompressedSize;
    private final byte[] data;
    private final DataDecompressor decompressorSupplier;

    public BlteChunk(long compressedSize, long decompressedSize, byte[] data, DataDecompressor decompressorSupplier) {
        this.compressedSize = compressedSize;
        this.decompressedSize = decompressedSize;
        this.data = data;
        this.decompressorSupplier = decompressorSupplier;
    }

    public long getCompressedSize() {
        return compressedSize;
    }

    public long getDecompressedSize() {
        return decompressedSize;
    }

    public byte[] getCompressedData() {
        return data;
    }

    public byte[] decompress() {
        return decompressorSupplier.decompress(data, 0, data.length, decompressedSize);
    }
}
