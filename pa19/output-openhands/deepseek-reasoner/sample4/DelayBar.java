/**
 * Countdown timer that delays water flow for a number of rounds,
 * then yields a positive distance for water expansion.
 */
public class DelayBar {

    private final int initialValue;
    private int currentValue;

    public DelayBar(int initialValue) {
        this.initialValue = initialValue;
        this.currentValue = initialValue;
    }

    /**
     * Decrements the current value by one each round.
     */
    public void countdown() {
        currentValue--;
    }

    /**
     * Returns the effective distance for water flow.
     * Negative during countdown, zero on the turn flow starts, positive thereafter.
     *
     * @return -currentValue
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
