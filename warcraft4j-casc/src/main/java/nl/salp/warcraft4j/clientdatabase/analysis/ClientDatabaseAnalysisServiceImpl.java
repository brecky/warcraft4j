package nl.salp.warcraft4j.clientdatabase.analysis;

import nl.salp.warcraft4j.clientdatabase.ClientDatabaseEntry;
import nl.salp.warcraft4j.clientdatabase.util.ClientDatabaseUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

import static nl.salp.warcraft4j.clientdatabase.util.ClientDatabaseUtil.findMappingsOnClasspath;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class ClientDatabaseAnalysisServiceImpl implements ClientDatabaseAnalysisService {
    @Override
    public ClasspathEntryMappingResults getMappingsOnClasspath(ClassLoader classLoader) {
        return new ClasspathEntryMappingResults(findMappingsOnClasspath(classLoader));
    }

    @Override
    public Collection<String> getUnmappedFiles(String dbcFileDirectory, ClassLoader classLoader) throws IOException {
        Collection<String> missingFiles = new TreeSet<>();
        Collection<String> mappedFiles = getAllMappedFiles(ClientDatabaseUtil.findMappingsOnClasspath(classLoader));
        for (String file : ClientDatabaseUtil.getAllClientDatabaseFiles(dbcFileDirectory)) {
            if (!mappedFiles.contains(file)) {
                missingFiles.add(file);
            }
        }
        return missingFiles;
    }


    private Collection<String> getAllMappedFiles(Collection<Class<? extends ClientDatabaseEntry>> mappingEntries) {
        Collection<String> files = new HashSet<>();
        for (Class<? extends ClientDatabaseEntry> mapping : mappingEntries) {
            String file = ClientDatabaseUtil.getMappedFile(mapping);
            if (isNotEmpty(file)) {
                files.add(file);
            }
        }
        return files;
    }
}
