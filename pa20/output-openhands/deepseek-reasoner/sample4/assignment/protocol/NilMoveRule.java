package assignment.protocol;

/**
 * Rule that ensures the source and destination of a move are different places.
 */
public class NilMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        return !move.getSource().equals(move.getDestination());
    }

    @Override
    public String getDescription() {
        return "the source and destination of move should be different places";
    }
}
