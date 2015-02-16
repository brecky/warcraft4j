package nl.salp.warcraft4j.wowclient.dbc.analyser.print;

import nl.salp.warcraft4j.wowclient.dbc.analyser.ParsedContent;
import nl.salp.warcraft4j.wowclient.dbc.analyser.ParsedFile;
import nl.salp.warcraft4j.wowclient.dbc.analyser.UnparsedFile;
import nl.salp.warcraft4j.wowclient.dbc.header.Header;

import java.io.IOException;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class ConsoleOverviewPrinter implements AnalysisPrinter {
    @Override
    public void print(ParsedContent parsedContent) throws IOException {
        System.out.println(format("Analysis results ["));
        System.out.println(format("    * Files processed: %d", (parsedContent.getParsedFileCount() + parsedContent.getUnparsedFileCount())));
        System.out.println(format("    * Files parsed   : %d [", parsedContent.getParsedFileCount()));
        for (ParsedFile file : parsedContent.getParsedFiles()) {
            Header header = file.getHeader();
            System.out.println(format("          - %s [", file.getFileName()));
            System.out.println(format("              - magicString         : %s", header.getMagicString()));
            System.out.println(format("              - fields              : %d", header.getFieldCount()));
            System.out.println(format("              - records             : %d", file.getRecords().size()));
            int idCollissions = file.getRecords().size() - file.getRecordIds().size();
            if (idCollissions > 0) {
                System.out.println(format("              - idCollissions       : %d", idCollissions));
            }
            System.out.println(format("          ]"));
        }
        System.out.println(format("      ]"));
        System.out.println(format("    * Files unparsed : %d [", parsedContent.getUnparsedFileCount()));
        for (UnparsedFile file : parsedContent.getUnparsedFiles()) {
            Header header = file.getHeader();
            int difference = header.getRecordSize() - (header.getFieldCount() * 4);
            System.out.println(format("          - %s [", file.getFileName()));
            System.out.println(format("              - magicString: %s", header.getMagicString()));
            System.out.println(format("              - records    : %d", header.getRecordCount()));
            System.out.println(format("              - fields     : %d", header.getFieldCount()));
            System.out.println(format("              - entry size : %db", header.getRecordSize()));
            System.out.println(format("              - difference : %db short (%db expected, %db actual)", difference, header.getRecordSize(), (header.getFieldCount() * 4)));
            System.out.println(format("          ]"));
        }
        System.out.println(format("      ]"));
        System.out.println(format("]"));
    }
}
