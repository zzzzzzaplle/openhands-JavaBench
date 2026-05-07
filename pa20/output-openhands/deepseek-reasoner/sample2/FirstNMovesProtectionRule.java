public class FirstNMovesProtectionRule implements Rule {
    private int numProtectedMoves;

    public FirstNMovesProtectionRule(int numProtectedMoves) {
        this.numProtectedMoves = numProtectedMoves;
    }

    @Override
    public boolean validate(Game game, Move move) {
        if (game.getNumMoves() >= numProtectedMoves) {
            return true;
        }
        // If destination has a piece (capture), it's not allowed
        return game.getPiece(move.getDestination()) == null;
    }

    @Override
    public String getDescription() {
        return "Capturing piece in the first " + this.numProtectedMoves + " moves are not allowed";
    }
}
