package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteOrder;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
public interface DbcDataType<T> {
    /**
     * Get the common name of the data type.
     *
     * @return The name.
     */
    String getName();

    /**
     * Get the name of the data type as used in DBC descriptors.
     *
     * @return The name.
     */
    String getDbcName();

    /**
     * Get the length of the data type in bytes.
     *
     * @return The length or {@code -1} if the length is variable.
     */
    int getByteLength();

    /**
     * Get the Java class representation for the data type.
     *
     * @return The Java class.
     */
    Class<T> getJavaClass();

    /**
     * Parse the value from a {@code byte[]}, using the default ({@code big endian}) byte order.
     *
     * @param value The value to parse.
     *
     * @return The parsed value.
     *
     * @throws IllegalArgumentException When the value couldn't be parsed.
     */
    T parse(byte[] value) throws IllegalArgumentException;

    /**
     * Parse the value from a {@code byte[]}, using the given byte order.
     *
     * @param value     The value to parse.
     * @param byteOrder The ByteOrder to use.
     *
     * @return The parsed value.
     *
     * @throws IllegalArgumentException When the value couldn't be parsed.
     */
    T parse(byte[] value, ByteOrder byteOrder) throws IllegalArgumentException;

    /**
     * Convert the value to a byte[], using the default ({@code big endian}) byte order.
     *
     * @param value The value to convert.
     *
     * @return The byte[] for the value.
     *
     * @throws IllegalArgumentException When the value couldn't be converted.
     */
    byte[] convert(T value) throws IllegalArgumentException;

    /**
     * Convert the value to a byte[], using the given byte order.
     *
     * @param value     The value to convert.
     * @param byteOrder The byte order to use.
     *
     * @return The byte[] for the value.
     *
     * @throws IllegalArgumentException When the value couldn't be converted.
     */
    byte[] convert(T value, ByteOrder byteOrder) throws IllegalArgumentException;

    /**
     * Set the value on the given field on the instance.
     *
     * @param value    The value.
     * @param field    The field.
     * @param instance The instance to set the value on.
     *
     * @throws IllegalArgumentException When setting the value failed.
     */
    void setValue(T value, Field field, Object instance) throws IllegalArgumentException;

    /**
     * Set the value on the instance using the given method (which should take a single argument of type {@code T}).
     *
     * @param value    The value.
     * @param method   The method to use.
     * @param instance The instance to set the value on.
     *
     * @throws IllegalArgumentException When setting the value failed.
     */
    void setValue(T value, Method method, Object instance) throws IllegalArgumentException;
}
