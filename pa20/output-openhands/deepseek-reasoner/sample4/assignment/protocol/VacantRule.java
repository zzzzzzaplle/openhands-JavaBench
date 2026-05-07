package assignment.protocol;

/**
 * Rule that ensures the source of a move is not vacant (has a piece).
 */
public class VacantRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        return game.getPiece(move.getSource()) != null;
    }

    @Override
    public String getDescription() {
        return "the source of move should have a piece";
    }
}
