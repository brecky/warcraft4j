package nl.salp.warcraft4j.wowclient.dbc;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * DBC data file StringBlock segment.
 *
 * @author Barre Dijkstra
 */
public class StringBlock {
    private final Map<Integer, String> strings;

    /**
     * Create a new {@link StringBlock}.
     *
     * @param strings The string values in the table, indexed by their position.
     */
    public StringBlock(Map<Integer, String> strings) {
        if (strings == null) {
            this.strings = new HashMap<>();
        } else {
            this.strings = strings;
        }
    }

    /**
     * Check if an entry is available for the given position.
     *
     * @param position The position.
     *
     * @return {@code true} if an entry is available.
     */
    public boolean isEntryAvailableForPosition(int position) {
        return strings.containsKey(position);
    }

    /**
     * Get the entry for the given position.
     *
     * @param position The position.
     *
     * @return The entry or {@code null} if no entry is available for the given position.
     */
    public String getEntry(int position) {
        return strings.get(position);
    }

    /**
     * Get the available positions registered.
     *
     * @return The available positions as unmodifiable set.
     */
    public Set<Integer> getAvailablePositions() {
        return Collections.unmodifiableSet(strings.keySet());
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
