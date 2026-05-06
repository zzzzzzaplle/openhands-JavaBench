package game;

import java.util.ArrayList;
import java.util.List;

/**
 * Archer piece - moves orthogonally any distance. For normal moves, path must be clear.
 * For captures, exactly one piece between source and destination serves as "screen".
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
        int x = source.x();
        int y = source.y();

        // Four orthogonal directions
        int[][] directions = {
            {1, 0}, {-1, 0}, {0, 1}, {0, -1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];

            // Move in that direction step by step
            for (int dist = 1; dist < size; dist++) {
                int newX = x + dx * dist;
                int newY = y + dy * dist;

                // Check bounds
                if (newX < 0 || newX >= size || newY < 0 || newY >= size) {
                    break;
                }

                Place dest = new Place(newX, newY);
                Piece pieceAtDest = game.getPiece(dest);

                if (pieceAtDest == null) {
                    // Empty square - can move here (non-capturing move)
                    moves.add(new Move(source, dest));
                } else {
                    // There's a piece here - check if it's an enemy (capture)
                    if (pieceAtDest.getPlayer() != this.player) {
                        // Count pieces between source and dest
                        int piecesBetween = countPiecesBetween(game, source, dest);
                        
                        if (piecesBetween == 1) {
                            // Exactly one piece in between - valid capture
                            moves.add(new Move(source, dest));
                        }
                    }
                    // Stop - can't go beyond any piece
                    break;
                }
            }
        }

        return moves.toArray(new Move[0]);
    }

    /**
     * Count pieces between source and destination (exclusive).
     */
    private int countPiecesBetween(Game game, Place source, Place dest) {
        int count = 0;
        int dx = Integer.signum(dest.x() - source.x());
        int dy = Integer.signum(dest.y() - source.y());
        
        int x = source.x() + dx;
        int y = source.y() + dy;
        
        while (x != dest.x() || y != dest.y()) {
            Place p = new Place(x, y);
            if (game.getPiece(p) != null) {
                count++;
            }
            x += dx;
            y += dy;
        }
        
        return count;
    }
}