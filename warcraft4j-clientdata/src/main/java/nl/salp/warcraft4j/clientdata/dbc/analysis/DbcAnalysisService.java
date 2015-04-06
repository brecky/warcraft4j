package nl.salp.warcraft4j.clientdata.dbc.analysis;

import java.io.IOException;
import java.util.Collection;

/**
 * Service for providing client database related analysis functions.
 *
 * @author Barre Dijkstra
 */
public interface DbcAnalysisService {
    /**
     * Get all entry mappings available on the classpath.
     *
     * @param classLoader The class loader to use (scan will include the parent class loaders).
     *
     * @return The mappings on the classpath.
     */
    ClasspathEntryMappingResults getMappingsOnClasspath(ClassLoader classLoader);

    /**
     * Find all DBC/DB2 files in the provided directory that don't have a entry mapping on the classpath.
     *
     * @param dbcFileDirectory The directory containing the DBC/DB2 files.
     * @param classLoader      The class loader to use (scan will include the parent class loaders).
     *
     * @return The names of the unmapped files.
     */
    Collection<String> getUnmappedFiles(String dbcFileDirectory, ClassLoader classLoader) throws IOException;


}
