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
        List<Move> candidates = new ArrayList<>();
        int size = game.getConfiguration().getSize();

        // Four orthogonal directions: up, down, left, right
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};

        for (int[] dir : directions) {
            int x = source.x() + dir[0];
            int y = source.y() + dir[1];

            while (x >= 0 && x < size && y >= 0 && y < size) {
                Place dest = new Place(x, y);
                candidates.add(new Move(source, dest));
                x += dir[0];
                y += dir[1];
            }
        }

        // Filter by rules
        Rule[] rules = getRules();
        List<Move> validMoves = new ArrayList<>();
        for (Move move : candidates) {
            if (isValidMove(game, move, rules)) {
                validMoves.add(move);
            }
        }

        return validMoves.toArray(new Move[0]);
    }

    private boolean isValidMove(Game game, Move move, Rule[] rules) {
        for (Rule rule : rules) {
            if (!rule.validate(game, move)) {
                return false;
            }
        }
        return true;
    }

    private Rule[] getRules() {
        return new Rule[]{
                new OutOfBoundaryRule(),
                new VacantRule(),
                new NilMoveRule(),
                new OccupiedRule(),
                new ArcherMoveRule()
        };
    }
}
