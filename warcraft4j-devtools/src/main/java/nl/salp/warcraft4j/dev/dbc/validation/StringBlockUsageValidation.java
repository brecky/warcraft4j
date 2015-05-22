package nl.salp.warcraft4j.clientdatabase.analysis.rules;

<<<<<<< Updated upstream:warcraft4j-casc/src/main/java/nl/salp/warcraft4j/clientdatabase/analysis/rules/StringBlockUsageValidation.java
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.parser.ClientDatabaseFile;
import nl.salp.warcraft4j.clientdatabase.parser.ClientDatabaseStringBlock;
import nl.salp.warcraft4j.clientdatabase.parser.DbcDataType;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;
=======
import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcDataType;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcField;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcStringTable;
>>>>>>> Stashed changes:warcraft4j-devtools/src/main/java/nl/salp/warcraft4j/dev/dbc/validation/StringBlockUsageValidation.java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

/**
 * Validate that the values of a StringBlock are used.
 *
 * @param <T> The {@link ClientDatabaseEntry} mapping type implementation.
 *
 * @author Barre Dijkstra
 */
public class StringBlockUsageValidation<T extends ClientDatabaseEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(StringBlockUsageValidation.class);
    /** The percentage of the StringBlock entries that should be used. */
    private final double minUsage;
    /** The parsed DBC/DB2 file. */
    private final ClientDatabaseFile file;
    /** The mapping type. */
    private final Class<T> type;
    /** The parsed instances of the mapping type. */
    private final Collection<T> instances;

    /**
     * @param file      The parsed DBC/DB2 file.
     * @param type      The mapping type.
     * @param instances The parsed instances of the mapping type.
     * @param minUsage  The percentage of the StringBlock entries that should be used.
     */
    public StringBlockUsageValidation(ClientDatabaseFile file, Class<T> type, Collection<T> instances, double minUsage) {
        this.file = file;
        this.type = type;
        this.instances = instances;
        this.minUsage = minUsage;
    }

    @Override
    public boolean isValid() {
        boolean valid;
        if (file.getStringBlock().getAvailablePositions().isEmpty()) {
            Collection<Field> stringBlockReferences = getStringBlockReferenceFields();
            valid = stringBlockReferences.isEmpty();
            if (valid) {
                LOGGER.debug("Successfully mapped {} StringBlock entries from {} [entries: 0, references: {}]", type.getName(), file.getFilename(), stringBlockReferences.size());
            } else {
                LOGGER.warn("{} maps to an invalid number of StringBlock entries from {} [entries: 0, references: {}]", type.getName(), file.getFilename(), stringBlockReferences.size());
            }
        } else {
            Collection<String> stringBlockEntries = getStringBlockEntries();
            Collection<Field> stringBlockReferences = getStringBlockReferenceFields();
            Collection<String> removedEntries = new HashSet<>();
            for (T instance : instances) {
                for (Field field : stringBlockReferences) {
                    String value = getValue(field, instance);
                    if (value != null && !removedEntries.contains(value)) {
                        if (stringBlockEntries.remove(value)) {
                            removedEntries.add(value);
                        } else {
                            LOGGER.warn("Confused: unable to find a StringBlock entry for {}[id={}].{} with value '{}', while it was parsed from the StringBlock...", type.getName(), instance.getId(), field.getName(), value);
                        }
                    }
                }
            }
            double usageCount = stringBlockEntries.size() / file.getStringBlock().getAvailablePositions().size();
            valid = usageCount >= minUsage;
            if (valid) {
<<<<<<< Updated upstream:warcraft4j-casc/src/main/java/nl/salp/warcraft4j/clientdatabase/analysis/rules/StringBlockUsageValidation.java
                LOGGER.debug(format("Successfully mapped %s StringBlock entries from %s [entries: %d, references: %d, mapped: %.2f%%, required: %.2%%]", type.getName(), file.getFilename(), file.getStringBlock().getAvailablePositions().size(), stringBlockReferences.size(), usageCount, minUsage));
            } else {
                LOGGER.warn(format("%s maps to an invalid number of StringBlock entries from %s [entries: %d, references: %d, mapped: %.2f%%, required: %.2%%]", type.getName(), file.getFilename(), file.getStringBlock().getAvailablePositions().size(), stringBlockReferences.size(), usageCount, minUsage));
=======
                LOGGER.debug("Successfully mapped {} StringBlock entries from {} [entries: {}, references: {}, mapped: {}%, required: {}%]", type.getName(), file.getFilename(), file.getStringTable().getNumberOfEntries(), stringBlockReferences.size(), usageCount, minUsage);
            } else {
                LOGGER.warn("{} maps to an invalid number of StringBlock entries from {} [entries: {}, references: {}, mapped: {}%, required: {}%]", type.getName(), file.getFilename(), file.getStringTable().getNumberOfEntries(), stringBlockReferences.size(), usageCount, minUsage);
>>>>>>> Stashed changes:warcraft4j-devtools/src/main/java/nl/salp/warcraft4j/dev/dbc/validation/StringBlockUsageValidation.java
            }
        }
        return valid;
    }

    /**
     * Get all values for the StringBlock.
     *
     * @return The values.
     */
    private Collection<String> getStringBlockEntries() {
        ClientDatabaseStringBlock stringBlock = file.getStringBlock();
        Collection<String> entries = new HashSet<>(stringBlock.getAvailablePositions().size());
        for (int position : stringBlock.getAvailablePositions()) {
            if (!entries.add(stringBlock.getEntry(position))) {
                LOGGER.warn("Duplicate StringBlock entry found for for file {} [pos: {}, string: {}]", file.getFilename(), position, stringBlock.getEntry(position));
            }
        }
        return entries;
    }


    /**
     * Get the fields that are referencing the StringBlock.
     *
     * @return The fields.
     */
    private Collection<Field> getStringBlockReferenceFields() {
        Collection<Field> fields = new HashSet<>();
        for (Field field : getMappedFields(type, false)) {
            DbcField dbcField = field.getAnnotation(DbcField.class);
            if (dbcField.dataType() == DbcDataType.STRINGTABLE_REFERENCE) {
                fields.add(field);
            }
        }
        return fields;
    }

    /**
     * Get the value of the field on the instance.
     *
     * @param field    The field.
     * @param instance The instance.
     *
     * @return The value (may be {@code null}).
     */
    private String getValue(Field field, T instance) {
        String value = null;

        try {
            boolean access = field.isAccessible();
            field.setAccessible(true);
            value = (String) field.get(instance);
            field.setAccessible(access);
        } catch (IllegalAccessException e) {
            // Ignore.
        }

        return value;
    }
}
