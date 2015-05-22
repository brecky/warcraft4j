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
package nl.salp.warcraft4j.clientdata.dbc;

import nl.salp.warcraft4j.clientdata.io.*;

import java.io.IOException;

import static nl.salp.warcraft4j.clientdata.io.DataType.getFixedLengthString;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * {@link DataParser} implementation for reading and parsing an {@link DbcHeader} instance.
 *
 * @author Barre Dijkstra
 */
class DbcHeaderParser extends RandomAccessDataParser<DbcHeader> {
    /** The magic String for a DBC file. */
    public static final String DBC_MAGICSTRING = "WDBC";
    /** The header size of a DBC file. */
    private static final int DBC_HEADER_SIZE = 20;
    /** The magic string for a DB2 file. */
    public static final String DB2_MAGICSTRING = "WDB2";
    /** The magic string for an ADB file. */
    public static final String ADB_MAGICSTRING = "WCH2";
    /** The default header size of a DB2. */
    private static final int DB2_HEADER_SIZE = 32;
    /** The version of the last build before the extended DB2 header was introduced. */
    public static final int DB2_LASTBUILD_BEFORE_EXTENSION = 12880;
    /** The base size for an extended DB2 header. */
    private static final int DB2_EXTENDED_HEADER_SIZE_BASE = 48;
    /** The length of the magic string. */
    private static final int MAGIC_STRING_LENGTH = 4;


    @Override
    public DbcHeader parse(DataReader reader) throws IOException, DataParsingException {
        DbcHeader.Builder builder = new DbcHeader.Builder();
        // DBC parsing
        String magicString = reader.readNext(getFixedLengthString(MAGIC_STRING_LENGTH));
        int headerSize = DBC_HEADER_SIZE;
        builder.withMagicString(magicString);
        builder.withEntryCount(reader.readNext(DataType.getInteger()));
        builder.withEntryFieldCount(reader.readNext(DataType.getInteger()));
        builder.withSingleEntrySize(reader.readNext(DataType.getInteger()));
        builder.withStringTableBlockSize(reader.readNext(DataType.getInteger()));
        // DB2 parsing.
        if (DB2_MAGICSTRING.equals(magicString) || ADB_MAGICSTRING.equals(magicString)) {
            headerSize = DB2_HEADER_SIZE;
            builder.withStringTableBlockHash(reader.readNext(DataType.getInteger()));
            int buildNumber = reader.readNext(DataType.getInteger());
            builder.withBuildNumber(buildNumber);
            builder.withTimestampLastWritten(reader.readNext(DataType.getInteger()));

            if (buildNumber > DB2_LASTBUILD_BEFORE_EXTENSION) {
                headerSize = DB2_EXTENDED_HEADER_SIZE_BASE;
                int minEntryId = reader.readNext(DataType.getInteger());
                builder.withMinEntryId(minEntryId);
                int maxEntryId = reader.readNext(DataType.getInteger());
                builder.withMaxEntryId(maxEntryId);
                builder.withLocaleId(reader.readNext(DataType.getInteger()));
                builder.withUnknownDataBlock(reader.readNext(DataType.getByteArray(4)));
                if (maxEntryId > 0) {
                    int entryCount = maxEntryId - minEntryId + 1;
                    builder.withRowIndexes(toPrimitive(reader.readNext(DataType.getInteger().asArrayType(entryCount))));
                    builder.withRowStringLength(toPrimitive(reader.readNext(DataType.getShort().asArrayType(entryCount))));

                    headerSize += (entryCount * DataType.getInteger().getLength());
                    headerSize += (entryCount * DataType.getShort().getLength());
                }
            }
        }
        builder.withHeaderSize(headerSize);
        return builder.build();
    }

    @Override
    public int getInstanceDataSize() {
        return DBC_HEADER_SIZE;
    }
}
