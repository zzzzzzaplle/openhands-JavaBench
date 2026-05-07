import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The main text-based runner for the Inertia puzzle game.
 */
public class InertiaTextGame {

    private final GameController gameController;
    private final boolean useUnicode;

    /**
     * Creates a new text game wrapping the given controller.
     *
     * @param gameController The game controller.
     * @param useUnicode     Whether to use Unicode characters for board display.
     */
    public InertiaTextGame(final GameController gameController, final boolean useUnicode) {
        this.gameController = gameController;
        this.useUnicode = useUnicode;
    }

    /**
     * Runs the main game loop.
     */
    public void run() {
        final Scanner scanner = new Scanner(System.in);
        final GameState gameState = gameController.getGameState();

        displayBoard();

        while (true) {
            if (!scanner.hasNextLine()) {
                break;
            }

            final String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Quit")) {
                System.out.println("Bye!");
                break;
            }

            if (input.equalsIgnoreCase("Undo")) {
                if (gameController.processUndo()) {
                    System.out.println("Undo");
                    displayBoard();
                } else {
                    System.out.println("Nothing to undo.");
                }
                continue;
            }

            final Direction direction = parseDirection(input);
            if (direction == null) {
                System.out.println("Unknown command.");
                continue;
            }

            final MoveResult result = gameController.processMove(direction);

            if (result instanceof Alive) {
                System.out.println("Alive!");
                displayBoard();
            } else if (result instanceof Dead) {
                System.out.println("Dead!");
                displayLives(gameState);
                if (gameState.hasLost()) {
                    System.out.println("Game Over!");
                    displayScore(gameState);
                    break;
                }
                displayBoard();
            } else if (result instanceof Invalid) {
                System.out.println("Invalid!");
            }

            if (gameState.hasWon()) {
                System.out.println("You Win!");
                displayScore(gameState);
                break;
            }
        }
    }

    private void displayBoard() {
        gameController.getGameState().getGameBoardView().output(useUnicode);
    }

    private void displayLives(final GameState gameState) {
        if (gameState.hasUnlimitedLives()) {
            System.out.println("Lives: Unlimited");
        } else {
            System.out.println("Lives: " + gameState.getNumLives());
        }
    }

    private void displayScore(final GameState gameState) {
        System.out.println("Score: " + gameState.getScore());
    }

    private static Direction parseDirection(final String input) {
        return switch (input.toUpperCase()) {
            case "U" -> Direction.UP;
            case "D" -> Direction.DOWN;
            case "L" -> Direction.LEFT;
            case "R" -> Direction.RIGHT;
            default -> null;
        };
    }

    /**
     * Main entry point for the Inertia text game.
     *
     * @param args Command-line arguments. First argument is the path to a game board file.
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: java InertiaTextGame <board-file>");
            return;
        }

        final Path inputFile = Paths.get(args[0]);
        final boolean useUnicode = args.length > 1 && args[1].equalsIgnoreCase("unicode");

        try {
            final GameState gameState = GameStateSerializer.loadFrom(inputFile);
            final GameController gameController = new GameController(gameState);
            final InertiaTextGame game = new InertiaTextGame(gameController, useUnicode);
            game.run();
        } catch (final FileNotFoundException e) {
            System.out.println("File not found: " + inputFile);
        } catch (final Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
