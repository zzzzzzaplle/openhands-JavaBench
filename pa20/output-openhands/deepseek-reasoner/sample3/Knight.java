import java.util.ArrayList;
import java.util.List;

public class Knight extends Piece {
    public Knight(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'K';
    }

    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        int[][] offsets = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        List<Move> moves = new ArrayList<>();
        int size = game.getConfiguration().getSize();

        for (int[] offset : offsets) {
            int destX = source.x() + offset[0];
            int destY = source.y() + offset[1];

            if (destX < 0 || destX >= size || destY < 0 || destY >= size) {
                continue;
            }

            Place dest = new Place(destX, destY);

            // Check if destination is not the same as source (NilMoveRule)
            if (source.equals(dest)) {
                continue;
            }

            // Check if destination has friendly piece (OccupiedRule)
            Piece destPiece = game.getPiece(dest);
            if (destPiece != null && destPiece.getPlayer().equals(this.getPlayer())) {
                continue;
            }

            // Check blocking square (KnightBlockRule)
            Place blockingPlace;
            if (Math.abs(offset[0]) == 2 && Math.abs(offset[1]) == 1) {
                blockingPlace = new Place((source.x() + destX) / 2, source.y());
            } else {
                blockingPlace = new Place(source.x(), (source.y() + destY) / 2);
            }
            if (game.getPiece(blockingPlace) != null) {
                continue;
            }

            // Check protection rule (no capture during first N moves)
            if (game.getNumMoves() < game.getConfiguration().getNumMovesProtection()) {
                if (destPiece != null) {
                    continue;
                }
            }

            moves.add(new Move(source, dest));
        }

        return moves.toArray(new Move[0]);
    }
}
