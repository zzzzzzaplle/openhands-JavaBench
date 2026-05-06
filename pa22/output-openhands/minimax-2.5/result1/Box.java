package game;

/**
 * Box entity that belongs to a specific player.
 */
public class Box extends Entity {
    private int playerId;

    public Box(int playerId) {
        this.playerId = playerId;
    }

    public int getPlayerId() {
        return playerId;
    }

    /**
     * Get the character representation of this box.
     */
    public char toChar() {
        return (char) ('a' + playerId);
    }
}