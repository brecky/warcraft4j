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
import nl.salp.warcraft4j.io.datatype.DataType;
import nl.salp.warcraft4j.io.parser.DataParsingException;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.RandomAccessDataReader;

import java.io.IOException;
import java.nio.ByteOrder;
import java.util.function.Supplier;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class BlteDataReader extends RandomAccessDataReader {
    private DataReader parsedDataReader;

    public BlteDataReader(Supplier<DataReader> rawDataReader, long fileSize) {
        parsedDataReader = parseBlteFile(rawDataReader, fileSize).getDataReader().get();
    }

    public BlteDataReader(Supplier<DataReader> rawDataReader, long fileSize, long offset, long length) {
        parsedDataReader = parseBlteFile(rawDataReader, fileSize).getDataReader(offset, length).get();
    }

    private BlteFile parseBlteFile(Supplier<DataReader> rawDataReader, long fileSize) throws CascParsingException {
        try (DataReader reader = rawDataReader.get()) {
            return reader.readNext(new BlteFileParser(fileSize));
        } catch (IOException e) {
            throw new CascParsingException(e);
        }
    }

    @Override
    public long position() {
        return parsedDataReader.position();
    }

    @Override
    public void position(long position) throws IOException {
        parsedDataReader.position(position);
    }

    @Override
    public boolean hasRemaining() throws IOException {
        return parsedDataReader.hasRemaining();
    }

    @Override
    public long remaining() throws IOException {
        return parsedDataReader.remaining();
    }

    @Override
    public long size() throws IOException {
        return parsedDataReader.size();
    }

    @Override
    public void skip(long bytes) throws IOException {
        parsedDataReader.skip(bytes);
    }

    @Override
    public <T> T readNext(DataType<T> dataType, ByteOrder byteOrder) throws IOException, DataParsingException {
        return parsedDataReader.readNext(dataType, byteOrder);
    }

    @Override
    public void close() throws IOException {
        if (parsedDataReader != null) {
            parsedDataReader.close();
        }
    }
}