package assignment.protocol;

/**
 * Rule that checks if a Knight's path is blocked by an adjacent piece in its primary direction.
 * Similar to the Horse blocking rule in Chinese chess.
 */
public class KnightBlockRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece piece = game.getPiece(move.getSource());
        if (!(piece instanceof Knight)) {
            return true;
        }
        Place source = move.getSource();
        Place dest = move.getDestination();
        int dx = Math.abs(dest.x() - source.x());
        int dy = Math.abs(dest.y() - source.y());

        // Determine the blocking square
        int blockX, blockY;
        if (dx == 2) {
            // Moving 2 squares horizontally, blocking square is at ((sx+dx)/2, sy)
            blockX = (source.x() + dest.x()) / 2;
            blockY = source.y();
        } else if (dy == 2) {
            // Moving 2 squares vertically, blocking square is at (sx, (sy+dy)/2)
            blockX = source.x();
            blockY = (source.y() + dest.y()) / 2;
        } else {
            // Not a valid knight move, but let KnightMoveRule handle that
            return true;
        }

        Place blockPlace = new Place(blockX, blockY);
        return game.getPiece(blockPlace) == null;
    }

    @Override
    public String getDescription() {
        return "knight is blocked by another piece";
    }
}
