package rule;

import game.Game;
import game.Move;
import game.Piece;

/**
 * Rule: During the first N moves, no piece can be captured.
 */
public class FirstNMovesProtectionRule implements Rule {
    private int numProtectedMoves;

    public FirstNMovesProtectionRule(int numProtectedMoves) {
        this.numProtectedMoves = numProtectedMoves;
    }

    @Override
    public boolean validate(Game game, Move move) {
        // If we're still in protection phase, check for captures
        if (game.getNumMoves() < numProtectedMoves) {
            Piece destPiece = game.getPiece(move.getDestination());
            if (destPiece != null) {
                // There's a piece at destination - this would be a capture
                return false;
            }
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Capturing piece in the first " + this.numProtectedMoves + " moves are not allowed";
    }
}