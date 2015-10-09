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

import nl.salp.warcraft4j.util.DataTypeUtil;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * {@link DataType} implementation for a fixed length string.
 */
class FixedLengthStringDataType extends DataType<String> {
    /** The length of the string. */
    private final int length;
    /** The character set to decode the String with. */
    private final Charset charset;
    /** The number of bytes per character for the character set. */
    private final int bytesPerCharacter;

    /**
     * Create a new FixedLengthString data type instance using the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
     *
     * @param length The length of the string (in characters).
     */
    public FixedLengthStringDataType(int length) {
        this(length, DEFAULT_CHARACTERSET);
    }

    /**
     * Create a new FixedLengthString data type instance with a specific character set.
     *
     * @param length  The length of the string (in characters).
     * @param charset The character set to decode the String with.
     */
    public FixedLengthStringDataType(int length, Charset charset) {
        this.length = length;
        this.charset = charset;
        this.bytesPerCharacter = DataTypeUtil.getAverageBytesPerCharacter(charset);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String[] newArray(int entries) throws UnsupportedOperationException {
        return new String[entries];
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getLength() {
        return length * bytesPerCharacter;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readNext(ByteBuffer buffer, ByteOrder byteOrder) {
        byte[] data = new byte[getLength()];
        buffer.order(byteOrder).get(data);
        return new String(data, charset);
    }
}
