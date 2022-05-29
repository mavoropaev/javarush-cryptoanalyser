package ua.com.javarush.mavoropaev.cryptoanalyser;

import ua.com.javarush.mavoropaev.cryptoanalyser.exception.ExitFunctionException;
import ua.com.javarush.mavoropaev.cryptoanalyser.exception.FileProcessingException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.*;

public class Encryption {
    private static final char[] ALPHABET = {'а', 'б', 'в', 'г', 'д', 'е', 'ё', 'ж', 'з',
            'и', 'й', 'к', 'л', 'м', 'н', 'о', 'п', 'р', 'с', 'т', 'у', 'ф', 'х', 'ц', 'ч', 'ш', 'щ',
            'ъ', 'ы', 'ь', 'э','ю', 'я', '.', ',', '«', '»', '"', '\'', ':', '!', '?', ' '};
    private static final HashMap<Character, Integer> SYMBOL_TO_NUMBER = new HashMap<>();
    private static final HashMap<Integer, Character> NUMBER_TO_SYMBOL = new HashMap<>();

    public Encryption(){
        initialEncryptMap();
    }

    public void encryptModule(boolean flagEncrypt) {
        if (flagEncrypt) {
            System.out.println("----->>>    Модуль шифрования данных шифром Цезаря    <<<-----");
        }
        else{
            System.out.println("----->>>    Модуль дешифрования данных    <<<-----");
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

    public void bruteForceModule(){
        FrequencyDictionary frequencyDictionary = new FrequencyDictionary();

        System.out.println("----->>>    Модуль дешифрования данных методом Brute force    <<<-----");

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
            //StringBuilder decryptString = new StringBuilder();

            for (int numSymbol = 1; numSymbol <= ALPHABET.length; numSymbol++) {
                int keyDecrypt = -numSymbol;
                if (decryptDictionaryModul(keyDecrypt, frequencyDictionary, pathFileOutput, listAllLines)) {
                    System.out.println("Найден ключ на " + numSymbol + " итерации.");
                    break;
                }
            }
        } catch (FileProcessingException ex){
            System.err.println(ex.getMessage());
        }
    }

    public void statisticModule(){
        FrequencyDictionary frequencyDictionary = new FrequencyDictionary();

        System.out.println("----->>>    Модуль дешифрования данных статистическим методом    <<<-----");

        Path pathFileInput;
        Path pathFileInputForTextAnalysis;
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
            String message = "Введите имя и путь файла для статистического анализа.";
            pathFileInputForTextAnalysis = getNameInputFile(scanner, message);
        } catch (ExitFunctionException ex) {
            return;
        }

        Map<Character, Integer> countSymbolTextAnalis = new HashMap<>();
        NavigableMap<Double, Character>  statisticSymbolTextAnalisTreeMap = new TreeMap<>();

        statisticAnalisText(pathFileInputForTextAnalysis, countSymbolTextAnalis, statisticSymbolTextAnalisTreeMap);

        statisticSymbolTextAnalisTreeMap = statisticSymbolTextAnalisTreeMap.descendingMap();

        /*
        System.out.println("Текст для анализа");
        for(Map.Entry< Double, Character > entry : statisticSymbolTextAnalisTreeMap.entrySet()) {
            Double key = entry.getKey();
            Character value = entry.getValue();
            System.out.println(value + "->" + key);
        }
         */

        Map<Character, Integer> countSymbolEncryptText = new HashMap<>();
        NavigableMap<Double, Character>  statisticSymbolEncryptText = new TreeMap<>();

        statisticAnalisText(pathFileInput, countSymbolEncryptText, statisticSymbolEncryptText);

        statisticSymbolEncryptText = statisticSymbolEncryptText.descendingMap();

        /*
        System.out.println("Зашифрованный текст");
        for(Map.Entry< Double, Character > entry : statisticSymbolEncryptText.entrySet()) {
            Double key = entry.getKey();
            Character value = entry.getValue();
            System.out.println(value + "->" + key);
        }
         */

        //Map< String, String > treeMap = new TreeMap< String, String >(Collections.reverseOrder());
        int iteration = 0;
        int keyDecrypt = 0;
        for(Map.Entry< Double, Character > entry : statisticSymbolTextAnalisTreeMap.entrySet()) {
            iteration++;
            Double key = entry.getKey();
            Character value = entry.getValue();
            if (!SYMBOL_TO_NUMBER.containsKey(value)){
                continue;
            }

            Double ceilingKey = statisticSymbolEncryptText.ceilingKey(key);
            Character ceilingKeyValue = statisticSymbolEncryptText.get(ceilingKey);
            if (ceilingKeyValue != null && SYMBOL_TO_NUMBER.containsKey(ceilingKeyValue)) {
                keyDecrypt = SYMBOL_TO_NUMBER.get(value) - SYMBOL_TO_NUMBER.get(ceilingKeyValue);
                if (decryptStatisticMetod(keyDecrypt, frequencyDictionary, pathFileInput, pathFileOutput)) {
                    System.out.println("Подобран ключ на " + iteration + " итерации.");
                    System.out.println("Совпадение частоты по символу '" + value + "'.");
                    break;
                }
            }

            Double floorKey = statisticSymbolEncryptText.floorKey(key);
            Character floorKeyValue = statisticSymbolEncryptText.get(floorKey);
            if (floorKeyValue != null && SYMBOL_TO_NUMBER.containsKey(floorKeyValue)) {
                keyDecrypt = SYMBOL_TO_NUMBER.get(value) - SYMBOL_TO_NUMBER.get(floorKeyValue);
                if (decryptStatisticMetod(keyDecrypt, frequencyDictionary, pathFileInput, pathFileOutput)) {
                    System.out.println("Подобран ключ на " + iteration + " итерации.");
                    System.out.println("Совпадение частоты по символу " + value + ".");
                    break;
                }
            }
        }
    }

