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

package nl.salp.warcraft4j.clientdata.datafile;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isEmpty;

/**
 * Parsed instance of a data file (non-indexed file containing actual files as data blocks).
 *
 * @author Barre Dijkstra
 */
public class DataFile {
    /** The maximum size of a data file. */
    private static final long MAX_SIZE = 2 ^ 30;
    /** File name. */
    private String fileName;
    /** The size of the file in bytes. */
    private int fileSize;
    /** The parsed data blocks from the file. */
    private List<DataBlock> dataBlocks;

    /**
     * Create a new parsed
     *
     * @param fileName   The file name.
     * @param fileSize   The file size in bytes.
     * @param dataBlocks The data blocks.
     */
    public DataFile(String fileName, int fileSize, List<DataBlock> dataBlocks) {
        verify(fileName, fileSize, dataBlocks);
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.dataBlocks = dataBlocks;
    }

    /**
     * Verify the file information.
     *
     * @param fileName   The file name.
     * @param fileSize   The file size in bytes.
     * @param dataBlocks The parsed data blocks contained in the file.
     *
     * @throws IllegalArgumentException When one of the arguments failed verification.
     */
    private void verify(String fileName, int fileSize, List<DataBlock> dataBlocks) throws IllegalArgumentException {
        if (isEmpty(fileName)) {
            throw new IllegalArgumentException("Unable to create a new DataFile with an empty file name.");
        }
        if (fileSize < 0 || fileSize >= MAX_SIZE) {
            throw new IllegalArgumentException(format("Unable to create a new DataFile for file %s with a size of %dB, the maximum size is %dB.", fileName, fileSize, MAX_SIZE));
        }
        if (dataBlocks == null || dataBlocks.isEmpty()) {
            throw new IllegalArgumentException(format("Unable to create a new DataFile for file %s with no data blocks.", fileName));
        }
        int dataSize = 0;
        for (DataBlock dataBlock : dataBlocks) {
            dataSize = dataSize + dataBlock.getBlockSize();
        }
        if (dataSize != fileSize) {
            throw new IllegalArgumentException(format("The combined size of all %d data blocks is %dB, while it should be equal to the file size of %dB", dataBlocks.size(), dataSize, fileSize));
        }
    }

    /**
     * Get the file name.
     *
     * @return The file name.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Get the file size.
     *
     * @return The file size in bytes.
     */
    public int getFileSize() {
        return fileSize;
    }

    /**
     * Get the parsed data blocks contained in the file.
     *
     * @return The data blocks.
     */
    public List<DataBlock> getDataBlocks() {
        return Collections.unmodifiableList(dataBlocks);
    }
}
