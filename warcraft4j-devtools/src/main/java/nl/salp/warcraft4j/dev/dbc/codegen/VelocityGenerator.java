/*
 * Licensed to the Warcraft4J Project under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The Warcraft4J Project licenses
 * this file to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package nl.salp.warcraft4j.dev.dbc.codegen;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.context.Context;
import org.apache.velocity.runtime.RuntimeServices;
import org.apache.velocity.runtime.log.LogChute;
import org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader;
import org.slf4j.Logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.lang.String.format;

/**
 * Base class for velocity based class generators.
 *
 * @author Barre Dijkstra
 */
public abstract class VelocityGenerator<T> {
    /** The name of the template file on the classpath. */
    private final String template;
    /** The velocity engine to use. */
    private final VelocityEngine velocityEngine;
    /** Flag that indicates if existing files should be overwritten ({@code true}) or not ({@code false}). */
    private final boolean overrideFiles;

    /**
     * Create a new velocity generator.
     *
     * @param template      The name of the template file on the classpath.
     * @param overrideFiles Flag that indicates if existing files should be overwritten ({@code true}) or not ({@code false}).
     */
    protected VelocityGenerator(String template, boolean overrideFiles) {
        this.template = template;
        this.velocityEngine = initialiseVelocity();
        this.overrideFiles = overrideFiles;
    }

    /**
     * Get the output file for the given instance.
     *
     * @param instance The instance.
     *
     * @return The file handle for the file to write to.
     */
    protected abstract File getOutputFile(T instance) throws IOException;

    /**
     * Get the parameters to use in the generation for the given instance.
     *
     * @param instance The instance to get the parameters for.
     *
     * @return The parameters.
     */
    protected abstract Map<String, Object> getParameters(T instance);

    /**
     * Get the logger instance.
     *
     * @return The logger instance.
     */
    protected abstract Logger getLogger();

    /**
     * Generate a new class based on the provided instance.
     *
     * @param instance The instance to generate a new class for.
     *
     * @return File handle to the generated class or {@code null} if the file was skipped.
     *
     * @throws IOException When generation failed.
     */
    public final File generate(T instance) throws IOException {
        Template template = velocityEngine.getTemplate(this.template);
        return generate(instance, template);
    }

    /**
     * Generate classes based on the provided instances.
     *
     * @param instances The instances to generate classes for.
     *
     * @return The file handles for the files that were generated (and not skipped).
     *
     * @throws IOException When generation failed.
     */
    public final Set<File> generate(Collection<T> instances) throws IOException {
        Template template = velocityEngine.getTemplate(this.template);
        Set<File> generatedFiles = new HashSet<>();
        for (T instance : instances) {
            File output = generate(instance, template);
            if (output != null) {
                generatedFiles.add(output);
            }
        }
        return generatedFiles;
    }

    /**
     * Generate a class based on the provided instance, using the provided template.
     *
     * @param instance The instance to generate the class for.
     * @param template The template to use.
     *
     * @return File handle to the generated file or {@code null} if the file was skipped.
     *
     * @throws IOException When generation failed.
     */
    private File generate(T instance, Template template) throws IOException {
        File outputFile = getOutputFile(instance);
        File output;
        if (outputFile.exists() && !overrideFiles) {
            getLogger().debug("Skipping existing file {} for {} instance {}", outputFile.getPath(), instance.getClass().getName(), instance);
            output = null;
        } else {
            if (outputFile.exists()) {
                getLogger().debug("Overwriting existing file {} for {} instance {}", outputFile.getPath(), instance.getClass().getName(), instance);
            }
            try (FileWriter writer = new FileWriter(outputFile, false)) {
                Context context = createContext(instance);
                template.merge(context, writer);
                writer.flush();
                output = outputFile;
                getLogger().debug("Generated file {} from {} instance {}", outputFile.getPath(), instance.getClass().getName(), instance);
            } catch (IOException e) {
                getLogger().error(format("Error generating file %s from %s instance %s", outputFile.getPath(), instance.getClass().getName(), instance), e);
                throw e;
            }
        }
        return output;
    }

    /**
     * Create a context for the instance.
     *
     * @param instance The instance to create the context for.
     *
     * @return The context.
     */
    private Context createContext(T instance) {
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, Object> entry : getParameters(instance).entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        return context;
    }

    /**
     * Initialise the velocity engine.
     *
     * @return The initialised engine.
     */
    private VelocityEngine initialiseVelocity() {
        Properties velocityProperties = new Properties();
        velocityProperties.setProperty(VelocityEngine.RESOURCE_LOADER, "classpath");
        velocityProperties.setProperty("classpath." + VelocityEngine.RESOURCE_LOADER + ".class", ClasspathResourceLoader.class.getName());
        VelocityEngine engine = new VelocityEngine();
        engine.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM, new Slf4jLogChute(getLogger()));
        engine.init(velocityProperties);
        return engine;

    }

    /**
     * Simple slf4j logger wrapper for velocity.
     */
    private static class Slf4jLogChute implements LogChute {
        /** The slf4j logger instance to use. */
        private final Logger logger;

        /**
         * Create a new instance.
         *
         * @param logger The slf4j logger instance to use.
         */
        public Slf4jLogChute(Logger logger) {
            this.logger = logger;
        }

        @Override
        public void init(RuntimeServices rs) throws Exception {
        }

        @Override
        public void log(int level, String message) {
            switch (level) {
                case LogChute.TRACE_ID:
                    logger.trace(message);
                    break;
                case LogChute.INFO_ID:
                    logger.info(message);
                    break;
                case LogChute.DEBUG_ID:
                    logger.debug(message);
                    break;
                case LogChute.WARN_ID:
                    logger.warn(message);
                    break;
                case LogChute.ERROR_ID:
                    logger.error(message);
                    break;
                default:
                    logger.error("[unknown LogChute level " + level + "]");
                    logger.debug(message);
                    break;
            }
        }

        @Override
        public void log(int level, String message, Throwable t) {
            switch (level) {
                case LogChute.TRACE_ID:
                    logger.trace(message, t);
                    break;
                case LogChute.INFO_ID:
                    logger.info(message, t);
                    break;
                case LogChute.DEBUG_ID:
                    logger.debug(message, t);
                    break;
                case LogChute.WARN_ID:
                    logger.warn(message, t);
                    break;
                case LogChute.ERROR_ID:
                    logger.error(message, t);
                    break;
                default:
                    logger.error("[unknown LogChute level " + level + "]");
                    logger.debug(message, t);
                    break;
            }
        }

        @Override
        public boolean isLevelEnabled(int level) {
            boolean enabled = false;
            switch (level) {
                case LogChute.TRACE_ID:
                    enabled = logger.isTraceEnabled();
                    break;
                case LogChute.INFO_ID:
                    enabled = logger.isInfoEnabled();
                    break;
                case LogChute.DEBUG_ID:
                    enabled = logger.isDebugEnabled();
                    break;
                case LogChute.WARN_ID:
                    enabled = logger.isWarnEnabled();
                    break;
                case LogChute.ERROR_ID:
                    enabled = logger.isErrorEnabled();
                    break;
                default:
                    logger.error("[unknown LogChute level " + level + "]");
                    enabled = true;
            }
            return enabled;
        }
    }
}

