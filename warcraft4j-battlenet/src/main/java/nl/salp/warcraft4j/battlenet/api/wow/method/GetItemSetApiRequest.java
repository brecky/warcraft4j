package nl.salp.warcraft4j.battlenet.api.wow.method;

import nl.salp.warcraft4j.battlenet.api.BattlenetApiGroup;
import nl.salp.warcraft4j.battlenet.api.JsonApiRequest;
import nl.salp.warcraft4j.battlenet.api.wow.dto.ItemSetDTO;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.battlenet.api.BattlenetApiGroup.WORLD_OF_WARCRAFT;

/**
 * Battle.NET API method for retrieving the information for a World of Warcarft item set.
 *
 * @author Barre Dijkstra
 */
public class GetItemSetApiRequest extends JsonApiRequest<ItemSetDTO> {
    /** The DTO class. */
    private static final Class<ItemSetDTO> DTO_CLASS = ItemSetDTO.class;
    /** The API method. */
    private static final String API_METHOD = "/%s/item/set/%d";
    /** The API to use. */
    private static final BattlenetApiGroup API = WORLD_OF_WARCRAFT;
    /** Flag indicating if the method requires authentication. */
    private static final boolean API_REQUIRES_AUTH = false;
    /** The ID of the item set to retrieve the information for. */
    private final long itemSetId;

    /**
     * Create a new BattlenetItemServiceMethod.
     *
     * @param itemSetId The ID of the item set to retrieve the data for.
     */
    public GetItemSetApiRequest(long itemSetId) {
        super(DTO_CLASS);
        this.itemSetId = itemSetId;
    }

    @Override
    public boolean isAuthenticationRequired() {
        return API_REQUIRES_AUTH;
    }

    @Override
    public String getRequestUri() {
        return format(API_METHOD, API.getApiUri(), itemSetId);
    }

    @Override
    public Map<String, String> getRequestParameters() {
        return Collections.emptyMap();
    }
}
