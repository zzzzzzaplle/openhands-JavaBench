/**
 * Represents a box entity on the game board.
 * Each box belongs to a specific player and can only be pushed by that player.
 */
public class Box extends Entity {

    private final int playerId;

    /**
     * Creates a box belonging to the specified player.
     *
     * @param playerId the ID of the player who owns this box
     */
    public Box(int playerId) {
        this.playerId = playerId;
    }

    /**
     * Returns the player ID that owns this box.
     *
     * @return player ID
     */
    public int getPlayerId() {
        return playerId;
    }
}
