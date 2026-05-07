/**
 * Represents a player entity on the game board.
 */
public class Player extends Entity {

    private final int id;

    /**
     * Creates a player with the specified ID.
     *
     * @param id the player identifier
     */
    public Player(int id) {
        this.id = id;
    }

    /**
     * Returns the player's ID.
     *
     * @return player ID
     */
    public int getId() {
        return id;
    }
}
