package rule;

import game.Game;
import game.Knight;
import game.Move;
import game.Place;

/**
 * Rule: Knight cannot be blocked by another piece in its path.
 * If moving 2 squares horizontally and 1 vertically, blocking square is at midpoint of horizontal.
 * If moving 1 square horizontally and 2 vertically, blocking square is at midpoint of vertical.
 */
public class KnightBlockRule implements Rule {
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

        // Calculate blocking square coordinates
        int blockX, blockY;
        
        if (Math.abs(dx - sx) == 2) {
            // Moving 2 horizontally - block is at middle of horizontal
            blockX = (sx + dx) / 2;
            blockY = sy;
        } else {
            // Moving 2 vertically - block is at middle of vertical
            blockX = sx;
            blockY = (sy + dy) / 2;
        }

        Place blockPlace = new Place(blockX, blockY);
        return game.getPiece(blockPlace) == null;
    }

    @Override
    public String getDescription() {
        return "knight is blocked by another piece";
    }
}