import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

/**
 * The main entry point for the text-based Inertia puzzle game.
 * <p>
 * Handles the STDIN loop, parsing user commands and delegating to the game controller.
 */
public class InertiaTextGame {

    /**
     * Runs the Inertia text game.
     *
     * @param args Command-line arguments. First argument is the path to the game board file.
     *             Optional second argument {@code --unicode} enables Unicode output.
     */
    public static void main(final String[] args) {
        if (args.length < 1) {
            System.out.println("Usage: InertiaTextGame <filename> [--unicode]");
            return;
        }

        final Path path = Paths.get(args[0]);
        final boolean useUnicode = args.length > 1 && "--unicode".equals(args[1]);

        try {
            final GameState gameState = GameStateSerializer.loadFrom(path);
            final GameController controller = new GameController(gameState);
            final Scanner scanner = new Scanner(System.in);

            while (true) {
                gameState.getGameBoardView().output(useUnicode);
                System.out.print("> ");

                if (!scanner.hasNextLine()) {
                    break;
                }

                final String command = scanner.nextLine().trim();

                if (command.isEmpty()) {
                    continue;
                }

                final String upperCommand = command.toUpperCase();

                if ("QUIT".equals(upperCommand)) {
                    return;
                }

                if ("UNDO".equals(upperCommand)) {
                    if (controller.processUndo()) {
                        System.out.println("Undo successful.");
                    } else {
                        System.out.println("Nothing to undo.");
                    }
                    continue;
                }

                final Direction direction = parseDirection(upperCommand);
                if (direction == null) {
                    System.out.println("Unknown command: " + command);
                    continue;
                }

                final MoveResult result = controller.processMove(direction);

                if (result instanceof Invalid) {
                    System.out.println("Cannot move in that direction!");
                } else if (result instanceof Dead) {
                    System.out.println("You died!");
                } else if (result instanceof Alive) {
                    final Alive alive = (Alive) result;
                    if (!alive.getCollectedGems().isEmpty()) {
                        System.out.println("Collected " + alive.getCollectedGems().size()
                                + " gem(s)!");
                    }
                    if (!alive.getCollectedExtraLives().isEmpty()) {
                        System.out.println("Collected " + alive.getCollectedExtraLives().size()
                                + " extra life/lives!");
                    }
                }

                if (gameState.hasWon()) {
                    gameState.getGameBoardView().output(useUnicode);
                    System.out.println("You win! Score: " + gameState.getScore());
                    return;
                }

                if (gameState.hasLost()) {
                    gameState.getGameBoardView().output(useUnicode);
                    System.out.println("Game Over! Score: " + gameState.getScore());
                    return;
                }
            }
        } catch (final Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Parses a direction from a command string.
     *
     * @param command The uppercase command string.
     * @return The corresponding Direction, or {@code null} if not recognized.
     */
    private static Direction parseDirection(final String command) {
        return switch (command) {
            case "U", "UP" -> Direction.UP;
            case "D", "DOWN" -> Direction.DOWN;
            case "L", "LEFT" -> Direction.LEFT;
            case "R", "RIGHT" -> Direction.RIGHT;
            default -> null;
        };
    }
}
