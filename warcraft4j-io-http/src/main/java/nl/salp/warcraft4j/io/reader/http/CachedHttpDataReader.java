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
package nl.salp.warcraft4j.io.reader.http;

import nl.salp.warcraft4j.io.reader.ByteArrayDataReader;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.parser.DataParsingException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class CachedHttpDataReader extends ByteArrayDataReader {
    private static final int READ_CHUNK_SIZE = 8192;

    public CachedHttpDataReader(String url) throws DataParsingException {
        super(getData(url));
    }

    public CachedHttpDataReader(String url, long offset, long length) {
        super(getData(url, offset, length));
    }

    private static byte[] getData(String url, long offset, long length) throws DataParsingException {
        byte[] data;
        if (offset < 0) {
            throw new DataParsingException(format("Can't create a http reader for %s with negative data block offset %d.", url, offset));
        }
        if (length < 0) {
            throw new DataParsingException(format("Can't create a http reader for %s with negative data block length %d.", url, length));
        }
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); HttpDataReader dataReader = new HttpDataReader(url)) {
            if (offset + length > dataReader.size()) {
                throw new DataParsingException(format("Can't create a http reader for %s with %d bytes of data from offset %d with length %d.",
                        url, dataReader.size(), offset, length));
            }
            long read = 0;
            long dataSize;
            if (offset > 0) {
                dataReader.skip(offset);
            }
            if (length == 0) {
                dataSize = dataReader.size() - offset;
            } else {
                dataSize = length;
            }
            while (read < dataSize) {
                int chunkSize = (int) Math.min(READ_CHUNK_SIZE, dataReader.remaining());
                outputStream.write(dataReader.readNext(DataTypeFactory.getByteArray(chunkSize)));
                read += chunkSize;
            }
            data = outputStream.toByteArray();
        } catch (IOException e) {
            throw new DataParsingException(format("Error caching the data from %s", url), e);
        }

        return data;
    }

    private static byte[] getData(String url) throws DataParsingException {
        byte[] data;
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream(); HttpDataReader dataReader = new HttpDataReader(url)) {
            long read = 0;
            long dataSize = dataReader.size();
            while (read < dataSize) {
                int chunkSize = (int) Math.min(READ_CHUNK_SIZE, dataReader.remaining());
                outputStream.write(dataReader.readNext(DataTypeFactory.getByteArray(chunkSize)));
                read += chunkSize;
            }
            data = outputStream.toByteArray();
        } catch (IOException e) {
            throw new DataParsingException(format("Error caching the data from %s", url), e);
        }

        return data;
    }
}
