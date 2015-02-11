package nl.salp.warcraft4j.wowclient.databaseclient.datatype;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import static java.lang.String.format;

/**
 * TODO Add description.
 *
 * @author Barre Dijkstra
 */
abstract class BaseDbcDataType<T> implements DbcDataType<T> {
    /** The default byte order. */
    protected static final ByteOrder DEFAULT_BYTE_ORDER = ByteOrder.BIG_ENDIAN;

    /**
     * Add the value to the given ByteBuffer.
     *
     * @param value      The value to convert.
     * @param byteBuffer The ByteBuffer to add the value to.
     */
    protected abstract void addToByteBuffer(T value, ByteBuffer byteBuffer);

    /**
     * Read the next value from the byte buffer.
     *
     * @param byteBuffer The byte buffer (already in right byte order).
     *
     * @return The read value.
     */
    protected abstract T readFromByteBuffer(ByteBuffer byteBuffer);

    /**
     * Set the value on the field of the given instance.
     *
     * @param field    The field.
     * @param instance The instance.
     * @param value    The value.
     *
     * @throws IllegalAccessException When setting was not allowed.
     */
    protected abstract void setFieldValue(Field field, Object instance, T value) throws IllegalAccessException;

    @Override
    public final T parse(byte[] value) throws IllegalArgumentException {
        return (parse(value, DEFAULT_BYTE_ORDER));
    }

    @Override
    public final T parse(byte[] value, ByteOrder byteOrder) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException(format("Tried parse a null byte[] to a %s.", getName()));
        }
        if ((getByteLength() > 0) && (value.length != getByteLength())) {
            throw new IllegalArgumentException(format("Tried to parse a byte[] with a length of %d to a %s, which requires %d bytes.", value.length, getName(), getByteLength()));
        }
        if (byteOrder == null) {
            throw new IllegalArgumentException(format("Unable to parse a byte[] to a %s with a null ByteOrder.", getName()));
        }

        return readFromByteBuffer(ByteBuffer.wrap(value).order(byteOrder));
    }

    @Override
    public final byte[] convert(T value) throws IllegalArgumentException {
        return convert(value, DEFAULT_BYTE_ORDER);
    }

    @Override
    public final byte[] convert(T value, ByteOrder byteOrder) throws IllegalArgumentException {
        if (value == null) {
            throw new IllegalArgumentException(format("Tried convert a null %s to a byte[].", getName()));
        }
        if (byteOrder == null) {
            throw new IllegalArgumentException(format("Tried to convert %s value %s to a byte[] with a null ByteOrder", getName(), value));
        }
        ByteBuffer byteBuffer = ByteBuffer.allocate(getByteLength());
        byteBuffer.order(byteOrder);
        addToByteBuffer(value, byteBuffer);
        byteBuffer.rewind();

        byte[] bytes = new byte[getByteLength()];
        byteBuffer.get(bytes);
        return bytes;
    }

    @Override
    public final void setValue(T value, Field field, Object instance) throws IllegalArgumentException {
        if (field == null) {
            throw new IllegalArgumentException(format("Unable to set the value %d on a null field.", value));
        }
        if (instance == null) {
            throw new IllegalArgumentException(format("Unable to set the value %d on field %s on a null object.", value, field.getName()));
        }
        if (!instance.getClass().isAssignableFrom(field.getDeclaringClass())) {
            throw new IllegalArgumentException(format("Unable to set the value %s on field %s of an instance of class %s, while the instance must be of type Class<? extends %s>.", value, field.getName(), instance.getClass().getName(), field.getDeclaringClass().getName()));
        }
        if (!field.getType().isAssignableFrom(getJavaClass())) {
            throw new IllegalArgumentException(String.format("Setting of value %s via %s.%s, since it requires to be of type Class<? extends %s>", value, instance.getClass().getName(), field.getName(), getJavaClass().getName()));
        }
        try {
            setFieldValue(field, instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Unable to set the value %s on %s.%s, due to access violations.", value, instance.getClass().getName(), field.getName()), e);
        }
    }


    @Override
    public final void setValue(T value, Method method, Object instance) throws IllegalArgumentException {
        if (method == null) {
            throw new IllegalArgumentException(format("Unable to set the value %d via a null method.", value));
        }
        if (instance == null) {
            throw new IllegalArgumentException(format("Unable to set the value %d via method %s on a null object.", value, method.getName()));
        }
        if (!instance.getClass().isAssignableFrom(method.getDeclaringClass())) {
            throw new IllegalArgumentException(format("Unable to set the value %s via %s on an instance of class %s, while the instance must be of type Class<? extends %s>.", value, method.getName(), instance.getClass().getName(), method.getDeclaringClass().getName()));
        }
        if (method.getParameterTypes().length != 1 || !method.getParameterTypes()[0].isAssignableFrom(getJavaClass())) {
            throw new IllegalArgumentException(String.format("Setting of value %s via %s.%s, since it requires 1 parameter of Class<? extends %s>", value, instance.getClass().getName(), method.getName(), getJavaClass().getName()));
        }
        try {
            method.invoke(instance, value);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(String.format("Unable to set the value %s on %s.%s, due to access violations.", value, instance.getClass().getName(), method.getName()), e);
        } catch (InvocationTargetException e) {
            throw new IllegalArgumentException(String.format("Unable to set the value %s on %s.%s, due to an exception: %s", value, instance.getClass().getName(), method.getName(), e.getMessage()), e);
        }
    }
}
