package assignment.protocol;

/**
 * Abstract class representing a piece on the game board.
 */
public abstract class Piece {
    private final Player player;

    public Piece(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the display label for this piece.
     *
     * @return a single character label
     */
    public abstract char getLabel();

    /**
     * Returns all available moves for this piece from the given source position
     * on the given game board. This generates geometrically valid candidate moves
     * based on the piece's movement type.
     *
     * @param game   the current game state
     * @param source the current position of the piece
     * @return array of candidate moves
     */
    public abstract Move[] getAvailableMoves(Game game, Place source);
}
