public class ArcherMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        if (!(game.getPiece(move.getSource()) instanceof Archer)) {
            return true;
        }
        Place source = move.getSource();
        Place dest = move.getDestination();

        // Archer moves orthogonally only
        int dx = dest.x() - source.x();
        int dy = dest.y() - source.y();
        if (dx != 0 && dy != 0) {
            return false;
        }

        int stepX = Integer.signum(dx);
        int stepY = Integer.signum(dy);

        int piecesBetween = 0;
        int curX = source.x() + stepX;
        int curY = source.y() + stepY;

        while (curX != dest.x() || curY != dest.y()) {
            if (game.getPiece(curX, curY) != null) {
                piecesBetween++;
            }
            curX += stepX;
            curY += stepY;
        }

        Piece destPiece = game.getPiece(dest);
        if (destPiece == null) {
            // Non-capturing move: path must be clear
            return piecesBetween == 0;
        } else {
            // Capturing move: exactly one piece (screen) between source and destination
            // Friendly piece check is already handled by OccupiedRule
            return piecesBetween == 1;
        }
    }

    @Override
    public String getDescription() {
        return "archer move rule is violated";
    }
}
