package avalor;

import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.util.Random;

public class LevelGenerator {

    private int N;
    private int t;
    private long T;
    private int startX;
    private int startY;
    private int[][] grid;
    private int lowerBound;
    private int upperBound;
    private Random random;

    // Retrieve values and call generate
    public LevelGenerator(int N, int t, long T, int startX, int startY, int lowerBound, int upperBound) {
        this.N = N;
        this.t = t;
        this.T = T;
        this.startX = startX;
        this.startY = startY;
        this.grid = new int[N][N];
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
        this.random = new Random();
        generateGrid();
    }

    // Generate values (1-100) for grid
    private void generateGrid() {
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                grid[i][j] = random.nextInt(this.upperBound) + this.lowerBound;
            }
        }
    }
    
    // Save
    public void writeToFile(String fileName) {
        try {
            File file = new File(fileName);
            File parentDir = file.getParentFile();

            // Create dir if non-existent
            if (parentDir != null && !parentDir.exists()) {
                boolean dirCreated = parentDir.mkdirs();
                if (dirCreated) {
                    System.out.println("Directory created: " + parentDir.getAbsolutePath());
                }
            }

            // Dump generated level to file
            try (FileWriter writer = new FileWriter(file)) {
                writer.write(N + "\n");
                writer.write(t + "\n");
                writer.write(T + "\n");
                writer.write("(" + startX + "," + startY + ")\n");

                for (int i = 0; i < N; i++) {
                    for (int j = 0; j < N; j++) {
                        writer.write(grid[i][j] + " ");
                    }
                    writer.write("\n");
                }

                System.out.println("Generated level written to " + fileName);
            }

        } catch (IOException e) {
            System.out.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        // Set vars
        int N = 3;
        int t = 2;
        int T = 1000;
        int startX = 0;
        int startY = 0;
        int lowerBound = 1;
        int upperBound = 10;

        // Generate level
        LevelGenerator level = new LevelGenerator(N, t, T, startX, startY, lowerBound, upperBound);

        // Save
        String fileName =  "./levels/generated/" + String.valueOf(N) + ".txt";
        level.writeToFile(fileName);
    }
}
