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
        Result result = planner.run();

        // Check length of path and valid score
        assertEquals(planner.path.size(), this.t+1);
        assertTrue(result.getScore() > 0);

        // Verify steps
        PathTuple firstStep = planner.path.get(1);
        assertEquals(1, firstStep.x);
        assertEquals(0, firstStep.y);

        PathTuple secondStep = planner.path.get(2);
        assertEquals(0, secondStep.x);
        assertEquals(1, secondStep.y);
    }

    @Test
    public void testGridRestorationAfterDelay() {
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
        Result result = planner.run();

        assertEquals(27, result.getScore(), 0.0);
    }
}
