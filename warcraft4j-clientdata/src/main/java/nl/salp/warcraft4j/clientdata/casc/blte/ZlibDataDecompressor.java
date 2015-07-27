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

import nl.salp.warcraft4j.clientdata.casc.CascFileParsingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class ZlibDataDecompressor implements DataDecompressor {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ZlibDataDecompressor.class);

    @Override
    public byte[] decompress(byte[] data, long compressedSize, long decompressedSize) throws CascFileParsingException {
        return decompress(data, 0, compressedSize, decompressedSize);
    }

    @Override
    public byte[] decompress(byte[] data, long offset, long compressedSize, long decompressedSize) throws CascFileParsingException {
        try {
            // CASCExplorer uses eq of ArrayUtils.subarray(data, 2, data.length - 2) instead of full array size, which is the 2 byte zip header
            Inflater decompresser = new Inflater(false);
            LOGGER.trace("Decompressing {}B of gzip'd chunk data to {}B.", compressedSize, compressedSize);
            decompresser.setInput(data, (int) offset, data.length);
            byte[] result = new byte[(int) decompressedSize];
            int resultLength = decompresser.inflate(result);
            decompresser.end();
            if (resultLength != decompressedSize) {
                throw new CascFileParsingException(String.format("Decompressed BLTE chunk to a %dB output while %dB was specified.", resultLength, decompressedSize));
            }
            LOGGER.trace("Succesfully decompressed gzip'd chunk data to {}B of data.", result.length, decompressedSize);
            return result;
        } catch (DataFormatException e) {
            throw new CascFileParsingException(e);
        }
    }

}
