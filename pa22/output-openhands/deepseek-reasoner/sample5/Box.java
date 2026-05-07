/**
 * Represents a box on the game board. Each box is associated with a specific
 * player and can only be pushed by that player.
 */
public class Box extends Entity {

    private final int playerId;

    /**
     * Create a new box belonging to the specified player.
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
