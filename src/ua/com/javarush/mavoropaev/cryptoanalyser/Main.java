package ua.com.javarush.mavoropaev.cryptoanalyser;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
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
        System.out.println("Модуль шифрования данных.");

        Path pathFileInput;
        Path pathFileOutput;
        int keyEncrypt;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Введите путь к файлу для шифрования или 5 для выхода:");
            String strFileInput = scanner.nextLine();
            try {
                if (Integer.parseInt(strFileInput) == EXIT) return;
            }
            catch (NumberFormatException ex){}

            try{
                pathFileInput = Path.of(strFileInput);
                if (Files.exists(pathFileInput)){
                    break;
                }
                System.out.println("Файл не существует! Повторите ввод.");
            }
            catch (InvalidPathException ex){
                System.err.println("Путь недействителен:" + strFileInput);
                System.err.println("Ошибка:" + ex.getMessage());
            }
        }

        while (true) {
            System.out.println("Введите путь для зашифрованного файла:");
            String strFileOutput = scanner.nextLine();

            try{
                pathFileOutput = Path.of(strFileOutput);
                if (Files.exists(pathFileOutput)){
                    break;
                }
                System.out.println("Файл не существует! Повторите ввод.");
            }
            catch (InvalidPathException ex){
                System.err.println("Путь недействителен:" + strFileOutput);
                System.err.println("Ошибка:" + ex.getMessage());
                System.out.println("Повторите ввод.");
            }
        }

        while (true) {
            System.out.println("Введите ключ (число от 1 до 50):");
            try {
                keyEncrypt = scanner.nextInt();
                if (keyEncrypt < 1 || keyEncrypt > 50){
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода! Не верно задан ключ шифрования!");
                System.out.println("Повторите ввод.");
            }
        }


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
