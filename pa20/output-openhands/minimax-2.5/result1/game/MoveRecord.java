package game;

/**
 * Record of a player's move in the game.
 */
public class MoveRecord {
    private Player player;
    private Move move;

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

    @Override
    public String toString() {
        return player.getName() + ": " + move;
    }
}