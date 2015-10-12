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
 * {@link DataType} implementation for a 32-bit unsigned integer wrapped in a 64-bit signed long.
 */
class UnsignedIntegerDataType extends DataType<Long> {
    /** Size of the data type in bytes (Integer.SIZE is in bits). */
    private static final int BYTES = Integer.SIZE / 8;
    /** 64-bit mask for 32-bit integers. */
    private static final long INT_MASK = Integer.MIN_VALUE | Integer.MAX_VALUE;

    /**
     * {@inheritDoc}
     */
    @Override
    protected Long[] newArray(int entries) throws UnsupportedOperationException {
        return new Long[entries];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return BYTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long readNext(ByteBuffer buffer, ByteOrder byteOrder) {
        return buffer.order(byteOrder).getInt() & INT_MASK;
    }
}
