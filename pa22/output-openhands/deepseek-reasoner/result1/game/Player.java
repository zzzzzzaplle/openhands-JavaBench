package assignment.game;

/**
 * Represents a player on the game board.
 * Each player has a unique ID.
 */
public class Player extends Entity {
    private final int id;

    /**
     * Create a player with the given ID.
     *
     * @param id the player ID
     */
    public Player(int id) {
        this.id = id;
    }

    /**
     * @return the player ID
     */
    public int getId() {
        return id;
    }
}
