package nl.salp.warcraft4j.clientdatabase.analysis.rules;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.parser.ClientDatabaseFile;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;

import static java.lang.String.format;

/**
 * Validate that all the fields in the data file are mapped.
 *
 * @param <T> The {@link ClientDatabaseEntry} mapping type implementation.
 *
 * @author Barre Dijkstra
 */
public class FieldCountMappingValidation<T extends ClientDatabaseEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldCountMappingValidation.class);
    /** The parsed DBC/DB2 file. */
    private final ClientDatabaseFile file;
    /** The mapping type. */
    private final Class<T> type;

    /**
     * Create a new instance.
     *
     * @param file The parsed DBC/DB2 file.
     * @param type The mapping type.
     */
    public FieldCountMappingValidation(ClientDatabaseFile file, Class<T> type) {
        this.file = file;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        int fileFields = file.getHeader().getFieldCount();
        int mappedFields = getMappedFieldCount(getMappedFields(type, false));
        boolean valid = fileFields == mappedFields;
        if (valid) {
            LOGGER.debug(format("Successfully mapped %s fields from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileFields, mappedFields));
        } else {
            LOGGER.warn(format("%s has an invalid number of fields mapped from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileFields, mappedFields));
        }
        return valid;
    }


    private int getMappedFieldCount(Collection<Field> fields) {
        int mappedFields = 0;
        for (Field field : fields) {
            DbcField f = field.getAnnotation(DbcField.class);
            if (f != null) {
                mappedFields += f.numberOfEntries();
            }
        }
        return mappedFields;
    }
}
