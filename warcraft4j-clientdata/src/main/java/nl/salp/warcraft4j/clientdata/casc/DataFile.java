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
package nl.salp.warcraft4j.clientdata.casc;

import nl.salp.warcraft4j.clientdata.casc.blte.BlteFile;
import nl.salp.warcraft4j.clientdata.casc.blte.BlteFileParser;
import nl.salp.warcraft4j.clientdata.io.RandomAccessDataReader;
import nl.salp.warcraft4j.clientdata.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.clientdata.io.file.FileDataReader;

import java.io.IOException;
import java.nio.ByteOrder;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class DataFile {
    private static final int HEADER_SIZE = 30;

    private final int fileNumber;
    private final Path path;
    private final Map<Integer, BlockHeader> entries;

    public DataFile(int fileNumber, Path path) throws CascParsingException {
        this.fileNumber = fileNumber;
        this.path = path;
        this.entries = new HashMap<>();
    }

    public int getFileNumber() {
        return fileNumber;
    }

    public Path getPath() {
        return path;
    }

    public BlteFile getEntry(IndexEntry index) {
        BlockHeader header = getBlockHeader(index);
        try (RandomAccessDataReader reader = getRawBlteReader(header).get()) {
            return reader.readNext(new BlteFileParser(index));
        } catch (IOException e) {
            throw new CascParsingException(format("Error reading %dB BLTE file from data file %d (%s) with data file offset %d", index.getFileSize(), fileNumber, path, header.getDataStart()), e);
        }
    }

    public Supplier<RandomAccessDataReader> getEntryReader(IndexEntry index) {
        return getEntry(index).getDataReader();
    }

    private BlockHeader getBlockHeader(IndexEntry index) {
        if (index.getFileNumber() != fileNumber) {
            throw new CascParsingException(format("Tried to read file with data file number %d from data file number %d", index.getFileNumber(), fileNumber));
        }
        if (!entries.containsKey(index.getDataFileOffset())) {
            entries.put(index.getDataFileOffset(), parseBlockHeader(index));
        }
        return entries.get(index.getDataFileOffset());
    }

    private Supplier<RandomAccessDataReader> getBlockReader(IndexEntry index) {
        return () -> new FileDataReader(path, index.getDataFileOffset(), index.getFileSize());
    }

    private Supplier<RandomAccessDataReader> getRawBlteReader(BlockHeader index) {
        return () -> new FileDataReader(path, index.getDataStart(), index.getDataSize());
    }

    private BlockHeader parseBlockHeader(IndexEntry index) {
        try (RandomAccessDataReader reader = getBlockReader(index).get()) {
            byte[] hash = reader.readNext(DataTypeFactory.getByteArray(16));
            long blockSize = reader.readNext(DataTypeFactory.getUnsignedInteger(), ByteOrder.LITTLE_ENDIAN);
            byte[] unknown = reader.readNext(DataTypeFactory.getByteArray(10));
            return new BlockHeader(index.getDataFileOffset(), hash, blockSize, unknown);
        } catch (IOException e) {
            throw new CascParsingException(format("Error reading data file %d (%s) for offset %d", fileNumber, path, index.getDataFileOffset()), e);
        }
    }

    private static class BlockHeader {
        private final byte[] hash;
        private final long offset;
        private final long blockSize;
        private final byte[] unknownData;

        public BlockHeader(long blockStartOffset, byte[] hash, long blockSize, byte[] unknownData) {
            this.offset = blockStartOffset;
            this.hash = hash;
            this.blockSize = blockSize;
            this.unknownData = unknownData;
        }

        public byte[] getHash() {
            return hash;
        }

        public long getBlockStart() {
            return offset;
        }

        public long getBlockSize() {
            return blockSize;
        }

        public long getBlockEnd() {
            return offset + blockSize;
        }

        public long getDataStart() {
            return getBlockStart() + HEADER_SIZE;
        }

        public long getDataSize() {
            return getBlockSize() - HEADER_SIZE;
        }

        public long getDataEnd() {
            return getDataStart() + getDataSize();
        }

        public byte[] getUnknownData() {
            return unknownData;
        }

        public byte[] readData(RandomAccessDataReader reader) throws IOException {
            reader.position(getDataStart());
            return reader.readNext(DataTypeFactory.getByteArray((int) getDataSize()));
        }
    }
}