    private void statisticAnalisText(Path pathFileInputForTextAnalysis, Map<Character, Integer> countSymbolTextAnalis,
                                     NavigableMap<Double, Character> statisticSymbolTextAnalisTreeMap) {
        int countSymbolAllTextAnalis = 0;
        try {
            for (String line : getInputStrings(pathFileInputForTextAnalysis)) {
                for (char symbol : line.toCharArray()) {
                    if (countSymbolTextAnalis.containsKey(symbol)){
                        int count = countSymbolTextAnalis.get(symbol);
                        countSymbolTextAnalis.replace(symbol, count+1);
                    }
                    else{
                        countSymbolTextAnalis.put(symbol, 1);
                    }
                    countSymbolAllTextAnalis++;
                }
            }
        } catch (FileProcessingException ex){
            System.err.println(ex.getMessage());
        }
        for(char symbol : countSymbolTextAnalis.keySet()){
            int count = countSymbolTextAnalis.get(symbol);
            double frequency = round((double)count/countSymbolAllTextAnalis, 5);
            while (true) {
                if (statisticSymbolTextAnalisTreeMap.containsKey(frequency)) {
                    frequency = frequency + 0.00001;
                }
                else{
                    break;
                }
            }
            statisticSymbolTextAnalisTreeMap.put(frequency, symbol);
        }
    }

    private boolean decryptStatisticMetod(int keyDecrypt, FrequencyDictionary frequencyDictionary,
                                          Path pathFileInput, Path pathFileOutput) {
        try {
            List<String> listAllLines = getInputStrings(pathFileInput);
            //StringBuilder decryptString = new StringBuilder();

            if (decryptDictionaryModul(keyDecrypt, frequencyDictionary, pathFileOutput, listAllLines)) {
                return true;
            }

        } catch (FileProcessingException ex){
            System.err.println(ex.getMessage());
        }
        return false;
    }

    private boolean decryptDictionaryModul(int keyDecrypt, FrequencyDictionary frequencyDictionary,
                                           Path pathFileOutput, List<String> listAllLines) {
        StringBuilder decryptString = new StringBuilder();
        for (String line : listAllLines) {
            for (char symbol : line.toCharArray()) {
                decryptString.append(getEncryptSymbol(keyDecrypt, symbol));
            }
            decryptString.append('\n');
        }

        String[] arrayWord = decryptString.toString().split(" ");
        int numberOfCoincidences = 0;
        Set<String> foundWords = new HashSet<>();
        for (int numWord = 0; numWord < arrayWord.length; numWord++){
            if (frequencyDictionary.dictionary.contains(arrayWord[numWord])){
                if (!foundWords.contains(arrayWord[numWord])) {
                    foundWords.add(arrayWord[numWord]);
                    numberOfCoincidences += 1;
                    //System.out.println(arrayWord[numWord]);
                }
            }
        }

        if (numberOfCoincidences < 7) {
            decryptString.delete(0, decryptString.length());
        }
        else{
            writeStrings(pathFileOutput, String.valueOf(decryptString));
            return true;
        }
        return false;
    }

    public static Path getNameInputFile(Scanner scanner, String message) throws ExitFunctionException {
        while (true) {
            System.out.println(message + " Введите " + Dialog.getEXIT() + " для выхода:");
            System.out.print("> ");
            String strFileInput = scanner.nextLine();

            if (exitFunction(strFileInput)){
                throw new ExitFunctionException("");
            }

            try{
                Path pathFileInput = Path.of(strFileInput);
                if (strFileInput == ""){
                    continue;
                }
                if (Files.exists(pathFileInput)){
                    return pathFileInput;
                }
                System.out.println("Файл не существует! Повторите ввод.");
            }
            catch (InvalidPathException ex){
                System.err.println("Путь недействителен:" + strFileInput);
                System.err.println("Ошибка:" + ex.getMessage());
            }
        }
    }

    public static Path getNameOutputFile(Scanner scanner, String message) {
        while (true) {
            System.out.println(message + " Введите " + Dialog.getEXIT() + " для выхода:");
            System.out.print("> ");
            String strFileOutput = scanner.nextLine();

            if (exitFunction(strFileOutput)){
                throw new ExitFunctionException("Выход из метода");
            }

            try{
                if (strFileOutput == ""){
                    continue;
                }
                Path pathFileOutput = Path.of(strFileOutput);

                Files.deleteIfExists(pathFileOutput);
                Files.createFile(pathFileOutput);
                return pathFileOutput;
            }
            catch (IOException ex){
                System.err.println("Ошибка создания файла " + strFileOutput);
                System.err.println("Ошибка:" + ex.getMessage());
            }
            catch (InvalidPathException ex){
                System.err.println("Путь недействителен:" + strFileOutput);
                System.err.println("Ошибка:" + ex.getMessage());
            }
        }
    }

    public static int getKeyEncrypt(Scanner scanner){
        while (true) {
            System.out.println("Введите ключ (число от 0 до "+ (ALPHABET.length) + ") :");
            System.out.print("> ");
            try {
                int keyEncrypt = scanner.nextInt();
                if (keyEncrypt < 0 || keyEncrypt > ALPHABET.length){
                    throw new InputMismatchException();
                }
                return keyEncrypt;
            } catch (InputMismatchException ex) {
                System.out.println("Ошибка ввода! Не верно задан ключ шифрования!");
                System.out.println("Повторите ввод.");
                System.out.print("> ");
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

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(Double.toString(value));
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

}
