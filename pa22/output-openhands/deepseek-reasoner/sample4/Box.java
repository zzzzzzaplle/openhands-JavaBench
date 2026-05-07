/**
 * Represents a box on the game board that can be pushed by its corresponding player.
 * Each box belongs to a specific player identified by playerId.
 */
public class Box extends Entity {

    private final int playerId;

    /**
     * Create a box for the specified player.
     *
     * @param playerId the ID of the player who can push this box
     */
    public Box(int playerId) {
        this.playerId = playerId;
    }

    /**
     * @return the player ID that owns this box
     */
    public int getPlayerId() {
        return playerId;
    }
}
