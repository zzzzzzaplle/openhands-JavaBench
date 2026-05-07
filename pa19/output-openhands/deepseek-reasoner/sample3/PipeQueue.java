import java.util.LinkedList;
import java.util.Random;

/**
 * Queue of upcoming pipes with auto-refill and undo support.
 */
public class PipeQueue {
    private static final int MAX_GEN_LENGTH = 5;
    private static final Random RANDOM = new Random();
    private static final PipeShape[] SHAPES = PipeShape.values();

    private final LinkedList<Pipe> pipeQueue = new LinkedList<>();
    private final int fixedLength;

    public PipeQueue(int fixedLength) {
        this.fixedLength = fixedLength;
        refill();
    }

    /**
     * Creates a queue prepopulated with given pipes.
     */
    public PipeQueue(int fixedLength, java.util.List<Pipe> initialPipes) {
        this.fixedLength = fixedLength;
        pipeQueue.addAll(initialPipes);
        refill();
    }

    public Pipe peek() {
        return pipeQueue.peekFirst();
    }

    public void consume() {
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

    private void refill() {
        while (pipeQueue.size() < fixedLength) {
            pipeQueue.addLast(generateNewPipe());
        }
    }

    private static Pipe generateNewPipe() {
        return new Pipe(SHAPES[RANDOM.nextInt(SHAPES.length)]);
    }

    public void display() {
        System.out.print("Next Pipes:  ");
        for (Pipe p : pipeQueue) {
            System.out.print(p.toSingleChar() + "    ");
        }
        System.out.println();
    }
}
