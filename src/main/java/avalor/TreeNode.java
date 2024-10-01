package avalor;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Inspired from this code: 
 * https://mcts.ai/code/java.html
 * 
 * */

public class TreeNode {
    public int x, y;
    public int score;
    public TreeNode parent;
    public List<TreeNode> children;
    public int visits;
    public double totalScore;
    public int depth;
    public LinkedList<TreeNode> parents;
    static double epsilon = 1e-6;

    public TreeNode(int x, int y, int score, TreeNode parent) {
        this.x = x;
        this.y = y;
        this.score = score;
        this.parent = parent;
        this.children = new ArrayList<>();
        this.visits = 0;
        this.totalScore = 0.0;
        // Keep track of depth
        this.depth = parent == null ? 0 : parent.depth + 1;
        // Copy parent's parents + parent itself
        if (parent == null) {
            this.parents = new LinkedList<>();
        } else {
            this.parents = new LinkedList<>(parent.parents);
            this.parents.add(parent);
        }
    }

    // Select a child node based on UCT (Upper Confidence Bound for Trees)
    public TreeNode selectChild() {
        TreeNode selected = null;
        double bestValue = Double.MIN_VALUE;
        for (TreeNode child : children) {
            Random random = new Random();
            double uctValue = child.totalScore / (child.visits + epsilon) 
                                + Math.sqrt(Math.log(this.visits + 1) / (child.visits + epsilon)) 
                                + random.nextDouble() * epsilon;
                                // small random number to break ties randomly in unexpanded nodes
            if (uctValue > bestValue) {
                selected = child;
                bestValue = uctValue;
            }
        }
        return selected;
    }

    // Expand by adding new child nodes
    public void expand(int[][] grid, int N, int[][] directions, int tSteps) {
        // Don't expand if at max depth
        if (this.depth == tSteps) {
            return;
        }
        
        // Find valid moves
        List<int[]> validMoves = new ArrayList<>();
        for (int[] direction : directions) {
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            if (isValidMove(nextX, nextY, N)) {
                validMoves.add(new int[]{nextX, nextY});
            }
        }
        // Expand by adding new child nodes into all possible directions
        for (int[] move : validMoves) {
            TreeNode child = new TreeNode(move[0], move[1], grid[move[0]][move[1]], this);
            children.add(child);
        }
    }

    // Simulate a random play from the current node
    public double simulate(int[][] grid, int[][] directions, int N, int tSteps, int delay) {
        Random random = new Random();
        int x = this.x;
        int y = this.y;
        LinkedList<PathTuple> path = new LinkedList<>();

        // Add all parents to path
        if (!parents.isEmpty()) {
            for (TreeNode parent : parents) {
                path.addLast(new PathTuple(parent.x, parent.y, parent.score));
            }
        }

        // Random exploration up to depth tSteps
        int i = this.depth;
        while(i < tSteps) {
            // Randomly select a direction
            int[] direction = directions[random.nextInt(directions.length)];
            int nextX = x + direction[0];
            int nextY = y + direction[1];
            // Check if move is valid
            if (isValidMove(nextX, nextY, N)) {
                // Move and add to path
                x = nextX;
                y = nextY;
                path.addLast(new PathTuple(x, y, grid[x][y]));
                i++;
            }
        }

        double totalScore = computeTotalScore(path, grid, delay);
        return totalScore;
    }
    
    // Backpropagate the result
    public void backpropagate(double totalScore) {
        TreeNode currentNode = this;
        while (currentNode != null) {
            currentNode.visits++;
            currentNode.totalScore += totalScore;
            currentNode = currentNode.parent;
        }
    }

    public Result getBestPath(int[][] grid, int delay) {
        LinkedList<PathTuple> bestPath = new LinkedList<>();
        // Begin with root node
        TreeNode bestNode = this;
        bestPath.addLast(new PathTuple(bestNode.x, bestNode.y, bestNode.score));

        while (!bestNode.isLeaf()) {
            // Find highest scoring child
            double bestScore = Double.MIN_VALUE;
            for (TreeNode child : bestNode.children) {
                if (child.totalScore > bestScore) {
                    bestNode = child;
                }
            }
            // Add to path
            bestPath.addLast(new PathTuple(bestNode.x, bestNode.y, bestNode.score));
        }

        // Compute score of best path
        double bestScore = computeTotalScore(bestPath, grid, delay);

        return new Result(Planner.pathToString(bestPath), bestScore);
    }

    // Compute total score of path
    public double computeTotalScore(LinkedList<PathTuple> path, int[][] grid, int delay) {
        // Map to track the last index where (x, y) was visited
        Map<String, Integer> lastVisited = new HashMap<>();
        double totalScore = 0.0;

        // Traverse the list
        Iterator<PathTuple> iterator = path.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            PathTuple current = iterator.next();
            String key = current.x + "," + current.y; // Hashmap key: "x,y"

            // Check if (x,y) already visited
            if (lastVisited.containsKey(key)) {
                // How long was it ago?
                int lastIndex = lastVisited.get(key);
                // Compute the delayed value: originalValue / delay * (current_index - latest_visited_index)
                double delayedValue = current.originalValue / delay * (i - lastIndex);
                totalScore += delayedValue;
            } else {
                // First time visit, add the original value
                totalScore += current.originalValue;
            }

            // Update the last visited index for the current (x,y)
            lastVisited.put(key, i);
        }

        return totalScore;
    }

    // Check if move is within grid
    protected boolean isValidMove(int x, int y, int N) {
        return x >= 0 && x < N && y >= 0 && y < N;
    }

    // Check if leaf node
    public boolean isLeaf() {
        return this.children.isEmpty();
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getScore() {
        return score;
    }

    public TreeNode getParent() {
        return parent;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public int getVisits() {
        return visits;
    }

    public double getTotalScore() {
        return totalScore;
    }

    public int getDepth() {
        return depth;
    }

    public LinkedList<TreeNode> getParents() {
        return parents;
    }
}
