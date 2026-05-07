import java.util.ArrayList;
import java.util.List;

public class JesonMor extends Game {

    public JesonMor(Configuration configuration) {
        this.configuration = configuration;
        int size = configuration.getSize();
        this.board = new Piece[size][size];
        Piece[][] initialBoard = configuration.getInitialBoard();
        for (int x = 0; x < size; x++) {
            System.arraycopy(initialBoard[x], 0, this.board[x], 0, size);
        }
        this.currentPlayer = null;
        this.numMoves = 0;
    }

    @Override
    public Player start() {
        Player[] players = configuration.getPlayers();
        currentPlayer = players[0];

        while (true) {
            refreshOutput();

            Move[] availableMoves = getAvailableMoves(currentPlayer);

            if (availableMoves.length == 0) {
                refreshOutput();
                return determineWinnerByScore();
            }

            Move move = currentPlayer.nextMove(this, availableMoves);

            Piece movingPiece = getPiece(move.getSource());

            movePiece(move);
            updateScore(currentPlayer, movingPiece, move);
            numMoves++;

            if (numMoves >= configuration.getNumMovesProtection()) {
                Player winner = getWinner(currentPlayer, movingPiece, move);
                if (winner != null) {
                    refreshOutput();
                    return winner;
                }
            }

            // Switch to the next player
            currentPlayer = (currentPlayer.equals(players[0])) ? players[1] : players[0];
        }
    }

    private Player determineWinnerByScore() {
        Player[] players = configuration.getPlayers();
        if (players[0].getScore() > players[1].getScore()) {
            return players[0];
        } else if (players[1].getScore() > players[0].getScore()) {
            return players[1];
        } else {
            return currentPlayer;
        }
    }

    @Override
    public Player getWinner(Player lastPlayer, Piece lastPiece, Move lastMove) {
        // During protection, no winner is declared
        if (numMoves < configuration.getNumMovesProtection()) {
            return null;
        }

        // Win condition 1: Knight leaves the central square
        Place centralPlace = configuration.getCentralPlace();
        if (lastPiece instanceof Knight
                && lastMove.getSource().equals(centralPlace)
                && !lastMove.getDestination().equals(centralPlace)) {
            return lastPlayer;
        }

        // Win condition 2: Only one player's pieces remain on the board
        Player[] players = configuration.getPlayers();
        boolean foundPlayer0 = false;
        boolean foundPlayer1 = false;
        int size = configuration.getSize();

        for (int x = 0; x < size; x++) {
            for (int y = 0; y < size; y++) {
                Piece piece = board[x][y];
                if (piece != null) {
                    if (piece.getPlayer().equals(players[0])) {
                        foundPlayer0 = true;
                    } else {
                        foundPlayer1 = true;
                    }
                }
            }
        }

        if (foundPlayer0 && !foundPlayer1) {
            return players[0];
        }
        if (!foundPlayer0 && foundPlayer1) {
            return players[1];
        }

        return null;
    }

    @Override
    public void updateScore(Player player, Piece piece, Move move) {
        int dx = Math.abs(move.getSource().x() - move.getDestination().x());
        int dy = Math.abs(move.getSource().y() - move.getDestination().y());
        int distance = dx + dy;
        player.setScore(player.getScore() + distance);
    }

    @Override
    public void movePiece(Move move) {
        Place source = move.getSource();
        Place dest = move.getDestination();
        board[dest.x()][dest.y()] = board[source.x()][source.y()];
        board[source.x()][source.y()] = null;
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
                    Move[] pieceMoves = piece.getAvailableMoves(this, source);
                    if (pieceMoves != null) {
                        for (Move move : pieceMoves) {
                            allMoves.add(move);
                        }
                    }
                }
            }
        }

        return allMoves.toArray(new Move[0]);
    }
}
