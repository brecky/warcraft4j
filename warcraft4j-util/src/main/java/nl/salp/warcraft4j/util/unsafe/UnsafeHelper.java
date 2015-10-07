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

import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedExceptionAction;
import java.util.Optional;

/**
 * Utility wrapper for unsafe and low-level/native operations.
 * <p>
 * <p>
 * Please use with caution and only use when you know what you're actually doing and where native operations/performance are really needed ;-)
 * <p>
 * Until JDK 9 and JEP 260 has been implemented uses the sun.misc.Unsafe, sun.nio.*
 *
 * @author Barre Dijkstra
 */
public final class UnsafeHelper {
    /** The name of the field holding the unsafe instance in the {@code sun.misc.Unsafe} class. */
    private static final String UNSAFE_INSTANCE_FIELDNAME = "theUnsafe";
    /** The initialised {@code sun.misc.Unsafe} instance. */
    private static Unsafe unsafe;
    /** The memory offset for byte arrays. */
    private static final long byteArrayOffset = Unsafe.ARRAY_BOOLEAN_BASE_OFFSET;
    /** The memory offset for int arrays. */
    private static final long intArrayOffset = Unsafe.ARRAY_INT_BASE_OFFSET;
    /** The memory offset for long arrays. */
    private static final long longArrayOffset = Unsafe.ARRAY_LONG_BASE_OFFSET;

    /**
     * Create a new UnsafeHelper instance.
     *
     * @throws IllegalArgumentException When initialisation of the instance failed.
     */
    public UnsafeHelper() throws IllegalArgumentException {
        // Only initialise unsafe when needed, not when the class is loaded. No need to worry for race conditions here, double initialisation won't matter that much.
        if (unsafe == null) {
            try {
                final PrivilegedExceptionAction<Unsafe> action = () -> {
                    Field theUnsafe = Unsafe.class.getDeclaredField(UNSAFE_INSTANCE_FIELDNAME);
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                };
                unsafe = AccessController.doPrivileged(action);
            } catch (Exception e) {
                throw new IllegalArgumentException("Unable to load unsafe instance", e);
            }
        }
    }

    /** The offset used for invalid offsets (e.g. offsets for objects that could not be found.) */
    public static final long INVALID_OFFSET = Unsafe.INVALID_FIELD_OFFSET;
    /** The address size in bytes for a 32-bit system. */
    private static final int ADDRESS_SIZE_32BIT = 4;
    /** The address size in bytes for a 64-bit system. */
    private static final int ADDRESS_SIZE_64BIT = 8;

    /**
     * Check if the system uses 64-bit memory addresses.
     *
     * @return {@code true} if the system uses 64-bit memory addresses.
     */
    public boolean isMemoryAddress64bit() {
        return Unsafe.ADDRESS_SIZE == ADDRESS_SIZE_64BIT;
    }

    /**
     * Check if the system uses 32-bit memory addresses.
     *
     * @return {@code true} if the system uses 32-bit memory addresses.
     */
    public boolean isMemoryAddress32bit() {
        return Unsafe.ADDRESS_SIZE == ADDRESS_SIZE_32BIT;
    }

    /**
     * Get the system's memory address size in bytes.
     *
     * @return The memory address size.
     */
    public int getMemoryAddressSize() {
        return Unsafe.ADDRESS_SIZE;
    }

    /**
     * Create a new instance of the class _without_ running a constructor.
     *
     * @param type The class.
     * @param <T>  The type of the instance to create.
     *
     * @return The instance.
     *
     * @throws InstantiationException When the instance could not be initialised.
     */
    @SuppressWarnings("unchecked")
    public <T> T createInstanceDirect(Class<T> type) throws InstantiationException {
        return (T) unsafe.allocateInstance(type);
    }

    /**
     * Get the memory offset of an instance field of an object.
     *
     * @param instance  The instance to get the field offset from.
     * @param fieldName The name of the field.
     *
     * @return The offset of the instance field for the object.
     */
    public long getFieldOffset(Object instance, String fieldName) {
        return getObjectField(instance, fieldName)
                .map(unsafe::objectFieldOffset)
                .orElse(INVALID_OFFSET);
    }

