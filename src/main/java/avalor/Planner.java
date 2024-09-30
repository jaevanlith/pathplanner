package avalor;

public abstract class Planner {

    protected int N;
    protected int t;
    protected long T;
    protected int startX, startY;
    protected int[][] grid;
    protected int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1} }; // (v,h)

    public Planner(int N, int t, long T, int startX, int startY, int[][] grid) {
        this.N = N;
        this.t = t;
        this.T = T;
        this.startX = startX;
        this.startY = startY;
        this.grid = grid;
    }

    // Every subclass should instantiate run method
    public abstract Result run();

    // Check if move is within grid
    protected boolean isValidMove(int x, int y) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    // Print grid
    public void printGrid() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    // Getters
    public int getN() {
        return N;
    }

    public int getT() {
        return t;
    }

    public long getMaxTime() {
        return T;
    }

    public int getStartX() {
        return startX;
    }

    public int getStartY() {
        return startY;
    }

    public int[][] getGrid() {
        return grid;
    }
}
