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

    private static final boolean FLAG_ENCRYPT = true;
    private static final boolean FLAG_DECRYPT = false;

    private static Set<Integer> setMenu;

    public Dialog(){
        startDialog();
    }

    public void startDialog(){
        initialSetMenu();
        Encryption caesarCipher = new Encryption();

        int numberMenu = EXIT - 1;
        while (numberMenu != EXIT) {
            numberMenu = choiceMainMenu();

            switch (numberMenu) {
                case ENCRYPT_MODULE:
                    caesarCipher.encryptModule(FLAG_ENCRYPT);
                    break;
                case DECRYPT_MODULE:
                    caesarCipher.encryptModule(FLAG_DECRYPT);
                    break;
                case BRUTEFORCE_MODULE:
                    caesarCipher.bruteForceModule();
                    break;
                case STATISTIC_MODULE:
                    caesarCipher.statisticModule();
                    break;
            }
        }
    }

    public static int choiceMainMenu(){
        System.out.println("Работает программа криптоанализатор.");

        while (true) {
            System.out.println("Выберите режим работы:");
            System.out.println(" - шифрование текста  - " + ENCRYPT_MODULE);
            System.out.println(" - расшифровка текста ключом - " + DECRYPT_MODULE);
            System.out.println(" - расшифровка текста методом brute force - " + BRUTEFORCE_MODULE);
            System.out.println(" - расшифровка текста методом статистического анализа - " + STATISTIC_MODULE);
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
