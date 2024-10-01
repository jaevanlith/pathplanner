package avalor;

public class MCTSPlanner extends Planner {

    public MCTSPlanner(int N, int t, long T, int startX, int startY, int[][] grid, int delay) {
        super(N, t, T, startX, startY, grid, delay);
    }

    @Override
    public Result run() {
        long startTime = System.currentTimeMillis();
        long endTime = startTime + T - 100;

        // Init root node
        TreeNode root = new TreeNode(currX, currY, grid[currX][currY], null);

        // Run until out of time
        while (System.currentTimeMillis() < endTime) {
            TreeNode node = root;

            // Selection; get leaf node
            while (!node.isLeaf()) {
                node = node.selectChild();
            }

            // Expansion; search through children if node has been visited
            if (node.visits > 0) {
                node.expand(grid, N, directions, t);
                node = node.selectChild();
            }

            // Simulation; explore directions randomly
            if (node != null) {
                double totalScore = node.simulate(grid, directions, N, t, delay);
                node.backpropagate(totalScore);
            }
        }

        // Return best path
        return root.getBestPath(grid, delay);
    }
}
