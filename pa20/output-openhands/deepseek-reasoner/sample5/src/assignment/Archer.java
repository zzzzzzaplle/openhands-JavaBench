package assignment;

import java.util.ArrayList;
import java.util.List;

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
        int size = game.getConfiguration().getSize();
        List<Move> moves = new ArrayList<>();
        // Four orthogonal directions: right, left, up, down
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int piecesBetween = 0;
            int cx = source.x() + dir[0];
            int cy = source.y() + dir[1];

            while (cx >= 0 && cx < size && cy >= 0 && cy < size) {
                boolean hasPiece = game.getPiece(cx, cy) != null;

                if (piecesBetween == 0) {
                    if (!hasPiece) {
                        // Non-capturing move
                        moves.add(new Move(source, new Place(cx, cy)));
                    } else {
                        // First piece found (the screen)
                        piecesBetween = 1;
                    }
                } else {
                    // We've already found one piece (the screen)
                    if (hasPiece) {
                        // Capturing move
                        moves.add(new Move(source, new Place(cx, cy)));
                        break; // No further moves in this direction
                    }
                }
                cx += dir[0];
                cy += dir[1];
            }
        }
        return moves.toArray(new Move[0]);
    }
}
