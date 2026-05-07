package assignment.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Knight piece that moves in an L-shape (2 squares in one direction, 1 in the orthogonal direction).
 * Can be blocked by an adjacent piece in its primary direction (like Horse in Chinese chess).
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
        int[][] offsets = {{2, 1}, {2, -1}, {-2, 1}, {-2, -1}, {1, 2}, {1, -2}, {-1, 2}, {-1, -2}};
        List<Move> moves = new ArrayList<>();
        int size = game.getConfiguration().getSize();

        for (int[] offset : offsets) {
            int destX = source.x() + offset[0];
            int destY = source.y() + offset[1];

            // Check board boundaries
            if (destX < 0 || destX >= size || destY < 0 || destY >= size) {
                continue;
            }

            Place dest = new Place(destX, destY);

            // Check blocking piece in primary direction
            int blockX, blockY;
            if (Math.abs(offset[0]) == 2) {
                // Moving 2 horizontally, block at ((sx+dx)/2, sy)
                blockX = (source.x() + destX) / 2;
                blockY = source.y();
            } else {
                // Moving 2 vertically, block at (sx, (sy+dy)/2)
                blockX = source.x();
                blockY = (source.y() + destY) / 2;
            }

            if (game.getPiece(blockX, blockY) != null) {
                continue;
            }

            moves.add(new Move(source, dest));
        }

        return moves.toArray(new Move[0]);
    }
}