    /**
     * Get the memory offset of a static field of a class.
     *
     * @param type      The class to get the field offset from.
     * @param fieldName The name of the field.
     *
     * @return The offset of the static field for the class.
     */
    public long getStaticFieldOffset(Class type, String fieldName) {
        return getStaticField(type, fieldName)
                .map(unsafe::staticFieldOffset)
                .orElse(INVALID_OFFSET);
    }

    /**
     * Get an instance field from an object, checking the full object hierarchy (excluding {@code Object}) and ignoring static fields.
     *
     * @param instance  The object to get the field from.
     * @param fieldName The name of the field.
     *
     * @return Optional containing the field if it was found and an instance field.
     */
    public Optional<Field> getObjectField(Object instance, String fieldName) {
        return Optional.ofNullable(instance)
                .map(Object::getClass)
                .flatMap(type -> getObjectField(type, fieldName));
    }

    /**
     * Get an instance field from a class, checking the full object hierarchy (excluding {@code Object}) and ignoring static fields.
     *
     * @param type      The class to get the field from.
     * @param fieldName The name of the field.
     *
     * @return Optional containing the field if it was found and an instance field.
     */
    public Optional<Field> getObjectField(Class<?> type, String fieldName) {
        return getField(type, fieldName)
                .filter(this::isObjectField);
    }

    /**
     * Get a static field from a class, checking the full class hierarchy (excluding {@code Object.class}) and ignoring non-static fields.
     *
     * @param type      The class to get the field from.
     * @param fieldName The name of the field.
     *
     * @return Optional containing the field if it was found and a static field.
     */
    public Optional<Field> getStaticField(Class<?> type, String fieldName) {
        return getField(type, fieldName)
                .filter(this::isStaticField);
    }

    /**
     * Check if the field is a static field.
     *
     * @param field The field to check.
     *
     * @return {@code true} if the field is not null and a static field.
     */
    public boolean isStaticField(Field field) {
        return Optional.ofNullable(field)
                .map(Field::getModifiers)
                .map(Modifier::isStatic)
                .orElse(false);
    }

    /**
     * Check if the field is an object field.
     *
     * @param field The field to check.
     *
     * @return {@code true} if the field is not null and an object field.
     */
    public boolean isObjectField(Field field) {
        return Optional.ofNullable(field)
                .map(f -> !isStaticField(f))
                .orElse(false);
    }

    /**
     * Get a field from a class, checking the full class hierarchy (excluding {@code Object.class}).
     *
     * @param type      The class to get the field from.
     * @param fieldName The name of the field.
     *
     * @return Optional containing the field if it was found.
     */
    public Optional<Field> getField(Class<?> type, String fieldName) {
        Optional<Field> field;
        if (type == null || fieldName == null || fieldName.length() == 0) {
            field = Optional.empty();
        } else {
            try {
                field = Optional.of(type.getDeclaredField(fieldName));
            } catch (NoSuchFieldException e) {
                if (type.getSuperclass() == null || type.getSuperclass() == Object.class) {
                    field = Optional.empty();
                } else {
                    field = getField(type.getSuperclass(), fieldName);
                }
            }
        }
        return field;
    }

    /**
     * Allocate a block of {@code native}, uninitialised, memory.
     * <p>
     * The memory should be released via {@link #freeMemory(long)} and can be resized via {@link #reallocateMemory(long, long)}.
     *
     * @param bytes The size of the block in bytes.
     *
     * @return The address of the allocated block.
     *
     * @throws IllegalArgumentException When the requested size is negative or too big.
     * @throws OutOfMemoryError         If a block of the requested size was refused by the operation system.
     * @see #reallocateMemory(long, long)
     * @see #freeMemory(long)
     */
    public long allocateMemory(long bytes) throws IllegalArgumentException, OutOfMemoryError {
        return unsafe.allocateMemory(bytes);
    }

