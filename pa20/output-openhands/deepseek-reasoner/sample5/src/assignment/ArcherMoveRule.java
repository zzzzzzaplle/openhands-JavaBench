package assignment;

public class ArcherMoveRule implements Rule {

    @Override
    public boolean validate(Game game, Move move) {
        if (!(game.getPiece(move.getSource()) instanceof Archer)) {
            return true;
        }
        Place src = move.getSource();
        Place dest = move.getDestination();
        int dx = dest.x() - src.x();
        int dy = dest.y() - src.y();

        // Must be orthogonal movement
        if (dx != 0 && dy != 0) {
            return false;
        }

        // Determine step direction
        int stepX = dx == 0 ? 0 : (dx > 0 ? 1 : -1);
        int stepY = dy == 0 ? 0 : (dy > 0 ? 1 : -1);

        int piecesBetween = 0;
        int cx = src.x() + stepX;
        int cy = src.y() + stepY;

        while (cx != dest.x() || cy != dest.y()) {
            if (game.getPiece(cx, cy) != null) {
                piecesBetween++;
            }
            cx += stepX;
            cy += stepY;
        }

        // Destination has a piece -> capturing move (exactly one piece between)
        if (game.getPiece(dest) != null) {
            return piecesBetween == 1;
        }
        // Non-capturing move (no pieces between)
        return piecesBetween == 0;
    }

    @Override
    public String getDescription() {
        return "archer move rule is violated";
    }
}
