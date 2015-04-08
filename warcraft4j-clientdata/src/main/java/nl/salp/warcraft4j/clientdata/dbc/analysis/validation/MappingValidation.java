package nl.salp.warcraft4j.clientdata.dbc.analysis.validation;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Validation for mappings between DBC/DB2 files and their mapping types.
 *
 * @author Barre Dijkstra
 */
public abstract class MappingValidation<T extends DbcEntry> {

    /**
     * Execute the rule.
     *
     * @return {@code true} if the rule is being followed.
     */
    public abstract boolean isValid();

    /**
     * Check if the file and mapping type are valid and a valid combination.
     *
     * @param file The file.
     * @param type The mapping type.
     *
     * @return {@code true} if both are valid and also in combination.
     */
    protected final boolean isValid(DbcFile file, Class<T> type) {
        return isNotNull(file) && isNotNull(type) && isMappingForFile(file, type);
    }

    /**
     * Check if the file is mapped by the specified mapping type.
     *
     * @param file The file.
     * @param type The mapping type.
     *
     * @return {@code true} if the mapping type contains the mapping for the file.
     */
    protected boolean isMappingForFile(DbcFile file, Class<T> type) {
        return isNotNull(file) && isNotNull(type) && file.getFilename().equals(DbcUtil.getMappedFile(type));
    }

    /**
     * Get all mapping type fields that are mapping to a file column.
     *
     * @param type                 The mapping type to get fields from.
     * @param includePaddingFields {@code true} to include the padding fields.
     *
     * @return The fields, ordered by the field order.
     */
    protected final Collection<Field> getMappedFields(Class<T> type, boolean includePaddingFields) {
        Collection<Field> fields = new TreeSet<>(new FieldOrderComparator());
        for (Field f : type.getDeclaredFields()) {
            DbcField dbcField = f.getAnnotation(DbcField.class);
            if (dbcField != null && (includePaddingFields || !dbcField.padding())) {
                fields.add(f);
            }
        }
        return fields;
    }

    /**
     * Check if the provided object is not null.
     *
     * @param object The object to check.
     *
     * @return {@code true} if the object is not null.
     */
    protected final boolean isNotNull(Object object) {
        return object != null;
    }

    /**
     * Comparator that compares 2 DbcField annotated fields based on their DbcField#order() value.
     */
    protected static class FieldOrderComparator implements Comparator<Field> {
        @Override
        public int compare(Field o1, Field o2) {
            int cmp;
            if (o1 == null && o2 == null) {
                cmp = 0;
            } else if (o1 == null) {
                cmp = 1;
            } else if (o2 == null) {
                cmp = -1;
            } else if (!o1.isAnnotationPresent(DbcField.class) && !o2.isAnnotationPresent(DbcField.class)) {
                cmp = 0;
            } else if (!o1.isAnnotationPresent(DbcField.class)) {
                cmp = 1;
            } else if (!o2.isAnnotationPresent(DbcField.class)) {
                cmp = -1;
            } else {
                cmp = Integer.valueOf(o1.getAnnotation(DbcField.class).order()).compareTo(o2.getAnnotation(DbcField.class).order());
            }
            return cmp;
        }
    }
}
