package assignment.protocol;

/**
 * Rule that ensures the destination of a move does not contain a piece belonging to the same player.
 */
public class OccupiedRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece sourcePiece = game.getPiece(move.getSource());
        if (sourcePiece == null) {
            return true;
        }
        Piece destPiece = game.getPiece(move.getDestination());
        if (destPiece == null) {
            return true;
        }
        return !sourcePiece.getPlayer().equals(destPiece.getPlayer());
    }

    @Override
    public String getDescription() {
        return "piece cannot be captured by another piece belonging to the same player";
    }
}
