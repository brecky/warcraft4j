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
    private static final String UNSAFE_INSTANCE_FIELDNAME = "theUnsafe";
    /** The initialised {@code sun.misc.Unsafe} instance. */
    private static Unsafe UNSAFE;

    /**
     * Create a new UnsafeHelper instance.
     *
     * @throws IllegalArgumentException When initialisation of the instance failed.
     */
    public UnsafeHelper() throws IllegalArgumentException {
        // Only initialise UNSAFE when needed, not when the class is loaded.
        if (UNSAFE == null) {
            try {
                final PrivilegedExceptionAction<Unsafe> action = () -> {
                    Field theUnsafe = Unsafe.class.getDeclaredField(UNSAFE_INSTANCE_FIELDNAME);
                    theUnsafe.setAccessible(true);
                    return (Unsafe) theUnsafe.get(null);
                };
                UNSAFE = AccessController.doPrivileged(action);
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
        return (T) UNSAFE.allocateInstance(type);
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
                .map(UNSAFE::objectFieldOffset)
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
                .map(UNSAFE::staticFieldOffset)
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
        return UNSAFE.allocateMemory(bytes);
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
        return UNSAFE.reallocateMemory(address, newSize);
    }

    /**
     * Free a block of memory allocated with {@link #allocateMemory(long)}.
     *
     * @param address The address of the memory block to free.
     *
     * @see #allocateMemory(long)
     */
    public void freeMemory(long address) {
        UNSAFE.freeMemory(address);
    }
}
