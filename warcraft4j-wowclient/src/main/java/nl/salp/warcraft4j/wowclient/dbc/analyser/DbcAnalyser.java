package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.analyser.print.ConsoleDetailPrinter;
import nl.salp.warcraft4j.wowclient.dbc.analyser.print.ConsoleOverviewPrinter;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TODO Document class.
 */
public class DbcAnalyser {
    private final String basePath;
    private final ParsedContent parsedContent;

    public DbcAnalyser(String basePath) throws Exception {
        this.basePath = basePath;
        this.parsedContent = new ParsedContent();
        parseAsynchronous(getDbcFiles());
    }

    private void parseAsynchronous(List<File> files) {
        int corePoolSize = 4;
        int maxPoolSize = 8;
        long keepAliveTime = 5000;
        ExecutorService threadPoolExecutor =
                new ThreadPoolExecutor(
                        corePoolSize,
                        maxPoolSize,
                        keepAliveTime,
                        TimeUnit.MILLISECONDS,
                        new LinkedBlockingQueue<Runnable>()
                );

        DbcAnalyserParseProgress progress = new DbcAnalyserParseProgress(files.size(), 5f);
        for (File file : files) {
            threadPoolExecutor.execute(new DbcAnalyserParseAction(parsedContent, file, progress));
        }

        threadPoolExecutor.shutdown();
        while (!progress.isDone()) {
            try {

                threadPoolExecutor.awaitTermination(50, TimeUnit.MILLISECONDS);
            } catch (InterruptedException e) {
                // Ignore.
            }
        }
    }

    private void parse(List<File> files) {
        DbcAnalyserParseProgress progress = new DbcAnalyserParseProgress(files.size(), 5f);
        for (File file : files) {
            new DbcAnalyserParseAction(parsedContent, file, progress).run();
        }
    }


    public ParsedContent getContent() {
        return parsedContent;
    }

    public List<File> getDbcFiles() {
        File dir = new File(basePath);

        return Arrays.asList(
                dir.listFiles(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name != null && (name.toLowerCase().endsWith(".db2") || name.toLowerCase().endsWith(".dbc"));
                    }
                }));
    }
}
