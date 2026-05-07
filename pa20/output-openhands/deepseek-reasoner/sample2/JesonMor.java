import java.util.ArrayList;
import java.util.List;

public class JesonMor extends Game {

    public JesonMor(Configuration configuration) {
        this.configuration = configuration;
        this.numMoves = 0;

        // Copy initial board
        int size = configuration.getSize();
        Piece[][] initialBoard = configuration.getInitialBoard();
        this.board = new Piece[size][size];
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                this.board[x][y] = initialBoard[x][y];
            }
        }

        // First player starts
        this.currentPlayer = configuration.getPlayers()[0];
    }

    @Override
    public Player start() {
        while (true) {
            refreshOutput();

            Move[] availableMoves = getAvailableMoves(currentPlayer);

            // If no available moves, determine winner by score comparison
            if (availableMoves == null || availableMoves.length == 0) {
                System.out.println(currentPlayer.getColor() + currentPlayer.getName()
                        + Color.DEFAULT + " has no available moves!");

                Player[] players = configuration.getPlayers();
                Player otherPlayer = players[0] == currentPlayer ? players[1] : players[0];

                if (currentPlayer.getScore() > otherPlayer.getScore()) {
                    return currentPlayer;
                } else if (otherPlayer.getScore() > currentPlayer.getScore()) {
                    return otherPlayer;
                } else {
                    // Scores equal: current player wins
                    return currentPlayer;
                }
            }

            Move move = currentPlayer.nextMove(this, availableMoves);
            if (move == null) {
                // Player resigned or no valid input
                Player[] players = configuration.getPlayers();
                return players[0] == currentPlayer ? players[1] : players[0];
            }

            Piece piece = getPiece(move.getSource());
            movePiece(move);
            numMoves++;
            updateScore(currentPlayer, piece, move);

            // Only check win conditions after protection phase
            if (numMoves > configuration.getNumMovesProtection()) {
                Player winner = getWinner(currentPlayer, piece, move);
                if (winner != null) {
                    refreshOutput();
                    return winner;
                }
            }

            // Switch player
            Player[] players = configuration.getPlayers();
            currentPlayer = (currentPlayer == players[0]) ? players[1] : players[0];
        }
    }

    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        Place centralPlace = configuration.getCentralPlace();

        // Condition 1: A Knight leaves the central square
        if (lastPiece instanceof Knight
                && lastMove.getSource().equals(centralPlace)
                && !lastMove.getDestination().equals(centralPlace)) {
            return lastPlayer;
        }

        // Condition 2: Only one player's pieces remain
        Player[] players = configuration.getPlayers();
        boolean player0HasPieces = false;
        boolean player1HasPieces = false;

        int size = configuration.getSize();
        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece p = board[x][y];
                if (p != null) {
                    if (p.getPlayer().equals(players[0])) {
                        player0HasPieces = true;
                    } else {
                        player1HasPieces = true;
                    }
                }
            }
        }

        if (player0HasPieces && !player1HasPieces) {
            return players[0];
        }
        if (player1HasPieces && !player0HasPieces) {
            return players[1];
        }

        return null;
    }

    @Override
    public void updateScore(Player player, Piece piece, Move move) {
        Place src = move.getSource();
        Place dst = move.getDestination();
        int distance = Math.abs(src.x() - dst.x()) + Math.abs(src.y() - dst.y());
        player.setScore(player.getScore() + distance);
    }

    @Override
    public void movePiece(Move move) {
        Place src = move.getSource();
        Place dst = move.getDestination();
        board[dst.x()][dst.y()] = board[src.x()][src.y()];
        board[src.x()][src.y()] = null;
    }

    @Override
    public Move[] getAvailableMoves(Player player) {
        List<Move> allMoves = new ArrayList<>();
        int size = configuration.getSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                if (piece != null && piece.getPlayer().equals(player)) {
                    Place source = new Place(x, y);
                    Move[] moves = piece.getAvailableMoves(this, source);
                    if (moves != null) {
                        // Apply protection rule if in protection phase
                        for (Move move : moves) {
                            if (isMoveValidForPlayer(player, move)) {
                                allMoves.add(move);
                            }
                        }
                    }
                }
            }
        }

        return allMoves.toArray(new Move[0]);
    }

    /**
     * Check if a move is valid considering the protection phase.
     * During protection, capture moves are filtered out.
     */
    private boolean isMoveValidForPlayer(Player player, Move move) {
        // Apply protection rule
        if (numMoves < configuration.getNumMovesProtection()) {
            FirstNMovesProtectionRule protectionRule =
                    new FirstNMovesProtectionRule(configuration.getNumMovesProtection());
            if (!protectionRule.validate(this, move)) {
                return false;
            }
        }
        return true;
    }
}
