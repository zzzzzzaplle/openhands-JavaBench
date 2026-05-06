package game;

/**
 * A read-only view of a game board.
 */
public class GameBoardView {
    private final GameBoard gameBoard;

    public GameBoardView(GameBoard gameBoard) {
        this.gameBoard = gameBoard;
    }

    /**
     * Output the board to System.out.
     */
    public void output(boolean useUnicodeChars) {
        for (int r = 0; r < gameBoard.getNumRows(); r++) {
            for (int c = 0; c < gameBoard.getNumCols(); c++) {
                Cell cell = gameBoard.getCell(r, c);
                char ch = useUnicodeChars ? cell.toUnicodeChar() : cell.toASCIIChar();
                System.out.print(ch);
            }
            System.out.println();
        }
    }
}