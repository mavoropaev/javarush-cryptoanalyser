package ua.com.javarush.mavoropaev.cryptoanalyser;

import ua.com.javarush.mavoropaev.cryptoanalyser.exception.ExitFunctionException;
import ua.com.javarush.mavoropaev.cryptoanalyser.exception.FileProcessingException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Encryption {
    private static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з',
            'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э','ю', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    private static final HashMap<Character, Integer> SYMBOL_TO_NUMBER = new HashMap<>();
    private static final HashMap<Integer, Character> NUMBER_TO_SYMBOL = new HashMap<>();

    public Encryption(){
        initialEncryptMap();
    }

    public static void encryptModule(boolean flagEncrypt) {
        if (flagEncrypt) {
            System.out.println("Модуль шифрования данных.");
        }
        else{
            System.out.println("Модуль дешифрования данных.");
        }

        Path pathFileInput;
        Path pathFileOutput;
        Scanner scanner = new Scanner(System.in);

        try {
            String message;
            if (flagEncrypt) {
                message = "Введите путь и имя файла для шифрования.";
            }
            else{
                message = "Введите имя и путь зашифрованного файла.";
            }
            pathFileInput = getNameInputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        try {
            String message;
            if (flagEncrypt) {
                message = "Введите путь и имя для зашифрованного файла.";
            }
            else{
                message = "Введите имя и путь для дешифрованного файла.";
            }
            pathFileOutput = getNameOutputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        int keyEncrypt = getKeyEncrypt(scanner);
        if (!flagEncrypt) keyEncrypt *= -1;

        try {
            for (String line : getInputStrings(pathFileInput)) {
                char[] encryptLine = new char[line.length() + 1];

                int numberSymbol = 0;
                for (char symbol : line.toCharArray()) {
                    encryptLine[numberSymbol] = getEncryptSymbol(keyEncrypt, symbol);
                    numberSymbol++;
                }
                encryptLine[numberSymbol] = '\n';

                writeStrings(pathFileOutput, String.valueOf(encryptLine));
            }
        } catch (FileProcessingException ex){
            System.err.println(ex.getMessage());
        }
    }

    private static void writeStrings(Path pathFileOutput, String encryptLine) {
        try {
            Files.writeString(pathFileOutput, encryptLine,
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch(IOException ex){
            throw new FileProcessingException("Ошибка записи в файл " + pathFileOutput, ex);
        }
    }

    private static List<String> getInputStrings(Path pathFileInput) {
        try {
            return Files.readAllLines(pathFileInput);
        }
        catch(IOException ex){
            throw new FileProcessingException("Ошибка чтения файла " + pathFileInput, ex);
        }
    }

    public static void bruteForceModule(){
        FrequencyDictionary frequencyDictionary = new FrequencyDictionary();

        System.out.println("Модуль дешифрования данных методом Brute force.");

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

        try {
            List<String> listAllLines = getInputStrings(pathFileInput);
            StringBuilder decryptString = new StringBuilder();

            for (int numSymbol = 1; numSymbol <= ALPHABET.length; numSymbol++) {
                int keyDecrypt = -numSymbol;
                for (String line : listAllLines) {
                    for (char symbol : line.toCharArray()) {
                        decryptString.append(getEncryptSymbol(keyDecrypt, symbol));
                    }
                    decryptString.append('\n');
                }

                String[] arrayWord = decryptString.toString().split(" ");
                int numberOfCoincidences = 0;
                for (int numWord = 0; numWord < arrayWord.length; numWord++){
                    if (frequencyDictionary.dictionary.contains(arrayWord[numWord])){
                        numberOfCoincidences += 1;
                    }
                }

                if (numberOfCoincidences < 5) {
                    decryptString.delete(0, decryptString.length());
                }
                else{
                    writeStrings(pathFileOutput, String.valueOf(decryptString));
                    break;
                }

            }
        } catch (FileProcessingException ex){
            System.err.println(ex.getMessage());
        }

    }

    public static void statisticModule(){

    }

    public static Path getNameInputFile(Scanner scanner, String message) throws ExitFunctionException {
        while (true) {
            System.out.println(message + " Введите " + Dialog.getEXIT() + " для выхода:");
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
            System.out.println(message + " Введите " + Dialog.getEXIT() + " для выхода:");
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
            System.out.println("Введите ключ (число от 0 до "+ (ALPHABET.length - 1) + ") :");
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

    private static void initialEncryptMap(){
        for (int i = 0; i < ALPHABET.length; i++){
            SYMBOL_TO_NUMBER.put(ALPHABET[i], i);
            NUMBER_TO_SYMBOL.put(i, ALPHABET[i]);
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

    private static boolean exitFunction(String checkString){
        try {
            if (Integer.parseInt(checkString) == Dialog.getEXIT()) return true;
            else return false;
        }
        catch (NumberFormatException ex){
            return  false;
        }
    }

}
