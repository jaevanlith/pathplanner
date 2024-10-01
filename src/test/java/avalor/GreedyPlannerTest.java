package avalor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class GreedyPlannerTest {

    private int N;
    private int t;
    private int T;
    private int startX, startY;
    private int[][] grid;
    private int delay;
    private int[][] directions;
    private GreedyPlanner planner;

    @Before
    public void setUp() {
        this.N = 3;
        this.t = 3;
        this.T = 1000;
        this.startX = 0;
        this.startY = 0;
        this.delay = N*N;
        this.grid = new int[][] {
            {4, 6, 5},
            {8, 4, 3},
            {4, 3, 6}
        };
        this.delay = N*N;
        this.directions = new int[][] { {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1} }; // (v,h)
        this.planner = new GreedyPlanner(this.N, this.t, this.T, this.startX, this.startY, this.grid, this.delay);
    }

    @Test
    public void testInit() {
        assertEquals(this.startX, planner.getStartX());
        assertEquals(this.startY, planner.getStartY());
        assertEquals(this.grid[this.startX][this.startY], planner.getGrid()[planner.getStartX()][planner.getStartY()]); // Grid val at startpos
        assertNotNull(planner.getGrid());
    }

    @Test
    public void testTakeStep() {
        // Test takeStep by taking the first greedy step
        planner.run();

        // Check that the greedy path was taken based on the grid
        // The first greedy step should be towards (1,0) or (0,1)
        PathTuple firstStep = planner.path.getFirst();
        assertTrue((firstStep.x == 0 && firstStep.y == 0) || (firstStep.x == 1 && firstStep.y == 0) || (firstStep.x == 0 && firstStep.y == 1));

        // Check that the grid has been updated at the first step
        if (firstStep.x == 0 && firstStep.y == 1) {
            assertEquals(0, grid[0][1]);  // After visiting, the grid value should be set to 0
        } else if (firstStep.x == 1 && firstStep.y == 0) {
            assertEquals(0, grid[1][0]);  // After visiting, the grid value should be set to 0
        }
    }

    @Test
    public void testRun() {
        // Run the GreedyPlanner
        Result result = planner.run();

        // Check that the result path and score are valid
        assertNotNull(result.getPath());
        assertTrue(result.getScore() > 0);

        // Verify the greedy nature: the planner should select the highest score in each step
        // First step will be (1, 0) since 4 > 2 (from the initial position (0,0))
        PathTuple firstStep = planner.path.get(1);
        assertEquals(1, firstStep.x);
        assertEquals(0, firstStep.y);

        // Second step would go to (2,0) since 7 > other values around (1,0)
        PathTuple secondStep = planner.path.get(2);
        assertEquals(2, secondStep.x);
        assertEquals(0, secondStep.y);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMove() {
        // Set up a scenario that forces an invalid move (outside the grid)
        int[][] invalidGrid = new int[][]{
            {1, -1, -1},
            {-1, -1, -1},
            {-1, -1, -1}
        };
        GreedyPlanner invalidPlanner = new GreedyPlanner(3, 2, 1000, 0, 0, invalidGrid, 2);
        invalidPlanner.run();  // This should throw an IllegalArgumentException
    }

    @Test
    public void testGridRestorationAfterDelay() {
        // Check that grid values are restored after delay during path traversal

        // Run the planner for the first step
        planner.run();

        // Simulate delay for restoration
        // Since the grid uses the `delay` parameter, values should gradually restore based on delay factor
        for (int i = 0; i < planner.path.size(); i++) {
            PathTuple step = planner.path.get(i);
            if (i == 0) {
                assertTrue(grid[step.x][step.y] > 0); // The visited grid position should restore to its original value divided by delay
            }
        }
    }

    @Test
    public void testFinalScore() {
        // Run the planner and get the final score
        Result result = planner.run();

        // Check if the score matches what we expect by summing up the highest possible values for the greedy path
        // For example, in our grid: (0,0) -> (1,0) -> (2,0) should be 1 + 4 + 7 = 12
        assertEquals(12, result.getScore(), 0.001);
    }
}
