public class KnightMoveRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece piece = game.getPiece(move.getSource());
        if (!(piece instanceof Knight)) {
            return true;
        }
        Place src = move.getSource();
        Place dst = move.getDestination();
        int dx = Math.abs(src.x() - dst.x());
        int dy = Math.abs(src.y() - dst.y());

        // Must be L-shaped: (2, 1) or (1, 2)
        return (dx == 2 && dy == 1) || (dx == 1 && dy == 2);
    }

    @Override
    public String getDescription() {
        return "knight move rule is violated";
    }
}
