package assignment.game;

/**
 * Represents a box on the game board.
 * Each box belongs to a specific player identified by {@code playerId}.
 */
public class Box extends Entity {
    private final int playerId;

    /**
     * Create a box associated with the given player.
     *
     * @param playerId the ID of the player who can push this box
     */
    public Box(int playerId) {
        this.playerId = playerId;
    }

    /**
     * @return the player ID who owns this box
     */
    public int getPlayerId() {
        return playerId;
    }
}
