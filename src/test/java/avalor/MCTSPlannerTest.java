package avalor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class MCTSPlannerTest {

    private int N;
    private int t;
    private long T;
    private int startX, startY;
    private int[][] grid;
    private int delay;
    private MCTSPlanner planner;

    @Before
    public void setUp() {
        this.N = 3;
        this.t = 3;
        this.T = 1000;
        this.startX = 0;
        this.startY = 0;
        this.delay = N * N;

        this.grid = new int[][]{
            {4, 6, 5},
            {8, 4, 3},
            {4, 3, 6}
        };

        this.planner = new MCTSPlanner(this.N, this.t, this.T, this.startX, this.startY, this.grid, this.delay);
    }

    @Test
    public void testInit() {
        assertEquals(0, planner.getStartX());
        assertEquals(0, planner.getStartY());
        assertNotNull(planner.getGrid());
        assertEquals(4, grid[0][0]);
    }

    @Test
    public void testRun_MCTSBehavior() {
        Result result = planner.run();

        assertNotNull(result);
        assertTrue(result.getPath().length() > 0);

        // Check that the path starts at the initial position (0,0)
        assertTrue(result.getPath().startsWith("(0,0)"));
    }

    @Test
    public void testWithinTime() {
        // Check if the planner completes within the given max time
        long startTime = System.currentTimeMillis();
        planner.run();
        long endTime = System.currentTimeMillis();

        assertTrue((endTime - startTime) <= T);
    }

}
