import java.util.*;

public class MazeWithRecursion {
    private static final char WALL = '#';
    private static final char PATH = ' ';
    private static final char START = 'S';
    private static final char END = 'E';
    private static final char VISITED = '.';

    private final int rows, cols;
    private final char[][] maze;
    private final boolean[][] visited;
    private final Random random = new Random();

    public MazeWithRecursion(int rows, int cols) {
        this.rows = rows % 2 == 0 ? rows + 1 : rows;
        this.cols = cols % 2 == 0 ? cols + 1 : cols;
        this.maze = new char[this.rows][this.cols];
        this.visited = new boolean[this.rows][this.cols];
        initializeMaze();
        generateMaze(1, 1);
        maze[1][1] = START;
        maze[this.rows - 2][this.cols - 2] = END;
    }

    private void initializeMaze() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maze[i], WALL);
        }
    }

    private void generateMaze(int r, int c) {
        int[] dr = {0, 0, -2, 2};
        int[] dc = {-2, 2, 0, 0};
        visited[r][c] = true;
        maze[r][c] = PATH;

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs);

        for (int i : dirs) {
            int nr = r + dr[i], nc = c + dc[i];
            if (inBounds(nr, nc) && !visited[nr][nc]) {
                maze[r + dr[i] / 2][c + dc[i] / 2] = PATH;
                generateMaze(nr, nc);
            }
        }
    }

    private boolean inBounds(int r, int c) {
        return r > 0 && r < rows - 1 && c > 0 && c < cols - 1;
    }

    public boolean solveMaze(int r, int c) {
        if (!inBounds(r, c) || maze[r][c] == WALL || maze[r][c] == VISITED) return false;
        if (maze[r][c] == END) return true;

        if (maze[r][c] != START) maze[r][c] = VISITED;

        if (solveMaze(r + 1, c) || solveMaze(r - 1, c) ||
            solveMaze(r, c + 1) || solveMaze(r, c - 1)) {
            return true;
        }

        if (maze[r][c] != START) maze[r][c] = PATH;
        return false;
    }

    public void printMaze() {
        for (char[] row : maze) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter maze size (rows cols): ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        if (rows < 5 || cols < 5) {
            System.out.println("Maze size too small. Using default 21x21.");
            rows = 21;
            cols = 21;
        }

        MazeWithRecursion mazeGame = new MazeWithRecursion(rows, cols);
        System.out.println("\nGenerated Maze:");
        mazeGame.printMaze();

        System.out.println("\nSolving Maze...");
        if (mazeGame.solveMaze(1, 1)) {
            System.out.println("\nSolved Maze:");
            mazeGame.printMaze();
        } else {
            System.out.println("No path found from start to exit.");
        }
    }
}
