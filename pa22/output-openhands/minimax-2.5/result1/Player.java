package game;

/**
 * Player entity.
 */
public class Player extends Entity {
    private int id;

    public Player(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    /**
     * Get the character representation of this player.
     */
    public char toChar() {
        return (char) ('A' + id);
    }
}