import java.util.Objects;

/**
 * Represents the full state of the game, including the board, score tracking, and undo history.
 */
public class GameState {

    /**
     * Constant indicating unlimited lives.
     */
    public static final int UNLIMITED_LIVES = -1;

    private final GameBoard gameBoard;
    private final MoveStack moveStack;

    private int numLives;
    private int numMoves;
    private int numDeaths;
    private final int initialNumOfGems;
    private int gemsCollected;

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
        this.initialNumOfGems = countGems(gameBoard);
        this.gemsCollected = 0;
    }

    /**
     * Counts the total number of gems on the board.
     */
    private static int countGems(final GameBoard board) {
        int count = 0;
        for (int r = 0; r < board.getNumRows(); r++) {
            for (int c = 0; c < board.getNumCols(); c++) {
                final Cell cell = board.getCell(r, c);
                if (cell instanceof EntityCell) {
                    if (((EntityCell) cell).getEntity() instanceof Gem) {
                        count++;
                    }
                }
            }
        }
        return count;
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
     * Returns the number of lives. If unlimited, returns {@link Integer#MAX_VALUE}.
     *
     * @return The number of lives.
     */
    public int getNumLives() {
        return hasUnlimitedLives() ? Integer.MAX_VALUE : numLives;
    }

    /**
     * Increases the number of lives by the given delta.
     *
     * @param delta The amount to increase.
     * @return The new number of lives (or {@link Integer#MAX_VALUE} if unlimited).
     */
    public int increaseNumLives(final int delta) {
        if (!hasUnlimitedLives()) {
            numLives += delta;
        }
        return getNumLives();
    }

    /**
     * Decreases the number of lives by the given delta, but not below zero.
     *
     * @param delta The amount to decrease.
     * @return The new number of lives (or {@link Integer#MAX_VALUE} if unlimited).
     */
    public int decreaseNumLives(final int delta) {
        if (!hasUnlimitedLives()) {
            numLives = Math.max(0, numLives - delta);
        }
        return getNumLives();
    }

    /**
     * Decrements the number of lives by one.
     *
     * @return The new number of lives (or {@link Integer#MAX_VALUE} if unlimited).
     */
    public int decrementNumLives() {
        return decreaseNumLives(1);
    }

    /**
     * Increments the number of moves made.
     *
     * @return The new move count.
     */
    public int incrementNumMoves() {
        return ++numMoves;
    }

    /**
     * Increments the number of deaths.
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
     * @return The gems collected count.
     */
    public int getGemsCollected() {
        return gemsCollected;
    }

    /**
     * Adds to the count of collected gems.
     *
     * @param count The number of gems to add.
     */
    public void addGemsCollected(final int count) {
        this.gemsCollected += count;
    }

    /**
     * Subtracts from the count of collected gems (used during undo).
     *
     * @param count The number of gems to remove.
     */
    public void removeGemsCollected(final int count) {
        this.gemsCollected = Math.max(0, this.gemsCollected - count);
    }

    /**
     * Returns whether the player has won (all gems collected).
     *
     * @return {@code true} if the player has collected all gems.
     */
    public boolean hasWon() {
        return gemsCollected >= initialNumOfGems && initialNumOfGems > 0;
    }

    /**
     * Returns whether the player has lost (lives reached zero and not in unlimited mode).
     *
     * @return {@code true} if the player has lost.
     */
    public boolean hasLost() {
        return !hasUnlimitedLives() && numLives <= 0;
    }

    /**
     * Calculates the current score.
     * <p>
     * Score = InitialBoardSize + (Gems * 10) - (Moves * 1) - (Undoes * 2) - (Deaths * 4)
     *
     * @return The current score.
     */
    public int getScore() {
        final int boardSize = gameBoard.getNumRows() * gameBoard.getNumCols();
        return boardSize + (gemsCollected * 10) - (numMoves * 1)
                - (moveStack.getPopCount() * 2) - (numDeaths * 4);
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
     * Returns a controller for the game board.
     *
     * @return A new game board controller.
     */
    public GameBoardController getGameBoardController() {
        return new GameBoardController(gameBoard);
    }

    /**
     * Returns a view for the game board.
     *
     * @return A new game board view.
     */
    public GameBoardView getGameBoardView() {
        return new GameBoardView(gameBoard);
    }

    /**
     * Returns the move stack.
     *
     * @return The move stack.
     */
    public MoveStack getMoveStack() {
        return moveStack;
    }
}
