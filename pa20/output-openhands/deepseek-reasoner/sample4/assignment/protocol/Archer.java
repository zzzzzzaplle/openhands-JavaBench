package assignment.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Archer piece that moves any distance orthogonally without jumping.
 * For non-capturing moves, the path must be completely clear.
 * For capturing moves, there must be exactly one screen piece between source and destination
 * (similar to the Cannon in Chinese chess).
 */
public class Archer extends Piece {
    public Archer(Player player) {
        super(player);
    }

    @Override
    public char getLabel() {
        return 'A';
    }

    @Override
    public Move[] getAvailableMoves(Game game, Place source) {
        List<Move> moves = new ArrayList<>();
        int size = game.getConfiguration().getSize();

        // Four orthogonal directions: right, left, up, down
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            boolean screenFound = false;
            int x = source.x() + dir[0];
            int y = source.y() + dir[1];

            while (x >= 0 && x < size && y >= 0 && y < size) {
                Piece pieceAtDest = game.getPiece(x, y);

                if (pieceAtDest == null) {
                    // Empty square
                    if (!screenFound) {
                        // Non-capturing move: path is clear
                        moves.add(new Move(source, new Place(x, y)));
                    }
                    // If screen found, we're between screen and potential target
                    // Can't land here for a capture (need exactly one screen between)
                } else {
                    if (!screenFound) {
                        // This is the first piece encountered = the screen
                        screenFound = true;
                    } else {
                        // This is the second piece = potential capture target
                        if (!pieceAtDest.getPlayer().equals(this.getPlayer())) {
                            moves.add(new Move(source, new Place(x, y)));
                        }
                        break; // Can't go past the second piece
                    }
                }

                x += dir[0];
                y += dir[1];
            }
        }

        return moves.toArray(new Move[0]);
    }
}
