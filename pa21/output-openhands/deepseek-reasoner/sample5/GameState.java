import java.util.Objects;

/**
 * Represents the complete state of the game, including the board, score tracking,
 * lives, and move history.
 */
public class GameState {

    /** Sentinel value indicating unlimited lives. */
    public static final int UNLIMITED_LIVES = -1;

    private final GameBoard gameBoard;
    private final GameBoardController gameBoardController;
    private final GameBoardView gameBoardView;
    private final MoveStack moveStack;
    private final int initialNumOfGems;

    private int numMoves;
    private int numDeaths;
    private int numLives;

    /**
     * Creates a game state with the specified game board and unlimited lives.
     *
     * @param gameBoard The game board.
     */
    public GameState(final GameBoard gameBoard) {
        this(gameBoard, UNLIMITED_LIVES);
    }

    /**
     * Creates a game state with the specified game board and number of lives.
     *
     * @param gameBoard The game board.
     * @param numLives  The number of lives. If negative, the player has unlimited lives.
     */
    public GameState(final GameBoard gameBoard, final int numLives) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
        this.gameBoardController = new GameBoardController(gameBoard);
        this.gameBoardView = new GameBoardView(gameBoard);
        this.moveStack = new MoveStack();
        this.initialNumOfGems = gameBoard.countGems();
        this.numLives = numLives;
        this.numMoves = 0;
        this.numDeaths = 0;
    }

    /**
     * @return The game board controller.
     */
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    /**
     * @return The game board view.
     */
    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }

    /**
     * @return The game board.
     */
    public GameBoard getGameBoard() {
        return gameBoard;
    }

    /**
     * @return The move stack.
     */
    public MoveStack getMoveStack() {
        return moveStack;
    }

    /**
     * @return The current number of lives, or {@link Integer#MAX_VALUE} if unlimited.
     */
    public int getNumLives() {
        if (hasUnlimitedLives()) {
            return Integer.MAX_VALUE;
        }
        return numLives;
    }

    /**
     * @return {@code true} if the player has unlimited lives.
     */
    public boolean hasUnlimitedLives() {
        return numLives < 0;
    }

    /**
     * @return The number of moves made.
     */
    public int getNumMoves() {
        return numMoves;
    }

    /**
     * @return The number of deaths.
     */
    public int getNumDeaths() {
        return numDeaths;
    }

    /**
     * @return The initial number of gems on the board.
     */
    public int getInitialNumOfGems() {
        return initialNumOfGems;
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
     * Increments the move count by one.
     *
     * @return The new move count.
     */
    public int incrementNumMoves() {
        return ++numMoves;
    }

    /**
     * Increments the death count by one.
     *
     * @return The new death count.
     */
    public int incrementNumDeaths() {
        return ++numDeaths;
    }

    /**
     * Calculates the current score.
     * Score = InitialBoardSize + (CollectedGems * 10) - (Moves * 1) - (Undoes * 2) - (Deaths * 4)
     *
     * @return The current score.
     */
    public int getScore() {
        final int boardSize = gameBoard.getNumRows() * gameBoard.getNumCols();
        final int collectedGems = initialNumOfGems - gameBoard.countGems();
        final int popCount = moveStack.getPopCount();

        return boardSize + (collectedGems * 10) - (numMoves * 1) - (popCount * 2) - (numDeaths * 4);
    }

    /**
     * Checks whether the player has won (all gems collected).
     *
     * @return {@code true} if all gems have been collected.
     */
    public boolean hasWon() {
        return gameBoard.countGems() == 0 && initialNumOfGems > 0;
    }

    /**
     * Checks whether the player has lost (lives reached zero and not unlimited).
     *
     * @return {@code true} if the player has lost.
     */
    public boolean hasLost() {
        return !hasUnlimitedLives() && numLives <= 0;
    }
}
