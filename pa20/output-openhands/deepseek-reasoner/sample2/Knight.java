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
        // All L-shaped offsets
        int[][] offsets = {
                {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
        };

        List<Move> candidates = new ArrayList<>();
        Rule[] rules = getRules();

        for (int[] offset : offsets) {
            int dx = offset[0];
            int dy = offset[1];
            Place dest = new Place(source.x() + dx, source.y() + dy);
            Move move = new Move(source, dest);

            if (isValidMove(game, move, rules)) {
                candidates.add(move);
            }
        }

        return candidates.toArray(new Move[0]);
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
                new KnightMoveRule(),
                new KnightBlockRule()
        };
    }
}
