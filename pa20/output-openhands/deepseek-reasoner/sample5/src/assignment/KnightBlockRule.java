package assignment;

public class KnightBlockRule implements Rule {

    @Override
    public boolean validate(Game game, Move move) {
        Piece piece = game.getPiece(move.getSource());
        if (!(piece instanceof Knight)) {
            return true;
        }
        int dx = move.getDestination().x() - move.getSource().x();
        int dy = move.getDestination().y() - move.getSource().y();
        Place blockPlace;
        if (Math.abs(dx) == 2) {
            // Moving 2 squares horizontally, blocking square is directly between
            blockPlace = new Place(move.getSource().x() + dx / 2, move.getSource().y());
        } else {
            // Moving 2 squares vertically, blocking square is directly between
            blockPlace = new Place(move.getSource().x(), move.getSource().y() + dy / 2);
        }
        return game.getPiece(blockPlace) == null;
    }

    @Override
    public String getDescription() {
        return "knight is blocked by another piece";
    }
}
