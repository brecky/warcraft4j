package nl.salp.warcraft4j.battlenet.api;

import com.owlike.genson.Genson;
import com.owlike.genson.GensonBuilder;
import com.owlike.genson.JsonBindingException;
import com.owlike.genson.stream.JsonStreamException;

/**
 * Base class for JSON based service methods.
 */
public abstract class JsonApiRequest<T> implements BattlenetApiRequest<T> {
    /** The initialised Genson instance for the implementations to use. */
    private static Genson gensonInstance = new GensonBuilder().failOnMissingProperty(true).create();
    /** The DTO class to parse the JSON to. */
    private final Class<T> dtoClass;

    /**
     * Create a new JsonServiceMethod instance.
     *
     * @param dtoClass The DTO class to parse the JSON to.
     */
    protected JsonApiRequest(Class<T> dtoClass) {
        this.dtoClass = dtoClass;
    }

    @Override
    public T parse(String json) throws BattlenetApiParsingException {
        try {
            return gensonInstance.deserialize(json, dtoClass);
        } catch (JsonStreamException | JsonBindingException e) {
            throw new BattlenetApiParsingException("Error parsing JSON", e);
        }
    }
}
