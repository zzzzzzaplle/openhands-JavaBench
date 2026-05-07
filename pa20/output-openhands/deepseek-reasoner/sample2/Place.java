import java.util.Objects;
import java.util.StringJoiner;

public class Place implements Cloneable {
    private final int x;
    private final int y;

    public Place(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Place place = (Place) o;
        return x == place.x && y == place.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Place.class.getSimpleName() + "[", "]")
                .add("x=" + x)
                .add("y=" + y)
                .toString();
    }

    @Override
    public Place clone() throws CloneNotSupportedException {
        return (Place) super.clone();
    }
}
