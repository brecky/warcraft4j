package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.StringBlock;
import nl.salp.warcraft4j.wowclient.dbc.header.Header;

/**
 * TODO Document class.
 */
public class UnparsedFile {
    private final String name;
    private final Header header;
    private final StringBlock stringBlock;
    private final byte[] data;
    private final Exception exception;

    public UnparsedFile(String name, Header header, StringBlock stringBlock, byte[] data) {
        this(name, header, stringBlock, data, null);
    }

    public UnparsedFile(String name, Header header, StringBlock stringBlock, byte[] data, Exception exception) {
        this.name = name;
        this.header = header;
        this.stringBlock = stringBlock;
        this.data = data;
        this.exception = exception;
    }

    public String getFileName() {
        return name;
    }

    public Header getHeader() {
        return header;
    }

    public StringBlock getStringBlock() {
        return stringBlock;
    }

    public byte[] getData() {
        return data;
    }

    public Exception getException() {
        return exception;
    }

    public boolean isDueToException() {
        return exception != null;
    }

    public boolean isDueToRecordCount() {
        return header.getRecordCount() == 0;
    }

    public int getRecordCount() {
        return header.getRecordCount();
    }

    public boolean isDueToDataLength() {
        return header.getRecordSize() != (header.getFieldCount() * 4);
    }

    public int getDataLengthShortage() {
        return (header.getFieldCount() * 4) - header.getRecordSize();
    }
}
