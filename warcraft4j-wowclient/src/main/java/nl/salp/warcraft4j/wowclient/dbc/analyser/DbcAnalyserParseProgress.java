package nl.salp.warcraft4j.wowclient.dbc.analyser;

import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class DbcAnalyserParseProgress implements DbcAnalyserParseListener {
    private final int totalCount;
    private final float markPercent;
    private int successCount;
    private int failCount;
    private final Map<File, Exception> exceptions;

    public DbcAnalyserParseProgress(int totalCount, float markPercent) {
        this.totalCount = totalCount;
        this.markPercent = markPercent;
        exceptions = new HashMap<>();
    }

    @Override
    public synchronized void onComplete(File file) {
        successCount++;
        mark();
    }

    @Override
    public synchronized void onError(File file, Exception exception) {
        exceptions.put(file, exception);
        failCount++;
        mark();
    }

    public boolean isDone() {
        return totalCount == (successCount + failCount);
    }

    public int getRemaining() {
        return totalCount - (successCount + failCount);
    }

    public void mark() {
        int completeCount = successCount + failCount;
        float percent = ((float) completeCount / totalCount * 100);
        if (percent % 10 == 0) {
            System.out.println(format("%.0f%% complete (successful: %d, failed: %d, total: %d, done: %s)", percent, successCount, failCount, totalCount, isDone()));
        }
    }

    public Map<File, Exception> getExceptions() {
        return Collections.unmodifiableMap(exceptions);
    }
}
