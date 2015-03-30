package nl.salp.warcraft4j.files.clientdatabase.util.ref;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

import static java.lang.String.format;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FieldReference implements Comparable<FieldReference> {
    private final WeakReference<ClientDatabase> clientDatabase;
    private final Class<? extends ClientDatabaseEntry> targetType;
    private final int targetEntries;
    private Set<Integer> values;
    private int targetHitsTotal;
    private int targetHitsUnique;
    private int targetMisses;

    public FieldReference(Class<? extends ClientDatabaseEntry> targetType, ClientDatabase clientDatabase) {
        this.clientDatabase = new WeakReference<>(clientDatabase);
        this.targetType = targetType;
        this.targetEntries = clientDatabase.getInstances(targetType).size();
        this.values = new HashSet<>();
    }

    public void score(int referenceValue) {
        ClientDatabaseEntry entry = resolve(referenceValue, targetType);
        if (entry == null) {
            targetMisses++;
        } else {
            targetHitsTotal++;
            if (values.add(referenceValue)) {
                targetHitsUnique++;
            }
        }
    }

    public int getTargetEntries() {
        return targetEntries;
    }

    public int getTargetHitsTotal() {
        return targetHitsTotal;
    }

    public int getTargetHitsUnique() {
        return targetHitsUnique;
    }

    public int getTargetMisses() {
        return targetMisses;
    }

    private <T extends ClientDatabaseEntry> T resolve(int value, Class<T> targetType) {
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
}
