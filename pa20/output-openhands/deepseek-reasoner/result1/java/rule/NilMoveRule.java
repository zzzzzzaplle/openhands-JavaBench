package rule;

import game.Game;
import game.Move;

/**
 * Rule: Source and destination must be different places.
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
