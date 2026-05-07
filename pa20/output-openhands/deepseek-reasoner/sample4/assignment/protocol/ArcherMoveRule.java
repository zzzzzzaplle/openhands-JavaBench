package assignment.protocol;

/**
 * Rule that validates an Archer's move conforms to its movement constraints.
 * Archer moves any distance orthogonally (same row or same column).
 * For non-capturing moves, the path must be completely clear.
 * For capturing moves, there must be exactly one piece (screen) between source and destination.
 */
public class ArcherMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece piece = game.getPiece(move.getSource());
        if (!(piece instanceof Archer)) {
            return true;
        }
        Place source = move.getSource();
        Place dest = move.getDestination();

        // Must be orthogonal (same row or same column)
        if (source.x() != dest.x() && source.y() != dest.y()) {
            return false;
        }

        int dx = Integer.signum(dest.x() - source.x());
        int dy = Integer.signum(dest.y() - source.y());

        // Count pieces between source and destination (exclusive)
        int piecesBetween = 0;
        int x = source.x() + dx;
        int y = source.y() + dy;
        while (x != dest.x() || y != dest.y()) {
            if (game.getPiece(x, y) != null) {
                piecesBetween++;
            }
            x += dx;
            y += dy;
        }

        Piece destPiece = game.getPiece(dest);
        if (destPiece == null) {
            // Non-capturing move: path must be completely clear
            return piecesBetween == 0;
        } else {
            // Capturing move: must have exactly one screen piece between
            return piecesBetween == 1;
        }
    }

    @Override
    public String getDescription() {
        return "archer move rule is violated";
    }
}
