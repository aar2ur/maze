import java.util.*;

public class MazeWithRecursion {
    // Символы, обозначающие различные элементы лабиринта
    private static final char WALL = '#';      // Непроходимая стена
    private static final char PATH = ' ';      // Проходимый путь
    private static final char START = 'S';     // Начальная точка
    private static final char END = 'E';       // Конечная точка
    private static final char VISITED = '.';   // Посещённая точка при поиске пути

    private final int rows, cols;              // Размеры лабиринта
    private final char[][] maze;               // Матрица лабиринта
    private final boolean[][] visited;         // Отслеживание посещённых клеток
    private final Random random = new Random();

    // Конструктор: инициализация лабиринта
    public MazeWithRecursion(int rows, int cols) {
        // Обеспечиваем нечётные размеры (для правильной генерации)
        this.rows = rows % 2 == 0 ? rows + 1 : rows;
        this.cols = cols % 2 == 0 ? cols + 1 : cols;
        this.maze = new char[this.rows][this.cols];
        this.visited = new boolean[this.rows][this.cols];
        initializeMaze();            // Заполняем лабиринт стенами
        generateMaze(1, 1);          // Генерируем проходимые пути
        maze[1][1] = START;          // Задаём начальную точку
        maze[this.rows - 2][this.cols - 2] = END;  // Задаём конечную точку
    }

    // Инициализация лабиринта стенами
    private void initializeMaze() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maze[i], WALL); // Все клетки по умолчанию — стены
        }
    }

    // Рекурсивная генерация лабиринта методом обратного отслеживания (backtracking)
    private void generateMaze(int r, int c) {
        // Возможные направления (влево, вправо, вверх, вниз), смещение на 2 клетки
        int[] dr = {0, 0, -2, 2};
        int[] dc = {-2, 2, 0, 0};

        visited[r][c] = true;
        maze[r][c] = PATH;  // Преобразуем текущую клетку в путь

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs); // Случайный порядок направлений

        for (int i : dirs) {
            int nr = r + dr[i];
            int nc = c + dc[i];
            // Проверяем границы и что клетка не посещалась
            if (inBounds(nr, nc) && !visited[nr][nc]) {
                // Пробиваем стену между текущей и соседней клеткой
                maze[r + dr[i] / 2][c + dc[i] / 2] = PATH;
                // Рекурсивно переходим к соседней клетке
                generateMaze(nr, nc);
            }
        }
    }

    // Проверка, находится ли клетка внутри допустимых границ лабиринта (не касаясь стен)
    private boolean inBounds(int r, int c) {
        return r > 0 && r < rows - 1 && c > 0 && c < cols - 1;
    }

    // Рекурсивный поиск пути от начальной точки к выходу (DFS)
    public boolean solveMaze(int r, int c) {
        // Базовые условия: выход за границы, стена или уже посещённая клетка
        if (!inBounds(r, c) || maze[r][c] == WALL || maze[r][c] == VISITED) return false;

        // Успешный выход: достигнута конечная точка
        if (maze[r][c] == END) return true;

        // Отмечаем клетку как посещённую (если это не стартовая точка)
        if (maze[r][c] != START) maze[r][c] = VISITED;

        // Рекурсивный вызов для всех четырёх направлений
        if (solveMaze(r + 1, c) || solveMaze(r - 1, c) ||
            solveMaze(r, c + 1) || solveMaze(r, c - 1)) {
            return true;
        }

        // Если путь не найден, возвращаем клетке статус пути (откат)
        if (maze[r][c] != START) maze[r][c] = PATH;
        return false;
    }

    // Вывод лабиринта в консоль (текстовое представление)
    public void printMaze() {
        for (char[] row : maze) {
            for (char c : row) {
                System.out.print(c);
            }
            System.out.println();
        }
    }

    // Основной метод: ввод, генерация, решение и вывод лабиринта
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter maze size (rows cols): ");
        int rows = scanner.nextInt();
        int cols = scanner.nextInt();

        // Проверка минимального размера лабиринта
        if (rows < 5 || cols < 5) {
            System.out.println("Maze size too small. Using default 21x21.");
            rows = 21;
            cols = 21;
        }

        // Создание и вывод сгенерированного лабиринта
        MazeWithRecursion mazeGame = new MazeWithRecursion(rows, cols);
        System.out.println("\nGenerated Maze:");
        mazeGame.printMaze();

        // Рекурсивный поиск пути и вывод результата
        System.out.println("\nSolving Maze...");
        if (mazeGame.solveMaze(1, 1)) {
            System.out.println("\nSolved Maze:");
            mazeGame.printMaze();
        } else {
            System.out.println("No path found from start to exit.");
        }
    }
}