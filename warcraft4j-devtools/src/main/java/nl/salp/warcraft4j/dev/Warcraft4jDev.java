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
package nl.salp.warcraft4j.dev;

import nl.salp.warcraft4j.casc.CascService;
import nl.salp.warcraft4j.casc.cdn.CdnCascContext;
import nl.salp.warcraft4j.casc.cdn.CdnCascService;
import nl.salp.warcraft4j.casc.cdn.local.LocalCdnCascContext;
import nl.salp.warcraft4j.dev.casc.model.ListFile;
import nl.salp.warcraft4j.dev.ui.ExecutionContext;
import nl.salp.warcraft4j.dev.ui.Logger;
import nl.salp.warcraft4j.dev.ui.Warcraft4jFrame;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class Warcraft4jDev {
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(Warcraft4jDev.class);

    public Warcraft4jDev() throws IOException {
        DevToolsConfig config = DevToolsConfig.fromFile("w4j_devtools.config");
        CdnCascContext cascContext = new LocalCdnCascContext(config);
        ListFile.fromFile(config.getListFilePath()).getCascFiles().stream()
                .filter(file -> file.getFilename().isPresent())
                .forEach(file -> cascContext.resolve(file.getFilename().get().toUpperCase(), file.getFilenameHash()));
        CascService cascService = new CdnCascService(cascContext);
        Logger logger = new Logger() {

            @Override
            public void debug(String message, Object... args) {
                LOGGER.debug(message, args);
            }

            @Override
            public void warn(String message, Object... args) {
                LOGGER.warn(message, args);
            }

            @Override
            public void trace(String message, Object... args) {
                LOGGER.trace(message, args);
            }

            @Override
            public void error(String message, Object... args) {
                LOGGER.error(message, args);
            }
        };

        ExecutionContext context = new ExecutionContext(config, cascService, logger);

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Warcraft4jFrame(context);
            }
        });
    }

    public static void main(String... args) throws IOException {
        new Warcraft4jDev();
    }
}
