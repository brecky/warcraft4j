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

import nl.salp.warcraft4j.io.reader.DataReader;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.IOException;
import java.util.function.Supplier;

import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FileReference {
    private final transient DataReaderProvider dataReaderProvider;
    private final String fileUri;
    private final long dataOffset;
    private final long dataLength;

    public FileReference(String fileUri, DataReaderProvider dataReaderProvider) throws IllegalArgumentException {
        this(fileUri, 0, 0, dataReaderProvider);
    }

    public FileReference(String fileUri, long dataOffset, long dataLength, DataReaderProvider dataReaderProvider) throws IllegalArgumentException {
        if (isEmpty(fileUri)) {
            throw new IllegalArgumentException("Can't create a file reference for a file with a null id.");
        }
        this.fileUri = fileUri;
        if (dataOffset < 0) {
            throw new IllegalArgumentException("Can't create a file reference for a data segment with a negative offset.");
        }
        this.dataOffset = dataOffset;
        if (dataLength < 0) {
            throw new IllegalArgumentException("Can't create a file reference for a data segment with a negative length.");
        }
        this.dataLength = dataLength;
        if (dataReaderProvider == null) {
            throw new IllegalArgumentException("Can't create a file reference with a null data reader provider.");
        }
        this.dataReaderProvider = dataReaderProvider;
    }

    public String getFileUri() {
        return fileUri;
    }

    public long getDataOffset() {
        return dataOffset;
    }

    public long getDataLength() {
        return dataLength;
    }

    public Supplier<DataReader> getDataReaderSupplier() {
        return this.dataReaderProvider.getDataReader(fileUri, dataOffset, dataLength);
    }

    public DataReader getDataReader() throws IOException {
        return getDataReaderSupplier().get();
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
