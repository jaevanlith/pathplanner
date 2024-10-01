package avalor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.File;

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

    private static String[] parseArguments(String[] args) throws IllegalArgumentException {
        // Default values
        int N = 3;
        String levelType = "generated";     // generated or provided
        String plannerType = "mcts";      // greedy or mcts
        boolean printGrid = true;

        // Parse arguments
        try {
            if (args.length > 0) {
                try {
                    N = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.err.println("Invalid grid size (N): " + args[0]);
                    throw new IllegalArgumentException("Invalid grid size: " + args[0]);
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
                if (!plannerType.equals("greedy") && !plannerType.equals("mcts")) {
                    throw new IllegalArgumentException("Invalid plannerType. Options: greedy, mcts");
                }
            }
            if (args.length > 3) {
                printGrid = Boolean.parseBoolean(args[3]);
            }
        } catch (Exception e) {
            throw e;
        }

        return new String[]{String.valueOf(N), levelType, plannerType, String.valueOf(printGrid)};
    }

    private static void writeResultToFile(String fileName, Result res, long executionTime, long maxTime, String plannerType) throws IOException {
        String resultFileName = fileName.replace("levels", "results").replace(".txt", ("_" + plannerType + ".txt"));

        File file = new File(resultFileName);
        File parentDir = file.getParentFile();

        // Create dir if non-existent
        if (parentDir != null && !parentDir.exists()) {
            boolean dirCreated = parentDir.mkdirs();
            if (dirCreated) {
                System.out.println("Directory created: " + parentDir.getAbsolutePath());
            }
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(resultFileName))) {
            writer.write(res.toString() + "\n");
            writer.write("Within time: " + (executionTime <= maxTime) + "\n");
        }

        System.out.println("Result written to: " + resultFileName);
    }
    
    public static void main(String[] args) throws Exception {
        // Parse cmd args
        // FORMATL: --N --levelType --plannerType --printGrid
        String[] parsedArgs = parseArguments(args);
        int N = Integer.parseInt(parsedArgs[0]);
        String levelType = parsedArgs[1];
        String plannerType = parsedArgs[2];
        boolean printGrid = Boolean.parseBoolean(parsedArgs[3]);
        int delay = N*N;

        try {
            // Init planner
            String fileName = "./levels/" + levelType + "/" + N + ".txt";
            Planner planner = initPlanner(fileName, plannerType, delay);
            
            if (printGrid) {
                planner.printGrid();
            }

            // Run planner and track time
            long startTime = System.currentTimeMillis();
            Result res = planner.run();
            long endTime = System.currentTimeMillis();

            // Save result
            writeResultToFile(fileName, res, endTime - startTime, planner.getMaxTime(), plannerType);

        } catch (IOException e) {
            throw e;
        }
    }
}
