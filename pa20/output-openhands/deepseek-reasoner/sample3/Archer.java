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
        List<Move> moves = new ArrayList<>();
        int size = game.getConfiguration().getSize();
        int numMovesProtection = game.getConfiguration().getNumMovesProtection();
        boolean inProtection = game.getNumMoves() < numMovesProtection;

        // Four orthogonal directions: right, left, up, down
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        for (int[] dir : directions) {
            int piecesBetween = 0;
            int curX = source.x() + dir[0];
            int curY = source.y() + dir[1];

            while (curX >= 0 && curX < size && curY >= 0 && curY < size) {
                Place dest = new Place(curX, curY);
                Piece destPiece = game.getPiece(dest);

                if (destPiece == null) {
                    // Empty square - valid non-capture move if path is clear
                    if (piecesBetween == 0) {
                        moves.add(new Move(source, dest));
                    }
                    // If piecesBetween > 0, this is not a valid move
                    // but continue walking to find potential capture target
                } else {
                    piecesBetween++;
                    if (piecesBetween == 1) {
                        // First piece encountered - this is the screen piece
                        // The screen itself is not a valid destination (it's occupied),
                        // so we continue looking past it
                    } else if (piecesBetween == 2) {
                        // Second piece - potential capture target
                        if (!destPiece.getPlayer().equals(this.getPlayer()) && !inProtection) {
                            moves.add(new Move(source, dest));
                        }
                        break; // No more valid moves beyond this point in this direction
                    } else {
                        break; // Beyond second piece, no valid moves
                    }
                }

                curX += dir[0];
                curY += dir[1];
            }
        }

        return moves.toArray(new Move[0]);
    }
}
