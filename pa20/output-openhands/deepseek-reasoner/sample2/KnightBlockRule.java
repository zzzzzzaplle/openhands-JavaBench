public class KnightBlockRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        Piece piece = game.getPiece(move.getSource());
        if (!(piece instanceof Knight)) {
            return true;
        }
        Place src = move.getSource();
        Place dst = move.getDestination();
        int dx = dst.x() - src.x();
        int dy = dst.y() - src.y();

        Place blockingPlace;
        if (Math.abs(dx) == 2 && Math.abs(dy) == 1) {
            // Moving 2 horizontally, 1 vertically: block at ((src.x + dst.x) / 2, src.y)
            blockingPlace = new Place((src.x() + dst.x()) / 2, src.y());
        } else if (Math.abs(dx) == 1 && Math.abs(dy) == 2) {
            // Moving 1 horizontally, 2 vertically: block at (src.x, (src.y + dst.y) / 2)
            blockingPlace = new Place(src.x(), (src.y() + dst.y()) / 2);
        } else {
            // Not a valid knight move, but that's checked by KnightMoveRule
            return true;
        }

        return game.getPiece(blockingPlace) == null;
    }

    @Override
    public String getDescription() {
        return "knight is blocked by another piece";
    }
}