    /**
     * Resize a block of memory allocated with {@link #allocateMemory(long)}.
     *
     * @param address The address of the existing memory block.
     * @param newSize The new size of the memory block.
     *
     * @return The address of the memory block.
     *
     * @throws IllegalArgumentException When the requested size is negative or too big.
     * @throws OutOfMemoryError         If resizing to the requested size was refused by the operation system.
     * @see #allocateMemory(long)
     * @see #freeMemory(long)
     */
    public long reallocateMemory(long address, long newSize) {
        return unsafe.reallocateMemory(address, newSize);
    }

    /**
     * Free a block of memory allocated with {@link #allocateMemory(long)}.
     *
     * @param address The address of the memory block to free.
     *
     * @see #allocateMemory(long)
     */
    public void freeMemory(long address) {
        unsafe.freeMemory(address);
    }

    /**
     * Get a byte from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     *
     * @return The data.
     */
    public byte getByte(Object object, long offset) {
        return unsafe.getByte(object, offset);
    }

    /**
     * Get a byte from a memory address.
     *
     * @param address The absolute memory address.
     *
     * @return The data.
     */
    public byte getByte(long address) {
        return unsafe.getByte(address);
    }

    /**
     * Store a byte at a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset to store the data from the start of the object memory address.
     * @param value  The value to store.
     */
    public void putByte(Object object, long offset, byte value) {
        unsafe.putByte(object, offset, value);
    }

    /**
     * Store a byte at a memory address.
     *
     * @param address The absolute memory address.
     * @param value   The value to store.
     */
    public void putByte(long address, byte value) {
        unsafe.putByte(address, value);
    }

    /**
     * Get a byte[] from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     * @param size   The number of entries to get.
     *
     * @return The data.
     */
    public byte[] getBytes(Object object, long offset, int size) {
        byte[] data = new byte[size];
        unsafe.copyMemory(object, offset, data, byteArrayOffset, size);
        return data;
    }

    /**
     * Get a byte[] from a memory address.
     *
     * @param address The absolute memory address.
     * @param size    The number of entries to get.
     *
     * @return The data.
     */
    public byte[] getBytes(long address, int size) {
        byte[] data = new byte[size];
        unsafe.copyMemory(null, address, data, byteArrayOffset, size);
        return data;
    }

    /**
     * Store a byte[] at a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset to store the data from the start of the object memory address.
     * @param values The values to store.
     */
    public void putBytes(Object object, long offset, byte... values) {
        if (values.length > 0) {
            long bytes = values.length;
            unsafe.copyMemory(values, byteArrayOffset, object, offset, bytes);
        }
    }

    /**
     * Store a byte[] at a memory address.
     *
     * @param address The absolute memory address.
     * @param values  The values to store.
     */
    public void putBytes(long address, byte... values) {
        if (values.length > 0) {
            long bytes = values.length;
            unsafe.copyMemory(values, byteArrayOffset, null, address, bytes);
        }
    }

    /**
     * Get an int from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     *
     * @return The data.
     */
    public int getInt(Object object, long offset) {
        return unsafe.getInt(object, offset);
    }

    /**
     * Get an int from a memory address.
     *
     * @param address The absolute memory address.
     *
     * @return The data.
     */
    public int getInt(long address) {
        return unsafe.getInt(address);
    }

    /**
     * Get an int[] from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     * @param size   The number of entries to get.
     *
     * @return The data.
     */
    public int[] getInts(Object object, long offset, int size) {
        int[] ints = new int[size];
        long bytes = size << 2;
        unsafe.copyMemory(object, offset, ints, intArrayOffset, bytes);
        return ints;
    }

    /**
     * Get an int[] from a memory address.
     *
     * @param address The absolute memory address.
     * @param size    The number of entries to get.
     *
     * @return The data.
     */
    public int[] getInts(long address, int size) {
        int[] ints = new int[size];
        long bytes = size << 2;
        unsafe.copyMemory(null, address, ints, intArrayOffset, bytes);
        return ints;
    }

