import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The main runner for the Inertia text-based game.
 * Handles the STDIN loop and parses user commands.
 */
public class InertiaTextGame {

    private final GameController gameController;
    private final boolean useUnicode;

    /**
     * Creates the game with the given controller and display settings.
     *
     * @param gameController The game controller.
     * @param useUnicode     Whether to use Unicode characters for display.
     */
    public InertiaTextGame(final GameController gameController, final boolean useUnicode) {
        this.gameController = gameController;
        this.useUnicode = useUnicode;
    }

    /**
     * Main entry point.
     * Usage: InertiaTextGame <board-file> [--unicode]
     *
     * @param args Command-line arguments.
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.err.println("Usage: InertiaTextGame <board-file> [--unicode]");
            System.exit(1);
        }

        final Path inputFile = Paths.get(args[0]);
        final boolean useUnicode = args.length > 1 && "--unicode".equals(args[1]);

        final GameState gameState;
        try {
            gameState = GameStateSerializer.loadFrom(inputFile);
        } catch (final FileNotFoundException e) {
            System.err.println("File not found: " + inputFile);
            System.exit(1);
            return;
        }

        final GameController gameController = new GameController(gameState);
        final InertiaTextGame game = new InertiaTextGame(gameController, useUnicode);
        game.run();
    }

    /**
     * Runs the main game loop.
     */
    public void run() {
        final Scanner scanner = new Scanner(System.in);

        while (true) {
            // Display the board
            gameController.getGameState().getGameBoardView().output(useUnicode);
            System.out.println();

            // Check win/loss conditions
            if (gameController.getGameState().hasWon()) {
                System.out.println("You won!");
                break;
            }
            if (gameController.getGameState().hasLost()) {
                System.out.println("You lost!");
                break;
            }

            // Read user input
            System.out.print("Enter command (WASD/Undo/Quit): ");
            if (!scanner.hasNextLine()) {
                break;
            }
            final String input = scanner.nextLine().trim();

            if (input.isEmpty()) {
                continue;
            }

            final String command = input.toLowerCase();

            switch (command) {
                case "w":
                case "u":
                case "up":
                    processMove(Direction.UP);
                    break;
                case "s":
                case "d":
                case "down":
                    processMove(Direction.DOWN);
                    break;
                case "a":
                case "l":
                case "left":
                    processMove(Direction.LEFT);
                    break;
                case "right":
                case "r":
                    processMove(Direction.RIGHT);
                    break;
                case "undo":
                    processUndo();
                    break;
                case "quit":
                case "exit":
                    System.out.println("Goodbye!");
                    return;
                default:
                    System.out.println("Unknown command: " + command);
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Processes a move in the given direction and displays the result.
     *
     * @param direction The direction to move.
     */
    private void processMove(final Direction direction) {
        final MoveResult result = gameController.processMove(direction);

        if (result instanceof Invalid) {
            System.out.println("Cannot move in that direction!");
        } else if (result instanceof Dead) {
            System.out.println("You hit a mine! Lost a life.");
            if (gameController.getGameState().hasLost()) {
                System.out.println("No more lives remaining!");
            }
        } else if (result instanceof Alive alive) {
            if (!alive.getCollectedGems().isEmpty()) {
                System.out.println("Collected " + alive.getCollectedGems().size() + " gem(s)!");
            }
            if (!alive.getCollectedExtraLives().isEmpty()) {
                System.out.println("Collected " + alive.getCollectedExtraLives().size()
                        + " extra life(ies)!");
            }
        }
    }

    /**
     * Processes an undo command.
     */
    private void processUndo() {
        if (gameController.processUndo()) {
            System.out.println("Undo successful!");
        } else {
            System.out.println("Nothing to undo!");
        }
    }
}
