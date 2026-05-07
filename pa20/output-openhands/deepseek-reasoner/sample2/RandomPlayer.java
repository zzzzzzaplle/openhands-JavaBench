import java.util.Random;

public class RandomPlayer extends Player {
    private static final Random RANDOM = new Random();

    public RandomPlayer(String name, Color color) {
        super(name, color);
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        if (availableMoves == null || availableMoves.length == 0) {
            return null;
        }

        int index = RANDOM.nextInt(availableMoves.length);
        return availableMoves[index];
    }
}
