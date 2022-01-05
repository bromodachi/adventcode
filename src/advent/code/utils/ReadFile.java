package advent.code.utils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ReadFile {

    final String fileName;

    public ReadFile(String fileName) {
        this.fileName = fileName;
    }



    public void readLine(Consumer<String> consumer) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                consumer.accept(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public <T> List<T> readLineConvertToList(Function<String, T> converter) throws IllegalAccessException {
        List<T> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(converter.apply(line));
            }
        } catch (IOException e) {
            throw new IllegalAccessException("Invalid");
        }
        return list;
    }

    public List<String> readFileAsString() throws IllegalAccessException {
        List<String> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
        } catch (IOException e) {
            throw new IllegalAccessException("Invalid");
        }
        return list;
    }


    public Scanner toScanner() throws FileNotFoundException {
        return new Scanner(getInputStream());
    }

    private InputStream getInputStream() {
        Class<ReadFile> clazz = ReadFile.class;
        return clazz.getResourceAsStream(fileName);
    }
}