package game;

import java.util.Random;

/**
 * A player that chooses moves randomly.
 */
public class RandomPlayer extends Player {
    private Random random;

    public RandomPlayer(String name, Color color) {
        super(name, color);
        this.random = new Random();
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        if (availableMoves == null || availableMoves.length == 0) {
            return null;
        }
        int index = random.nextInt(availableMoves.length);
        return availableMoves[index];
    }
}