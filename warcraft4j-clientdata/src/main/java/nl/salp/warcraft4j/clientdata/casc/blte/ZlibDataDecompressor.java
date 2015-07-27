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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public byte[] decompress(byte[] data, long compressedSize, long decompressedSize) throws CascParsingException {
        return decompress(data, 0, compressedSize, decompressedSize);
    }

    @Override
    public byte[] decompress(byte[] data, long offset, long compressedSize, long decompressedSize) throws CascParsingException {
        try {
            // CASCExplorer uses eq of ArrayUtils.subarray(data, 2, data.length - 2) instead of full array size, which is the 2 byte zip header
            Inflater decompresser = new Inflater(false);
            LOGGER.trace("Decompressing {}B of gzip'd chunk data to {}B.", compressedSize, compressedSize);
            decompresser.setInput(data, (int) offset, data.length);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            long resultLength = 0;
            while (!decompresser.finished()) {
                byte[] result = new byte[4096];
                resultLength += decompresser.inflate(result);
                out.write(result);
            }
            byte[] result = out.toByteArray();
            decompresser.end();
            if (resultLength != decompressedSize && (compressedSize != decompressedSize)) {
                throw new CascParsingException(String.format("Decompressed BLTE chunk to a %dB output while %dB was specified.", resultLength, decompressedSize));
            }
            LOGGER.trace("Succesfully decompressed gzip'd chunk data to {}B of data.", result.length);
            return result;
        } catch (DataFormatException | IOException e) {
            throw new CascParsingException(e);
        }
    }

}
