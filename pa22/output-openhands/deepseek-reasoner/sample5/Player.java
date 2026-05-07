/**
 * Represents a player on the game board.
 */
public class Player extends Entity {

    private final int id;

    /**
     * Create a new player with the specified ID.
     *
     * @param id the player identifier (0-based index)
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
