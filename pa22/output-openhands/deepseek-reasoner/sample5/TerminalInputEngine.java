import java.io.InputStream;
import java.util.Scanner;

/**
 * Reads user input from a terminal (System.in) and converts it to game actions.
 * Supports two-player controls: WASD+R for player 0, KHJL+U for player 1,
 * and "exit"/"quit" for quitting the game.
 */
public class TerminalInputEngine implements InputEngine {

    private final Scanner scanner;

    /**
     * Create a new terminal input engine reading from the given input stream.
     *
     * @param terminalStream the input stream (typically System.in)
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.scanner = new Scanner(terminalStream);
    }

    @Override
    public Action fetchAction() {
        if (!scanner.hasNextLine()) {
            return new Exit(-1);
        }
        String input = scanner.nextLine().trim().toLowerCase();

        if (input.equals(StringResources.EXIT_COMMAND_TEXT) || input.equals("quit")) {
            return new Exit(-1);
        }

        if (input.length() != 1) {
            return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
        }

        char c = input.charAt(0);
        switch (c) {
            case 'w':
                return new Up(0);
            case 'a':
                return new Left(0);
            case 's':
                return new Down(0);
            case 'd':
                return new Right(0);
            case 'r':
                return new Undo(0);
            case 'k':
                return new Up(1);
            case 'h':
                return new Left(1);
            case 'j':
                return new Down(1);
            case 'l':
                return new Right(1);
            case 'u':
                return new Undo(1);
            default:
                return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
        }
    }
}
