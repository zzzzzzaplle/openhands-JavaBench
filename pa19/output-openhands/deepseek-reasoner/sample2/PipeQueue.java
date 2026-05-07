import java.util.LinkedList;
import java.util.Random;

/**
 * Manages a queue of pipes that the player draws from.
 */
public class PipeQueue {

    private static final int MAX_GEN_LENGTH = 5;
    private final LinkedList<Pipe> pipeQueue;
    private static final Random RANDOM = new Random();

    public PipeQueue() {
        this.pipeQueue = new LinkedList<>();
        refill();
    }

    public PipeQueue(java.util.List<Pipe> initialPipes) {
        this.pipeQueue = new LinkedList<>();
        if (initialPipes != null) {
            pipeQueue.addAll(initialPipes);
        }
        refill();
    }

    /**
     * Returns the current head pipe without removing it.
     */
    public Pipe peek() {
        refill();
        return pipeQueue.peekFirst();
    }

    /**
     * Consumes (removes) the head pipe from the queue.
     */
    public void consume() {
        refill();
        if (!pipeQueue.isEmpty()) {
            pipeQueue.pollFirst();
        }
        refill();
    }

    /**
     * Inserts a pipe back at the front of the queue (for undo).
     */
    public void undo(Pipe pipe) {
        pipeQueue.addFirst(pipe);
    }

    /**
     * Generates a new random pipe.
     */
    private static Pipe generateNewPipe() {
        PipeShape[] shapes = PipeShape.values();
        return new Pipe(shapes[RANDOM.nextInt(shapes.length)]);
    }

    /**
     * Refills the queue to the maximum length if needed.
     */
    private void refill() {
        while (pipeQueue.size() < MAX_GEN_LENGTH) {
            pipeQueue.addLast(generateNewPipe());
        }
    }

    /**
     * Displays the queue contents.
     */
    public void display() {
        System.out.print("Next Pipes:  ");
        for (Pipe p : pipeQueue) {
            System.out.print(p.toSingleChar() + "    ");
        }
        System.out.println();
    }
}
