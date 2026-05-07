import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * A queue of pipes with auto-refill. The player can peek, consume, and undo pipes.
 */
public class PipeQueue implements Iterable<Pipe> {

    private static final int MAX_GEN_LENGTH = 5;
    private static final Random RANDOM = new Random();

    private final LinkedList<Pipe> pipeQueue;

    public PipeQueue() {
        this.pipeQueue = new LinkedList<>();
        refill();
    }

    public PipeQueue(List<Pipe> initialPipes) {
        this.pipeQueue = new LinkedList<>();
        if (initialPipes != null) {
            pipeQueue.addAll(initialPipes);
        }
        refill();
    }

    /**
     * Returns the head pipe without removing it.
     *
     * @return the head pipe, or null if queue is empty
     */
    public Pipe peek() {
        return pipeQueue.peek();
    }

    /**
     * Consumes (removes) the head pipe and refills to maintain length.
     */
    public void consume() {
        if (!pipeQueue.isEmpty()) {
            pipeQueue.poll();
        }
        refill();
    }

    /**
     * Inserts a pipe back at the front of the queue (undo).
     *
     * @param pipe the pipe to restore
     */
    public void undo(Pipe pipe) {
        pipeQueue.addFirst(pipe);
    }

    /**
     * Refills the queue to {@link #MAX_GEN_LENGTH} with random pipes.
     */
    private void refill() {
        while (pipeQueue.size() < MAX_GEN_LENGTH) {
            pipeQueue.addLast(generateNewPipe());
        }
    }

    /**
     * Generates a random pipe.
     *
     * @return a new random pipe
     */
    private static Pipe generateNewPipe() {
        PipeShape[] shapes = PipeShape.values();
        PipeShape shape = shapes[RANDOM.nextInt(shapes.length)];
        return new Pipe(shape);
    }

    @Override
    public Iterator<Pipe> iterator() {
        return pipeQueue.iterator();
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
