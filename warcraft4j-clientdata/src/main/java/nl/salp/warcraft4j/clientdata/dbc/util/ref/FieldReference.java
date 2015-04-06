package nl.salp.warcraft4j.clientdata.dbc.util.ref;

import nl.salp.warcraft4j.clientdata.dbc.DbcStore;
import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * Scored reference between a field and an entry type.
 *
 * @author Barre Dijkstra
 */
public class FieldReference implements Comparable<FieldReference> {
    /** The client database holding the instances. */
    private final WeakReference<DbcStore> clientDatabase;
    /** The type of the referenced entry. */
    private final Class<? extends DbcEntry> targetType;
    /** The total number of instances the referenced entry has. */
    private final int targetEntries;
    /** The values that have been referenced. */
    private Set<Integer> values;
    /** The total number of times there was a reference hit. */
    private int targetHitsTotal;
    /** The total number of times there was an unique reference hit. */
    private int targetHitsUnique;
    /** The total number of times there was a reference miss. */
    private int targetMisses;

    /**
     * Create a new field reference.
     *
     * @param targetType     The referenced type.
     * @param dbcStore The client database holding the referenced instances.
     */
    public FieldReference(Class<? extends DbcEntry> targetType, DbcStore dbcStore) {
        this.clientDatabase = new WeakReference<>(dbcStore);
        this.targetType = targetType;
        this.targetEntries = dbcStore.getInstances(targetType).size();
        this.values = new HashSet<>();
    }

    /**
     * Score the reference id.
     *
     * @param referenceValue The referenced id.
     */
    public void score(int referenceValue) {
        DbcEntry entry = resolve(referenceValue, targetType);
        if (entry == null) {
            targetMisses++;
        } else {
            targetHitsTotal++;
            if (values.add(referenceValue)) {
                targetHitsUnique++;
            }
        }
    }

    /**
     * Get the number of entries the referenced type has.
     *
     * @return The number of instances.
     */
    public int getTargetEntries() {
        return targetEntries;
    }

    /**
     * Get the total number of references.
     *
     * @return The number of references.
     */
    public int getTargetHitsTotal() {
        return targetHitsTotal;
    }

    /**
     * Get the number of unique references.
     *
     * @return The number of unique references.
     */
    public int getTargetHitsUnique() {
        return targetHitsUnique;
    }

    /**
     * Get the number of reference misses (e.g. there is no instance of the type with the id being equal to the referenced id).
     *
     * @return The number of missed references.
     */
    public int getTargetMisses() {
        return targetMisses;
    }

    public double getMissPercentage() {
        return (double) targetMisses / (targetHitsTotal + targetMisses);
    }

    public double getHitPercentage() {
        return (double) targetHitsTotal / (targetHitsTotal + targetMisses);
    }

    public double getReferencedEntryPercentage() {
        return (double) targetHitsUnique / targetEntries;
    }

    /**
     * Resolve an entry type instance by its ID.
     *
     * @param value      The id.
     * @param targetType The entry type.
     * @param <T>        The type.
     *
     * @return The instance or {@code null} when no instances was found with the given id.
     */
    private <T extends DbcEntry> T resolve(int value, Class<T> targetType) {
        return clientDatabase.get().resolve(targetType, value);
    }

    @Override
    public String toString() {
        return format("FieldReference [type:%s, entries:%d, hits:%d, uniqueHits:%d, misses:%d]", targetType.getSimpleName(), targetEntries, targetHitsTotal, targetHitsUnique, targetMisses);
    }

    @Override
    public int compareTo(FieldReference o) {
        int cmp;
        if (o == null) {
            cmp = -1;
        } else if (targetMisses < o.targetMisses) {
            cmp = -1;
        } else if (targetMisses > o.targetMisses) {
            cmp = 1;
        } else if (targetHitsUnique > o.targetHitsUnique) {
            cmp = -1;
        } else if (targetHitsUnique < o.targetHitsUnique) {
            cmp = 1;
        } else if (targetHitsTotal > o.targetHitsTotal) {
            cmp = -1;
        } else if (targetHitsTotal < o.targetHitsTotal) {
            cmp = 1;
        } else {
            cmp = 0;
        }
        return cmp;
    }

    public Class<? extends DbcEntry> getTargetType() {
        return targetType;
    }
}
