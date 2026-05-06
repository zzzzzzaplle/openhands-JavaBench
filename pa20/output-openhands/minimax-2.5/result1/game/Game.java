package game;

import rule.Rule;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Abstract base class for the JesonMor game.
 */
public abstract class Game {
    protected Configuration configuration;
    protected Piece[][] board;
    protected Player currentPlayer;
    protected int numMoves;
    protected List<Rule> rules;

    /**
     * Get the configuration.
     */
    public Configuration getConfiguration() {
        return configuration;
    }

    /**
     * Get the piece at a given place.
     */
    public Piece getPiece(Place place) {
        if (place == null) return null;
        return board[place.x()][place.y()];
    }

    /**
     * Set the piece at a given place.
     */
    public void setPiece(Place place, Piece piece) {
        board[place.x()][place.y()] = piece;
    }

    /**
     * Get the current player.
     */
    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Set the current player.
     */
    public void setCurrentPlayer(Player player) {
        this.currentPlayer = player;
    }

    /**
     * Get the number of moves made so far.
     */
    public int getNumMoves() {
        return numMoves;
    }

    /**
     * Get the central place on the board.
     */
    public Place getCentralPlace() {
        return configuration.getCentralPlace();
    }

    /**
     * Initialize the game with the given configuration.
     */
    protected void initialize(Configuration configuration) {
        this.configuration = configuration;
        this.numMoves = 0;
        
        // Copy the initial board
        int size = configuration.getSize();
        this.board = new Piece[size][];
        for (int i = 0; i < size; i++) {
            this.board[i] = configuration.getInitialBoard()[i].clone();
        }
        
        // Set first player
        this.currentPlayer = configuration.getPlayers()[0];
    }

    /**
     * Start the game and return the winner.
     */
    public abstract Player start();

    /**
     * Get the winner after a move.
     */
    public abstract Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove);

    /**
     * Update the score for a player after a move.
     */
    public abstract void updateScore(Player player, Piece piece, Move move);

    /**
     * Execute a move on the board.
     */
    public abstract void movePiece(Move move);

    /**
     * Get all available moves for a player.
     */
    public abstract Move[] getAvailableMoves(Player player);

    /**
     * Validate a move against all rules.
     */
    protected boolean isValidMove(Move move) {
        for (Rule rule : rules) {
            if (!rule.validate(this, move)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Get the first violated rule for a move.
     */
    protected Rule getViolatedRule(Move move) {
        for (Rule rule : rules) {
            if (!rule.validate(this, move)) {
                return rule;
            }
        }
        return null;
    }

    /**
     * Get all moves for a player from all their pieces.
     */
    protected Move[] getAllMovesForPlayer(Player player) {
        List<Move> allMoves = new ArrayList<>();
        int size = configuration.getSize();
        
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Place place = new Place(x, y);
                Piece piece = board[x][y];
                
                if (piece != null && piece.getPlayer() == player) {
                    Move[] pieceMoves = piece.getAvailableMoves(this, place);
                    for (Move move : pieceMoves) {
                        // Only add if the move passes all rules
                        if (isValidMove(move)) {
                            allMoves.add(move);
                        }
                    }
                }
            }
        }
        
        return allMoves.toArray(new Move[0]);
    }

    /**
     * Refresh the console output.
     */
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

    /**
     * Get piece at specific coordinates.
     */
    public Piece getPiece(int x, int y) {
        if (x < 0 || x >= board.length || y < 0 || y >= board.length) {
            return null;
        }
        return board[x][y];
    }

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