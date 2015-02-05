package nl.salp.warcraft4j.battlenet;

/**
 * TODO Document class.
 *
 * @author Barre Dijkstra
 */
public enum BattlenetRegion {
    AMERICAS("us"),
    EUROPE("eu"),
    KOREA("kr"),
    TAIWAN("tw"),
    CHINA("cn"),
    SEA("us");

    private final String apiUri;

    private BattlenetRegion(String apiUri) {
        this.apiUri = apiUri;
    }

    public String getApiUri() {
        return apiUri;
    }
}
