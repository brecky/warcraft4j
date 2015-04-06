package nl.salp.warcraft4j.clientdata.dbc.analysis.validation;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil;
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
    private final Collection<Class<? extends DbcEntry>> mappingTypes;

    /**
     * Create a new instance.
     *
     * @param mappingTypes The type mappings to check.
     */
    public EntryTypeCollisionValidation(Collection<Class<? extends DbcEntry>> mappingTypes) {
        this.mappingTypes = mappingTypes;

    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        Map<DbcType, Collection<Class<? extends DbcEntry>>> typeMappings = getTypeMappings();
        for (DbcType entryType : typeMappings.keySet()) {
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
    private Map<DbcType, Collection<Class<? extends DbcEntry>>> getTypeMappings() {
        Map<DbcType, Collection<Class<? extends DbcEntry>>> entries = new HashMap<>();
        for (Class<? extends DbcEntry> type : mappingTypes) {
            DbcType entryType = DbcUtil.getEntryType(type);
            if (!entries.containsKey(entryType)) {
                entries.put(entryType, new HashSet<Class<? extends DbcEntry>>());
            }
            entries.get(entryType).add(type);
        }
        return entries;
    }
}
