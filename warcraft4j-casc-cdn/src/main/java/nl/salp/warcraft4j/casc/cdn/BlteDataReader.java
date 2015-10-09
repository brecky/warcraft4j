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
import nl.salp.warcraft4j.casc.ContentChecksum;
import nl.salp.warcraft4j.hash.Hashes;
import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class BlteDataReader implements DataReader {
    /** The logger. */
    protected static final Logger LOGGER = LoggerFactory.getLogger(BlteDataReader.class);
    private final ContentChecksum contentChecksum;
    private final BlteFile blteFile;
    private final DataReader parsedDataReader;

    public BlteDataReader(Supplier<DataReader> rawDataReader, long fileSize) {
        this(rawDataReader, fileSize, null);
    }

    public BlteDataReader(Supplier<DataReader> rawDataReader, long fileSize, long offset, long length) {
        this(rawDataReader, fileSize, offset, length, null);
    }

    public BlteDataReader(Supplier<DataReader> rawDataReader, long fileSize, ContentChecksum contentChecksum) {
        this.contentChecksum = contentChecksum;
        this.blteFile = parseBlteFile(rawDataReader, fileSize);
        validateData();
        this.parsedDataReader = blteFile.getDataReader().get();
    }

    public BlteDataReader(Supplier<DataReader> rawDataReader, long fileSize, long offset, long length, ContentChecksum contentChecksum) {
        this.contentChecksum = contentChecksum;
        this.blteFile = parseBlteFile(rawDataReader, fileSize);
        validateData();
        this.parsedDataReader = blteFile.getDataReader(offset, length).get();
    }

    private BlteFile parseBlteFile(Supplier<DataReader> rawDataReader, long fileSize) throws CascParsingException {
        try (DataReader reader = rawDataReader.get()) {
            return new BlteFileParser().parse(reader, fileSize);
        } catch (IOException e) {
            throw new CascParsingException(e);
        }
    }

    private void validateData() throws CascParsingException {
        if (contentChecksum != null) {
            ContentChecksum decompressedChecksum = new ContentChecksum(Hashes.MD5.hash(blteFile.decompress()));
            LOGGER.trace("Validating BLTE data with checksum {} against expected checksum {}", decompressedChecksum, contentChecksum.toHexString());
            if (!contentChecksum.equals(decompressedChecksum)) {
                throw new CascParsingException(format("Porbably corrupt BLTE data with checksum %s while %s was expected.",
                        decompressedChecksum.toHexString(), contentChecksum.toHexString()));
            }
        }

    }

    public Optional<ContentChecksum> getContentChecksum() {
        return Optional.ofNullable(contentChecksum);
    }

    public BlteFile getBlteFile() {
        return blteFile;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long position() throws DataReadingException {
        return parsedDataReader.position();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void position(long position) throws DataReadingException {
        parsedDataReader.position(position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isRandomAccessSupported() {
        return parsedDataReader.hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasRemaining() throws DataReadingException {
        return parsedDataReader.hasRemaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long remaining() throws DataReadingException {
        return parsedDataReader.remaining();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long size() throws DataReadingException {
        return parsedDataReader.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skip(long bytes) throws DataReadingException {
        parsedDataReader.skip(bytes);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T peek(DataType<T> dataType) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return parsedDataReader.peek(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T peek(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return parsedDataReader.peek(dataType, byteOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T readNext(DataType<T> dataType) throws DataReadingException, DataParsingException {
        return parsedDataReader.readNext(dataType);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws DataReadingException, DataParsingException {
        return parsedDataReader.readNext(dataType, byteOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T read(DataType<T> dataType, long position) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return parsedDataReader.read(dataType, position);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T read(DataType<T> dataType, long position, ByteOrder byteOrder) throws DataReadingException, DataParsingException, UnsupportedOperationException {
        return parsedDataReader.read(dataType, position, byteOrder);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() throws IOException {
        if (parsedDataReader != null) {
            parsedDataReader.close();
        }
    }
}