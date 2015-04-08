package nl.salp.warcraft4j.clientdata.dbc.util.ref;

import nl.salp.warcraft4j.clientdata.dbc.DbcStore;
import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Field reference information.
 *
 * @author Barre Dijkstra
 */
public class FieldReferenceInfo implements Comparable<FieldReferenceInfo> {
    /** The type the field is on. */
    private final Class<? extends DbcEntry> entryType;
    /** The field. */
    private final Field field;
    /** The DbcField annotation (can be {@code null}). */
    private final DbcField dbcField;
    /** The total number of entries of the field. */
    private int entriesTotal;
    /** The total number of unique values of the field. */
    private int entriesUnique;
    /** The total number of entries with a value of the field. */
    private int entriesValue;

    /**
     * Create a new instance.
     *
     * @param entryType The entry type the field is on.
     * @param field     The field.
     */
    public FieldReferenceInfo(Class<? extends DbcEntry> entryType, Field field) {
        this.entryType = entryType;
        this.field = field;
        this.dbcField = field.getAnnotation(DbcField.class);
    }

    /**
     * Check if the field is a possible reference.
     *
     * @return {@code true} if the field is a possible reference.
     */
    public boolean isPossibleReference() {
        return (dbcField != null)
                && (!"id".equalsIgnoreCase(field.getName()))
                && (int.class == field.getType() || Integer.class == field.getType());
    }

    /**
     * Score the possible references from the field values to other entry types.
     *
     * @param dbcStore The client database holding all entries.
     *
     * @return The scored possible references.
     */
    public Collection<FieldReference> scoreReferences(DbcStore dbcStore) {
        this.entriesTotal = 0;
        this.entriesUnique = 0;
        this.entriesValue = 0;
        if (!isPossibleReference()) {
            return Collections.emptySet();
        }

        Map<Class<? extends DbcEntry>, FieldReference> references = new HashMap<>();

        Set<Integer> values = new HashSet<>();
        for (DbcEntry entry : dbcStore.getInstances(entryType)) {
            int value = getValue(entry);
            registerValue(value, values);
            if (value > 0) {
                for (Class<? extends DbcEntry> targetType : dbcStore.getRegisteredTypes()) {
                    FieldReference reference;
                    if (references.containsKey(targetType)) {
                        reference = references.get(targetType);
                    } else {
                        reference = new FieldReference(targetType, dbcStore);
                    }
                    reference.score(value);

                    if (!references.containsKey(targetType) && reference.getTargetHitsTotal() > 0) {
                        references.put(targetType, reference);
                    }

                }
            }
        }
        return new TreeSet<>(references.values());
    }

    /**
     * Register a value.
     *
     * @param value          The value to register.
     * @param previousValues The list of previously registered values.
     */
    private void registerValue(int value, Set<Integer> previousValues) {
        this.entriesTotal++;
        if (value > 0) {
            this.entriesValue++;
            if (previousValues.add(value)) {
                this.entriesUnique++;
            }
        }
    }

    /**
     * Get the value for the field from the given entry.
     *
     * @param e The entry.
     *
     * @return The value or {@code -1} when the value could not be retrieved.
     */
    private int getValue(DbcEntry e) {
        int value = -1;
        if (e != null) {
            try {
                boolean accessible = field.isAccessible();
                field.setAccessible(true);
                value = field.getInt(e);
                field.setAccessible(accessible);
            } catch (IllegalAccessException e1) {
                // Ignore;
            }
        }
        return value;
    }

    /**
     * Get the DbcField for the field.
     *
     * @return The DbcField or {@code null} when there is no DbcField annotation present.
     */
    public DbcField getDbcField() {
        return dbcField;
    }

    /**
     * Get the Java field implementation of the field.
     *
     * @return The field.
     */
    public Field getField() {
        return field;
    }

    /**
     * Get the total number of entries (with or without value).
     *
     * @return The total number of entries.
     */
    public int getEntryCount() {
        return entriesTotal;
    }

    /**
     * Get the number of unique values.
     *
     * @return The number of unique values.
     */
    public int getUniqueValueCount() {
        return entriesUnique;
    }

    /**
     * Get the number of entries with a value.
     *
     * @return The number of entries.
     */
    public int getEntriesWithValue() {
        return entriesValue;
    }

    /**
     * Get the percentage of the entries that have a value.
     *
     * @return The percentage.
     */
    public double getEntriesWithValuePercentage() {
        return (double) entriesValue / entriesTotal;
    }

    /**
     * Get the percentage of the entries with a value that are unique values.
     *
     * @return The percentage.
     */
    public double getUniqueEntryPercentage() {
        return (double) entriesUnique / entriesValue;
    }

    @Override
    public String toString() {
        String str;
        if (isPossibleReference()) {
            str = String.format("FieldInfo [type:%s, field:%s, order:%d, entries:%d, entriesWithValue:%d, uniqueEntryValues:%d]",
                    entryType.getSimpleName(),
                    field.getName(),
                    dbcField.order(),
                    entriesTotal,
                    entriesValue,
                    entriesUnique
            );
        } else {
            str = String.format("FieldInfo [type:%s, field:%s, possibleReference:false]",
                    entryType.getSimpleName(),
                    field.getName()
            );
        }
        return str;
    }

    @Override
    public int compareTo(FieldReferenceInfo o) {
        int value1 = (dbcField != null) ? dbcField.order() : 0;
        int value2 = (o != null && o.dbcField != null) ? o.dbcField.order() : 0;

        return Integer.valueOf(value1).compareTo(value2);
    }
}
