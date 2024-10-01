package avalor;

public class Result {
    public String path;
    public double score;

    Result(String path, double score) {
        this.path = path;
        this.score = score;
    }

    public String getPath() {
        return path;
    }

    public double getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "Path: " + path + "\nScore: " + score;
    }
}
