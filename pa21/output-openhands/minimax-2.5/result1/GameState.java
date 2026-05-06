package game;

/**
 * Game state with lives, score, and win/loss conditions.
 */
public class GameState {
    public static final int UNLIMITED_LIVES = -1;

    private final GameBoard gameBoard;
    private final GameBoardController gameBoardController;
    private final GameBoardView gameBoardView;
    private final MoveStack moveStack;

    private int numDeaths;
    private int numMoves;
    private int numLives;
    private int initialNumOfGems;

    public GameState(GameBoard gameBoard) {
        this(gameBoard, UNLIMITED_LIVES);
    }

    public GameState(GameBoard gameBoard, int numLives) {
        this.gameBoard = gameBoard;
        this.gameBoardController = new GameBoardController(gameBoard);
        this.gameBoardView = new GameBoardView(gameBoard);
        this.moveStack = new MoveStack();

        this.numLives = numLives;
        this.numDeaths = 0;
        this.numMoves = 0;

        // Count initial gems
        this.initialNumOfGems = countGems();
    }

    public GameBoard getGameBoard() {
        return gameBoard;
    }

    public GameBoardController getGameBoardController() {
        return gameBoardController;
    }

    public GameBoardView getGameBoardView() {
        return gameBoardView;
    }

    public MoveStack getMoveStack() {
        return moveStack;
    }

    /**
     * Get current number of lives.
     */
    public int getNumLives() {
        if (numLives < 0) {
            return Integer.MAX_VALUE;
        }
        return numLives;
    }

    /**
     * Check if player has unlimited lives.
     */
    public boolean hasUnlimitedLives() {
        return numLives < 0;
    }

    /**
     * Increase number of lives.
     */
    public int increaseNumLives(int delta) {
        if (numLives < 0) {
            return getNumLives();
        }
        numLives += delta;
        return numLives;
    }

    /**
     * Decrease number of lives.
     */
    public int decreaseNumLives(int delta) {
        if (numLives < 0) {
            return getNumLives();
        }
        numLives -= delta;
        return numLives;
    }

    /**
     * Decrement number of lives.
     */
    public int decrementNumLives() {
        return decreaseNumLives(1);
    }

    public void incrementNumMoves() {
        numMoves++;
    }

    public void incrementNumDeaths() {
        numDeaths++;
    }

    public int getNumDeaths() {
        return numDeaths;
    }

    public int getNumMoves() {
        return numMoves;
    }

    /**
     * Get number of collected gems.
     */
    public int getNumCollectedGems() {
        return initialNumOfGems - countGems();
    }

    /**
     * Count remaining gems on the board.
     */
    private int countGems() {
        int count = 0;
        for (int r = 0; r < gameBoard.getNumRows(); r++) {
            for (int c = 0; c < gameBoard.getNumCols(); c++) {
                Cell cell = gameBoard.getCell(r, c);
                if (cell instanceof EntityCell) {
                    Entity entity = ((EntityCell) cell).getEntity();
                    if (entity instanceof Gem) {
                        count++;
                    }
                }
            }
        }
        return count;
    }

    /**
     * Check if player has won.
     */
    public boolean hasWon() {
        return getNumCollectedGems() >= initialNumOfGems;
    }

    /**
     * Check if player has lost.
     */
    public boolean hasLost() {
        if (hasUnlimitedLives()) {
            return false;
        }
        return numLives <= 0;
    }

    /**
     * Calculate score: InitialBoardSize + (Gems * 10) - (Moves * 1) - (Undoes * 2) - (Deaths * 4)
     */
    public int getScore() {
        int boardSize = gameBoard.getNumRows() * gameBoard.getNumCols();
        int gemsScore = getNumCollectedGems() * 10;
        int movesPenalty = numMoves * 1;
        int undosPenalty = moveStack.getPopCount() * 2;
        int deathsPenalty = numDeaths * 4;

        return boardSize + gemsScore - movesPenalty - undosPenalty - deathsPenalty;
    }
}