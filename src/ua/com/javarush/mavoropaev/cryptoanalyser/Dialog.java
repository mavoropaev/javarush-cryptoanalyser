package ua.com.javarush.mavoropaev.cryptoanalyser;

import java.util.HashSet;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Dialog {

    private static final int ENCRYPT_MODULE    = 1;
    private static final int DECRYPT_MODULE    = 2;
    private static final int BRUTEFORCE_MODULE = 3;
    private static final int STATISTIC_MODULE  = 4;
    private static final int EXIT              = 0;

    private static Set<Integer> setMenu;

    public Dialog(){
        startDialog();
    }

    public void startDialog(){
        new Encryption();
        initialSetMenu();

        int numberMenu = EXIT - 1;
        while (numberMenu != EXIT) {
            numberMenu = choiceMenu();

            switch (numberMenu) {
                case ENCRYPT_MODULE:
                    Encryption.encryptModule(true);
                    break;
                case DECRYPT_MODULE:
                    Encryption.encryptModule(false);
                    break;
                case BRUTEFORCE_MODULE:
                    Encryption.bruteForceModule();
                    break;
                case STATISTIC_MODULE:
                    Encryption.statisticModule();
                    break;
            }
        }
    }

    public static int choiceMenu(){
        System.out.println("Работает программа криптоанализатор.");

        while (true) {
            System.out.println("Выберите режим работы:");
            System.out.println(" - шифрование текста  - " + ENCRYPT_MODULE);
            System.out.println(" - расшифровка текста ключом - " + DECRYPT_MODULE);
            System.out.println(" - расшифровка текста методом brute force - " + BRUTEFORCE_MODULE);
            System.out.println(" - расшифровка теас4та методом статистического анализа - " + STATISTIC_MODULE);
            System.out.println(" - выход - " + EXIT);
            System.out.print("> ");

            Scanner scanner = new Scanner(System.in);
            try {
                int numberModule = scanner.nextInt();
                if (!setMenu.contains(numberModule)){
                    throw new InputMismatchException();
                }
                return numberModule;
            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода! Не верно выбран режим работы!");
                System.out.println("Повторите ввод.");
            }
        }
    }

    public static int getEXIT() {
        return EXIT;
    }

    private static void initialSetMenu(){
        setMenu = new HashSet<>();
        setMenu.add(ENCRYPT_MODULE);
        setMenu.add(DECRYPT_MODULE);
        setMenu.add(BRUTEFORCE_MODULE);
        setMenu.add(STATISTIC_MODULE);
        setMenu.add(EXIT);
    }


}
