package nl.salp.warcraft4j.clientdata.io;

/**
 * Exception for mapping problems that occurred while parsing data.
 *
 * @author Barre Dijkstra
 */
public class DataParsingException extends RuntimeException {
    /**
     * Create a new instance.
     *
     * @param message The error message.
     */
    public DataParsingException(String message) {
        super(message);
    }

    /**
     * Create a new instance.
     *
     * @param message The error message.
     * @param cause   The exception that caused the exception.
     */
    public DataParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * Create a new instance.
     *
     * @param cause The exception that caused the exception.
     */
    public DataParsingException(Throwable cause) {
        super(cause);
    }
}