    /**
     * Store an int[] at a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset to store the data from the start of the object memory address.
     * @param values The values to store.
     */
    public void putInts(Object object, long offset, int... values) {
        if (values.length > 0) {
            long bytes = values.length << 2;
            unsafe.copyMemory(values, intArrayOffset, object, offset, bytes);
        }
    }

    /**
     * Store an int[] at a memory address.
     *
     * @param address The absolute memory address.
     * @param values  The values to store.
     */
    public void putInts(long address, int... values) {
        if (values.length > 0) {
            long bytes = values.length << 2;
            unsafe.copyMemory(values, longArrayOffset, null, address, bytes);
        }
    }

    /**
     * Get a long from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     *
     * @return The data.
     */
    public long getLong(Object object, long offset) {
        return unsafe.getLong(object, offset);
    }

    /**
     * Get a long from a memory address.
     *
     * @param address The absolute memory address.
     *
     * @return The data.
     */
    public long getLong(long address) {
        return unsafe.getLong(address);
    }

    /**
     * Store a long at a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset to store the data from the start of the object memory address.
     * @param value  The value to store.
     */
    public void putLong(Object object, long offset, long value) {
        unsafe.putLong(object, offset, value);
    }

    /**
     * Store a long at a memory address.
     *
     * @param address The absolute memory address.
     * @param value   The value to store.
     */
    public void putLong(long address, long value) {
        unsafe.putLong(null, address, value);
    }

    /**
     * Get an long[] from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     * @param size   The number of entries to get.
     *
     * @return The data.
     */
    public long[] getLongs(Object object, long offset, int size) {
        long[] longs = new long[size];
        long bytes = size << 3;
        unsafe.copyMemory(object, offset, longs, longArrayOffset, bytes);
        return longs;
    }

    /**
     * Get a long[] from a memory address.
     *
     * @param address The absolute memory address.
     * @param size    The number of entries to get.
     *
     * @return The data.
     */
    public long[] getLongs(long address, int size) {
        long[] longs = new long[size];
        long bytes = size << 3;
        unsafe.copyMemory(null, address, longs, longArrayOffset, bytes);
        return longs;
    }

    /**
     * Store a long[] at a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset to store the data from the start of the object memory address.
     * @param values The values to store.
     */
    public void putLongs(Object object, long offset, long... values) {
        if (values.length > 0) {
            long bytes = values.length << 3;
            unsafe.copyMemory(values, longArrayOffset, object, offset, bytes);
        }
    }

    /**
     * Store a long[] at a memory address.
     *
     * @param address The absolute memory address.
     * @param values  The values to store.
     */
    public void putLongs(long address, long... values) {
        if (values.length > 0) {
            long bytes = values.length << 3;
            unsafe.copyMemory(values, longArrayOffset, null, address, bytes);
        }
    }

    /**
     * Get a boolean from a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset of the data from the start of the object memory address.
     *
     * @return The data.
     */
    public boolean getBoolean(Object object, long offset) {
        return unsafe.getBoolean(object, offset);
    }

    /**
     * Get a boolean from a memory address.
     *
     * @param address The absolute memory address.
     *
     * @return The data.
     */
    public boolean getBoolean(long address) {
        return unsafe.getBoolean(null, address);
    }

    /**
     * Store a boolean at a memory address relative to the address of an object.
     *
     * @param object The object to use for memory address (or {@code null} for using the offset as an absolute address).
     * @param offset The offset to store the data from the start of the object memory address.
     * @param value  The value to store.
     */
    public void putBoolean(Object object, long offset, boolean value) {
        unsafe.putBoolean(object, offset, value);
    }

    /**
     * Store a boolean at a memory address.
     *
     * @param address The absolute memory address.
     * @param value   The value to store.
     */
    public void putBoolean(long address, boolean value) {
        unsafe.putBoolean(null, address, value);
    }
}
