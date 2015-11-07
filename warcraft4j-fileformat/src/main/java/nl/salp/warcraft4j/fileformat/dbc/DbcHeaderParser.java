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
package nl.salp.warcraft4j.fileformat.dbc;

import nl.salp.warcraft4j.io.DataParsingException;
import nl.salp.warcraft4j.io.DataReader;
import nl.salp.warcraft4j.io.DataReadingException;
import nl.salp.warcraft4j.io.datatype.DataTypeFactory;

import static java.nio.ByteOrder.LITTLE_ENDIAN;
import static nl.salp.warcraft4j.fileformat.dbc.DbcHeader.*;
import static org.apache.commons.lang3.ArrayUtils.toPrimitive;

/**
 * Parser for reading and parsing an {@link DbcHeader} instance.
 *
 * @author Barre Dijkstra
 */
class DbcHeaderParser {


    /**
     * Parse the header from the data in a reader.
     *
     * @param reader The data reader.
     *
     * @return The parsed {@link DbcHeader} instance.
     *
     * @throws DataReadingException When there was a problem reading the data.
     * @throws DataParsingException When there was a problem parsing the data.
     */
    public DbcHeader parse(DataReader reader) throws DataReadingException, DataParsingException {
        DbcHeaderBuilder builder = new DbcHeaderBuilder();
        // DBC parsing
        String magicString = reader.readNext(DataTypeFactory.getFixedLengthString(MAGIC_STRING_LENGTH));
        int headerSize = DBC_HEADER_SIZE;
        builder.withMagicString(magicString);
        builder.withEntryCount(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));
        builder.withEntryFieldCount(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));
        builder.withSingleEntrySize(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));
        builder.withStringTableBlockSize(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));
        // DB2 parsing.
        if (DB2_MAGICSTRING.equals(magicString) || ADB_MAGICSTRING.equals(magicString)) {
            headerSize = DB2_HEADER_SIZE;
            builder.withStringTableBlockHash(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));
            int buildNumber = reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN);
            builder.withBuildNumber(buildNumber);
            builder.withTimestampLastWritten(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));

            if (buildNumber > DB2_LASTBUILD_BEFORE_EXTENSION) {
                headerSize = DB2_EXTENDED_HEADER_SIZE_BASE;
                int minEntryId = reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN);
                builder.withMinEntryId(minEntryId);
                int maxEntryId = reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN);
                builder.withMaxEntryId(maxEntryId);
                builder.withLocaleId(reader.readNext(DataTypeFactory.getInteger(), LITTLE_ENDIAN));
                builder.withUnknownDataBlock(reader.readNext(DataTypeFactory.getByteArray(4)));
                if (maxEntryId > 0) {
                    int entryCount = maxEntryId - minEntryId + 1;
                    builder.withRowIndexes(toPrimitive(reader.readNext(DataTypeFactory.getInteger().asArrayType(entryCount), LITTLE_ENDIAN)));
                    builder.withRowStringLength(toPrimitive(reader.readNext(DataTypeFactory.getShort().asArrayType(entryCount), LITTLE_ENDIAN)));

                    headerSize += (entryCount * DataTypeFactory.getInteger().getLength());
                    headerSize += (entryCount * DataTypeFactory.getShort().getLength());
                }
            }
        }
        builder.withHeaderSize(headerSize);
        return builder.build();
    }
}