package nl.salp.warcraft4j.wowclient.dbc.analyser;

import java.util.*;

/**
 * TODO Document class.
 */
public class ParsedContent {
    private Map<String, UnparsedFile> unparsedFiles;
    private Map<String, ParsedFile> parsedFiles;
    private Map<Value, List<ParsedFile>> parsedFileIds;

    public ParsedContent() {
        parsedFiles = new HashMap<>();
        unparsedFiles = new HashMap<>();
        parsedFileIds = new HashMap<>();
    }

    public void addUnparsed(UnparsedFile unparsedFile) {
        unparsedFiles.put(unparsedFile.getFileName(), unparsedFile);
    }

    public void addParsed(ParsedFile parsedFile) {
        parsedFiles.put(parsedFile.getFileName(), parsedFile);
        for (Value id : parsedFile.getRecordIds()) {
            if (!parsedFileIds.containsKey(id)) {
                parsedFileIds.put(id, new ArrayList<ParsedFile>());
            }
            parsedFileIds.get(id).add(parsedFile);
        }
    }

    public boolean isIdPresent(Value id) {
        return parsedFileIds.containsKey(id);
    }


    public List<ParsedFile> getParsedFiles(Value id) {
        List<ParsedFile> files;
        if (isIdPresent(id)) {
            files = parsedFileIds.get(id);
        } else {
            files = Collections.emptyList();
        }
        return files;
    }

    public ParsedFile getParsedFile(String name) {
        ParsedFile file = null;
        if (name != null && parsedFiles.containsKey(name)) {
            file = parsedFiles.get(name);
        }
        return file;
    }

    public List<ParsedFile> getParsedFiles() {
        return new ArrayList<>(parsedFiles.values());
    }

    public int getParsedFileCount() {
        return parsedFiles.size();
    }

    public List<UnparsedFile> getUnparsedFiles() {
        return new ArrayList<>(unparsedFiles.values());
    }

    public int getUnparsedFileCount() {
        return unparsedFiles.size();
    }

    public int getNumberOfIds() {
        return parsedFileIds.size();
    }

}
