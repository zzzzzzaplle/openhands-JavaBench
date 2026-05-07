package assignment.game;

import java.util.Objects;

/**
 * Represents a player entity in the Sokoban game.
 * Each player has a unique ID (0-25, supporting up to 26 players).
 */
public class Player extends Entity {

    private final int id;

    /**
     * Creates a player with the specified ID.
     *
     * @param id the player's unique identifier
     */
    public Player(int id) {
        this.id = id;
    }

    /**
     * Returns the player's ID.
     *
     * @return the player ID
     */
    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player player)) return false;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
