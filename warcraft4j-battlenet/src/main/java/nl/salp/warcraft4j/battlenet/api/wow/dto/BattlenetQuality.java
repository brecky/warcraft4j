package nl.salp.warcraft4j.battlenet.api.wow.dto;

import nl.salp.warcraft4j.model.data.Quality;

/**
 * TODO Document class.
 */
public enum BattlenetQuality {
    WOW_TOKEN(Quality.WOW_TOKEN, 8),
    HEIRLOOM(Quality.HEIRLOOM, 7),
    ARTIFACT(Quality.ARTIFACT, 6),
    LEGENDARY(Quality.LEGENDARY, 5),
    EPIC(Quality.EPIC, 4),
    RARE(Quality.RARE, 3),
    UNCOMMON(Quality.UNCOMMON, 2),
    COMMON(Quality.COMMON, 1),
    POOR(Quality.POOR, 0);

    /** The quality. */
    private final Quality quality;
    /** The id. */
    private final int id;

    /**
     * Create a new BattlenetQuality.
     *
     * @param quality The quality.
     * @param id      The id for the quality.
     */
    private BattlenetQuality(Quality quality, int id) {
        this.quality = quality;
        this.id = id;
    }

    public Quality getQuality() {
        return quality;
    }

    public int getId() {
        return id;
    }

    public static BattlenetQuality getQuality(Quality quality) {
        BattlenetQuality bnetQ = null;
        for (BattlenetQuality q : BattlenetQuality.values()) {
            if (q.getQuality() == quality) {
                bnetQ = q;
                break;
            }
        }
        return bnetQ;
    }


    public static BattlenetQuality getQuality(int quality) {
        BattlenetQuality bnetQ = null;
        for (BattlenetQuality q : BattlenetQuality.values()) {
            if (q.getId() == quality) {
                bnetQ = q;
                break;
            }
        }
        return bnetQ;
    }
}
