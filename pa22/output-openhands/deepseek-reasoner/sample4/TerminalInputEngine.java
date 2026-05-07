import java.io.InputStream;
import java.util.Scanner;

/**
 * Reads user input from the terminal and converts it to game actions.
 * Supports up to two players.
 */
public class TerminalInputEngine implements InputEngine {

    private final Scanner terminalScanner;

    /**
     * Create a terminal input engine reading from the given input stream.
     *
     * @param terminalStream the input stream (typically System.in)
     */
    public TerminalInputEngine(InputStream terminalStream) {
        this.terminalScanner = new Scanner(terminalStream);
    }

    @Override
    public Action fetchAction() {
        if (!terminalScanner.hasNextLine()) {
            return new Exit(-1);
        }
        String input = terminalScanner.nextLine().trim();

        // Check for exit commands
        if (input.equalsIgnoreCase(StringResources.EXIT_COMMAND_TEXT) || input.equalsIgnoreCase("quit")) {
            return new Exit(-1);
        }

        if (input.length() != 1) {
            return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
        }

        char cmd = input.charAt(0);

        // Player 0 commands: W (up), A (left), S (down), D (right), R (undo)
        switch (cmd) {
            case 'W', 'w' -> { return new Up(0); }
            case 'A', 'a' -> { return new Left(0); }
            case 'S', 's' -> { return new Down(0); }
            case 'D', 'd' -> { return new Right(0); }
            case 'R', 'r' -> { return new Undo(0); }
        }

        // Player 1 commands: K (up), H (left), J (down), L (right), U (undo)
        switch (cmd) {
            case 'K', 'k' -> { return new Up(1); }
            case 'H', 'h' -> { return new Left(1); }
            case 'J', 'j' -> { return new Down(1); }
            case 'L', 'l' -> { return new Right(1); }
            case 'U', 'u' -> { return new Undo(1); }
        }

        return new InvalidInput(-1, StringResources.INVALID_INPUT_MESSAGE);
    }
}
