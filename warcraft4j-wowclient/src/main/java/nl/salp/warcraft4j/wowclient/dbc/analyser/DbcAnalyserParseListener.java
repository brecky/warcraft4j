package nl.salp.warcraft4j.wowclient.dbc.analyser;

import java.io.File;

/**
 * TODO Document class.
 */
public interface DbcAnalyserParseListener {
    void onComplete(File file);
    void onError(File file, Exception exception);
}
