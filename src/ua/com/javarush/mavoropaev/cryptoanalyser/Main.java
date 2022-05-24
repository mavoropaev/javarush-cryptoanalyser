package ua.com.javarush.mavoropaev.cryptoanalyser;

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
        InitialEncryptMap();
        setMenu = InitialSetMenu();

        int numberMenu = -1;
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
            System.out.println("Введите путь к файлу для шифрования или " + EXIT + " для выхода:");
            String strFileInput = scanner.nextLine();

            if (ExitFunction(strFileInput)) return;

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
                System.out.println("Повторите ввод.");
            }
        }

        while (true) {
            System.out.println("Введите путь для зашифрованного файла или " + EXIT + " для выхода:");
            String strFileOutput = scanner.nextLine();

            if (ExitFunction(strFileOutput)) return;

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
            System.out.println("Введите ключ (число от 0 до "+ ALPHABET.length + " :");
            try {
                keyEncrypt = scanner.nextInt();
                if (keyEncrypt < 0 || keyEncrypt > ALPHABET.length){
                    throw new InputMismatchException();
                }
                break;
            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода! Не верно задан ключ шифрования!");
                System.out.println("Повторите ввод.");
            }
        }

        List<String> list = new ArrayList<String>();
        try {
             list = Files.readAllLines(pathFileInput);
        }
        catch(Exception ex){

        }
        char encryptSymbol;
        for(String line : list){
            char[] encryptLine = new char[line.length() + 1];
            int numberSymbol = 0;

            for (char symbol : line.toCharArray()){
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

                encryptLine[numberSymbol] = encryptSymbol;
                numberSymbol++;
                System.out.println("decrypt -> encrypt = " + symbol + " -> " + encryptSymbol);

            }
            encryptLine[numberSymbol] = '\n';
            System.out.println(String.valueOf(encryptLine));
            try {
                Files.writeString(pathFileOutput, String.valueOf(encryptLine), StandardOpenOption.APPEND);
            }
            catch (Exception ex){

            }
        }



    }
    public static void DecryptModule(){

    }
    public static void BruteForceModule(){

    }
    public static void StatisticModule(){

    }

    private static boolean ExitFunction(String checkString){
        try {
            if (Integer.parseInt(checkString) == EXIT) return true;
            else return false;
        }
        catch (NumberFormatException ex){
            return  false;
        }
    }
    private static HashSet<Integer> InitialSetMenu(){
        HashSet<Integer> setMenu = new HashSet<>();
        setMenu.add(ENCRYPT_MODULE);
        setMenu.add(DECRYPT_MODULE);
        setMenu.add(BRUTEFORCE_MODULE);
        setMenu.add(STATISTIC_MODULE);
        setMenu.add(EXIT);
        return setMenu;
    }
    private static void InitialEncryptMap(){
        for (int i = 0; i < ALPHABET.length; i++){
            SYMBOL_TO_NUMBER.put(ALPHABET[i], i);
            NUMBER_TO_SYMBOL.put(i, ALPHABET[i]);
        }
    }

    public static int ChoiceMenu(){
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

}
