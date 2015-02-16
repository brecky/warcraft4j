package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.header.Header;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.String.format;

/**
 * TODO Document class.
 */
public class Record implements Comparable<Record> {
    private final int row;
    private final Value id;
    private final List<Value> fieldValues;

    public Record(int row, List<Value> fieldValues) {
        this.row = row;
        if (fieldValues == null || fieldValues.isEmpty()) {
            this.id = new Value(-1);
            this.fieldValues = Collections.emptyList();
        } else {
            id = fieldValues.get(0);
            this.fieldValues = fieldValues;
        }
    }

    public int getRow() {
        return row;
    }

    public Value getId() {
        return id;
    }

    public List<Value> getFieldValues() {
        return Collections.unmodifiableList(fieldValues);
    }

    public int getFieldCount() {
        return fieldValues.size();
    }

    public Value getValue(int col) {
        return fieldValues.get(col);
    }

    public boolean isValueAvailable(int col) {
        return col < fieldValues.size();
    }


    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int compareTo(Record other) {
        int comp;
        if (other == null) {
            comp = 1;
        } else {
            comp = this.row - other.row;
        }
        return comp;
    }

    public static class Builder {
        private final int fieldCount;
        private int row;
        private List<Value> fieldValues;

        public Builder(Header header) {
            fieldCount = header.getFieldCount();
            row = -1;
            this.fieldValues = new ArrayList<>();
        }

        public Builder withValue(Value value) {
            this.fieldValues.add(value);
            return this;
        }

        public Builder withRow(int row) {
            this.row = row;
            return this;
        }

        public Record build() {
            if (row < 0) {
                throw new IllegalArgumentException("Unable to create a Record without setting a row number.");
            }
            if (fieldValues.size() != fieldCount) {
                throw new IllegalArgumentException(format("Unable to create the Record, expected %d fields but got %d.", fieldCount, fieldValues.size()));
            }
            return new Record(row, fieldValues);
        }
    }
}
