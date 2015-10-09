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
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * Data type for reading various types of data types (with different byte ordering).
 *
 * @author Barre Dijkstra
 */
public abstract class DataType<T> {
    /** The default character set. */
    public static final Charset DEFAULT_CHARACTERSET = StandardCharsets.US_ASCII;
    /** The default ByteOrder used by Java (network byte order). */
    protected static final ByteOrder BYTE_ORDER_JAVA = ByteOrder.BIG_ENDIAN;

    /**
     * Create a new native array for the type with the given number of entries.
     *
     * @param entries The number of entries.
     *
     * @return The new array.
     *
     * @throws UnsupportedOperationException When the creation of an array is not supported (e.g. for array wrappers).
     */
    protected abstract T[] newArray(int entries) throws UnsupportedOperationException;

    /**
     * Get a native array type for the data type.
     *
     * @param entries the number of entries.
     *
     * @return The DataType that supports parsing to a native array of the type.
     *
     * @throws UnsupportedOperationException When the creation of a native array type is not supported (e.g. for array types).
     */
    public DataType<T[]> asArrayType(int entries) throws UnsupportedOperationException {
        return new ArrayWrapper<>(this, entries);
    }

    /**
     * Get the length of the data type in bytes.
     *
     * @return The length or {@code &lt;= 0} for variable length data types.
     *
     * @see #isVariableLength()
     */
    public abstract int getLength();

    /**
     * Check if the data type is a variable length data type.
     *
     * @return {@code true} if the data type is variable length.
     */
    public boolean isVariableLength() {
        return getLength() <= 0;
    }

    /**
     * Check if a byte is a terminator byte for variable length data types.
     * <p>
     * This method may be overwritten by implementations.
     * </p>
     *
     * @return The {@code true} if the byte is a terminator byte.
     */
    public boolean isVariableLengthTerminator(byte b) {
        return b == 0x0;
    }

    /**
     * Get the default byte order for the data type.
     *
     * @return The default byte order.
     */
    public ByteOrder getDefaultByteOrder() {
        return BYTE_ORDER_JAVA;
    }

    /**
     * Read the next value in the given data type from the given ByteBuffer.
     *
     * @param buffer The ByteBuffer.
     *
     * @return The next value.
     */
    public T readNext(ByteBuffer buffer) {
        return readNext(buffer, getDefaultByteOrder());
    }

    /**
     * Read the next value in the given data type from the given ByteBuffer that is in the given byte order.
     *
     * @param buffer    The ByteBuffer.
     * @param byteOrder The byte order the buffer is in.
     *
     * @return The next value.
     */
    public abstract T readNext(ByteBuffer buffer, ByteOrder byteOrder);
}
