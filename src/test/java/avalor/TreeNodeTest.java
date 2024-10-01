package avalor;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.LinkedList;

public class TreeNodeTest {

    private int N;
    private int t;
    private int[][] grid;
    private int delay;
    private int[][] directions;
    private TreeNode root;

    @Before
    public void setUp() {
        this.N = 3;
        this.t = 3;
        this.grid = new int[][] {
            {4, 6, 5},
            {8, 4, 3},
            {4, 3, 6}
        };
        this.delay = N*N;
        this.directions = new int[][] { {0, 1}, {1, 0}, {0, -1}, {-1, 0}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1} }; // (v,h)
        this.root = new TreeNode(0, 0, grid[0][0], null);
    }

    @Test
    public void testTreeNodeInit() {
        TreeNode node = new TreeNode(1, 1, 4, root);
        assertEquals(1, node.getX());
        assertEquals(1, node.getY());
        assertEquals(4, node.getScore());
        assertEquals(root, node.getParent());
        assertTrue(node.getChildren().isEmpty());
        assertEquals(1, node.getDepth());
        assertEquals(1, node.getParents().size());
    }

    @Test
    public void testExpand() {
        // Expand from root
        root.expand(this.grid, this.N, this.directions, this.t);

        // Should have 3 children
        assertEquals(3, root.children.size());

        // Check if values and positions correspond to grid
        TreeNode firstChild = root.children.get(0);
        TreeNode secondChild = root.children.get(1);
        TreeNode thirdChild = root.children.get(2);
        
        assertEquals(firstChild.getScore(), grid[firstChild.x][firstChild.y]);
        assertEquals(secondChild.getScore(), grid[secondChild.x][secondChild.y]);
        assertEquals(thirdChild.getScore(), grid[thirdChild.x][thirdChild.y]);
    }

    @Test
    public void testSimulate() {
        double score = root.simulate(this.grid, this.directions, this.N, this.t, this.delay);

        // Random, but should be between min and max possible score (14-25 for t=3)
        assertTrue(score >= 3);
        assertTrue(score <= 50);
    }

    @Test
    public void testBackpropagate() {
        // Backprop fake score
        root.backpropagate(10.0);

        // Check if root updated
        assertEquals(1, root.visits);
        assertEquals(10.0, root.totalScore, 0.0001);
    }

    // @Test
    // public void testGetBestPath() {
    //     // Simulate expanding the root node and then getting the best path
    //     root.expand(grid, 3, directions, 3);

    //     // Perform a backpropagation with mock scores on the children
    //     TreeNode firstChild = root.children.get(0);
    //     TreeNode secondChild = root.children.get(1);
    //     firstChild.backpropagate(5.0);
    //     secondChild.backpropagate(15.0);  // Second child has a higher score

    //     // Get the best path from the root
    //     Result bestPathResult = root.getBestPath(grid, 2);

    //     // Since secondChild has a higher score, we expect the path to include (0,0) -> (1,0) (or (0,1) depending on the directions array)
    //     String bestPathString = bestPathResult.getPath();
    //     assertTrue(bestPathString.contains("(0, 0)") && (bestPathString.contains("(1, 0)") || bestPathString.contains("(0, 1)")));
    // }

    // @Test
    // public void testComputeTotalScore() {
    //     LinkedList<PathTuple> path = new LinkedList<>();
    //     path.add(new PathTuple(0, 0, 1)); // Start at (0,0) with value 1
    //     path.add(new PathTuple(0, 1, 2)); // Move to (0,1) with value 2
    //     path.add(new PathTuple(0, 0, 1)); // Return to (0,0)

    //     // Total score should be: 1 (first visit) + 2 (first visit) + 1/delay*(2-0) (delayed second visit)
    //     double totalScore = root.computeTotalScore(path, grid, 2);

    //     assertEquals(1 + 2 + 0.5, totalScore, 0.0001);
    // }
}
