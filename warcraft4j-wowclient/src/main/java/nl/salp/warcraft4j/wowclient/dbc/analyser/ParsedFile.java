package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.StringBlock;
import nl.salp.warcraft4j.wowclient.dbc.header.Header;

import java.util.*;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class ParsedFile {
    private String fileName;
    private Header header;
    private StringBlock stringBlock;
    private List<Record> records;
    private List<Field> fields;
    private Map<Value, List<Record>> idRecordMapping;
    private Map<Integer, Field> fieldMapping;

    public ParsedFile(String fileName, Header header, StringBlock stringBlock, List<Record> records, List<Field> fields) {
        this.fileName = fileName;
        this.header = header;
        this.stringBlock = stringBlock;
        this.records = records;
        idRecordMapping = new HashMap<>();
        for (Record record : records) {
            if (!idRecordMapping.containsKey(record)) {
                idRecordMapping.put(record.getId(), new ArrayList<Record>());
            }
            idRecordMapping.get(record.getId()).add(record);
        }
        this.fields = fields;
        fieldMapping = new HashMap<>();
        for (Field field : fields) {
            if (fieldMapping.containsKey(field.getFieldIndex())) {
                throw new IllegalArgumentException(format("Multiple indexes found for field with index %d.", field.getFieldIndex()));
            }
            fieldMapping.put(field.getFieldIndex(), field);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public Header getHeader() {
        return header;
    }

    public StringBlock getStringBlock() {
        return stringBlock;
    }

    public List<Record> getRecords() {
        return Collections.unmodifiableList(records);
    }

    public int getRecordCount() {
        return records.size();
    }

    public Set<Value> getRecordIds() {
        return Collections.unmodifiableSet(idRecordMapping.keySet());
    }

    public boolean isRecordPresentWithId(Value id) {
        return idRecordMapping.containsKey(id);
    }

    public List<Record> getRecords(Value id) {
        List<Record> records;
        if (idRecordMapping.containsKey(id)) {
            records = idRecordMapping.get(id);
        } else {
            records = Collections.emptyList();
        }
        return records;
    }

    public int getRecordCount(Value id) {
        int count = 0;
        if (idRecordMapping.containsKey(id)) {
            count = idRecordMapping.get(id).size();
        }
        return count;
    }

    public boolean isIdMappedToMultipleRecords(Value id) {
        return isRecordPresentWithId(id) && idRecordMapping.get(id).size() > 1;
    }

    public List<Value> getIdsWithMultipleRecords() {
        List<Value> ids = new ArrayList<>();
        for (Value id : idRecordMapping.keySet()) {
            if (idRecordMapping.get(id).size() > 0) {
                ids.add(id);
            }
        }
        return ids;
    }

    public List<Field> getFields() {
        return Collections.unmodifiableList(fields);
    }

    public int getFieldCount() {
        return fields.size();
    }

    public Field getField(int fieldIndex) {
        Field field = null;
        if (fieldMapping.containsKey(fieldIndex)) {
            field = fieldMapping.get(fieldIndex);
        }
        return field;
    }

    public static class Builder {
        private String fileName;
        private Header header;
        private StringBlock stringBlock;
        private List<Record> records;
        private List<Field> fields;

        public Builder() {
            records = new ArrayList<>();
            fields = new ArrayList<>();
        }

        public Builder withFileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public Builder withHeader(Header header) {
            this.header = header;
            return this;
        }

        public Builder withStringBlock(StringBlock stringBlock) {
            this.stringBlock = stringBlock;
            return this;
        }

        public Builder withRecord(Record record) {
            this.records.add(record);
            return this;
        }

        public Builder withField(Field field) {
            this.fields.add(field);
            return this;
        }

        private List<Record> calculateRecords() {
            validateFields();
            List<Record> records = new ArrayList<>(header.getRecordCount());
            for (int i = 0; i < header.getRecordCount(); i++) {
                Record.Builder builder = new Record.Builder(header).withRow(i);
                for (Field field : fields) {
                    builder.withValue(field.getValue(i));
                }
                records.add(builder.build());
            }
            return records;
        }

        private List<Field> calculateFields() {
            validateRecords();
            List<Field> fields = new ArrayList<>(header.getFieldCount());
            for (int i = 0; i < header.getFieldCount(); i++) {
                Field.Builder builder = new Field.Builder().withColumn(i);
                for (Record record : records) {
                    builder.withValue(record.getValue(i));
                }
                fields.add(builder.build());
            }
            return fields;
        }

        private void validateFields() {
            if (fields.size() != header.getFieldCount()) {
                throw new IllegalArgumentException(format("ParsedFile requires %d fields, but only found %d.", header.getFieldCount(), fields.size()));
            }
            int recordCount = header.getRecordCount();
            for (Field f : fields) {
                if (f.getRecordCount() != recordCount) {
                    throw new IllegalArgumentException(format("ParsedFile has %d records but Field %d has only %d records.", recordCount, f.getFieldIndex(), f.getRecordCount()));
                }
            }
        }

        private void validateRecords() {
            if (records.size() != header.getRecordCount()) {
                throw new IllegalArgumentException(format("ParsedFile requires %d records, but only found %d.", header.getRecordCount(), records.size()));
            }
            int recordCount = header.getRecordCount();
            for (Field f : fields) {
                if (f.getRecordCount() != recordCount) {
                    throw new IllegalArgumentException(format("ParsedFile has %d fields but Record %d has only %d fields.", recordCount, f.getFieldIndex(), f.getRecordCount()));
                }
            }
        }

        public ParsedFile build() {
            if (fileName == null) {
                throw new IllegalArgumentException("Can't construct a ParsedFile with no file name set.");
            }
            if (header == null) {
                throw new IllegalArgumentException("Can't construct a ParsedFile with no header.");
            }
            if (stringBlock == null) {
                throw new IllegalArgumentException("Can't construct a ParsedFile with no StringBlock.");
            }
            List<Record> records = this.records;
            List<Field> fields = this.fields;
            if (records.isEmpty() && fields.isEmpty()) {
                throw new IllegalArgumentException("Can't construct a ParsedFile with neither fields nor records available.");
            } else if (records.isEmpty()) {
                records = calculateRecords();
            } else if (fields.isEmpty()) {
                fields = calculateFields();
            }
            return new ParsedFile(fileName, header, stringBlock, records, fields);
        }
    }
}
