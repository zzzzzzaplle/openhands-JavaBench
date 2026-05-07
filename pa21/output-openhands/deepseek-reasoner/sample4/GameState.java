import java.util.Objects;

/**
 * Represents the full state of the game, including the board, lives, score tracking, and move history.
 */
public class GameState {

    /** Sentinel value indicating unlimited lives. */
    public static final int UNLIMITED_LIVES = -1;

    private final GameBoard gameBoard;
    private final MoveStack moveStack;
    private int numLives;
    private int numMoves;
    private int numDeaths;
    private int initialNumOfGems;

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
     * @param numLives  The number of lives. Use {@link #UNLIMITED_LIVES} for unlimited lives.
     */
    public GameState(final GameBoard gameBoard, final int numLives) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
        this.moveStack = new MoveStack();
        this.numLives = numLives;
        this.numMoves = 0;
        this.numDeaths = 0;
        this.initialNumOfGems = gameBoard.getNumGems();
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
     * Creates and returns a {@link GameBoardController} for this game's board.
     *
     * @return A new {@link GameBoardController}.
     */
    public GameBoardController getGameBoardController() {
        return new GameBoardController(gameBoard);
    }

    /**
     * Creates and returns a {@link GameBoardView} for this game's board.
     *
     * @return A new {@link GameBoardView}.
     */
    public GameBoardView getGameBoardView() {
        return new GameBoardView(gameBoard);
    }

    /**
     * Returns the number of lives remaining.
     *
     * @return {@link Integer#MAX_VALUE} if lives are unlimited, otherwise the remaining lives.
     */
    public int getNumLives() {
        if (hasUnlimitedLives()) {
            return Integer.MAX_VALUE;
        }
        return numLives;
    }

    /**
     * Returns the actual stored lives value (may be {@link #UNLIMITED_LIVES}).
     *
     * @return The raw lives value.
     */
    public int getRawNumLives() {
        return numLives;
    }

    /**
     * Checks if the player has unlimited lives.
     *
     * @return {@code true} if lives are unlimited.
     */
    public boolean hasUnlimitedLives() {
        return numLives < 0;
    }

    /**
     * Increases the number of lives by the specified delta.
     *
     * @param delta The amount to increase.
     * @return The new number of lives.
     */
    public int increaseNumLives(final int delta) {
        if (!hasUnlimitedLives()) {
            numLives += delta;
        }
        return getNumLives();
    }

    /**
     * Decreases the number of lives by the specified delta.
     *
     * @param delta The amount to decrease.
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
     * Increments the move counter by one.
     *
     * @return The new move count.
     */
    public int incrementNumMoves() {
        return ++numMoves;
    }

    /**
     * Increments the death counter by one.
     *
     * @return The new death count.
     */
    public int incrementNumDeaths() {
        return ++numDeaths;
    }

    /**
     * Returns the number of moves made.
     *
     * @return The move count.
     */
    public int getNumMoves() {
        return numMoves;
    }

    /**
     * Returns the number of deaths.
     *
     * @return The death count.
     */
    public int getNumDeaths() {
        return numDeaths;
    }

    /**
     * Returns the initial number of gems on the board.
     *
     * @return The initial gem count.
     */
    public int getInitialNumOfGems() {
        return initialNumOfGems;
    }

    /**
     * Returns the number of gems collected so far.
     *
     * @return The collected gem count.
     */
    public int getCollectedGems() {
        return initialNumOfGems - gameBoard.getNumGems();
    }

    /**
     * Checks if the player has won by collecting all gems.
     *
     * @return {@code true} if all gems have been collected.
     */
    public boolean hasWon() {
        return getCollectedGems() >= initialNumOfGems && initialNumOfGems > 0;
    }

    /**
     * Checks if the player has lost due to running out of lives.
     *
     * @return {@code true} if lives have reached zero and lives are not unlimited.
     */
    public boolean hasLost() {
        return !hasUnlimitedLives() && numLives <= 0;
    }

    /**
     * Calculates the current score.
     * <p>
     * Score = InitialBoardSize + (Gems * 10) - (Moves * 1) - (Undoes * 2) - (Deaths * 4)
     *
     * @return The calculated score.
     */
    public int getScore() {
        final int initialBoardSize = gameBoard.getNumRows() * gameBoard.getNumCols();
        final int gems = getCollectedGems();
        final int undoes = moveStack.getPopCount();
        return initialBoardSize + (gems * 10) - (numMoves * 1) - (undoes * 2) - (numDeaths * 4);
    }
}
