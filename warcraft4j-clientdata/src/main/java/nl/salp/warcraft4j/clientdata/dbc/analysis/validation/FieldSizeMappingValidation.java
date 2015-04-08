package nl.salp.warcraft4j.clientdata.dbc.analysis.validation;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.Collection;

import static java.lang.String.format;

/**
 * Validate that the number of bytes of a file entry are mapped.
 *
 * @param <T> The {@link DbcEntry} mapping type implementation.
 *
 * @author1 Barre Dijkstra
 */
public class FieldSizeMappingValidation<T extends DbcEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(FieldSizeMappingValidation.class);
    /** The parsed DBC/DB2 file. */
    private final DbcFile file;
    /** The mapping type. */
    private final Class<T> type;

    /**
     * Create a new instance.
     *
     * @param file The parsed DBC/DB2 file.
     * @param type The mapping type.
     */
    public FieldSizeMappingValidation(DbcFile file, Class<T> type) {
        this.file = file;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        int fileEntrySize = file.getHeader().getEntrySize();
        int mappedEntrySize = getSize(getMappedFields(type, true));
        boolean valid = fileEntrySize == mappedEntrySize;
        if (valid) {
            LOGGER.debug(format("Successfully mapped %s bytes from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileEntrySize, mappedEntrySize));
        } else {
            LOGGER.warn(format("%s has an invalid number bytes mapped from %s: [expected: %d, actual: %d]", type.getName(), file.getFilename(), fileEntrySize, mappedEntrySize));
        }
        return valid;
    }

    /**
     * Get the size of the provided fields in bytes.
     *
     * @param fields The fields.
     *
     * @return The size in bytes for all the fields.
     */
    private int getSize(Collection<Field> fields) {
        int size = 0;
        for (Field f : fields) {
            size += getSize(f);
        }
        return size;
    }

    /**
     * Get the size of the provided field in bytes.
     *
     * @param field The fields
     *
     * @return The size in bytes.
     */
    private int getSize(Field field) {
        int size = 0;
        DbcField f = field.getAnnotation(DbcField.class);
        if (f.length() > 0) {
            size = f.length();
        } else {
            size = f.dataType().getDataType(f).getLength();
        }
        return size;
    }
}
