package rule;

import game.Game;
import game.Move;

/**
 * Rule: Source and destination must be within board boundaries.
 */
public class OutOfBoundaryRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        int size = game.getConfiguration().getSize();
        int sx = move.getSource().x();
        int sy = move.getSource().y();
        int dx = move.getDestination().x();
        int dy = move.getDestination().y();
        
        return sx >= 0 && sx < size && sy >= 0 && sy < size &&
               dx >= 0 && dx < size && dy >= 0 && dy < size;
    }

    @Override
    public String getDescription() {
        return "place is out of boundary of gameboard";
    }
}