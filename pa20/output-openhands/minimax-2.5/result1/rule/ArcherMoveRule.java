package rule;

import game.Archer;
import game.Game;
import game.Move;
import game.Piece;
import game.Place;

/**
 * Rule: Archer moves must conform to orthogonal sliding and jumping capture constraints.
 */
public class ArcherMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        // Only applies to Archer pieces
        if (!(game.getPiece(move.getSource()) instanceof Archer)) {
            return true;
        }

        Place source = move.getSource();
        Place dest = move.getDestination();
        
        int sx = source.x();
        int sy = source.y();
        int dx = dest.x();
        int dy = dest.y();
        
        // Must be orthogonal (same row or same column)
        if (sx != dx && sy != dy) {
            return false;
        }
        
        Piece destPiece = game.getPiece(dest);
        
        if (destPiece == null) {
            // Non-capturing move - path must be clear
            return isPathClear(game, source, dest);
        } else {
            // Capturing move - must have exactly one piece in between
            return countPiecesBetween(game, source, dest) == 1;
        }
    }

    /**
     * Check if the path between source and destination is clear (for non-capturing moves).
     */
    private boolean isPathClear(Game game, Place source, Place dest) {
        int dx = Integer.signum(dest.x() - source.x());
        int dy = Integer.signum(dest.y() - source.y());
        
        int x = source.x() + dx;
        int y = source.y() + dy;
        
        while (x != dest.x() || y != dest.y()) {
            Place p = new Place(x, y);
            if (game.getPiece(p) != null) {
                return false; // Path is blocked
            }
            x += dx;
            y += dy;
        }
        
        return true;
    }

    /**
     * Count pieces between source and destination (exclusive).
     */
    private int countPiecesBetween(Game game, Place source, Place dest) {
        int count = 0;
        int dx = Integer.signum(dest.x() - source.x());
        int dy = Integer.signum(dest.y() - source.y());
        
        int x = source.x() + dx;
        int y = source.y() + dy;
        
        while (x != dest.x() || y != dest.y()) {
            Place p = new Place(x, y);
            if (game.getPiece(p) != null) {
                count++;
            }
            x += dx;
            y += dy;
        }
        
        return count;
    }

    @Override
    public String getDescription() {
        return "archer move rule is violated";
    }
}