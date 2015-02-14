package nl.salp.warcraft4j.wowclient.dbc.header;

import nl.salp.warcraft4j.wowclient.io.WowReader;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
class DbcHeaderParser {
    public DbcHeader read(String magicString, WowReader reader) throws IOException {
        if (!DbcHeader.MAGICSTRING.equals(magicString)) {
            throw new IllegalArgumentException(format("Unknown DBC magic String encountered: %s", magicString));
        }
        return parseDbcHeader(reader);
    }

    private DbcHeader parseDbcHeader(WowReader reader) throws IOException {
        int recordCount = reader.readNextInt32();
        int fieldCount = reader.readNextInt32();
        int recordSize = reader.readNextInt32();
        int stringBlockSize = reader.readNextInt32();

        return new DbcHeader(DbcHeader.MAGICSTRING, recordCount, fieldCount, recordSize, stringBlockSize);
    }
}
