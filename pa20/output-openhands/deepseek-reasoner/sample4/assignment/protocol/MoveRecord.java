package assignment.protocol;

/**
 * Infrastructure class that records a player and their move.
 * This is an independent utility type available for use by client code.
 */
public class MoveRecord {
    private final Player player;
    private final Move move;

    public MoveRecord(Player player, Move move) {
        this.player = player;
        this.move = move;
    }

    public Player getPlayer() {
        return player;
    }

    public Move getMove() {
        return move;
    }
}
