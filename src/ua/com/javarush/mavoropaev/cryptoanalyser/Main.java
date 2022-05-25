package ua.com.javarush.mavoropaev.cryptoanalyser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Main {
    private static final int ENCRYPT_MODULE    = 1;
    private static final int DECRYPT_MODULE    = 2;
    private static final int BRUTEFORCE_MODULE = 3;
    private static final int STATISTIC_MODULE  = 4;
    private static final int EXIT              = 0;

    private static  Set<Integer> setMenu;

    private static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з',
            'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э','ю', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    private static final HashMap<Character, Integer> SYMBOL_TO_NUMBER = new HashMap<>();
    private static final HashMap<Integer, Character> NUMBER_TO_SYMBOL = new HashMap<>();

    public static void main(String[] args) {

        initialEncryptMap();
        initialSetMenu();
        dialogModule();

    }

    private static void dialogModule() {
        int numberMenu = EXIT - 1;
        while (numberMenu != EXIT) {
            numberMenu = choiceMenu();

            switch (numberMenu) {
                case ENCRYPT_MODULE:
                    encryptModule();
                    break;
                case DECRYPT_MODULE:
                    decryptModule();
                    break;
                case BRUTEFORCE_MODULE:
                    bruteForceModule();
                    break;
                case STATISTIC_MODULE:
                    statisticModule();
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

    public static void encryptModule() {
        System.out.println("Модуль шифрования данных.");

        Path pathFileInput;
        Path pathFileOutput;
        Scanner scanner = new Scanner(System.in);

        try {
            String message = "Введите путь и имя файла для шифрования.";
            pathFileInput = getNameInputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        try {
            String message = "Введите путь и имя для зашифрованного файла.";
            pathFileOutput = getNameOutputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        int keyEncrypt = getKeyEncrypt(scanner);

        try {
            for (String line : Files.readAllLines(pathFileInput)) {
                char[] encryptLine = new char[line.length() + 1];

                int numberSymbol = 0;
                for (char symbol : line.toCharArray()) {
                    encryptLine[numberSymbol] = getEncryptSymbol(keyEncrypt, symbol);
                    numberSymbol++;
                }
                encryptLine[numberSymbol] = '\n';

                Files.writeString(pathFileOutput, String.valueOf(encryptLine),
                                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (IOException ex){
            System.out.println("Ошибка работы с файлом :" + ex.getMessage());
        }
    }

    public static void decryptModule(){
        System.out.println("Модуль дешифрования данных.");

        Path pathFileInput;
        Path pathFileOutput;

        Scanner scanner = new Scanner(System.in);
        try {
            String message = "Введите имя и путь зашифрованного файла.";
            pathFileInput = getNameInputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        try {
            String message = "Введите имя и путь для дешифрованного файла.";
            pathFileOutput = getNameOutputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        int keyEncrypt = getKeyEncrypt(scanner);

        try {
            for (String line : Files.readAllLines(pathFileInput)) {
                char[] encryptLine = new char[line.length() + 1];

                int numberSymbol = 0;
                for (char symbol : line.toCharArray()) {
                    encryptLine[numberSymbol] = getEncryptSymbol(-keyEncrypt, symbol);
                    numberSymbol++;
                }
                encryptLine[numberSymbol] = '\n';

                Files.writeString(pathFileOutput, String.valueOf(encryptLine),
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            }
        } catch (IOException ex){
            System.out.println("Ошибка работы с файлом :" + ex.getMessage());
        }
    }

    private static char getEncryptSymbol(int keyEncrypt, char symbol) {
        char encryptSymbol;
        if (!SYMBOL_TO_NUMBER.containsKey(symbol)){
            encryptSymbol = symbol;
        }
        else {
            int newNumberSymbol = SYMBOL_TO_NUMBER.get(symbol) + keyEncrypt;
            if (newNumberSymbol > ALPHABET.length - 1) {
                newNumberSymbol -= ALPHABET.length;
            }
            if (newNumberSymbol < 0) {
                newNumberSymbol += ALPHABET.length;
            }
            encryptSymbol = NUMBER_TO_SYMBOL.get(newNumberSymbol);
        }
        return encryptSymbol;
    }

    public static Path getNameInputFile(Scanner scanner, String message) throws ExitFunctionException {
        while (true) {
            System.out.println(message + " Введите " + EXIT + " для выхода:");
            String strFileInput = scanner.nextLine();

            if (exitFunction(strFileInput)){
                throw new ExitFunctionException("");
            }

            try{
                Path pathFileInput = Path.of(strFileInput);
                if (Files.exists(pathFileInput)){
                    return pathFileInput;
                }
                System.out.println("Файл не существует! Повторите ввод.");
            }
            catch (InvalidPathException ex){
                System.err.println("Путь недействителен:" + strFileInput);
                System.err.println("Ошибка:" + ex.getMessage());
                System.out.println("Повторите ввод.");
            }
        }
    }

    public static Path getNameOutputFile(Scanner scanner, String message) throws ExitFunctionException {
        while (true) {
            System.out.println(message + " Введите " + EXIT + " для выхода:");
            String strFileOutput = scanner.nextLine();

            if (exitFunction(strFileOutput)){
                throw new ExitFunctionException("Выход из метода");
            }

            try{
                Path pathFileOutput = Path.of(strFileOutput);

                Files.deleteIfExists(pathFileOutput);
                Files.createFile(pathFileOutput);
                return pathFileOutput;
            }
            catch (IOException ex){
                System.err.println("Ошибка создания файла " + strFileOutput);
                System.err.println("Ошибка:" + ex.getMessage());
                System.out.println("Повторите ввод.");
            }
            catch (InvalidPathException ex){
                System.err.println("Путь недействителен:" + strFileOutput);
                System.err.println("Ошибка:" + ex.getMessage());
                System.out.println("Повторите ввод.");
            }
        }
    }

    public static int getKeyEncrypt(Scanner scanner){
        while (true) {
            System.out.println("Введите ключ (число от 0 до "+ ALPHABET.length + " :");
            try {
                int keyEncrypt = scanner.nextInt();
                if (keyEncrypt < 0 || keyEncrypt > ALPHABET.length){
                    throw new InputMismatchException();
                }
                return keyEncrypt;
            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода! Не верно задан ключ шифрования!");
                System.out.println("Повторите ввод.");
            }
        }
    }


    public static void bruteForceModule(){

    }
    public static void statisticModule(){

    }

    private static boolean exitFunction(String checkString){
        try {
            if (Integer.parseInt(checkString) == EXIT) return true;
            else return false;
        }
        catch (NumberFormatException ex){
            return  false;
        }
    }
    private static void initialSetMenu(){
        setMenu = new HashSet<>();
        setMenu.add(ENCRYPT_MODULE);
        setMenu.add(DECRYPT_MODULE);
        setMenu.add(BRUTEFORCE_MODULE);
        setMenu.add(STATISTIC_MODULE);
        setMenu.add(EXIT);
    }
    private static void initialEncryptMap(){
        for (int i = 0; i < ALPHABET.length; i++){
            SYMBOL_TO_NUMBER.put(ALPHABET[i], i);
            NUMBER_TO_SYMBOL.put(i, ALPHABET[i]);
        }
    }



}
