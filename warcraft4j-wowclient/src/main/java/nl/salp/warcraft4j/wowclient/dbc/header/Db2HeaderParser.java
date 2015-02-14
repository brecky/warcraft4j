package nl.salp.warcraft4j.wowclient.dbc.header;

import nl.salp.warcraft4j.wowclient.io.WowReader;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
 class Db2HeaderParser {
    public DbcHeader read(String magicString, WowReader reader) throws IOException {
        if (!Db2Header.MAGICSTRING.equals(magicString)) {
            throw new IllegalArgumentException(format("Unknown DB2 magic String encountered: %s", magicString));
        }
        return parseDb2Header(reader);
    }

    private Db2Header parseDb2Header(WowReader reader) throws IOException {
        int recordCount = reader.readNextInt32();
        int fieldCount = reader.readNextInt32();
        int recordSize = reader.readNextInt32();
        int stringBlockSize = reader.readNextInt32();
        int stringBlockHash = reader.readNextInt32();
        int build = reader.readNextInt32();
        int timestampLastWritten = reader.readNextInt32();

        if (build > Db2ExtHeader.LAST_BUILD_PRE_EXTENDED_HEADER) {
            return parseDb2ExtHeader(reader, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten);
        } else {
            return new Db2Header(Db2Header.MAGICSTRING, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten);
        }
    }

    private Db2ExtHeader parseDb2ExtHeader(WowReader reader, int recordCount, int fieldCount, int recordSize, int stringBlockSize, int stringBlockHash, int build, int timestampLastWritten) throws IOException {
        int minId = reader.readNextInt32();
        int maxId = reader.readNextInt32();
        int locale = reader.readNextInt32();
        byte[] unknown = reader.readNextBytes(4);
        int[] indices;
        short[] rowLengths;
        if (maxId > 0) {
            int entryCount = maxId - minId + 1;
            indices = reader.readNextInt32Array(entryCount);
            rowLengths = reader.readNextShortArray(entryCount);
        } else {
            indices = new int[0];
            rowLengths = new short[0];
        }
        return new Db2ExtHeader(Db2Header.MAGICSTRING, recordCount, fieldCount, recordSize, stringBlockSize, stringBlockHash, build, timestampLastWritten, minId, maxId, locale, unknown, indices, rowLengths);
    }
}
