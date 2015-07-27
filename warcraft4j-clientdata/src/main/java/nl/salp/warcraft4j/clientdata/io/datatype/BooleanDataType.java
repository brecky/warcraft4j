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
package nl.salp.warcraft4j.clientdata.io.datatype;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * {@link DataType} implementation for a boolean.
 */
class BooleanDataType extends DataType<Boolean> {

    @Override
    protected Boolean[] newArray(int entries) throws UnsupportedOperationException {
        return new Boolean[entries];
    }

    @Override
    public int getLength() {
        return 1;
    }

    @Override
    public ByteOrder getDefaultByteOrder() {
        return ByteOrder.LITTLE_ENDIAN;
    }

    @Override
    public Boolean readNext(ByteBuffer buffer) {
        return getValue(buffer.get());
    }

    /**
     * Get the boolean value of a byte.
     *
     * @param data The byte to get the value from.
     *
     * @return The boolean value of the byte.
     */
    private boolean getValue(byte data) {
        return (data >= 1) ? true : false;
    }
}