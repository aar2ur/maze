import java.util.*;

public class MazeWithRecursion {
    // Символы, обозначающие различные элементы лабиринта
    private static final char WALL = '#';      // Непроходимая стена
    private static final char PATH = ' ';      // Проходимый путь
    private static final char START = 'S';     // Начальная точка
    private static final char END = 'E';       // Конечная точка
    private static final char VISITED = '.';   // Посещённая точка при поиске пути

    private final int totalRows, totalCols;              // Размеры лабиринта
    private final char[][] mazeGrid;                     // Матрица лабиринта
    private final boolean[][] visitedCells;              // Отслеживание посещённых клеток
    private final Random rand = new Random();            // Генератор случайных чисел

    // Конструктор: инициализация лабиринта
    public MazeWithRecursion(int rows, int cols) {
        // Обеспечиваем нечётные размеры (для правильной генерации)
        this.totalRows = rows % 2 == 0 ? rows + 1 : rows;
        this.totalCols = cols % 2 == 0 ? cols + 1 : cols;
        this.mazeGrid = new char[this.totalRows][this.totalCols];
        this.visitedCells = new boolean[this.totalRows][this.totalCols];
        initializeMaze();                          // Заполняем лабиринт стенами
        generateMaze(1, 1);                         // Генерируем проходимые пути
        mazeGrid[1][1] = START;                     // Задаём начальную точку
        mazeGrid[this.totalRows - 2][this.totalCols - 2] = END;  // Задаём конечную точку
    }

    // Инициализация лабиринта стенами
    private void initializeMaze() {
        for (int row = 0; row < totalRows; row++) {
            Arrays.fill(mazeGrid[row], WALL); // Все клетки по умолчанию — стены
        }
    }

    // Рекурсивная генерация лабиринта методом обратного отслеживания (backtracking)
    // Алгоритм начинает с указанной клетки и случайным образом выбирает направление движения.
    // Если соседняя клетка ещё не посещена и находится в допустимых границах,
    // между текущей и следующей клеткой пробивается стена (делается проход),
    // и рекурсивно вызывается генерация из новой клетки.
    // Таким образом формируется связный, но случайный и проходной лабиринт.
    private void generateMaze(int row, int col) {
        // Возможные направления (влево, вправо, вверх, вниз), смещение на 2 клетки
        int[] rowOffsets = {0, 0, -2, 2};
        int[] colOffsets = {-2, 2, 0, 0};

        visitedCells[row][col] = true;
        mazeGrid[row][col] = PATH;  // Преобразуем текущую клетку в путь

        List<Integer> directions = Arrays.asList(0, 1, 2, 3);
        Collections.shuffle(directions); // Случайный порядок направлений

        for (int direction : directions) {
            int nextRow = row + rowOffsets[direction];
            int nextCol = col + colOffsets[direction];
            // Проверяем границы и что клетка не посещалась
            if (isInBounds(nextRow, nextCol) && !visitedCells[nextRow][nextCol]) {
                // Пробиваем стену между текущей и соседней клеткой
                mazeGrid[row + rowOffsets[direction] / 2][col + colOffsets[direction] / 2] = PATH;
                // Рекурсивно переходим к соседней клетке
                generateMaze(nextRow, nextCol);
            }
        }
    }

    // Проверка, находится ли клетка внутри допустимых границ лабиринта (не касаясь стен)
    private boolean isInBounds(int row, int col) {
        return row > 0 && row < totalRows - 1 && col > 0 && col < totalCols - 1;
    }

    // Рекурсивный поиск пути от начальной точки к выходу (DFS)
    public boolean solveMaze(int row, int col) {
        // Базовые условия: выход за границы, стена или уже посещённая клетка
        if (!isInBounds(row, col) || mazeGrid[row][col] == WALL || mazeGrid[row][col] == VISITED) return false;

        // Успешный выход: достигнута конечная точка
        if (mazeGrid[row][col] == END) return true;

        // Отмечаем клетку как посещённую (если это не стартовая точка)
        if (mazeGrid[row][col] != START) mazeGrid[row][col] = VISITED;

        // Рекурсивный вызов для всех четырёх направлений
        if (solveMaze(row + 1, col) || solveMaze(row - 1, col) ||
            solveMaze(row, col + 1) || solveMaze(row, col - 1)) {
            return true;
        }

        // Если путь не найден, возвращаем клетке статус пути (откат)
        if (mazeGrid[row][col] != START) mazeGrid[row][col] = PATH;
        return false;
    }

    // Вывод лабиринта в консоль с координатами строк и столбцов
    public void printMaze() {
        System.out.print("    ");
        for (int col = 0; col < totalCols; col++) {
            System.out.print((col % 10)); // Верхняя строка с номерами столбцов
        }
        System.out.println();

        for (int row = 0; row < totalRows; row++) {
            System.out.printf("%3d ", row % 100); // Номер строки слева
            for (int col = 0; col < totalCols; col++) {
                System.out.print(mazeGrid[row][col]);
            }
            System.out.println();
        }
    }

    // Основной метод: ввод, генерация, решение и вывод лабиринта
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int inputRows = 0, inputCols = 0;

        // Проверка ввода: обрабатываем нецелочисленные значения и ошибки
        while (true) {
            try {
                System.out.print("Enter maze size (rows cols): (minimum 5x5) ");
                inputRows = Integer.parseInt(scanner.next());
                inputCols = Integer.parseInt(scanner.next());
                if (inputRows < 5 || inputCols < 5) {
                    System.out.println("Maze size too small. Try again with values >= 5.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter two integers.");
                scanner.nextLine(); // очистить буфер
            }
        }

        // Создание и вывод сгенерированного лабиринта
        MazeWithRecursion mazeGame = new MazeWithRecursion(inputRows, inputCols);
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