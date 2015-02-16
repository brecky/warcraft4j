package nl.salp.warcraft4j.wowclient.dbc.analyser.print;

import nl.salp.warcraft4j.wowclient.dbc.analyser.ParsedContent;

import java.io.IOException;

/**
 * TODO Document class.
 */
public interface AnalysisPrinter {
    void print(ParsedContent parsedContent) throws IOException;
}
