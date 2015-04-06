package nl.salp.warcraft4j.battlenet.api.wow.method;

import nl.salp.warcraft4j.battlenet.api.BattlenetApiGroup;
import nl.salp.warcraft4j.battlenet.api.JsonApiRequest;
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemDTO;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.battlenet.api.BattlenetApiGroup.WORLD_OF_WARCRAFT;

/**
 * TODO Document class.
 */
public class GetItemApiRequest extends JsonApiRequest<ItemDTO> {
    /** The DTO class. */
    private static final Class<ItemDTO> DTO_CLASS = ItemDTO.class;
    /** The API method. */
    private static final String API_METHOD = "/%s/item/%d";
    /** The API to use. */
    private static final BattlenetApiGroup API = WORLD_OF_WARCRAFT;
    /** Flag indicating if the method requires authentication. */
    private static final boolean API_REQUIRES_AUTH = false;
    /** The ID of the item to retrieve the information for. */
    private final long itemId;

    /**
     * Create a new BattlenetItemServiceMethod.
     *
     * @param itemId The ID of the item to retrieve the data for.
     */
    public GetItemApiRequest(long itemId) {
        super(DTO_CLASS);
        this.itemId = itemId;
    }

    @Override
    public boolean isAuthenticationRequired() {
        return API_REQUIRES_AUTH;
    }

    @Override
    public String getRequestUri() {
        return format(API_METHOD, API.getApiUri(), itemId);
    }

    @Override
    public Map<String, String> getRequestParameters() {
        return Collections.emptyMap();
    }
}
