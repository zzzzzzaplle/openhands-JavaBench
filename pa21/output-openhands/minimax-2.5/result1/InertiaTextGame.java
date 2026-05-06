package game;

import java.util.Scanner;

/**
 * Main runner for the Inertia game - handles STDIN loop and user commands.
 */
public class InertiaTextGame {
    private final GameController controller;
    private final GameState gameState;
    private final Scanner scanner;
    private boolean running;

    public InertiaTextGame(GameState gameState) {
        this.controller = new GameController(gameState);
        this.gameState = gameState;
        this.scanner = new Scanner(System.in);
        this.running = true;
    }

    /**
     * Run the game loop.
     */
    public void run() {
        while (running) {
            // Display board
            gameState.getGameBoardView().output(false);
            
            // Check win/loss
            if (gameState.hasWon()) {
                System.out.println("You win! Score: " + gameState.getScore());
                break;
            }
            if (gameState.hasLost()) {
                System.out.println("You lose! Score: " + gameState.getScore());
                break;
            }

            // Display status
            System.out.println();
            System.out.print("Score: " + gameState.getScore());
            System.out.print(" | Moves: " + gameState.getNumMoves());
            System.out.print(" | Lives: " + gameState.getNumLives());
            System.out.print(" | Gems: " + gameState.getNumCollectedGems());
            System.out.print(" | Undos available: " + gameState.getMoveStack().availableUndos());
            System.out.println();

            // Get input
            System.out.print("Enter move (U=up, D=down, L=left, R=right, Undo, Quit): ");
            String input = scanner.nextLine().trim().toLowerCase();

            // Process command
            if (input.equals("u")) {
                processMove(Direction.UP);
            } else if (input.equals("d")) {
                processMove(Direction.DOWN);
            } else if (input.equals("l")) {
                processMove(Direction.LEFT);
            } else if (input.equals("r")) {
                processMove(Direction.RIGHT);
            } else if (input.equals("undo")) {
                processUndo();
            } else if (input.equals("quit") || input.equals("q")) {
                System.out.println("Final Score: " + gameState.getScore());
                running = false;
            } else {
                System.out.println("Invalid command. Use U, D, L, R, Undo, or Quit.");
            }
        }
    }

    private void processMove(Direction direction) {
        MoveResult result = controller.processMove(direction);
        
        if (result instanceof MoveResult.Valid.Alive) {
            // Move successful
            int gemsCollected = ((MoveResult.Valid.Alive) result).getCollectedGems().size();
            int livesCollected = ((MoveResult.Valid.Alive) result).getCollectedExtraLives().size();
            if (gemsCollected > 0 || livesCollected > 0) {
                System.out.println("Collected " + gemsCollected + " gem(s) and " + livesCollected + " life/lives!");
            }
        } else if (result instanceof MoveResult.Valid.Dead) {
            System.out.println("You hit a mine! Lives lost.");
        } else if (result instanceof MoveResult.Invalid) {
            System.out.println("Cannot move in that direction.");
        }
    }

    private void processUndo() {
        if (controller.processUndo()) {
            System.out.println("Undo successful.");
        } else {
            System.out.println("No moves to undo.");
        }
    }

    /**
     * Main entry point.
     */
    public static void main(String[] args) {
        // Simple test board - in real usage, would load from file
        // For now, create a simple board programmatically
        GameBoard board = new GameBoard(5, 5);
        
        // Add walls around the border
        for (int r = 0; r < 5; r++) {
            for (int c = 0; c < 5; c++) {
                if (r == 0 || r == 4 || c == 0 || c == 4) {
                    board.setEntity(new Position(r, c), new Wall(new Position(r, c)));
                }
            }
        }
        
        // Add player at center
        Player player = new Player();
        board.setEntity(new Position(2, 2), player);
        
        // Add some gems
        board.setEntity(new Position(1, 1), new Gem());
        board.setEntity(new Position(1, 3), new Gem());
        
        // Add a stop cell
        board.setEntity(new Position(3, 2), new StopCell(new Position(3, 2)));
        
        // Create game state
        GameState gameState = new GameState(board, 3);
        
        // Run game
        InertiaTextGame game = new InertiaTextGame(gameState);
        game.run();
    }
}