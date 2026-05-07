package assignment;

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
        int[] dx = {2, 2, -2, -2, 1, 1, -1, -1};
        int[] dy = {1, -1, 1, -1, 2, -2, 2, -2};
        int size = game.getConfiguration().getSize();
        List<Move> moves = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            int nx = source.x() + dx[i];
            int ny = source.y() + dy[i];
            if (nx >= 0 && nx < size && ny >= 0 && ny < size) {
                moves.add(new Move(source, new Place(nx, ny)));
            }
        }
        return moves.toArray(new Move[0]);
    }
}
