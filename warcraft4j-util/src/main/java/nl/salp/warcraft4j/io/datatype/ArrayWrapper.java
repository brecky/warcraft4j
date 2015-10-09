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
 * {@link DataType} implementation that wraps a {@link DataType} as a native array.
 *
 * @param <K> The type of the data type to wrap.
 */
class ArrayWrapper<K> extends DataType<K[]> {
    /** The wrapped data type. */
    private final DataType<K> wrappedType;
    /** The number of entries for the array. */
    private final int arrayLength;

    /**
     * Create a new ArrayWrapper.
     *
     * @param wrappedType The data type to wrap as an array.
     * @param arrayLength The number of elements in the array.
     */
    public ArrayWrapper(DataType<K> wrappedType, int arrayLength) {
        this.wrappedType = wrappedType;
        this.arrayLength = arrayLength;
    }

    @Override
    protected K[][] newArray(int entries) {
        throw new UnsupportedOperationException("Creation of arrays from arrays is not supported.");
    }

    @Override
    public DataType<K[][]> asArrayType(int entries) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("Creation of array an array data type from array and array data type is not supported.");
    }

    @Override
    public int getLength() {
        return wrappedType.getLength() * arrayLength;
    }

    @Override
    public ByteOrder getDefaultByteOrder() {
        return wrappedType.getDefaultByteOrder();
    }

    @Override
    public K[] readNext(ByteBuffer buffer, ByteOrder byteOrder) {
        buffer.order(byteOrder);
        K[] entries = wrappedType.newArray(arrayLength);
        for (int i = 0; i < arrayLength; i++) {
            entries[i] = wrappedType.readNext(buffer);
        }
        return entries;
    }
}
