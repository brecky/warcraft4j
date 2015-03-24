package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.StringBlock;

import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document class.
 */
public class StringTokenReferences {
    private final ParsedFile parsedFile;
    private Map<Integer, Integer> fieldReferences;

    public StringTokenReferences(ParsedFile parsedFile) {
        this.parsedFile = parsedFile;
        fieldReferences = new HashMap<>();
        analyse(parsedFile);
    }

    private void analyse(ParsedFile parsedFile) {
        StringBlock block = parsedFile.getStringBlock();
        for (Field field : parsedFile.getFields()) {
            for (Value value : field.getValues()) {
                if (block.isEntryAvailableForPosition(value.asInt())) {
                    if (fieldReferences.containsKey(field)) {
                        int count = fieldReferences.get(field);
                        fieldReferences.put(field.getFieldIndex(), count);
                    } else {
                        fieldReferences.put(field.getFieldIndex(), 1);
                    }
                }
            }
        }
    }

    public ParsedFile getParsedFile() {
        return parsedFile;
    }

    public int getTotalRecords() {
        return parsedFile.getRecordCount();
    }

    public int getTotalFields() {
        return parsedFile.getFieldCount();
    }

    public int getReferences(int field) {
        int references = 0;
        if (fieldReferences.containsKey(field)) {
            references = fieldReferences.get(field);
        }
        return references;
    }

    public int getReferences(Field field) {
        int references = 0;
        if (field != null) {
            references = getReferences(field.getFieldIndex());
        }
        return references;
    }

    public float getReferencePercentage(int field) {
        float percentage = 0;
        if (fieldReferences.containsKey(field)) {
            int references = fieldReferences.get(field);
            percentage = ((float) references / getTotalFields() * 100);
        }
        return percentage;
    }

    public float getReferencePercentage(Field field) {
        float percentage = 0;
        if (field != null) {
            percentage = getReferencePercentage(field.getFieldIndex());
        }
        return percentage;
    }
}
