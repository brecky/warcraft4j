package nl.salp.warcraft4j.clientdatabase.analysis.rules;

<<<<<<< Updated upstream:warcraft4j-casc/src/main/java/nl/salp/warcraft4j/clientdatabase/analysis/rules/FieldCountMappingValidation.java
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.parser.ClientDatabaseFile;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
=======
import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
>>>>>>> Stashed changes:warcraft4j-devtools/src/main/java/nl/salp/warcraft4j/dev/dbc/validation/FieldCountMappingValidation.java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;

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
            LOGGER.debug("Successfully mapped {} fields from {}: [expected: {}, actual: {}]", type.getName(), file.getFilename(), fileFields, mappedFields);
        } else {
            LOGGER.warn("{} has an invalid number of fields mapped from {}: [expected: {}, actual: {}]", type.getName(), file.getFilename(), fileFields, mappedFields);
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
