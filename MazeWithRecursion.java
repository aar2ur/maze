import java.util.*;

public class MazeWithRecursion {
    // Символы для обозначения различных частей лабиринта
    private static final char WALL = '#';  // Стены
    private static final char PATH = ' ';  // Путь
    private static final char START = 'S'; // Начальная точка
    private static final char END = 'E';   // Конечная точка
    private static final char VISITED = '.'; // Посещённая клетка

    private final int rows, cols;  // Размеры лабиринта (строки и столбцы)
    private final char[][] maze;   // Матрица для хранения лабиринта
    private final boolean[][] visited; // Массив для отслеживания посещённых клеток
    private final Random random = new Random(); // Для случайных чисел (например, при перемешивании направлений)

    // Конструктор, инициализирующий размеры лабиринта и создающий его
    public MazeWithRecursion(int rows, int cols) {
        // Если размеры нечетные, увеличиваем их на 1 для правильной генерации
        this.rows = rows % 2 == 0 ? rows + 1 : rows;
        this.cols = cols % 2 == 0 ? cols + 1 : cols;
        this.maze = new char[this.rows][this.cols];
        this.visited = new boolean[this.rows][this.cols];
        initializeMaze();
        generateMaze(1, 1);  // Генерация лабиринта начиная с точки (1,1)
        maze[1][1] = START;  // Начальная точка
        maze[this.rows - 2][this.cols - 2] = END; // Конечная точка
    }

    // Метод для инициализации лабиринта (заполнение стенами)
    private void initializeMaze() {
        for (int i = 0; i < rows; i++) {
            Arrays.fill(maze[i], WALL); // Все клетки заполняются стенами
        }
    }

    // Метод для генерации лабиринта с использованием рекурсии
    private void generateMaze(int r, int c) {
        // Массив для возможных направлений
        int[] dr = {0, 0, -2, 2};
        int[] dc = {-2, 2, 0, 0};
        visited[r][c] = true; // Отметка текущей клетки как посещённой
        maze[r][c] = PATH;    // Преобразуем текущую клетку в путь

        List<Integer> dirs = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(dirs); // Перемешиваем направления для случайной генерации

        for (int i : dirs) {
            int nr = r + dr[i], nc = c + dc[i];
            if (inBounds(nr, nc) && !visited[nr][nc]) {  // Проверка границ и не посещённости клетки
                maze[r + dr[i] / 2][c + dc[i] / 2] = PATH; // Открываем путь между клетками
                generateMaze(nr, nc);  // Рекурсивно вызываем для соседней клетки
            }
        }
    }

    // Проверка, находится ли клетка в пределах лабиринта
    private boolean inBounds(int r, int c) {
        return r > 0 && r < rows - 1 && c > 0 && c < cols - 1;
    }

    // Метод для решения лабиринта с использованием рекурсии
    public boolean solveMaze(int r, int c) {
        // Базовые условия: выход за пределы, стена или уже посещённая клетка
        if (!inBounds(r, c) || maze[r][c] == WALL || maze[r][c] == VISITED) return false;
        
        // Если найдена конечная точка
        if (maze[r][c] == END) return true;

        // Отметить текущую клетку как посещённую (если это не начальная точка)
        if (maze[r][c] != START) maze[r][c] = VISITED;

        // Рекурсивно пытаемся пройти в соседние клетки (вниз, вверх, вправо, влево)
        if (solveMaze(r + 1, c) || solveMaze(r - 1, c) ||
            solveMaze(r, c + 1) || solveMaze(r, c - 1)) {
            return true; // Если один из путей успешен
        }

        // Если путь не найден, восстанавливаем клетку как путь (для отладки)
        if (maze[r][c] != START) maze[r][c] = PATH;
        return false; // Путь не найден
    }

    // Метод для вывода лабиринта на экран
    public void printMaze() {
        for (char[] row : maze) {
            for (char c : row) {
                System.out.print(c);  // Выводим символ для каждой клетки
            }
            System.out.println();  // Переход на новую строку
        }
    }

    // Главный метод, где происходит ввод данных и запуск игры
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in); // Для ввода с консоли
        System.out.print("Enter maze size (rows cols): ");
        int rows = scanner.nextInt();  // Вводим количество строк
        int cols = scanner.nextInt();  // Вводим количество столбцов

        // Если размер лабиринта слишком мал, используем стандартные размеры
        if (rows < 5 || cols < 5) {
            System.out.println("Maze size too small. Using default 21x21.");
            rows = 21;
            cols = 21;
        }

        // Создаём объект лабиринта и генерируем его
        MazeWithRecursion mazeGame = new MazeWithRecursion(rows, cols);
        System.out.println("\nGenerated Maze:");
        mazeGame.printMaze();  // Выводим сгенерированный лабиринт

        System.out.println("\nSolving Maze...");
        if (mazeGame.solveMaze(1, 1)) {  // Пытаемся найти путь от начальной точки
            System.out.println("\nSolved Maze:");
            mazeGame.printMaze();  // Выводим решённый лабиринт
        } else {
            System.out.println("No path found from start to exit.");  // Если путь не найден
        }
    }
}

