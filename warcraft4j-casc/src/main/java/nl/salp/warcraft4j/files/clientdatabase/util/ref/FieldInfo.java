package nl.salp.warcraft4j.files.clientdatabase.util.ref;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FieldInfo implements Comparable<FieldInfo> {
    private final Class<? extends ClientDatabaseEntry> entryType;
    private final Field field;
    private final DbcField dbcField;
    private int entriesTotal;
    private int entriesUnique;
    private int entriesValue;

    public FieldInfo(Class<? extends ClientDatabaseEntry> entryType, Field field) {
        this.entryType = entryType;
        this.field = field;
        this.dbcField = field.getAnnotation(DbcField.class);
    }

    public boolean isPossibleReference() {
        return (dbcField != null)
                && (!"id".equalsIgnoreCase(field.getName()))
                && (int.class == field.getType() || Integer.class == field.getType());
    }

    public Collection<FieldReference> scoreReferences(ClientDatabase clientDatabase) {
        this.entriesTotal = 0;
        this.entriesUnique = 0;
        this.entriesValue = 0;
        if (!isPossibleReference()) {
            return Collections.emptySet();
        }

        Map<Class<? extends ClientDatabaseEntry>, FieldReference> references = new HashMap<>();

        Set<Integer> values = new HashSet<>();
        for (ClientDatabaseEntry entry : clientDatabase.getInstances(entryType)) {
            int value = getValue(entry);
            registerValue(value, values);
            if (value > 0) {
                for (Class<? extends ClientDatabaseEntry> targetType : clientDatabase.getRegisteredTypes()) {
                    FieldReference reference;
                    if (references.containsKey(targetType)) {
                        reference = references.get(targetType);
                    } else {
                        reference = new FieldReference(targetType, clientDatabase);
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

    private void registerValue(int value, Set<Integer> previousValues) {
        this.entriesTotal++;
        if (value > 0) {
            this.entriesValue++;
            if (previousValues.add(value)) {
                this.entriesUnique++;
            }
        }
    }

    private int getValue(ClientDatabaseEntry e) {
        int value = -1;
        try {
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            value = field.getInt(e);
            field.setAccessible(accessible);
        } catch (IllegalAccessException e1) {
            // Ignore;
        }
        return value;
    }

    public DbcField getDbcField() {
        return dbcField;
    }

    public Field getField() {
        return field;
    }

    public int getEntries() {
        return entriesTotal;
    }

    public int getUniqueEntries() {
        return entriesUnique;
    }

    public int getEntriesWithValue() {
        return entriesValue;
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
    public int compareTo(FieldInfo o) {
        int value1 = (dbcField != null) ? dbcField.order() : 0;
        int value2 = (o != null && o.dbcField != null) ? o.dbcField.order() : 0;

        return Integer.valueOf(value1).compareTo(value2);
    }
}
