package avalor;

import java.util.Iterator;

public class GreedyPlanner extends Planner {

    private double score;

    public GreedyPlanner(int N, int t, long T, int startX, int startY, int[][] grid, int delay) {
        super(N, t, T, startX, startY, grid, delay);
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

    @Override
    public Result run() {
        // Init current position
        this.currX = startX;
        this.currY = startY;

        // Take steps
        for (int i = 0; i < t+1; i++) {
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
        return new Result(Planner.pathToString(this.path), this.score);
    }

}
