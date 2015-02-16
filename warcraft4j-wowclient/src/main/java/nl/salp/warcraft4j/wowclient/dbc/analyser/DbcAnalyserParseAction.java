package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.StringBlock;
import nl.salp.warcraft4j.wowclient.dbc.StringBlockParser;
import nl.salp.warcraft4j.wowclient.dbc.header.Header;
import nl.salp.warcraft4j.wowclient.dbc.header.HeaderParser;
import nl.salp.warcraft4j.wowclient.io.WowByteArrayReader;
import nl.salp.warcraft4j.wowclient.io.WowFileReader;
import nl.salp.warcraft4j.wowclient.io.WowReader;

import java.io.File;
import java.io.IOException;

/**
 * TODO Document class.
 */
public class DbcAnalyserParseAction implements Runnable {
    private final ParsedContent parsedContent;
    private final File file;
    private final DbcAnalyserParseListener listener;

    public DbcAnalyserParseAction(ParsedContent parsedContent, File file, DbcAnalyserParseListener listener) {
        this.parsedContent = parsedContent;
        this.file = file;
        this.listener = listener;
    }

    @Override
    public void run() {
        try (WowReader reader = new WowFileReader(file)) {
            Header header = new HeaderParser().parse(reader);
            byte[] entryData = reader.readNextBytes(header.getRecordBlockSize());
            byte[] stringBlockData = reader.readNextBytes(header.getStringBlockSize());
            StringBlock stringBlock = new StringBlockParser().parse(stringBlockData);
            parseFile(file.getName(), header, stringBlock, entryData);
            listener.onComplete(file);
        } catch (IOException e) {
            listener.onError(file, e);
        }

    }

    private void parseFile(String fileName, Header header, StringBlock stringBlock, byte[] entryData) {
        if ((header.getFieldCount() * 4) != header.getRecordSize() || header.getRecordCount() == 0) {
            parsedContent.addUnparsed(new UnparsedFile(fileName, header, stringBlock, entryData));
        } else {
            try {
                ParsedFile.Builder fileBuilder = new ParsedFile.Builder().withFileName(fileName).withHeader(header).withStringBlock(stringBlock);
                WowByteArrayReader reader = new WowByteArrayReader(entryData);
                for (int row = 0; row < header.getRecordCount(); row++) {
                    Record.Builder recordBuilder = new Record.Builder(header).withRow(row);
                    for (int i = 0; i < header.getFieldCount(); i++) {
                        Value value = new Value(reader.readNextInt32());
                        recordBuilder.withValue(value);
                    }
                    fileBuilder.withRecord(recordBuilder.build());
                }
                parsedContent.addParsed(fileBuilder.build());
            } catch (IOException e) {
                parsedContent.addUnparsed(new UnparsedFile(fileName, header, stringBlock, entryData, e));
            }
        }
    }
}
