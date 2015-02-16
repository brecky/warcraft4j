package nl.salp.warcraft4j.wowclient.dbc.analyser;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * TODO Document class.
 */
public class Field implements Comparable<Field> {
    private final int fieldIndex;
    private final List<Value> values;

    public Field(int fieldIndex, List<Value> values) {
        this.fieldIndex = fieldIndex;
        if (values == null || values.isEmpty()) {
            this.values = Collections.emptyList();
        } else {
            this.values = values;
        }
    }

    public int getFieldIndex() {
        return fieldIndex;
    }

    public List<Value> getValues() {
        return values;
    }

    public int getRecordCount() {
        return values.size();
    }

    public Value getValue(int entry) {
        Value value = Value.EMPTY;
        if (isValueAvailable(entry)) {
            value = values.get(entry);
        }
        return value;
    }

    public boolean isValueAvailable(int entry) {
        return 0 < entry && entry < getRecordCount();
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
    public int compareTo(Field other) {
        int cmp;
        if (other == null) {
            cmp = 1;
        } else {
            cmp = this.fieldIndex - other.fieldIndex;
        }
        return cmp;
    }

    public static class Builder {
        private int fieldIndex;
        private List<Value> values;

        public Builder() {
            fieldIndex = -1;
            values = new ArrayList<>();
        }

        public Builder withColumn(int fieldIndex) {
            this.fieldIndex = fieldIndex;
            return this;
        }

        public Builder withValue(Value value) {
            values.add(value);
            return this;
        }

        public Field build() {
            if (fieldIndex < 0) {
                throw new IllegalArgumentException("Unable to create a Field without setting a field index.");
            }
            return new Field(fieldIndex, values);
        }
    }
}
