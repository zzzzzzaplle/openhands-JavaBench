/**
 * Represents a player on the game board.
 */
public class Player extends Entity {

    private final int id;

    /**
     * Create a player with the given ID.
     *
     * @param id the player's unique identifier
     */
    public Player(int id) {
        this.id = id;
    }

    /**
     * @return the player's ID
     */
    public int getId() {
        return id;
    }
}
