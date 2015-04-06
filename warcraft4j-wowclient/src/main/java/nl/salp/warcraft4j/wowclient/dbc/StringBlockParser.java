package nl.salp.warcraft4j.wowclient.dbc;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * TODO Document class.
 */
public class StringBlockParser {
    /**
     * Parse the provided data to a StringBlock.
     *
     * @param data The data in raw byte[] format.
     *
     * @return The StringBlock.
     *
     * @throws IOException When reading of the data failed.
     */
    public StringBlock parse(byte[] data) throws IOException {
        return new StringBlock(parseStrings(data));
    }

    /**
     * Parse the data to a map with the strings indexed by their start position.
     *
     * @param stringData The data.
     *
     * @return The map.
     *
     * @throws IOException When reading of the data failed.
     */
    private Map<Integer, String> parseStrings(byte[] stringData) throws IOException {
        Map<Integer, String> strings = new HashMap<>();

        ByteBuffer buffer = ByteBuffer.wrap(stringData);
        while (buffer.hasRemaining()) {
            int position = buffer.position();
            ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
            byte b;
            while (buffer.hasRemaining() && (b = buffer.get()) != 0) {
                byteOut.write(b);
            }
            String value = new String(byteOut.toByteArray(), StandardCharsets.US_ASCII);
            if (!value.isEmpty()) {
                strings.put(position, value);
            }
        }
        return strings;
    }

}
