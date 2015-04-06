package nl.salp.warcraft4j.wowclient.dbc.analyser;

import nl.salp.warcraft4j.wowclient.dbc.StringBlock;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

/**
 * TODO Document class.
 */
public class Value {
    public static final byte[] EMPTY_VALUE = new byte[]{0, 0, 0, 0};
    public static final Value EMPTY = new Value(EMPTY_VALUE);
    private final int hashCode;
    private final byte[] data;

    public Value(byte[] data) {
        hashCode = new String(data, StandardCharsets.US_ASCII).hashCode();
        this.data = data;
    }

    private ByteBuffer dataBuffer() {
        return ByteBuffer.wrap(data);
    }

    private ByteBuffer dataBuffer(ByteOrder byteOrder) {
        return ByteBuffer.wrap(data).order(byteOrder);
    }

    public String asString() {
        return new String(data, StandardCharsets.US_ASCII);
    }

    public float asFloat() {
        return dataBuffer().getFloat();
    }

    public byte[] asByteArray() {
        return data;
    }

    public short[] asShortArray() {
        short[] data = new short[2];
        ByteBuffer buffer = dataBuffer(ByteOrder.LITTLE_ENDIAN);
        data[0] = buffer.getShort();
        data[1] = buffer.getShort();
        return data;
    }

    public char[] asCharArray() {
        char[] data = new char[2];
        ByteBuffer buffer = dataBuffer();
        data[0] = buffer.getChar();
        data[1] = buffer.getChar();
        return data;
    }

    public byte[] get() {
        return data;
    }

    public int asInt() {
        return dataBuffer(ByteOrder.LITTLE_ENDIAN).getInt();
    }

    public boolean isEmpty() {
        boolean empty = true;
        for (byte b : data) {
            if (b != 0) {
                empty = false;
                break;
            }
        }
        return empty;
    }

    public boolean isStringBlockValue(StringBlock stringBlock) {
        return stringBlock.isEntryAvailableForPosition(asInt());
    }

    public String asString(StringBlock stringBlock) {
        String value = null;
        if (isStringBlockValue(stringBlock)) {
            value = stringBlock.getEntry(asInt());
        } else {
            value = asString();
        }
        return value;
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public boolean equals(Object obj) {
        boolean eq = false;
        if (obj != null && Value.class.isAssignableFrom(obj.getClass())) {
            eq = get() == ((Value) obj).get();
        }
        return eq;
    }

    @Override
    public String toString() {
        return isEmpty() ? "empty" : asString();
    }
}
