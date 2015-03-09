package nl.salp.warcraft4j.wowclient.dbc.header;

import nl.salp.warcraft4j.wowclient.io.WowReader;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class HeaderParser {
    /**
     * @param reader
     *
     * @return
     *
     * @throws IOException
     */
    public Header parse(WowReader reader) throws IOException {
        String magicString = reader.readNextFixedLengthString(4);
        Header header;
        if (DbcHeader.isHeaderFor(magicString)) {
            header = new DbcHeaderParser().read(magicString, reader);
        } else if (Db2Header.isHeaderFor(magicString)) {
            header = new Db2HeaderParser().read(magicString, reader);
        } else {
            throw new IllegalArgumentException(format("Cannot parse the header for unknown DBC file type %s", magicString));
        }
        return header;
    }
}
