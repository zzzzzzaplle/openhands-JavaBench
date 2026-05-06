import java.util.Objects;

/**
 * Represents the full state of the game, including the board, score, lives, and move history.
 */
public class GameState {

    public static final int UNLIMITED_LIVES = -1;

    private final GameBoard gameBoard;
    private final GameBoardController gameBoardController;
    private final GameBoardView gameBoardView;
    private final MoveStack moveStack;
    private final int initialNumOfGems;

    private int numLives;
    private int numMoves;
    private int numDeaths;

    /**
     * Creates a game state with the given game board and unlimited lives.
     *
     * @param gameBoard The game board.
     */
    public GameState(final GameBoard gameBoard) {
        this(gameBoard, UNLIMITED_LIVES);
    }

    /**
     * Creates a game state with the given game board and initial number of lives.
     *
     * @param gameBoard The game board.
     * @param numLives  The initial number of lives, or UNLIMITED_LIVES for unlimited.
     */
    public GameState(final GameBoard gameBoard, final int numLives) {
        this.gameBoard = Objects.requireNonNull(gameBoard);
        this.numLives = numLives;
        this.numMoves = 0;
        this.numDeaths = 0;
        this.gameBoardController = new GameBoardController(gameBoard);
        this.gameBoardView = new GameBoardView(gameBoard);
        this.moveStack = new MoveStack();
        this.initialNumOfGems = countGems();
    }

    /**
     * Counts the number of gems currently on the board.
     *
     * @return The number of gems.
     */
    private int countGems() {
        int count = 0;
        for (int r = 0; r < gameBoard.getNumRows(); r++) {
            for (int c = 0; c < gameBoard.getNumCols(); c++) {
                final Cell cell = gameBoard.getCell(r, c);
                if (cell instanceof EntityCell entityCell) {
                    if (entityCell.getEntity() instanceof Gem) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Returns whether the player has won (all gems collected).
     *
     * @return True if the player has won.
     */
    public boolean hasWon() {
        return initialNumOfGems > 0 && countGems() == 0;
    }

    /**
     * Returns whether the player has lost (lives reached zero).
     *
     * @return True if the player has lost.
     */
    public boolean hasLost() {
        return numLives == 0;
    }

    /**
     * Returns whether the player has unlimited lives.
     *
     * @return True if lives are unlimited.
     */
    public boolean hasUnlimitedLives() {
        return numLives < 0;
    }

    /**
     * Increases the number of lives by the given delta.
     *
     * @param delta The amount to increase.
     * @return The new number of lives.
     */
    public int increaseNumLives(final int delta) {
        if (numLives >= 0) {
            numLives += delta;
        }
        return getNumLives();
    }

    /**
     * Decreases the number of lives by the given delta.
     *
     * @param delta The amount to decrease.
     * @return The new number of lives.
     */
    public int decreaseNumLives(final int delta) {
        if (numLives >= 0) {
            numLives -= delta;
            if (numLives < 0) {
                numLives = 0;
            }
        }
        return getNumLives();
    }

    /**
     * Decrements the number of lives by 1.
     *
     * @return The new number of lives.
     */
    public int decrementNumLives() {
        return decreaseNumLives(1);
    }

    /**
     * Increments the move counter by 1.
     *
     * @return The new move count.
     */
    public int incrementNumMoves() {
        return ++numMoves;
    }

    /**
     * Increments the death counter by 1.
     *
     * @return The new death count.
     */
    public int incrementNumDeaths() {
        return ++numDeaths;
    }

    /**
     * Returns the number of lives.
     * If lives are unlimited, returns Integer.MAX_VALUE.
     *
     * @return The number of lives.
     */
    public int getNumLives() {
        if (numLives < 0) {
            return Integer.MAX_VALUE;
        }
        return numLives;
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
     * Calculates the current score.
     * Score = InitialBoardSize + (Gems * 10) - (Moves * 1) - (Undoes * 2) - (Deaths * 4)
     *
     * @return The current score.
     */
    public int getScore() {
        final int boardSize = gameBoard.getNumRows() * gameBoard.getNumCols();
        final int gemsCollected = initialNumOfGems - countGems();
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
     * Returns the game board controller.
     *
     * @return The game board controller.
     */
    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    /**
     * Returns the game board view.
     *
     * @return The game board view.
     */
    public GameBoardView getGameBoardView() {
        return gameBoardView;
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
