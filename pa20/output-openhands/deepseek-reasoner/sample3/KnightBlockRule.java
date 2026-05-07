public class KnightBlockRule implements Rule {
    @Override
    public boolean validate(Game game, Move move) {
        if (!(game.getPiece(move.getSource()) instanceof Knight)) {
            return true;
        }
        Place source = move.getSource();
        Place dest = move.getDestination();
        int dx = Math.abs(source.x() - dest.x());
        int dy = Math.abs(source.y() - dest.y());

        Place blockingPlace;
        if (dx == 2 && dy == 1) {
            // moving 2 horizontally, 1 vertically
            blockingPlace = new Place((source.x() + dest.x()) / 2, source.y());
        } else if (dx == 1 && dy == 2) {
            // moving 1 horizontally, 2 vertically
            blockingPlace = new Place(source.x(), (source.y() + dest.y()) / 2);
        } else {
            return true;
        }

        return game.getPiece(blockingPlace) == null;
    }

    @Override
    public String getDescription() {
        return "knight is blocked by another piece";
    }
}
