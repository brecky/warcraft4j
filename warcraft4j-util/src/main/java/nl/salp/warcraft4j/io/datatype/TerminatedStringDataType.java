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

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * {@link DataType} implementation for a zero-terminated string or a string till the end of the buffer.
 */
class TerminatedStringDataType extends DataType<String> {
    /** The length of the terminated String in bytes, set to -1 to indicate a variable length. */
    private static final int LENGTH_BYTES = -1;
    /** The character to decode the String with. */
    private final Charset charset;
    /** The number of bytes per character. */
    private final int bytesPerChar;

    /**
     * Create a new TerminatedStringDataType with the default character set ({@link DataType#DEFAULT_CHARACTERSET}).
     */
    public TerminatedStringDataType() {
        this(DEFAULT_CHARACTERSET);
    }

    /**
     * Create a new TerminatedStringDataType.
     *
     * @param charset The character set to use for decoding the characters.
     */
    public TerminatedStringDataType(Charset charset) {
        this.charset = charset;
        this.bytesPerChar = DataTypeUtil.getAverageBytesPerCharacter(charset);
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
        return LENGTH_BYTES;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String readNext(ByteBuffer buffer, ByteOrder byteOrder) {
        buffer.order(byteOrder);
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        byte c;
        while (buffer.hasRemaining() && (c = buffer.get()) != 0) {
            byteStream.write(c);
        }
        return new String(byteStream.toByteArray(), charset);
    }
}
