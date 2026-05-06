package rule;

import game.Game;
import game.Knight;
import game.Move;

/**
 * Rule: Knight moves must conform to its L-shape (2 in one direction, 1 in orthogonal).
 */
public class KnightMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        // Only applies to Knight pieces
        if (!(game.getPiece(move.getSource()) instanceof Knight)) {
            return true;
        }

        int sx = move.getSource().x();
        int sy = move.getSource().y();
        int dx = move.getDestination().x();
        int dy = move.getDestination().y();

        int deltaX = Math.abs(dx - sx);
        int deltaY = Math.abs(dy - sy);

        // L-shape: (2, 1) or (1, 2)
        return (deltaX == 2 && deltaY == 1) || (deltaX == 1 && deltaY == 2);
    }

    @Override
    public String getDescription() {
        return "knight move rule is violated";
    }
}
