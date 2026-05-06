package game;

/**
 * Abstract base class for pieces in the game.
 */
public abstract class Piece {
    protected Player player;

    public Piece(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }

    /**
     * Get the display label for this piece.
     * @return 'K' for Knight, 'A' for Archer
     */
    public abstract char getLabel();

    /**
     * Get all available moves for this piece from the given source position.
     * @param game the current game
     * @param source the current position of the piece
     * @return array of possible moves
     */
    public abstract Move[] getAvailableMoves(Game game, Place source);

    public Piece clone() throws CloneNotSupportedException {
        Piece cloned = (Piece) super.clone();
        cloned.player = this.player;
        return cloned;
    }
}