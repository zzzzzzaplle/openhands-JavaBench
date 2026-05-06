package game;

/**
 * The game board containing cells and the player.
 */
public class GameBoard {
    private final int numRows;
    private final int numCols;
    private final Cell[][] board;
    private Player player;

    public GameBoard(int numRows, int numCols) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = new Cell[numRows][numCols];
        
        // Initialize all cells as empty entity cells
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                board[r][c] = new EntityCell(new Position(r, c));
            }
        }
    }

    public GameBoard(int numRows, int numCols, Cell[][] board) {
        this.numRows = numRows;
        this.numCols = numCols;
        this.board = board;
        
        // Find the player
        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                if (board[r][c] instanceof EntityCell) {
                    Entity entity = ((EntityCell) board[r][c]).getEntity();
                    if (entity instanceof Player) {
                        this.player = (Player) entity;
                        break;
                    }
                }
            }
        }
    }

    public int getNumRows() {
        return numRows;
    }

    public int getNumCols() {
        return numCols;
    }

    public Cell getCell(int row, int col) {
        if (row < 0 || row >= numRows || col < 0 || col >= numCols) {
            return null;
        }
        return board[row][col];
    }

    public Cell getCell(Position position) {
        return getCell(position.getRow(), position.getCol());
    }

    public Cell[] getRow(int row) {
        return board[row];
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    /**
     * Get the position of the player.
     */
    public Position getPlayerPosition() {
        if (player == null) {
            return null;
        }
        EntityCell owner = player.getOwner();
        return owner != null ? owner.getPosition() : null;
    }

    /**
     * Set an entity at a position.
     */
    public void setEntity(Position position, Entity entity) {
        Cell cell = getCell(position);
        if (cell instanceof EntityCell) {
            ((EntityCell) cell).setEntity(entity);
        }
    }

    /**
     * Remove entity at a position.
     */
    public void removeEntity(Position position) {
        Cell cell = getCell(position);
        if (cell instanceof EntityCell) {
            ((EntityCell) cell).setEntity(null);
        }
    }
}