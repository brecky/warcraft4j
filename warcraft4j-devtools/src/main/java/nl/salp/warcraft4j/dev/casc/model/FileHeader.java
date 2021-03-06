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
package nl.salp.warcraft4j.dev.casc.model;

import nl.salp.warcraft4j.Warcraft4jException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.util.DataTypeUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.function.Supplier;

import static nl.salp.warcraft4j.util.DataTypeUtil.byteArrayToHexString;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class FileHeader {
    private final byte[] header;
    private final int hash;

    public FileHeader(byte[] header) {
        this.header = header;
        this.hash = DataTypeUtil.hash(header);
    }

    public byte[] getHeader() {
        return header;
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && FileHeader.class.isAssignableFrom(obj.getClass())) {
            eq = Arrays.equals(header, ((FileHeader) obj).header);
        }
        return eq;
    }

    public String toHexString() {
        return byteArrayToHexString(header);
    }

    @Override
    public String toString() {
        return new String(header, StandardCharsets.UTF_8);
    }

    public static FileHeader parse(Supplier<DataReader> dataReaderSupplier) {
        try (DataReader dataReader = dataReaderSupplier.get()) {
            int headerSize = (int) Math.min(dataReader.remaining(), 4);
            byte[] header = dataReader.readNext(DataTypeFactory.getByteArray(headerSize));
            return new FileHeader(header);
        } catch (IOException e) {
            throw new Warcraft4jException(e);
        }
    }
}
