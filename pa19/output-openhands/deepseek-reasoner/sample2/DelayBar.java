/**
 * Tracks the countdown before water starts flowing, and the distance water should travel.
 */
public class DelayBar {

    private final int initialValue;
    private int currentValue;

    public DelayBar(int initialValue) {
        this.initialValue = initialValue;
        this.currentValue = initialValue;
    }

    /**
     * Decrements the current value by 1 each round.
     * Continues past zero so that distance() becomes positive after the delay ends.
     */
    public void countdown() {
        currentValue--;
    }

    /**
     * Returns how far the water should flow.
     * Negative during countdown, positive after delay ends.
     */
    public int distance() {
        return -currentValue;
    }

    /**
     * Displays the delay bar.
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
