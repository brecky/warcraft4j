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
import org.apache.commons.lang.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
class RawDataDecompressor implements DataDecompressor {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(RawDataDecompressor.class);

    @Override
    public byte[] decompress(byte[] data, long compressedSize, long decompressedSize) throws CascParsingException {
        if (decompressedSize != 0 && data.length != decompressedSize) {
            throw new CascParsingException(format("Error parsing raw BLTE file chunk, got %d bytes of compressed data instead of %d", data.length, decompressedSize));
        }
        LOGGER.trace("Returning {} bytes raw BLTE file chunk as data.", data.length);
        return data;
    }

    @Override
    public byte[] decompress(byte[] data, long dataOffset, long dataLength, long decompressedSize) throws CascParsingException {
        if (decompressedSize != 0 && data.length != decompressedSize) {
            throw new CascParsingException(format("Error parsing raw BLTE file chunk, got %d bytes of compressed data instead of %d", data.length, decompressedSize));
        }
        LOGGER.trace("Returning {} bytes raw BLTE file chunk as data.", data.length);
        return ArrayUtils.subarray(data, (int) dataOffset, (int) dataLength);
    }
}