package avalor;

import java.util.Iterator;
import java.util.LinkedList;

public class GreedyPlanner extends Planner {

    private int currX;
    private int currY;
    private int delay;
    private LinkedList<PathTuple> path;
    private int score;

    private class PathTuple {
        int x, y, originalValue;

        public PathTuple(int x, int y, int originalValue) {
            this.x = x;
            this.y = y;
            this.originalValue = originalValue;
        }
    }

    public GreedyPlanner(int N, int t, long T, int startX, int startY, int[][] grid) {
        super(N, t, T, startX, startY, grid);
        this.currX = startX;
        this.currY = startY;
        this.delay = N;
        this.path = new LinkedList<>();
        this.score = grid[startX][startY];
    }

    // Take a step and update the grid
    private void takeStep(int[] direction) throws IllegalArgumentException {
        int nextX = currX + direction[0];
        int nextY = currY + direction[1];

        // Check if move valid
        if (isValidMove(nextX, nextY)) {
            // Add to path
            this.path.addLast(new PathTuple(currX, currY, grid[currX][currY]));

            this.currX = nextX;
            this.currY = nextY;
            this.score += grid[currX][currY];

            // Set the new position's grid value to 0 (drone visited)
            grid[nextX][nextY] = 0;
        } else {
            throw new IllegalArgumentException("Invalid move");
        }

        // Increment visited cells up to original val
        Iterator<PathTuple> iterator = path.iterator();
        int i = 0;
        while (iterator.hasNext() && i < delay) {
            PathTuple tuple = iterator.next();
            grid[tuple.x][tuple.y] = Math.min(grid[tuple.x][tuple.y] + tuple.originalValue / delay, tuple.originalValue);
            i++;
        }
    }

    private String pathToString() {
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
    

    @Override
    public Result run() {
        // Init current position
        this.currX = startX;
        this.currY = startY;

        // Take steps
        for (int i = 0; i < t; i++) {
            // Greedily select max action
            int[] maxDirection = {0, 0};
            int maxValue = -1;
            for (int[] direction : directions) {
                int nextX = currX + direction[0];
                int nextY = currY + direction[1];
                // Check if valid and exceeding max
                if (isValidMove(nextX, nextY) && grid[nextX][nextY] > maxValue) {
                    maxValue = grid[nextX][nextY];
                    maxDirection = direction;
                }
            }

            // Make move
            takeStep(maxDirection);
        }

        // Return result
        return new Result(pathToString(), this.score);
    }

}
