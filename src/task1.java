import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class task1 {
    public static void main(String[] args) {
        caesarCipher();
        permutationCipher();
    }

    // Метод для шифра Цезаря
    public static void caesarCipher() {
        System.out.println("Шифр Цезаря\n");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите числа от 20 до 50 через пробел: ");
        String input = scanner.nextLine();
        String[] inputNumbers = input.split(" ");
        int[] numbers = Arrays.stream(inputNumbers)
                .mapToInt(Integer::parseInt)
                .filter(num -> num >= 20 && num <= 50)
                .toArray();

        if (numbers.length == 0) {
            System.out.println("Некорректный ввод!");
            return;
        }

        // Генерация случайного сдвига
        Random random = new Random();
        int shift = random.nextInt(10) + 1; // Сдвиг от 1 до 10
        System.out.println("Сдвиг: " + shift);

        // Шифрование
        int[] encrypted = Arrays.stream(numbers)
                .map(num -> num + shift) // Убираем модуль
                .toArray();

        System.out.println("Зашифрованные числа (Цезарь): " + Arrays.toString(encrypted));

        // Дешифрование
        int[] decrypted = Arrays.stream(encrypted)
                .map(num -> num - shift)
                .toArray();

        System.out.println("Дешифрованные числа (Цезарь): " + Arrays.toString(decrypted));
    }

    // Метод для шифра усложненной перестановки
    public static void permutationCipher() {
        System.out.println("\nШифр усложненной перестановки\n");
        Scanner scanner = new Scanner(System.in);
        System.out.println("Введите числа от 20 до 50 через пробел: ");
        String input = scanner.nextLine();
        String[] inputNumbers = input.split(" ");
        int[] numbers = Arrays.stream(inputNumbers)
                .mapToInt(Integer::parseInt)
                .filter(num -> num >= 20 && num <= 50)
                .toArray();

        if (numbers.length == 0) {
            System.out.println("Некорректный ввод!");
            return;
        }

        System.out.println("Введите K1 (порядок для перестановки столбцов): ");
        String k1Input = scanner.nextLine();
        System.out.println("Введите K2 (порядок для перестановки строк): ");
        String k2Input = scanner.nextLine();

        // Преобразуем K1 и K2 в массивы
        int[] k1 = Arrays.stream(k1Input.split(" "))
                .mapToInt(Integer::parseInt)
                .map(n -> n - 1) // Преобразуем для 0-индексации
                .toArray();
        int[] k2 = Arrays.stream(k2Input.split(" "))
                .mapToInt(Integer::parseInt)
                .map(n -> n - 1) // Преобразуем для 0-индексации
                .toArray();

        // Определение размеров матрицы
        int rowLength = (numbers.length + k1.length - 1) / k1.length; // Количество строк
        int[][] matrix = new int[rowLength][k1.length];

        // Заполнение матрицы
        int index = 0;
        for (int r = 0; r < rowLength; r++) {
            for (int c = 0; c < k1.length; c++) {
                if (index < numbers.length) {
                    matrix[r][c] = numbers[index++];
                } else {
                    matrix[r][c] = 0; // Заполняем нулями, если не хватает чисел
                }
            }
        }

        System.out.println("Матрица после заполнения: ");
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }

        // Шифрование: перестановка столбцов
        int[][] encrypted = new int[rowLength][k1.length];
        for (int r = 0; r < rowLength; r++) {
            for (int c = 0; c < k1.length; c++) {
                if (r < matrix.length && k1[c] < matrix[r].length) {
                    encrypted[r][c] = matrix[r][k1[c]];
                }
            }
        }

        System.out.println("Матрица после перестановки столбцов (шифрование): ");
        for (int[] row : encrypted) {
            System.out.println(Arrays.toString(row));
        }

        // Перестановка строк
        int[][] permutedMatrix = new int[k2.length][k1.length];
        for (int r = 0; r < k2.length; r++) {
            if (k2[r] < encrypted.length) {
                permutedMatrix[r] = encrypted[k2[r]];
            } else {
                // Если индекс выходит за пределы, просто заполняем нулями
                permutedMatrix[r] = new int[k1.length];
            }
        }

        System.out.println("Матрица после перестановки строк: ");
        for (int[] row : permutedMatrix) {
            System.out.println(Arrays.toString(row));
        }

        // Запись зашифрованных ненулевых значений в новый массив (вектор результата)
        int[] finalEncrypted = Arrays.stream(permutedMatrix)
                .flatMapToInt(Arrays::stream)
                .filter(value -> value != 0) // Фильтрация ненулевых значений
                .toArray();

        // Вывод зашифрованных чисел
        System.out.println("Зашифрованные числа: " + Arrays.toString(finalEncrypted));

        // Дешифрование:
        // Восстановление строчек в исходном порядке по k2
        // System.out.println("Начало дешифрования:");
        int[][] restoredMatrix = new int[k2.length][k1.length];
        for (int r = 0; r < k2.length; r++) {
            //System.out.println("Восстановление строки " + r + " (индекс k2[" + r + "] = " + k2[r] + ")");
            if (k2[r] < permutedMatrix.length) {
                restoredMatrix[r] = permutedMatrix[k2[r]];
                //System.out.println("Восстановленная строка: " + Arrays.toString(restoredMatrix[r]));
            } else {
                restoredMatrix[r] = new int[k1.length];
                //System.out.println("Строка " + r + " заполнена нулями: " + Arrays.toString(restoredMatrix[r]));
            }
        }

        System.out.println("Матрица после восстановления строк (дешифрование): ");
        for (int[] row : restoredMatrix) {
            System.out.println(Arrays.toString(row));
        }

        // Обратная перестановка по столбцам
        int[][] originalMatrix = new int[restoredMatrix.length][k1.length];
        for (int r = 0; r < restoredMatrix.length; r++) {
            for (int c = 0; c < k1.length; c++) {
                if (k1[c] < restoredMatrix[r].length) {
                    originalMatrix[r][k1[c]] = restoredMatrix[r][c];
                    //System.out.println("Восстановление в оригинальную матрицу: originalMatrix[" + r + "][" + k1[c] + "] = " + restoredMatrix[r][c]);
                }
            }
        }

        // Вывод матрицы после восстановления столбцов
        System.out.println("Матрица после восстановления столбцов (дешифрование): ");
        for (int[] row : originalMatrix) {
            System.out.println(Arrays.toString(row));
        }

        // Запись всех значений в новый массив (вектор результата)
        int[] originalNumbers = Arrays.stream(originalMatrix)
                .flatMapToInt(Arrays::stream)
                .filter(value -> value != 0) // Фильтрация ненулевых значений
                .toArray();

        // Вывод дешифрованных чисел
        System.out.println("Дешифрованные числа: " + Arrays.toString(originalNumbers));
    }
}