package ua.com.javarush.mavoropaev.cryptoanalyser;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static final int ENCRYPT_MODULE    = 1;
    public static final int DECRYPT_MODULE    = 2;
    public static final int BRUTEFORCE_MODULE = 3;
    public static final int STATISTIC_MODULE  = 4;
    public static final int EXIT              = 5;

    public static void main(String[] args) {
        int numberMenu = 0;
        while (numberMenu != EXIT) {
            numberMenu = ChoiceMenu();
            switch (numberMenu) {
                case ENCRYPT_MODULE:
                    EncryptModule();
                    break;
                case DECRYPT_MODULE:
                    DecryptModule();
                    break;
                case BRUTEFORCE_MODULE:
                    BruteForceModule();
                    break;
                case STATISTIC_MODULE:
                    StatisticModule();
                    break;
            }

        }
    }

    public static void EncryptModule(){

    }
    public static void DecryptModule(){

    }
    public static void BruteForceModule(){

    }
    public static void StatisticModule(){

    }

    public static int ChoiceMenu(){
        System.out.println("Работает программа криптоанализатор.");

        while (true) {
            System.out.println("Выберите режим работы:");
            System.out.println(" - шифрование текста  - 1");
            System.out.println(" - расшифровка текста ключом - 2");
            System.out.println(" - расшифровка текста методом brute force - 3");
            System.out.println(" - расшифровка теас4та методом статистического анализа - 4");
            System.out.println(" - выход - 5");
            System.out.print("> ");

            Scanner scanner = new Scanner(System.in);
            try {

                int numberModule = scanner.nextInt();
                if (numberModule < 1 || numberModule > 5){
                    throw new InputMismatchException();
                }

                return numberModule;

            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода! Не верно выбран режим работы!");
                System.out.println("Повторите ввод.");
            }
        }
    }

}
