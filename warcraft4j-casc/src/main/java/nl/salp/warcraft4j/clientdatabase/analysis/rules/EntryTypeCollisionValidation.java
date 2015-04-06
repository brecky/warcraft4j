package nl.salp.warcraft4j.clientdatabase.analysis.rules;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.util.ClientDatabaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import static java.lang.String.format;

/**
 * Validation that validates that only one mapping type uses the same entry type.
 *
 * @author Barre Dijkstra
 */
public class EntryTypeCollisionValidation extends MappingValidation {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryCountMappingValidation.class);
    /** The mapping types. */
    private final Collection<Class<? extends ClientDatabaseEntry>> mappingTypes;

    /**
     * Create a new instance.
     *
     * @param mappingTypes The type mappings to check.
     */
    public EntryTypeCollisionValidation(Collection<Class<? extends ClientDatabaseEntry>> mappingTypes) {
        this.mappingTypes = mappingTypes;

    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        Map<ClientDatabaseEntryType, Collection<Class<? extends ClientDatabaseEntry>>> typeMappings = getTypeMappings();
        for (ClientDatabaseEntryType entryType : typeMappings.keySet()) {
            if (typeMappings.get(entryType).size() > 1) {
                LOGGER.warn(format("Entry type %s has multiple mapping types mapped to it: %s", entryType, typeMappings.get(entryType)));
                valid = false;
            }
        }
        return valid;
    }

    /**
     * Get the entry types mapped to their mapping types.
     *
     * @return The mapped entry types.
     */
    private Map<ClientDatabaseEntryType, Collection<Class<? extends ClientDatabaseEntry>>> getTypeMappings() {
        Map<ClientDatabaseEntryType, Collection<Class<? extends ClientDatabaseEntry>>> entries = new HashMap<>();
        for (Class<? extends ClientDatabaseEntry> type : mappingTypes) {
            ClientDatabaseEntryType entryType = ClientDatabaseUtil.getEntryType(type);
            if (!entries.containsKey(entryType)) {
                entries.put(entryType, new HashSet<Class<? extends ClientDatabaseEntry>>());
            }
            entries.get(entryType).add(type);
        }
        return entries;
    }
}
