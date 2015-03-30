package nl.salp.warcraft4j.files.clientdatabase.util.ref;

import nl.salp.warcraft4j.files.clientdatabase.ClientDatabase;
import nl.salp.warcraft4j.files.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.files.clientdatabase.parser.DbcField;

import java.lang.reflect.Field;
import java.util.*;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class FieldReferences {
    private final Class<? extends ClientDatabaseEntry> sourceType;
    private final Map<FieldInfo, Collection<FieldReference>> fields;


    public FieldReferences(Class<? extends ClientDatabaseEntry> sourceType) {
        this.sourceType = sourceType;
        this.fields = new TreeMap<>();
    }

    public void scoreFieldReferences(ClientDatabase clientDatabase) {
        for (Field field : getReferenceFields()) {
            FieldInfo f = new FieldInfo(sourceType, field);
            Collection<FieldReference> references = f.scoreReferences(clientDatabase);
            fields.put(f, references);
        }
    }

    public Collection<FieldInfo> getFields() {
        return this.fields.keySet();
    }

    public Collection<FieldReference> getReferences(FieldInfo field) {
        Collection<FieldReference> references;
        if (field != null && this.fields.containsKey(field)) {
            references = this.fields.get(field);
        } else {
            references = Collections.emptySet();
        }
        return references;
    }

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
