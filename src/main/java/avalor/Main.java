package avalor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    private static Planner initPlanner(String fileName, String plannerType, int delay) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            int N = Integer.parseInt(reader.readLine().trim());
            int t = Integer.parseInt(reader.readLine().trim());
            long T = Long.parseLong(reader.readLine().trim());

            // Parse start position
            String[] startPos = reader.readLine().trim().replace("(", "").replace(")", "").split(",");
            int startX = Integer.parseInt(startPos[0]);
            int startY = Integer.parseInt(startPos[1]);

            // Parse grid
            int[][] grid = new int[N][N];
            for (int i = 0; i < N; i++) {
                String[] row = reader.readLine().trim().split("\\s+");
                for (int j = 0; j < N; j++) {
                    grid[i][j] = Integer.parseInt(row[j]);
                }
            }
            
            // Construct planner
            if (plannerType.equals("greedy")) {
                return new GreedyPlanner(N, t, T, startX, startY, grid, delay);
            } else if (plannerType.equals("mcts")) {
                return new MCTSPlanner(N, t, T, startX, startY, grid, delay);
            } else {
                throw new IllegalArgumentException("Invalid planner type");
            }

        } catch (IOException e) {
            throw e;
        }
    }
    
    public static void main(String[] args) throws Exception {
        // Default vars
        int N = 3;
        String levelType = "generated";     // generated or provided
        String plannerType = "greedy";        // greedy or mcts
        boolean printGrid = true;
        int delay = N;                   // Delay for restoring visited cells to original value

        // Parse cmd arguments
        try {
            if (args.length > 0) {
                try {
                    N = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid grid size (N): " + args[0]);
                    return;
                }
            }
            if (args.length > 1) {
                levelType = args[1];
                if (!levelType.equals("generated") && !levelType.equals("provided")) {
                    throw new IllegalArgumentException("Invalid levelType. Options: generated, provided");
                }
            }
            if (args.length > 2) {
                plannerType = args[2];
                if (!plannerType.equals("greedy")) {
                    throw new IllegalArgumentException("Invalid plannerType. Options: greedy");
                }
            }
            if (args.length > 3) {
                printGrid = Boolean.parseBoolean(args[3]);
            }
        } catch (Exception e) {
            throw e;
        }
        
        try {
            // Init
            String fileName = "./levels/" + levelType + "/" + N + ".txt";
            Planner planner = initPlanner(fileName, plannerType, delay);
            if (printGrid) {
                planner.printGrid();
            }

            // Run
            long startTime = System.currentTimeMillis();
            Result res = planner.run();
            long endTime = System.currentTimeMillis();

            // Print result
            System.out.println(res);
            System.out.println("Within time: " + ((endTime - startTime) <= planner.getMaxTime()));
        } catch (IOException e) {
            throw e;
        }
    }
}
