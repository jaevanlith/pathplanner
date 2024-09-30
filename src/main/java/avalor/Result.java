package avalor;

public class Result {
    public String path;
    public int score;

    Result(String path, int score) {
        this.path = path;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Path: " + path + "\nScore: " + score;
    }
}
