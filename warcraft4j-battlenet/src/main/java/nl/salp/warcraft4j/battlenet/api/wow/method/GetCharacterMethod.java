package nl.salp.warcraft4j.battlenet.api.wow.method;

import nl.salp.warcraft4j.battlenet.BattlenetApi;
import nl.salp.warcraft4j.battlenet.api.JsonServiceMethod;
import nl.salp.warcraft4j.battlenet.api.wow.dto.CharacterDTO;

import java.util.Collections;
import java.util.Map;

import static java.lang.String.format;
import static nl.salp.warcraft4j.battlenet.BattlenetApi.WORLD_OF_WARCRAFT;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public class GetCharacterMethod extends JsonServiceMethod<CharacterDTO> {
    /** The DTO class. */
    private static final Class<CharacterDTO> DTO_CLASS = CharacterDTO.class;
    /** The API method. */
    private static final String API_METHOD = "/%s/character/%s/%s";
    /** The API to use. */
    private static final BattlenetApi API = WORLD_OF_WARCRAFT;
    /** Flag indicating if the method requires authentication. */
    private static final boolean API_REQUIRES_AUTH = false;
    /** The name of the parameter of the detail fields to retrieve. */
    private static final String PARAMETER_FIELDS = "fields";
    /** The realm of the character. */
    private final String realm;
    /** The character name. */
    private final String characterName;
    /** The character details to retrieve. */
    private final CharacterDetailField[] characterDetailFields;

    /**
     * Create a new GetCharacterMethod instance.
     *
     * @param realm                 The realm of the character.
     * @param characterName         The character name.
     * @param characterDetailFields The character details to retrieve.
     */
    public GetCharacterMethod(String realm, String characterName, CharacterDetailField... characterDetailFields) {
        super(DTO_CLASS);
        this.realm = realm;
        this.characterName = characterName;
        this.characterDetailFields = characterDetailFields;
    }

    @Override
    public boolean isAuthenticationRequired() {
        return API_REQUIRES_AUTH;
    }

    @Override
    public String getRequestUri() {
        return format(API_METHOD, API.getApiUri(), realm, characterName);
    }

    @Override
    public Map<String, String> getRequestParameters() {
        StringBuilder fieldsBuilder = new StringBuilder();
        for (int i = 0; i < characterDetailFields.length; i++) {
            if (i > 0) {
                fieldsBuilder.append(',');
            }
            fieldsBuilder.append(characterDetailFields[i].getFieldName());
        }
        return Collections.singletonMap(PARAMETER_FIELDS, fieldsBuilder.toString());
    }
}
