package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight piece - moves in L-shape (2 squares in one direction, 1 in orthogonal).
 */
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
        List<Move> moves = new ArrayList<>();
        int size = game.getConfiguration().getSize();
        int x = source.x();
        int y = source.y();

        // All 8 L-shaped moves
        int[][] deltas = {
            {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
            {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        for (int[] delta : deltas) {
            int newX = x + delta[0];
            int newY = y + delta[1];

            // Check bounds
            if (newX >= 0 && newX < size && newY >= 0 && newY < size) {
                Place dest = new Place(newX, newY);
                Piece pieceAtDest = game.getPiece(dest);

                // Can't move to a square occupied by friendly piece
                if (pieceAtDest == null || pieceAtDest.getPlayer() != this.player) {
                    moves.add(new Move(source, dest));
                }
            }
        }

        return moves.toArray(new Move[0]);
    }
}
