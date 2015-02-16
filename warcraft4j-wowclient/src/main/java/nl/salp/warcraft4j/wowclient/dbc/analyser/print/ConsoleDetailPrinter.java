package nl.salp.warcraft4j.wowclient.dbc.analyser.print;

import nl.salp.warcraft4j.wowclient.dbc.StringBlock;
import nl.salp.warcraft4j.wowclient.dbc.analyser.Field;
import nl.salp.warcraft4j.wowclient.dbc.analyser.ParsedContent;
import nl.salp.warcraft4j.wowclient.dbc.analyser.ParsedFile;
import nl.salp.warcraft4j.wowclient.dbc.analyser.StringTokenReferences;
import nl.salp.warcraft4j.wowclient.dbc.header.Header;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class ConsoleDetailPrinter implements AnalysisPrinter {
    private final String dbcFile;

    public ConsoleDetailPrinter(String dbcFile) {
        this.dbcFile = dbcFile;
    }

    @Override
    public void print(ParsedContent parsedContent) throws IOException {
        ParsedFile file = parsedContent.getParsedFile(dbcFile);
        Header header = file.getHeader();
        StringBlock stringBlock = file.getStringBlock();

        StringTokenReferences stringTokenReferences = new StringTokenReferences(file);

        System.out.println(format("%s [", file.getFileName()));
        System.out.println(format("    - magic string : %s", header.getMagicString()));
        System.out.println(format("    - records      : %d", file.getRecordCount()));
        System.out.println(format("    - fields       : %d", file.getFieldCount()));
        System.out.println(format("    - string table [", header.getStringBlockSize()));
        System.out.println(format("        - size        : %d bytes", header.getStringBlockSize()));
        System.out.println(format("        - entries     : %d", stringBlock.getAvailablePositions().size()));
        System.out.println(format("        - field score ["));
        for (Field field : file.getFields()) {
            System.out.println(format("            - %d: %.2f%% (%d/%d)", field.getFieldIndex(), stringTokenReferences.getReferencePercentage(field), stringTokenReferences.getReferences(field), stringTokenReferences.getTotalRecords()));
        }
        System.out.println(format("        ]"));
        System.out.println(format("    ]"));
        System.out.println(format("]"));
    }
}
