import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The main runner for the Inertia puzzle game that handles the STDIN loop
 * and parses user commands.
 */
public class InertiaTextGame {

    private final GameState gameState;
    private final GameController gameController;
    private final boolean useUnicode;

    /**
     * Creates an inertia text game with the specified game state and display preference.
     *
     * @param gameState   The game state.
     * @param useUnicode  Whether to use Unicode characters for display.
     */
    public InertiaTextGame(final GameState gameState, final boolean useUnicode) {
        this.gameState = gameState;
        this.gameController = new GameController(gameState);
        this.useUnicode = useUnicode;
    }

    /**
     * Runs the main game loop, reading commands from STDIN and printing the board.
     */
    public void run() {
        final Scanner scanner = new Scanner(System.in);
        printBoard();

        while (scanner.hasNextLine()) {
            final String input = scanner.nextLine().trim().toUpperCase();

            if ("QUIT".equals(input)) {
                break;
            }

            if ("UNDO".equals(input)) {
                processUndo();
                continue;
            }

            final Direction direction = parseDirection(input);
            if (direction == null) {
                System.out.println("Unknown command: " + input);
                continue;
            }

            processMove(direction);

            if (gameState.hasWon()) {
                System.out.println("You won!");
                printBoard();
                System.out.println("Score: " + gameState.getScore());
                break;
            }

            if (gameState.hasLost()) {
                System.out.println("You lost!");
                printBoard();
                System.out.println("Score: " + gameState.getScore());
                break;
            }
        }
    }

    /**
     * Processes a move in the specified direction and prints the result.
     *
     * @param direction The direction to move.
     */
    private void processMove(final Direction direction) {
        final MoveResult result = gameController.processMove(direction);

        if (result instanceof MoveResult.Invalid) {
            System.out.println("Cannot move in that direction");
            return;
        }

        printBoard();

        if (result instanceof MoveResult.Valid.Alive) {
            final MoveResult.Valid.Alive alive = (MoveResult.Valid.Alive) result;
            if (!alive.getCollectedGems().isEmpty()) {
                System.out.println("You found " + alive.getCollectedGems().size() + " gem(s)!");
            }
            if (!alive.getCollectedExtraLives().isEmpty()) {
                System.out.println("You found " + alive.getCollectedExtraLives().size()
                        + " extra life/lives!");
            }
        } else if (result instanceof MoveResult.Valid.Dead) {
            System.out.println("You died!");
        }
    }

    /**
     * Processes an undo command.
     */
    private void processUndo() {
        if (gameController.processUndo()) {
            printBoard();
            System.out.println("Undo successful");
        } else {
            System.out.println("Nothing to undo");
        }
    }

    /**
     * Prints the current game board.
     */
    private void printBoard() {
        gameState.getGameBoardView().output(useUnicode);
    }

    /**
     * Parses a direction from the user input string.
     *
     * @param input The input string.
     * @return The corresponding Direction, or {@code null} if not recognized.
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
     * Entry point for the Inertia text game.
     *
     * @param args Command line arguments:
     *             <ol>
     *               <li>Path to the board file (required)</li>
     *               <li>"true" or "false" for Unicode output (optional, default: false)</li>
     *             </ol>
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: java InertiaTextGame <board-file> [useUnicode]");
            System.exit(1);
        }

        final Path boardFile = Paths.get(args[0]);
        final boolean useUnicode = args.length > 1 && Boolean.parseBoolean(args[1]);

        try {
            final GameState gameState = GameStateSerializer.loadFrom(boardFile);
            final InertiaTextGame game = new InertiaTextGame(gameState, useUnicode);
            game.run();
        } catch (final FileNotFoundException e) {
            System.err.println("Board file not found: " + boardFile);
            System.exit(1);
        } catch (final Exception e) {
            System.err.println("Error loading game: " + e.getMessage());
            System.exit(1);
        }
    }
}
