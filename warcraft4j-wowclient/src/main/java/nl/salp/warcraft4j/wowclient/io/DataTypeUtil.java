package nl.salp.warcraft4j.wowclient.io;

import java.nio.charset.Charset;

/**
 * Utility methods for data types.
 *
 * @author Barre Dijkstra
 */
public class DataTypeUtil {
    /**
     * Private constructor to prevent instantiation.
     */
    private DataTypeUtil() {
    }

    /**
     * Get the average number of bytes a character can consist of in the provided charset.
     *
     * @param charset The character set.
     *
     * @return The maximum number of bytes per character.
     */
    public static int getAverageBytesPerCharacter(Charset charset) {
        int avg = 0;
        if (charset != null) {
            // TODO Determine whether it should be rounded or cut off.
            avg = Math.round(charset.newEncoder().averageBytesPerChar());
        }
        return avg;
    }

    /**
     * Get the maximum number of bytes a character can consist of in the provided charset.
     *
     * @param charset The character set.
     *
     * @return The maximum number of bytes per character.
     */
    public static int getMaximumBytesPerCharacter(Charset charset) {
        int max = 0;
        if (charset != null) {
            // TODO Determine whether it should be rounded or cut off.
            max = Math.round(charset.newEncoder().maxBytesPerChar());
        }
        return max;
    }
}
