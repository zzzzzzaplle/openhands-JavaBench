package assignment.game;

import java.util.Objects;

/**
 * Represents a box entity in the Sokoban game.
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
     * @return the player ID
     */
    public int getPlayerId() {
        return playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Box box)) return false;
        return playerId == box.playerId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId);
    }
}
