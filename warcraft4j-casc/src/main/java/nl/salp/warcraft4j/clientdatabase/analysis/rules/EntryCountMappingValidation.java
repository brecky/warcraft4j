package nl.salp.warcraft4j.clientdatabase.analysis.rules;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.parser.ClientDatabaseFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

import static java.lang.String.format;

/**
 * Validate that the all entries are mapped and parsed.
 *
 * @param <T> The {@link ClientDatabaseEntry} mapping type implementation.
 *
 * @author Barre Dijkstra
 */
public class EntryCountMappingValidation<T extends ClientDatabaseEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(EntryCountMappingValidation.class);
    /** The parsed DBC/DB2 file. */
    private final ClientDatabaseFile file;
    /** The mapping type. */
    private final Class<T> type;
    /** The parsed instances of the mapping type. */
    private final Collection<T> instances;

    /**
     * Create a new validation instance.
     *
     * @param file      The parsed DBC/DB2 file.
     * @param type      The mapping type.
     * @param instances The parsed instances of the mapping type.
     */
    public EntryCountMappingValidation(ClientDatabaseFile file, Class<T> type, Collection<T> instances) {
        this.file = file;
        this.type = type;
        this.instances = instances;
    }

    @Override
    public boolean isValid() {
        int fileEntries = file.getHeader().getRecordCount();
        int parsedEntries = instances.size();
        boolean valid = fileEntries == parsedEntries;
        if (valid) {
            LOGGER.debug(format("Successfully parsed %s instances from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileEntries, parsedEntries));
        } else {
            LOGGER.warn(format("%s has an invalid number of parsed instances from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileEntries, parsedEntries));
        }
        return valid;
    }
}
