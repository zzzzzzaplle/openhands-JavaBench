package assignment.protocol;

import java.util.Random;

/**
 * An AI player that picks a random move from the available moves.
 */
public class RandomPlayer extends Player {
    private final transient Random random;

    public RandomPlayer(String name, Color color) {
        super(name, color);
        this.random = new Random();
    }

    @Override
    public Move nextMove(Game game, Move[] availableMoves) {
        if (availableMoves.length == 0) {
            return null;
        }
        int index = random.nextInt(availableMoves.length);
        return availableMoves[index];
    }
}
