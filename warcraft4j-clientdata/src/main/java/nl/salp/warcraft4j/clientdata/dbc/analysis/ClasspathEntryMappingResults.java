package nl.salp.warcraft4j.clientdata.dbc.analysis;

import nl.salp.warcraft4j.clientdata.dbc.DbcEntry;
import nl.salp.warcraft4j.clientdata.dbc.DbcType;
import nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.clientdata.dbc.util.DbcUtil.getMappedFile;
import static org.apache.commons.lang3.StringUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ClasspathEntryMappingResults {
    private Map<DbcType, Class<? extends DbcEntry>> typeMappings;
    private Map<String, Class<? extends DbcEntry>> fileMappings;
    private Collection<Class<? extends DbcEntry>> mappings;

    public ClasspathEntryMappingResults(Collection<Class<? extends DbcEntry>> mappings) {
        if (mappings == null) {
            this.mappings = Collections.emptySet();
        }
        this.typeMappings = new HashMap<>();
        this.fileMappings = new HashMap<>();
        for (Class<? extends DbcEntry> mapping : this.mappings) {
            String file = getMappedFile(mapping);
            if (isEmpty(file)) {
                throw new IllegalArgumentException(format("Unable to parse mapping %s with DbcFile annotation present", mapping.getName()));
            }
            DbcType entryType = DbcUtil.getEntryType(mapping);
            if (entryType == null) {
                throw new IllegalArgumentException(format("Unable to parse mapping %s with no static client database entry field present", mapping.getName()));
            }

            fileMappings.put(file, mapping);
            typeMappings.put(entryType, mapping);
        }
    }

    public Collection<Class<? extends DbcEntry>> getMappingTypes() {
        return Collections.unmodifiableCollection(this.mappings);
    }

    public Collection<String> getMappedFiles() {
        return Collections.unmodifiableCollection(this.fileMappings.keySet());
    }

    public Collection<DbcType> getMappedEntryTypes() {
        return Collections.unmodifiableCollection(this.typeMappings.keySet());
    }

    public Class<? extends DbcEntry> getMappingType(DbcType entryType) {
        Class<? extends DbcEntry> mappingType = null;
        if (entryType != null) {
            mappingType = typeMappings.get(entryType);
        }
        return mappingType;
    }

    public DbcType getEntryType(Class<? extends DbcEntry> mappingType) {
        return DbcUtil.getEntryType(mappingType);
    }

    public String getFile(Class<? extends DbcEntry> mapping) {
        return getMappedFile(mapping);
    }

    public Class<? extends DbcEntry> getMappingType(String file) {
        Class<? extends DbcEntry> mappingType = null;
        if (isNotEmpty(file)) {
            mappingType = fileMappings.get(file);
        }
        return mappingType;
    }
}
