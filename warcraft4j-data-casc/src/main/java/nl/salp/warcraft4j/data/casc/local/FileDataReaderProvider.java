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
package nl.salp.warcraft4j.data.casc.local;

import nl.salp.warcraft4j.data.casc.CascParsingException;
import nl.salp.warcraft4j.data.casc.DataReaderProvider;
import nl.salp.warcraft4j.io.reader.DataReader;
import nl.salp.warcraft4j.io.reader.file.FileDataReader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FileDataReaderProvider implements DataReaderProvider {
    @Override
    public Supplier<DataReader> getDataReader(String uri) throws CascParsingException {
        return () -> new FileDataReader(toPath(uri));
    }

    @Override
    public Supplier<DataReader> getDataReader(String uri, long offset, long length) throws CascParsingException{
        return () -> new FileDataReader(toPath(uri), offset, length);
    }

    private Path toPath(String uri) {
        if (isEmpty(uri)) {
            throw new CascParsingException("Can't create a file reader for an empty path.");
        }
        Path path = Paths.get(uri);
        if (Files.notExists(path) || !Files.isRegularFile(path)) {
            throw new CascParsingException(format("Can't create a file reader for %s, file either doesn't exist or is not a file.", uri));
        }
        if (!Files.isReadable(path)) {
            throw new CascParsingException(format("Can't create a file reader for non-readable file %s", uri));
        }
        return path;
    }
}
