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
package nl.salp.warcraft4j.io.datatype;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@link DataType} implementation for a byte[].
 */
class ByteArrayDataType extends DataType<byte[]> {
    /** The number of entries in the byte[]. */
    private final int length;

    /**
     * Create a new ByteArrayDataType of a specific length.
     *
     * @param length The number of entries in the byte[].
     */
    public ByteArrayDataType(int length) {
        this.length = length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected byte[][] newArray(int entries) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Creation of arrays from arrays is not supported.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public DataType<byte[][]> asArrayType(int entries) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Creation of an array data types from array data types is not supported.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return length;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public byte[] readNext(ByteBuffer buffer, ByteOrder byteOrder) {
        byte[] data = new byte[length];
        buffer.order(byteOrder).get(data);
        return data;
    }
}
