package avalor;

import java.util.Iterator;
import java.util.LinkedList;

public abstract class Planner {

    protected int N;
    protected int t;
    protected long T;
    protected int startX, startY;
    protected int currX, currY;
    protected int delay;
    protected int[][] grid;
    protected LinkedList<PathTuple> path;
    protected int[][] directions = { {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1} }; // (v,h)

    public Planner(int N, int t, long T, int startX, int startY, int[][] grid, int delay) {
        this.N = N;
        this.t = t;
        this.T = T;
        this.startX = startX;
        this.startY = startY;
        this.currX = startX;
        this.currY = startY;
        this.grid = grid;
        this.delay = delay;
        this.path = new LinkedList<>();
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

    public static String pathToString(LinkedList<PathTuple> path) {
        StringBuilder pathStr = new StringBuilder();
        Iterator<PathTuple> iterator = path.iterator();
    
        // Compose string (x,y)--...--(x,y)
        while (iterator.hasNext()) {
            PathTuple tuple = iterator.next();
            pathStr.append("(").append(tuple.x).append(",").append(tuple.y).append(")");
            if (iterator.hasNext()) {
                pathStr.append("--"); // Add separator between path elements
            }
        }
    
        return pathStr.toString();
    }

    // Getters
    public int getN() {
        return N;
    }

    public int getSteps() {
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
