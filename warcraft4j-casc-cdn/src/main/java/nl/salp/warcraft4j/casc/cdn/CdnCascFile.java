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
package nl.salp.warcraft4j.casc.cdn;

import nl.salp.warcraft4j.casc.CascFile;
import nl.salp.warcraft4j.casc.FileHeader;
import nl.salp.warcraft4j.io.DataReader;

import java.lang.ref.WeakReference;
import java.util.Optional;
import java.util.function.Supplier;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

/**
 * {@link CascFile} implementation for a CDN based CASC, describing a file stored in the CASC.
 *
 * @author Barre Dijkstra
 * @see nl.salp.warcraft4j.casc.CascFile
 */
public class CdnCascFile implements CascFile {
    /** Weak reference to the CASC the file is stored in. */
    private final WeakReference<CdnCascContext> cascContext;
    /** Hash of the filename. */
    private final long hash;
    /** The filename, may be {@code null}. */
    private String filename;
    /** The parsed header of the file, may be {@code null}. */
    private FileHeader header;

    /**
     * Create a new instance.
     *
     * @param hash        The hash of the filename.
     * @param cascContext The CASC the file is stored in.
     *
     * @throws IllegalArgumentException When an invalid CASC context is provided.
     */
    public CdnCascFile(long hash, CdnCascContext cascContext) throws IllegalArgumentException {
        this(hash, null, cascContext);
    }

    /**
     * Create a new instance.
     *
     * @param hash        The hash of the filename.
     * @param filename    The name of the file.
     * @param cascContext The CASC the file is stored in.
     *
     * @throws IllegalArgumentException When invalid data is provided.
     */
    public CdnCascFile(long hash, String filename, CdnCascContext cascContext) throws IllegalArgumentException {
        this.cascContext = Optional.ofNullable(cascContext)
                .map(WeakReference<CdnCascContext>::new)
                .orElseThrow(() -> new IllegalArgumentException("Unable to initialise a CASC file for a null CdnCascContext."));
        this.hash = Optional.of(hash)
                .filter(cascContext::isRegistered)
                .orElseThrow(() -> new IllegalArgumentException(format("Unable to initialise a CASC file for filename hash %d, which is not registered in the CASC.", hash)));
        if (isNotEmpty(filename) && CdnCascContext.hashFilename(filename) != hash) {
            throw new IllegalArgumentException(format("Filename %s has hash %d while the hashcode %d has been provided.", filename, CdnCascContext.hashFilename(filename),
                    hash));
        }
        setFilename(filename);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getFilenameHash() {
        return hash;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<String> getFilename() {
        return Optional.ofNullable(filename);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setFilename(String filename) {
        if (isNotEmpty(filename)) {
            this.filename = filename;
            getCascContext().ifPresent(ctx -> ctx.resolve(filename, hash));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<FileHeader> getHeader() {
        if (header == null) {
            header = readHeader();
        }
        return Optional.ofNullable(header);
    }

    private FileHeader readHeader() {
        return getCascContext()
                .filter(ctx -> ctx.isRegisteredData(getFilenameHash()))
                .map(ctx -> (Supplier<DataReader>) () -> ctx.getFileDataReader(getFilenameHash()))
                .map(FileHeader::parse)
                .orElse(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setHeader(FileHeader header) {
        this.header = header;
    }

    /**
     * Get the CASC context.
     *
     * @return Optional containing the CASC context or empty when the CASC context has been closed/GC'd.
     */
    private Optional<CdnCascContext> getCascContext() {
        return Optional.ofNullable(cascContext.get());
    }
}
