package rule;

import game.Game;
import game.Move;
import game.Piece;

/**
 * Rule: Destination cannot contain a friendly piece.
 */
public class OccupiedRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece sourcePiece = game.getPiece(move.getSource());
        Piece destPiece = game.getPiece(move.getDestination());

        if (destPiece == null) {
            return true; // Empty square - OK
        }

        // Can't capture own piece
        return sourcePiece.getPlayer() != destPiece.getPlayer();
    }

    @Override
    public String getDescription() {
        return "piece cannot be captured by another piece belonging to the same player";
    }
}
