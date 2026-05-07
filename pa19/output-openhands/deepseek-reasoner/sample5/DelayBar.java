/**
 * Countdown timer that determines how many rounds until water starts flowing.
 * After the countdown reaches zero, distance() becomes positive.
 */
public class DelayBar {

    private final int initialValue;
    private int currentValue;

    public DelayBar(int initialValue) {
        this.initialValue = initialValue;
        this.currentValue = initialValue;
    }

    /**
     * Decrements the current value by 1 each round (cannot go below 0).
     */
    public void countdown() {
        if (currentValue > 0) {
            currentValue--;
        }
    }

    /**
     * Returns how far the water should flow.
     * Negative during countdown, 0 when countdown just finished, positive after.
     *
     * @return Current distance value
     */
    public int distance() {
        return -currentValue;
    }

    /**
     * Displays the delay bar showing remaining rounds.
     */
    public void display() {
        if (currentValue > 0) {
            System.out.print("Rounds Countdown: ");
            System.out.print(StringUtils.createPadding(currentValue, '='));
            System.out.print(StringUtils.createPadding(initialValue - currentValue, ' '));
            System.out.print(" " + currentValue);
            System.out.println();
        } else {
            System.out.println();
        }
    }
}
