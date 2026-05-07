public class ArcherMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece piece = game.getPiece(move.getSource());
        if (!(piece instanceof Archer)) {
            return true;
        }
        Place src = move.getSource();
        Place dst = move.getDestination();

        // Must be orthogonal: same x or same y
        if (src.x() != dst.x() && src.y() != dst.y()) {
            return false;
        }

        // Count pieces between source and destination (exclusive)
        int piecesBetween = countPiecesBetween(game, src, dst);

        Piece destPiece = game.getPiece(dst);
        if (destPiece == null) {
            // Non-capturing move: path must be completely clear
            return piecesBetween == 0;
        } else {
            // Capturing move: exactly one piece (the "screen") must be between
            return piecesBetween == 1;
        }
    }

    private int countPiecesBetween(Game game, Place src, Place dst) {
        int count = 0;
        if (src.x() == dst.x()) {
            // Vertical move
            int minY = Math.min(src.y(), dst.y());
            int maxY = Math.max(src.y(), dst.y());
            for (int y = minY + 1; y < maxY; y++) {
                if (game.getPiece(src.x(), y) != null) {
                    count++;
                }
            }
        } else {
            // Horizontal move
            int minX = Math.min(src.x(), dst.x());
            int maxX = Math.max(src.x(), dst.x());
            for (int x = minX + 1; x < maxX; x++) {
                if (game.getPiece(x, src.y()) != null) {
                    count++;
                }
            }
        }
        return count;
    }

    @Override
    public String getDescription() {
        return "archer move rule is violated";
    }
}
