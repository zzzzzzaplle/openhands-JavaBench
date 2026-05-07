package assignment.protocol;

/**
 * Rule that prevents capturing pieces during the first N moves of the game.
 * The protection phase also prevents win condition evaluation.
 */
public class FirstNMovesProtectionRule implements Rule {
    private final int numProtectedMoves;

    public FirstNMovesProtectionRule(int numProtectedMoves) {
        this.numProtectedMoves = numProtectedMoves;
    }

    @Override
    public boolean validate(Game game, Move move) {
        // If the game has passed the protection phase, this rule doesn't apply
        if (game.getNumMoves() >= numProtectedMoves) {
            return true;
        }
        // During protection phase, no piece can be captured
        // A capture happens when destination has an opponent's piece
        Piece destPiece = game.getPiece(move.getDestination());
        return destPiece == null;
    }

    @Override
    public String getDescription() {
        return "Capturing piece in the first " + this.numProtectedMoves + " moves are not allowed";
    }
}
