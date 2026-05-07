package assignment.protocol;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract class representing the game logic.
 */
public abstract class Game implements Cloneable {
    protected Configuration configuration;
    protected Piece[][] board;
    protected Player currentPlayer;
    protected int numMoves;

    /**
     * Starts the game loop and returns the winner.
     *
     * @return the winning player
     */
    public abstract Player start();

    /**
     * Determines the winner based on the last move and board state.
     *
     * @param lastPlayer the player who made the last move
     * @param lastPiece  the piece that was moved
     * @param lastMove   the last move made
     * @return the winning player, or null if no winner yet
     */
    public abstract Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove);

    /**
     * Updates the player's score based on the piece and move.
     *
     * @param player the player whose score to update
     * @param piece  the piece that was moved
     * @param move   the move that was executed
     */
    public abstract void updateScore(Player player, Piece piece, Move move);

    /**
     * Executes a move on the board by moving the piece from source to destination.
     *
     * @param move the move to execute
     */
    public abstract void movePiece(Move move);

    /**
     * Returns all available moves for the given player, filtered through the rule pipeline.
     *
     * @param player the player whose available moves to fetch
     * @return array of valid moves
     */
    public abstract Move[] getAvailableMoves(Player player);

    /**
     * Returns the piece at the given board coordinates.
     *
     * @param x column index
     * @param y row index
     * @return the piece at the position, or null if empty
     */
    public Piece getPiece(int x, int y) {
        return board[x][y];
    }

    /**
     * Returns the piece at the given place.
     *
     * @param place the place to check
     * @return the piece at the place, or null if empty
     */
    public Piece getPiece(Place place) {
        return board[place.x()][place.y()];
    }

    /**
     * Returns the central place of the board.
     *
     * @return the central place
     */
    public Place getCentralPlace() {
        return configuration.getCentralPlace();
    }

    /**
     * Returns the game configuration.
     *
     * @return the configuration
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Returns the current number of moves made in the game.
     *
     * @return the number of moves
     */
    public int getNumMoves() {
        return numMoves;
    }

    public void refreshOutput() {
        int size = this.configuration.getSize();
        ArrayList<List<String>> contents = new ArrayList<List<String>>();
        for (int row = size - 1; row >= 0; row--) {
            ArrayList<String> rowContent = new ArrayList<String>();
            for (int col = 0; col < size; col++) {
                Piece piece = this.getPiece(col, row);
                if (piece == null) {
                    if (this.getCentralPlace().equals(new Place(col, row))) {
                        rowContent.add("x");
                    } else {
                        rowContent.add(".");
                    }
                } else {
                    Player player = piece.getPlayer();
                    rowContent.add(String.format("%s%c%s",
                            player.getColor(),
                            piece.getLabel(),
                            Color.DEFAULT));
                }
            }
            contents.add(rowContent);
        }
        ArrayList<String> xCoordinates = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            xCoordinates.add(String.valueOf((char) ('a' + i)));
        }
        Collections.reverse(contents);

        // clear screen
        System.out.print("\u001b[2J");
        System.out.flush();

        System.out.println();
        System.out.println("### COMP3021 Programming Assignment 1 ###");
        System.out.println();
        System.out.println("Guide: to move a piece, input the coordinate of source and the destination.");
        System.out.println("For example: a1->b2 means to move the piece at 'a1' to 'b2'");
        if (this.numMoves < this.configuration.getNumMovesProtection()) {
            System.out.println();
            System.out.println("Notice: first " + this.configuration.getNumMovesProtection() + " moves are not allowed to" +
                    " capture pieces or win the game.");
        }
        System.out.println();
        System.out.println("Total Moves: " + this.numMoves);
        // print scores of players
        for (Player player :
                this.configuration.getPlayers()) {
            System.out.printf("%s%s%s score: %d\n", player.getColor(), player.getName(), Color.DEFAULT,
                    player.getScore());
        }
        System.out.println();
        // print the gameboard
        int leftPadding = 8;
        StringBuilder paddingSpaceBuilder = new StringBuilder();
        paddingSpaceBuilder.append(" ".repeat(leftPadding));
        System.out.printf("%s%s\n",
                paddingSpaceBuilder.toString(),
                xCoordinates.parallelStream()
                        .collect(Collectors.joining(" ")));
        StringBuilder borderBuilder = new StringBuilder();
        borderBuilder.append("-".repeat(Math.max(0, contents.get(0).size() * 2 - 1)));
        System.out.printf("%s%s\n",
                paddingSpaceBuilder.toString(),
                borderBuilder.toString());
        for (int row = contents.size() - 1; row >= 0; row--) {
            System.out.printf("%" + (leftPadding - 1) + "d|%s|%d\n",
                    row + 1,
                    contents.get(row).parallelStream().map(Object::toString).collect(Collectors.joining(" ")),
                    row + 1);
        }
        System.out.printf("%s%s\n",
                paddingSpaceBuilder.toString(),
                borderBuilder.toString());
        System.out.printf("%s%s\n",
                paddingSpaceBuilder.toString(),
                xCoordinates.parallelStream()
                        .collect(Collectors.joining(" ")));
        System.out.println();
    }

    @Override
    public Game clone() throws CloneNotSupportedException {
        Game cloned = (Game) super.clone();
        cloned.configuration = this.configuration.clone();
        cloned.board = this.board.clone();
        for (int i = 0; i < this.configuration.getSize(); i++) {
            cloned.board[i] = this.board[i].clone();
            // no need to deep copy pieces
            if (this.configuration.getSize() >= 0)
                System.arraycopy(this.board[i], 0, cloned.board[i], 0, this.configuration.getSize());
        }
        cloned.currentPlayer = currentPlayer == null ? null : currentPlayer.clone();
        return cloned;
    }
}
