package assignment.protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * Concrete implementation of the JesonMor board game.
 * A turn-based, two-player game played on a square grid board of odd size.
 */
public class JesonMor extends Game {

    public JesonMor(Configuration configuration) {
        this.configuration = configuration;
        this.numMoves = 0;
        this.currentPlayer = null;
    }

    @Override
    public Player start() {
        // Initialize board from configuration
        Piece[][] initialBoard = configuration.getInitialBoard();
        int size = configuration.getSize();
        this.board = new Piece[size][size];
        for (int x = 0; x < size; x++) {
            this.board[x] = new Piece[size];
            System.arraycopy(initialBoard[x], 0, this.board[x], 0, size);
        }

        // Set the first player to move
        this.currentPlayer = configuration.getPlayers()[0];
        Player[] players = configuration.getPlayers();

        // Main game loop
        while (true) {
            refreshOutput();

            Move[] availableMoves = getAvailableMoves(currentPlayer);

            if (availableMoves.length == 0) {
                // No moves available - determine winner by score comparison
                Player other = players[0] == currentPlayer ? players[1] : players[0];
                if (currentPlayer.getScore() > other.getScore()) {
                    return currentPlayer;
                } else if (other.getScore() > currentPlayer.getScore()) {
                    return other;
                } else {
                    // Scores are equal, current player wins
                    return currentPlayer;
                }
            }

            Move move = currentPlayer.nextMove(this, availableMoves);

            Piece piece = getPiece(move.getSource());
            movePiece(move);
            updateScore(currentPlayer, piece, move);

            // Check win condition
            Player winner = getWinner(currentPlayer, piece, move);
            if (winner != null) {
                refreshOutput();
                return winner;
            }

            // Switch player and increment move count
            currentPlayer = currentPlayer == players[0] ? players[1] : players[0];
            numMoves++;
        }
    }

    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        // No winner can be declared during the protection phase
        if (numMoves < configuration.getNumMovesProtection()) {
            return null;
        }

        // Condition 1: A Knight leaves the central square
        Place centralPlace = configuration.getCentralPlace();
        if (lastPiece instanceof Knight &&
                lastMove.getSource().equals(centralPlace) &&
                !lastMove.getDestination().equals(centralPlace)) {
            return lastPlayer;
        }

        // Condition 2: Only one player's pieces remain on the board
        Player[] players = configuration.getPlayers();
        int currentPlayerPieces = countPieces(lastPlayer);
        Player otherPlayer = players[0] == lastPlayer ? players[1] : players[0];
        int otherPlayerPieces = countPieces(otherPlayer);

        if (currentPlayerPieces > 0 && otherPlayerPieces == 0) {
            return lastPlayer;
        }
        if (otherPlayerPieces > 0 && currentPlayerPieces == 0) {
            return otherPlayer;
        }

        return null;
    }

    @Override
    public void updateScore(Player player, Piece piece, Move move) {
        int dx = Math.abs(move.getDestination().x() - move.getSource().x());
        int dy = Math.abs(move.getDestination().y() - move.getSource().y());
        player.setScore(player.getScore() + dx + dy);
    }

    @Override
    public void movePiece(Move move) {
        Piece piece = getPiece(move.getSource());
        // Move piece to destination
        board[move.getDestination().x()][move.getDestination().y()] = piece;
        // Clear source square
        board[move.getSource().x()][move.getSource().y()] = null;
    }

    @Override
    public Move[] getAvailableMoves(Player player) {
        int size = configuration.getSize();
        List<Move> validMoves = new ArrayList<>();

        // Build rule pipeline
        int numProtected = configuration.getNumMovesProtection();
        Rule[] rules = new Rule[]{
                new VacantRule(),
                new NilMoveRule(),
                new OutOfBoundaryRule(),
                new OccupiedRule(),
                new FirstNMovesProtectionRule(numProtected),
                new KnightMoveRule(),
                new KnightBlockRule(),
                new ArcherMoveRule()
        };

        // Iterate over all board positions to find pieces belonging to this player
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                if (piece == null || !piece.getPlayer().equals(player)) {
                    continue;
                }

                Place source = new Place(x, y);
                Move[] candidateMoves = piece.getAvailableMoves(this, source);

                for (Move move : candidateMoves) {
                    boolean valid = true;
                    for (Rule rule : rules) {
                        if (!rule.validate(this, move)) {
                            valid = false;
                            break;
                        }
                    }
                    if (valid) {
                        validMoves.add(move);
                    }
                }
            }
        }

        return validMoves.toArray(new Move[0]);
    }

    /**
     * Counts the number of pieces belonging to the given player on the board.
     *
     * @param player the player whose pieces to count
     * @return the number of pieces
     */
    private int countPieces(Player player) {
        int count = 0;
        int size = configuration.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                if (piece != null && piece.getPlayer().equals(player)) {
                    count++;
                }
            }
        }
        return count;
    }
}
