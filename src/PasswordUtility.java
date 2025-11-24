import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.security.SecureRandom;

public class PasswordUtility {
    // Заданные значения из варианта 12
    private static final double P = 10e-6; // Вероятность подбора пароля
    private static final double V = 10.0 / 60; // Скорость подбора паролей (паролей в минуту)
    private static final double T = 5 * 24 * 60; // Максимальный срок действия пароля в минутах (5 дней)

    private static final int A = 62; // Мощность алфавита (26 букв + 26 букв + 10 цифр)

    public static void main(String[] args) {
        System.out.println("Вводные данные:");
        System.out.println("P (Вероятность подбора пароля): " + P);
        System.out.println("V (Скорость подбора паролей): " + V + " паролей в минуту");
        System.out.println("T (Максимальный срок действия пароля): " + (T / 60) + " часов");
        System.out.println("A (Мощность алфавита): " + A + " символов");

        double SStar = calculateMinimumPasswordStrength();
        System.out.println("\nМинимально допустимое количество возможных паролей (S*): " + Math.ceil(SStar));

        int L = calculateMinimumLength(SStar);
        System.out.println("Минимальная длина пароля (L): " + L);

        String generatedPassword = generatePassword(L);
        System.out.println("Сгенерированный пароль: " + generatedPassword);

        savePasswordToFile(generatedPassword, "generated_password.txt");
    }

    // Функция для вычисления нижней границы S*
    private static double calculateMinimumPasswordStrength() {
        // Формула: S* = (V * T) / P
        return (V * T) / P;
    }

    // Функция для вычисления минимальной длины пароля
    private static int calculateMinimumLength(double SStar) {
        int L = 1;
        while (Math.pow(A, L) < SStar) {
            L++;
        }
        return L;
    }

    // Функция для генерации пароля
    private static String generatePassword(int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()";
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            password.append(characters.charAt(index));
        }
        return password.toString();
    }

    // Функция для сохранения пароля в файл
    private static void savePasswordToFile(String password, String filename) {
        File file = new File(filename);
        // Удаляем файл, если он существует
        if (file.exists()) {
            file.delete();
        }
        // Сохраняем новый пароль в файл
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(password);
        } catch (IOException e) {
            System.err.println("Ошибка при записи в файл: " + e.getMessage());
        }
    }
}