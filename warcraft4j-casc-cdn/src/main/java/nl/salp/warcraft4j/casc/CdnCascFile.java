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
package nl.salp.warcraft4j.casc;

import java.lang.ref.WeakReference;
import java.util.Optional;

import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public class CdnCascFile implements CascFile {
    private final WeakReference<CascContext> cascContext;
    private final long hash;
    private String filename;
    private FileHeader header;

    public CdnCascFile(long hash, CascContext cascContext) {
        this(hash, null, null, cascContext);
    }

    public CdnCascFile(long hash, String filename, CascContext cascContext) {
        this(hash, filename, null, cascContext);
    }

    public CdnCascFile(long hash, FileHeader header, CascContext cascContext) {
        this(hash, null, header, cascContext);
    }

    public CdnCascFile(long hash, String filename, FileHeader header, CascContext cascContext) {
        this.hash = hash;
        this.cascContext = new WeakReference<>(cascContext);
        this.filename = filename;
        this.header = header;
    }

    @Override
    public long getFilenameHash() {
        return hash;
    }

    @Override
    public Optional<String> getFilename() {
        return Optional.ofNullable(filename);
    }

    @Override
    public void setFilename(String filename) {
        if (isNotEmpty(filename)) {
            this.filename = filename;
            getCascContext().ifPresent(ctx -> {
                        ctx.resolve(filename, hash);
                    }
            );
        }
    }

    @Override
    public Optional<FileHeader> getHeader() {
        return Optional.ofNullable(header);
    }

    /**
     * The header is only kept in the current CascFile instance and stored in the CDN CASC service.
     *
     * @param header The header.
     */
    @Override
    public void setHeader(FileHeader header) {
        this.header = header;
    }

    private Optional<CascContext> getCascContext() {
        return Optional.ofNullable(cascContext.get());
    }
}
