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
package nl.salp.warcraft4j.util.unsafe;

import static java.lang.String.format;

/**
 * Native memory segment that lives outside the heap and is unmanaged (no garbage collection, security, etc.).
 * <p>
 * The segment _must_ be closed for the memory to be freed up.
 *
 * @author Barre Dijkstra
 */
public class NativeMemorySegment implements AutoCloseable {
    /** The {@link UnsafeHelper} instance to use. */
    private final UnsafeHelper unsafeHelper;
    /** Flag to indicate that the segment has been closed and released. */
    private boolean closed;
    /** The address of the memory segment. */
    private long address;
    /** The size of the memory segment. */
    private long size;

    /**
     * Create and assign a new native memory segment.
     *
     * @param size The size of the segment.
     *
     * @throws IllegalArgumentException When the size is negative or too big.
     * @throws OutOfMemoryError         When the allocation of the memory segment was refused by the operating system.
     */
    public NativeMemorySegment(long size) throws IllegalArgumentException, OutOfMemoryError {
        this.unsafeHelper = new UnsafeHelper();
        this.address = this.unsafeHelper.allocateMemory(size);
        this.size = size;
        this.closed = false;
    }

    /**
     * Resize the segment.
     *
     * @param newSize The new size of the segment in bytes.
     *
     * @throws IllegalArgumentException When the size is negative or too big.
     * @throws OutOfMemoryError         When the allocation of the memory segment was refused by the operating system.
     */
    public void resize(long newSize) throws IllegalArgumentException, OutOfMemoryError {
        this.address = this.unsafeHelper.reallocateMemory(this.address, newSize);
        this.size = newSize;
    }

    /**
     * Get a byte from the memory segment.
     *
     * @param offset The offset from the start of the segment.
     *
     * @return The data, which may be uninitialised (potentially resulting in garbage).
     *
     * @throws IllegalArgumentException When the offset and/or size are invalid.
     * @throws IllegalStateException    When the memory segment has been closed.
     */
    public byte get(long offset) throws IllegalArgumentException, IllegalStateException {
        return unsafeHelper.getByte(getAddress(offset, 1));
    }

    /**
     * Get a byte[] from the memory segment.
     *
     * @param offset The offset from the start of the segment.
     * @param size   The size of the data to retrieve in bytes.
     *
     * @return The data, which may be uninitialised (potentially resulting in garbage).
     *
     * @throws IllegalArgumentException When the offset and/or size are invalid.
     * @throws IllegalStateException    When the memory segment has been closed.
     */
    public byte[] get(long offset, int size) throws IllegalArgumentException, IllegalStateException {
        return unsafeHelper.getBytes(getAddress(offset, size), size);
    }

    /**
     * Store a byte[] in the memory segment.
     *
     * @param offset The offset from the start of the segment.
     * @param data   The data to store.
     *
     * @return The absolute address of the stored data.
     *
     * @throws IllegalArgumentException When the offset and/or data are invalid.
     * @throws IllegalStateException    When the memory segment has been closed.
     */
    public long put(long offset, byte... data) throws IllegalArgumentException, IllegalStateException {
        long address;
        if (data == null || data.length < 1) {
            address = getAddress(offset, 0);
        } else {
            address = getAddress(offset, data.length);
            unsafeHelper.putBytes(address, data);
        }
        return address;
    }

    /**
     * Get the address of the memory segment.
     *
     * @return The address.
     */
    public long getAddress() {
        return this.address;
    }

    /**
     * Get the size of the memory segment in bytes.
     *
     * @return The size.
     */
    public long getSize() {
        return this.size;
    }

    /**
     * Check if the native memory segment is closed and released.
     *
     * @return {@code true} if the segment has been closed and release.
     */
    public boolean isClosed() {
        return this.closed;
    }

    /**
     * Get the memory address for a size starting at an offset.
     *
     * @param offset The memory offset.
     * @param size   The size of the data to retrieve.
     *
     * @return The memory address.
     *
     * @throws IllegalArgumentException When the offset and/or size are invalid.
     * @throws IllegalStateException    When the memory segment has been closed.
     */
    private long getAddress(long offset, long size) throws IllegalArgumentException, IllegalStateException {
        if (closed) {
            throw new IllegalStateException("Trying to get an address for a closed memory segment.");
        }
        if (offset < 0) {
            throw new IllegalArgumentException(format("Unable to read %d bytes from negative offset %d.", size, offset));
        }
        if (offset + size > this.size) {
            throw new IllegalArgumentException(format("Unable to read %d bytes from offset %d from a %d byte memory segment.", size, offset, this.size));
        }
        return address + offset;
    }

    /**
     * Close the memory segment, releasing the allocated memory.
     *
     * @throws Exception When releasing the memory fails.
     */
    @Override
    public void close() throws Exception {
        if (!this.closed) {
            this.unsafeHelper.freeMemory(this.address);
            this.address = -1;
            this.size = 0;
            this.closed = true;
        }
    }
}
