import java.math.BigInteger;
import java.util.Scanner;

public class RSA {

    private BigInteger n, phi, e, d;

    // Конструктор для генерации ключей
    public RSA(BigInteger p, BigInteger q) {
        // n=p×q — это модуль, произведение
        n = p.multiply(q);
        System.out.println("\nn = p * q = " + p + " * " + q + " = " + n);

        // ϕ(n)=(p−1)×(q−1) — это функция Эйлера, которая используется для нахождения
        // открытой экспоненты e и закрытой экспоненты d
        phi = (p.subtract(BigInteger.ONE)).multiply(q.subtract(BigInteger.ONE));
        System.out.println("φ(n) = (p - 1) * (q - 1) = (" + p + " - 1) * (" + q + " - 1) = " + phi);

        // Генерация e, взаимно простого с phi
        e = BigInteger.valueOf(3); // Начинаем с 3
        while (phi.gcd(e).compareTo(BigInteger.ONE) > 0 && e.compareTo(phi) < 0) {
            e = e.add(BigInteger.TWO); // Увеличиваем e
        }
        System.out.println("Выбрано e = " + e + ", так как GCD(φ(n), e) = " + phi.gcd(e) + " (взаимно простое)");


        // Вычисление d
        d = e.modInverse(phi);
        System.out.println("d = e^(-1) mod φ(n) = " + d + "\n");
    }

    // Метод шифрования
    public BigInteger[] encrypt(String message) {
        BigInteger[] encrypted = new BigInteger[message.length()];
        System.out.println("\nШифрование сообщения: " + message);
        for (int i = 0; i < message.length(); i++) {
            BigInteger m = BigInteger.valueOf(message.charAt(i) - 'A' + 1);
            encrypted[i] = m.modPow(e, n);
            System.out.println("Шифрование '" + message.charAt(i) + "': c = m^e mod n = " + m + "^" + e + " mod " + n + " = " + encrypted[i]);
        }
        return encrypted;
    }

    // Метод дешифрования
    public String decrypt(BigInteger[] encryptedMessage) {
        StringBuilder decrypted = new StringBuilder();
        System.out.println("\nДешифрование сообщения:");
        for (BigInteger c : encryptedMessage) {
            BigInteger m = c.modPow(d, n);
            decrypted.append((char) (m.intValue() + 'A' - 1));
            System.out.println("Дешифрование c = " + c + ": m = c^d mod n = " + c + "^" + d + " mod " + n + " = " + m + " ('" + (char) (m.intValue() + 'A' - 1) + "')");
        }
        return decrypted.toString();
    }

    // Метод проверки простоты
    public static boolean isPrime(BigInteger num) {
        if (num.compareTo(BigInteger.TWO) < 0) return false; // Числа меньше 2 не являются простыми
        if (num.equals(BigInteger.TWO)) return true; // 2 - простое число
        if (num.mod(BigInteger.TWO).equals(BigInteger.ZERO)) return false; // Четные числа не простые

        // Проверка деления на все числа от 3 до корня числа
        for (BigInteger i = BigInteger.valueOf(3); i.multiply(i).compareTo(num) <= 0; i = i.add(BigInteger.TWO)) {
            if (num.mod(i).equals(BigInteger.ZERO)) {
                return false; // Если делится, то не простое
            }
        }
        return true; // Простое число
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        BigInteger p, q;

        // Ввод простых чисел p и q
        while (true) {
            System.out.print("Введите простое число p: ");
            p = scanner.nextBigInteger();
            System.out.print("Введите простое число q: ");
            q = scanner.nextBigInteger();

            // Проверка простоты
            if (isPrime(p) && isPrime(q) && p.compareTo(BigInteger.ONE) > 0 && q.compareTo(BigInteger.ONE) > 0) {
                break; // Выход из цикла, если оба числа простые и больше 1
            } else {
                System.out.println("Одно из введенных чисел не является простым или меньше 2. Пожалуйста, введите снова.");
            }
        }

        // Генерация ключей
        RSA rsa = new RSA(p, q);
        System.out.println("Открытый ключ: (e: " + rsa.e + ", n: " + rsa.n + ")");
        System.out.println("Закрытый ключ: (d: " + rsa.d + ", n: " + rsa.n + ")\n");

        // Ввод сообщения
        String message;
        while (true) {
            System.out.print("Введите сообщение (только буквы A-Z): ");
            message = scanner.next().toUpperCase();

            // Проверка на наличие только английских букв
            if (message.matches("[A-Z]+")) {
                break; // Выход из цикла, если сообщение корректно
            } else {
                System.out.println("Ошибка: сообщение должно содержать только английские буквы A-Z. Пожалуйста, введите снова.");
            }
        }

        // Шифрование
        BigInteger[] encryptedMessage = rsa.encrypt(message);
        System.out.print("\nЗашифрованное сообщение: ");
        for (BigInteger c : encryptedMessage) {
            System.out.print(c + " ");
        }
        System.out.println();

        // Дешифрование
        String decryptedMessage = rsa.decrypt(encryptedMessage);
        System.out.println("\nРасшифрованное сообщение: " + decryptedMessage);

        scanner.close();
    }
}