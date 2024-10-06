# UAV Path Planning

This project is concerned with UAV path planning for a grid world model.

### Levels
A level consists of: N (the width and height of the grid), t (length of the path), T (computational time limit), (x,y) (starting coordinates), and the grid data.

Example levels can be found under `levels` and new levels can be generated by running the main method of the `LevelGenerator`.

### Algorithms
There two algorithms implemented in this project: Greedy and Monte Carlo Tree Search (MCTS).

### How to run

**Remotely:** Any push or pull request to the main branch will start the CI pipeline. This will automatically run the algorithms on all available levels. The results will be written to `results`.

**Locally:** Run `avalor.Main` along with the arguments `N`, `levelType`, `plannerType`, and `printGrid`.
