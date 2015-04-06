package nl.salp.warcraft4j.clientdatabase.analysis;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntryType;
import nl.salp.warcraft4j.clientdatabase.util.ClientDatabaseUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.clientdatabase.util.ClientDatabaseUtil.getMappedFile;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ClasspathEntryMappingResults {
    private Map<ClientDatabaseEntryType, Class<? extends ClientDatabaseEntry>> typeMappings;
    private Map<String, Class<? extends ClientDatabaseEntry>> fileMappings;
    private Collection<Class<? extends ClientDatabaseEntry>> mappings;

    public ClasspathEntryMappingResults(Collection<Class<? extends ClientDatabaseEntry>> mappings) {
        if (mappings == null) {
            this.mappings = Collections.emptySet();
        }
        this.typeMappings = new HashMap<>();
        this.fileMappings = new HashMap<>();
        for (Class<? extends ClientDatabaseEntry> mapping : this.mappings) {
            String file = getMappedFile(mapping);
            if (isEmpty(file)) {
                throw new IllegalArgumentException(format("Unable to parse mapping %s with DbcFile annotation present", mapping.getName()));
            }
            ClientDatabaseEntryType entryType = ClientDatabaseUtil.getEntryType(mapping);
            if (entryType == null) {
                throw new IllegalArgumentException(format("Unable to parse mapping %s with no static client database entry field present", mapping.getName()));
            }

            fileMappings.put(file, mapping);
            typeMappings.put(entryType, mapping);
        }
    }

    public Collection<Class<? extends ClientDatabaseEntry>> getMappingTypes() {
        return Collections.unmodifiableCollection(this.mappings);
    }

    public Collection<String> getMappedFiles() {
        return Collections.unmodifiableCollection(this.fileMappings.keySet());
    }

    public Collection<ClientDatabaseEntryType> getMappedEntryTypes() {
        return Collections.unmodifiableCollection(this.typeMappings.keySet());
    }

    public Class<? extends ClientDatabaseEntry> getMappingType(ClientDatabaseEntryType entryType) {
        Class<? extends ClientDatabaseEntry> mappingType = null;
        if (entryType != null) {
            mappingType = typeMappings.get(entryType);
        }
        return mappingType;
    }

    public ClientDatabaseEntryType getEntryType(Class<? extends ClientDatabaseEntry> mappingType) {
        return ClientDatabaseUtil.getEntryType(mappingType);
    }

    public String getFile(Class<? extends ClientDatabaseEntry> mapping) {
        return getMappedFile(mapping);
    }

    public Class<? extends ClientDatabaseEntry> getMappingType(String file) {
        Class<? extends ClientDatabaseEntry> mappingType = null;
        if (isNotEmpty(file)) {
            mappingType = fileMappings.get(file);
        }
        return mappingType;
    }
}
