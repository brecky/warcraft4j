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
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
class BlteSingleChunkParser extends BlteChunkParser {
    /** The logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(BlteSingleChunkParser.class);

    private final long fileSize;

    public BlteSingleChunkParser(long fileSize) {
        super(1);
        this.fileSize = fileSize;
    }

    @Override
    protected Logger logger() {
        return LOGGER;
    }

    @Override
    protected BlteChunkHeader parseChunkHeader(DataReader reader) {
        return new BlteChunkHeader(fileSize);
    }

    @Override
    protected BlteChunk parseChunk(DataReader reader, BlteChunkHeader header) throws DataReadingException, DataParsingException, CascParsingException {
        long compressedSize = header.getCompressedSize();
        byte[] chunkData = reader.readNext(DataTypeFactory.getByteArray((int) compressedSize));
        char compressionType = (char) chunkData[0];
        byte[] data = ArrayUtils.subarray(chunkData, 1, chunkData.length);
        if (data.length != compressedSize - 1) {
            throw new CascParsingException(format("Error parsing BLTE chunk, got %d bytes of compressed data instead of %d bytes", data.length, compressedSize));
        }
        return new BlteChunk(data.length, data, getDecompressor(compressionType));
    }
}
