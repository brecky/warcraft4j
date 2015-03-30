package nl.salp.warcraft4j.files.clientdatabase.util.ref;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;

import java.lang.reflect.Field;
import java.util.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FieldReferenceScore {
    private final Class<? extends ClientDatabaseEntry> sourceType;
    private final Field sourceField;
    private int sourceEntries;
    private int sourceEntriesWithValue;
    private final Map<Class<? extends ClientDatabaseEntry>, Integer> hits;
    private final Map<Class<? extends ClientDatabaseEntry>, Integer> targetEntries;
    private final Map<Class<? extends ClientDatabaseEntry>, Integer> uniqueHits;
    private int sourceUniqueValues;

    public FieldReferenceScore(Class<? extends ClientDatabaseEntry> sourceType, Field sourceField) {
        this.sourceType = sourceType;
        this.sourceField = sourceField;
        this.hits = new HashMap<>();
        this.targetEntries = new HashMap<>();
        this.uniqueHits = new HashMap<>();
    }

    public void checkReferenceHit(ClientDatabase clientDatabase) {
        sourceEntries = 0;
        hits.clear();
        targetEntries.clear();
        uniqueHits.clear();
        sourceUniqueValues = 0;
        if (int.class == sourceField.getType() || Integer.class == sourceField.getType()) {
            Set<Integer> uniqueValues = new HashSet<>();
            Set<Integer> uniqueTargetValues = new HashSet<>();
            for (ClientDatabaseEntry e : clientDatabase.getInstances(sourceType)) {
                sourceEntries++;
                int value = getValue(e);
                if (value > 0) {
                    if (uniqueValues.add(value)) {
                        sourceUniqueValues++;
                    }
                    sourceEntriesWithValue++;
                    for (Class<? extends ClientDatabaseEntry> targetType : clientDatabase.getRegisteredTypes()) {
                        if (!this.targetEntries.containsKey(targetType)) {
                            this.targetEntries.put(targetType, clientDatabase.getInstances(targetType).size());
                        }
                        ClientDatabaseEntry target = clientDatabase.resolve(targetType, value);
                        if (target != null) {
                            uniqueTargetValues.add(value);
                            registerHit(targetType);
                        }
                    }
                }
            }
        }
    }

    private int getValue(ClientDatabaseEntry e) {
        int value = -1;
        try {
            boolean accessible = sourceField.isAccessible();
            sourceField.setAccessible(true);
            value = sourceField.getInt(e);
            sourceField.setAccessible(accessible);
        } catch (IllegalAccessException e1) {
            // Ignore;
        }
        return value;
    }

    private void registerHit(Class<? extends ClientDatabaseEntry> target) {
        if (this.hits.containsKey(target)) {
            int hits = this.hits.get(target) + 1;
            this.hits.put(target, hits);
        } else {
            this.hits.put(target, 1);
        }
    }

    public boolean isPotentialReference() {
        return !this.hits.isEmpty();
    }

    public int getHits(Class<? extends ClientDatabaseEntry> target) {
        int hits = 0;
        if (this.hits.containsKey(target)) {
            hits = this.hits.get(target);
        }
        return hits;
    }

    public Collection<Class<? extends ClientDatabaseEntry>> getTargets() {
        return this.hits.keySet();
    }

    public int getSourceEntries() {
        return sourceEntries;
    }

    public int getSourceEntriesWithValue() {
        return sourceEntriesWithValue;
    }

    public Class<? extends ClientDatabaseEntry> getSourceType() {
        return sourceType;
    }

    public Field getSourceField() {
        return sourceField;
    }

    public int getTargetEntries(Class<? extends ClientDatabaseEntry> target) {
        int entries = 0;
        if (target != null && targetEntries.containsKey(target)) {
            entries = targetEntries.get(target);
        }
        return entries;
    }

    public int getSourceUniqueValues() {
        return sourceUniqueValues;
    }
}
