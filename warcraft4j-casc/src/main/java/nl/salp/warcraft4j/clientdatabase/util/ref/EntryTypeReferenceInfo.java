package nl.salp.warcraft4j.clientdatabase.util.ref;

import nl.salp.warcraft4j.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.parser.DbcField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Field references and their corresponding reference scores for a type.
 *
 * @author Barre Dijkstra
 */
public class EntryTypeReferenceInfo {
    /** The type to score the field references of. */
    private final Class<? extends ClientDatabaseEntry> sourceType;
    /** The field references, indexed by the fields. */
    private final Map<FieldReferenceInfo, Collection<FieldReference>> fields;


    /**
     * Create a new instance.
     *
     * @param sourceType The type to score the field references of.
     */
    public EntryTypeReferenceInfo(Class<? extends ClientDatabaseEntry> sourceType) {
        this.sourceType = sourceType;
        this.fields = new TreeMap<>();
    }

    /**
     * Score the field references.
     *
     * @param clientDatabase The client database holding all entries.
     */
    public void scoreFieldReferences(ClientDatabase clientDatabase) {
        for (Field field : getReferenceFields()) {
            FieldReferenceInfo f = new FieldReferenceInfo(sourceType, field);
            Collection<FieldReference> references = f.scoreReferences(clientDatabase);
            fields.put(f, references);
        }
    }

    /**
     * Get all fields that have scored references.
     *
     * @return The fields.
     */
    public Collection<FieldReferenceInfo> getFields() {
        return this.fields.keySet();
    }

    /**
     * Get the scored references for a field.
     *
     * @param field The field.
     *
     * @return The references.
     */
    public Collection<FieldReference> getReferences(FieldReferenceInfo field) {
        Collection<FieldReference> references;
        if (field != null && this.fields.containsKey(field)) {
            references = this.fields.get(field);
        } else {
            references = Collections.emptySet();
        }
        return references;
    }

    /**
     * Get all fields of the type that are potentially reference fields.
     *
     * @return The fields.
     */
    private Set<Field> getReferenceFields() {
        Set<Field> fields = new HashSet<>();
        for (Field field : this.sourceType.getDeclaredFields()) {
            DbcField f = field.getAnnotation(DbcField.class);
            if ((f != null)
                    && (!"id".equalsIgnoreCase(field.getName()))
                    && (int.class == field.getType() || Integer.class == field.getType())) {
                fields.add(field);
            }
        }
        return fields;
    }
}
