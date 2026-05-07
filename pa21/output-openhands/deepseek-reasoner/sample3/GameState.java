import java.util.Objects;

/**
 * Represents the complete state of the game, including board, scoring, lives, and move history.
 */
public class GameState {

    /** Constant indicating unlimited lives. */
    public static final int UNLIMITED_LIVES = -1;

    private final GameBoard gameBoard;
    private final MoveStack moveStack;
    private final int initialNumOfGems;

    private int numDeaths;
    private int numMoves;
    private int numLives;

    /**
     * Creates a game state with unlimited lives.
     *
     * @param gameBoard The game board.
     */
    public GameState(final GameBoard gameBoard) {
        this(gameBoard, UNLIMITED_LIVES);
    }

    /**
     * Creates a game state with the specified number of lives.
     *
     * @param gameBoard The game board.
     * @param numLives  The number of lives, or {@link #UNLIMITED_LIVES} for unlimited.
     */
    public GameState(final GameBoard gameBoard, final int numLives) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
        this.moveStack = new MoveStack();
        this.numLives = numLives;
        this.numDeaths = 0;
        this.numMoves = 0;
        this.initialNumOfGems = this.gameBoard.getRemainingGems();
    }

    /**
     * Returns the game board.
     *
     * @return The game board.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * Returns the move stack.
     *
     * @return The move stack.
     */
    public MoveStack getMoveStack() {
        return moveStack;
    }

    /**
     * Returns the game board controller for this game state.
     *
     * @return A new game board controller.
     */
    public GameBoardController getGameBoardController() {
        return new GameBoardController(gameBoard);
    }

    /**
     * Returns the game board view for this game state.
     *
     * @return A new game board view.
     */
    public GameBoardView getGameBoardView() {
        return new GameBoardView(gameBoard);
    }

    /**
     * Returns the number of lives remaining.
     *
     * @return The number of lives remaining, or {@link Integer#MAX_VALUE} if unlimited.
     */
    public int getNumLives() {
        if (numLives < 0) {
            return Integer.MAX_VALUE;
        }
        return numLives;
    }

    /**
     * Returns whether the player has unlimited lives.
     *
     * @return {@code true} if lives are unlimited.
     */
    public boolean hasUnlimitedLives() {
        return numLives < 0;
    }

    /**
     * Checks if the player has won (all gems collected).
     *
     * @return {@code true} if all gems have been collected.
     */
    public boolean hasWon() {
        return getNumCollectedGems() >= initialNumOfGems;
    }

    /**
     * Checks if the player has lost (lives reached zero and not unlimited).
     *
     * @return {@code true} if the player has lost.
     */
    public boolean hasLost() {
        return !hasUnlimitedLives() && numLives <= 0;
    }

    /**
     * Increases the number of lives by the given delta.
     *
     * @param delta The amount to increase by.
     * @return The new number of lives.
     */
    public int increaseNumLives(final int delta) {
        if (!hasUnlimitedLives()) {
            numLives += delta;
        }
        return getNumLives();
    }

    /**
     * Decreases the number of lives by the given delta.
     *
     * @param delta The amount to decrease by.
     * @return The new number of lives.
     */
    public int decreaseNumLives(final int delta) {
        if (!hasUnlimitedLives()) {
            numLives -= delta;
            if (numLives < 0) {
                numLives = 0;
            }
        }
        return getNumLives();
    }

    /**
     * Decrements the number of lives by one.
     *
     * @return The new number of lives.
     */
    public int decrementNumLives() {
        return decreaseNumLives(1);
    }

    /**
     * Increments the number of moves by one.
     *
     * @return The new number of moves.
     */
    public int incrementNumMoves() {
        return ++numMoves;
    }

    /**
     * Increments the number of deaths by one.
     *
     * @return The new number of deaths.
     */
    public int incrementNumDeaths() {
        return ++numDeaths;
    }

    /**
     * Returns the number of deaths.
     *
     * @return The number of deaths.
     */
    public int getNumDeaths() {
        return numDeaths;
    }

    /**
     * Returns the number of moves made.
     *
     * @return The number of moves.
     */
    public int getNumMoves() {
        return numMoves;
    }

    /**
     * Returns the number of gems collected.
     *
     * @return The number of collected gems.
     */
    public int getNumCollectedGems() {
        return initialNumOfGems - gameBoard.getRemainingGems();
    }

    /**
     * Returns the initial number of gems on the board.
     *
     * @return The initial number of gems.
     */
    public int getInitialNumOfGems() {
        return initialNumOfGems;
    }

    /**
     * Calculates and returns the current score.
     * <p>
     * Score = InitialBoardSize + (Gems * 10) - (Moves * 1) - (Undoes * 2) - (Deaths * 4)
     * </p>
     *
     * @return The current score.
     */
    public int getScore() {
        final int initialBoardSize = gameBoard.getNumRows() * gameBoard.getNumCols();
        final int gems = getNumCollectedGems();
        final int undoes = moveStack.getPopCount();
        return initialBoardSize + (gems * 10) - (numMoves * 1) - (undoes * 2) - (numDeaths * 4);
    }
}
