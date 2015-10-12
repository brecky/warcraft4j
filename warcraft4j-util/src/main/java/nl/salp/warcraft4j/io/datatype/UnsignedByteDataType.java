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
 * {@link DataType} implementation for a 8-bit unsigned byte wrapped in a 16-bit signed short.
 */
class UnsignedByteDataType extends DataType<Short> {
    /** 64-bit mask for 32-bit integers. */
    private static final short BYTE_MASK = 0x00FF;

    /**
     * {@inheritDoc}
     */
    @Override
    protected Short[] newArray(int entries) throws UnsupportedOperationException {
        return new Short[entries];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Short readNext(ByteBuffer buffer, ByteOrder byteOrder) {
        return (short) (buffer.order(byteOrder).get() & BYTE_MASK);
    }
}
