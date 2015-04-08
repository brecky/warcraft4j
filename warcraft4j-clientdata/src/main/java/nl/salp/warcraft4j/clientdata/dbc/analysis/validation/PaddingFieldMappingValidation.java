package nl.salp.warcraft4j.clientdata.dbc.analysis.validation;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.parser.DbcFile;
import nl.salp.warcraft4j.clientdata.dbc.DbcField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import static java.lang.String.format;

/**
 * Validate that all the padding fields in the mapping are transient and don't have an accessor.
 *
 * @param <T> The {@link DbcEntry} mapping type implementation.
 *
 * @author Barre Dijkstra
 */
public class PaddingFieldMappingValidation<T extends DbcEntry> extends MappingValidation<T> {
    /** The logger instance for the class. */
    private static final Logger LOGGER = LoggerFactory.getLogger(PaddingFieldMappingValidation.class);
    /** Method name mask for boolean accessors (is...()). */
    private static final String ACCESSOR_IS_MASK = "is%s";
    /** Method name mask for non-boolean accessors (get...()). */
    private static final String ACCESSOR_GET_MASK = "get%s";
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
    public PaddingFieldMappingValidation(DbcFile file, Class<T> type) {
        this.file = file;
        this.type = type;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        for (Field field : getMappedFields(type, true)) {
            valid = isValid(field) && valid;
        }
        return valid;
    }

    /**
     * Check if the field is either not a padding field, or is transient and has no accessor method.
     *
     * @param field The field to check.
     *
     * @return {@code true} if the field is valid.
     */
    private boolean isValid(Field field) {
        boolean valid = true;

        if (isPaddingField(field)) {
            if (!Modifier.isTransient(field.getModifiers())) {
                valid = false;
                LOGGER.warn(format("%s.%s is a non transient padding field.", type.getName(), field.getName()));
            }
            if (isAccessorMethodAvailable(field)) {
                valid = false;
            }
        }

        return valid;
    }

    /**
     * Check if a probable accessor method is available for the given field
     *
     * @param field The field.
     *
     * @return {@code true} if a probable accessor method is available.
     */
    private boolean isAccessorMethodAvailable(Field field) {
        return isMethodAvailable(field, ACCESSOR_IS_MASK) || isMethodAvailable(field, ACCESSOR_GET_MASK);
    }

    /**
     * Check if a method is available on the type for the field.
     *
     * @param field    The field.
     * @param nameMask The mask of the method.
     *
     * @return {@code true} if the method is available.
     */
    private boolean isMethodAvailable(Field field, String nameMask) {
        boolean methodAvailable = false;

        String fieldName = String.valueOf(Character.toUpperCase(field.getName().charAt(0)));
        if (field.getName().length() > 1) {
            fieldName = fieldName + field.getName().substring(1);
        }
        try {
            String methodName = format(nameMask, fieldName);
            type.getMethod(methodName);
            methodAvailable = true;
            LOGGER.warn(format("%s.%s() is most probably an accessor method for padding field %s.", type.getName(), methodName, field.getName()));
        } catch (NoSuchMethodException e) {
            // Ignore.
        }
        return methodAvailable;
    }

    /**
     * Check if the field is a padding field.
     *
     * @param field The field.
     *
     * @return {@code true} if the field is a padding field.
     */
    private boolean isPaddingField(Field field) {
        DbcField f = field.getAnnotation(DbcField.class);
        return f != null && f.padding();
    }
}
