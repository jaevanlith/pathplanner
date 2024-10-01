package avalor;

public class PathTuple {
    int x, y, originalValue;

    public PathTuple(int x, int y, int originalValue) {
        this.x = x;
        this.y = y;
        this.originalValue = originalValue;
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getOriginalValue() {
        return originalValue;
    }
}
