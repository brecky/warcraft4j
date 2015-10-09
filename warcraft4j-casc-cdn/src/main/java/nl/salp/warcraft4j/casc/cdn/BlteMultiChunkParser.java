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
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.util.Checksum;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteOrder;

import static java.lang.String.format;
import static nl.salp.warcraft4j.hash.Hashes.MD5;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
class BlteMultiChunkParser extends BlteChunkParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlteMultiChunkParser.class);

    public BlteMultiChunkParser(int chunkCount) {
        super(chunkCount);
    }

    @Override
    protected Logger logger() {
        return LOGGER;
    }

    @Override
    protected BlteChunkHeader parseChunkHeader(DataReader reader) throws DataReadingException, DataParsingException, CascParsingException {
        long compressedSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        long decompressedSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.BIG_ENDIAN);
        byte[] hash = reader.readNext(DataTypeFactory.getByteArray(16));
        return new BlteChunkHeader(compressedSize, decompressedSize, hash);
    }

    @Override
    protected BlteChunk parseChunk(DataReader reader, BlteChunkHeader header) throws DataReadingException, DataParsingException, CascParsingException {
        long compressedSize = header.getCompressedSize();
        long decompressedSize = header.getDecompressedSize();
        byte[] chunkData = reader.readNext(DataTypeFactory.getByteArray((int) compressedSize));
        char compressionType = (char) chunkData[0];
        byte[] data = ArrayUtils.subarray(chunkData, 1, chunkData.length);
        Checksum checksum = header.getChecksum();
        Checksum dataChecksum = new Checksum(MD5.hash(chunkData));
        LOGGER.trace("Comparing data checksum MD5[{}] from {} ({}) bytes of data with expected checksum MD5[{}]",
                dataChecksum, chunkData.length, compressedSize, checksum);
        if (!checksum.equals(dataChecksum)) {
            throw new CascParsingException(format("Error parsing BLTE chunk with compression %s, mismatching checksum (got %s, expected %s)", compressionType, dataChecksum, checksum));
        }

        return new BlteChunk(data.length, decompressedSize, data, getDecompressor(compressionType));
    }

}
