package assignment;

public class FirstNMovesProtectionRule implements Rule {
    private final int numProtectedMoves;

    public FirstNMovesProtectionRule(int numProtectedMoves) {
        this.numProtectedMoves = numProtectedMoves;
    }

    @Override
    public boolean validate(Game game, Move move) {
        // During protection phase, no capturing is allowed
        if (game.getNumMoves() < numProtectedMoves) {
            Piece destPiece = game.getPiece(move.getDestination());
            return destPiece == null;
        }
        return true;
    }

    @Override
    public String getDescription() {
        return "Capturing piece in the first " + this.numProtectedMoves + " moves are not allowed";
    }
}
