import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * Main runner for the Inertia puzzle game.
 * <p>
 * Reads game state from a file specified as a command-line argument.
 * Processes user commands from STDIN in a loop until the game ends or the user quits.
 */
public class InertiaTextGame {

    private final GameState gameState;
    private final GameController gameController;
    private final GameBoardView gameBoardView;
    private boolean useUnicode;

    /**
     * Creates a new text-based game from the given game state.
     *
     * @param gameState The game state.
     */
    public InertiaTextGame(final GameState gameState) {
        this.gameState = gameState;
        this.gameController = new GameController(gameState);
        this.gameBoardView = gameState.getGameBoardView();
        this.useUnicode = true;
    }

    /**
     * Sets whether to use Unicode characters for display.
     *
     * @param useUnicode {@code true} to use Unicode, {@code false} for ASCII.
     */
    public void setUseUnicode(final boolean useUnicode) {
        this.useUnicode = useUnicode;
    }

    /**
     * Runs the main game loop.
     */
    public void run() {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                // Check win/lose before each turn
                if (gameState.hasWon()) {
                    System.out.println("Congratulations! You've won!");
                    displayStatus();
                    return;
                }
                if (gameState.hasLost()) {
                    System.out.println("Game Over! You've run out of lives.");
                    displayStatus();
                    return;
                }

                displayBoard();
                displayStatus();
                System.out.print("Enter command (U/D/L/R, Undo, Quit): ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                final String input = scanner.nextLine().trim().toUpperCase();
                if (input.isEmpty()) {
                    continue;
                }

                if ("QUIT".equals(input)) {
                    System.out.println("Thanks for playing!");
                    break;
                }

                if ("UNDO".equals(input)) {
                    final boolean undone = gameController.processUndo();
                    if (undone) {
                        System.out.println("Move undone.");
                    } else {
                        System.out.println("Nothing to undo.");
                    }
                    continue;
                }

                final Direction direction = parseDirection(input);
                if (direction == null) {
                    System.out.println("Unknown command: " + input);
                    continue;
                }

                final MoveResult result = gameController.processMove(direction);
                handleMoveResult(result, direction);
            }
        }
    }

    /**
     * Handles the result of a move, printing appropriate messages.
     *
     * @param result    The move result.
     * @param direction The direction that was attempted.
     */
    private void handleMoveResult(final MoveResult result, final Direction direction) {
        if (result instanceof Invalid) {
            System.out.println("Cannot move " + direction + " - blocked.");
        } else if (result instanceof Dead) {
            System.out.println("Hit a mine! Lost a life.");
        } else if (result instanceof Alive) {
            final Alive alive = (Alive) result;
            if (!alive.getCollectedGems().isEmpty()) {
                System.out.println("Collected " + alive.getCollectedGems().size() + " gem(s)!");
            }
            if (!alive.getCollectedExtraLives().isEmpty()) {
                System.out.println("Collected " + alive.getCollectedExtraLives().size() + " extra life/lives!");
            }
        }
    }

    /**
     * Displays the game board.
     */
    private void displayBoard() {
        gameBoardView.output(useUnicode);
    }

    /**
     * Displays the current game status (score, lives, gems).
     */
    private void displayStatus() {
        System.out.println("Score: " + gameState.getScore()
                + " | Lives: " + (gameState.hasUnlimitedLives() ? "\u221E" : gameState.getNumLives())
                + " | Gems: " + gameState.getCollectedGems() + "/" + gameState.getInitialNumOfGems()
                + " | Moves: " + gameState.getNumMoves()
                + " | Deaths: " + gameState.getNumDeaths());
    }

    /**
     * Parses a direction from the user input string.
     *
     * @param input The uppercase input string.
     * @return The corresponding {@link Direction}, or {@code null} if unknown.
     */
    private static Direction parseDirection(final String input) {
        return switch (input) {
            case "U", "UP" -> Direction.UP;
            case "D", "DOWN" -> Direction.DOWN;
            case "L", "LEFT" -> Direction.LEFT;
            case "R", "RIGHT" -> Direction.RIGHT;
            default -> null;
        };
    }

    /**
     * Main entry point.
     *
     * @param args Command line arguments. First argument is the path to the game board file.
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java InertiaTextGame <board-file>");
            System.exit(1);
        }

        final Path boardFile = Paths.get(args[0]);
        try {
            final GameState gameState = GameStateSerializer.loadFrom(boardFile);
            final InertiaTextGame game = new InertiaTextGame(gameState);
            game.run();
        } catch (final FileNotFoundException e) {
            System.err.println("Board file not found: " + boardFile);
            System.exit(1);
        } catch (final Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
