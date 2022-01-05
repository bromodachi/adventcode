package advent.code.utils;

import java.util.ArrayList;
import java.util.List;

public class ConverterUtil {
    public static List<Integer> readLineAsList(String line) {
        List<Integer> list = new ArrayList<>();
        int value = 0;
        boolean readValue = false;
        for (int i = 0; i < line.length(); i ++) {
            final char c = line.charAt(i);
            if (Character.isDigit(c)) {
                readValue = true;
                value = value * 10 + (c - '0');
            }
            else {
                if (readValue) {
                    list.add(value);
                }
                readValue = false;
                value = 0;
            }
        }
        if (readValue) {
            list.add(value);
        }
        return list;
    }

    public static int [] readLineAsArray(String line) {
        int commaCount = 0;
        for (int i = 0; i < line.length(); i ++) {
            final char c = line.charAt(i);
            if (c == ',') {
                commaCount += 1;
            }
        }
        int [] list = new int[commaCount + 1];
        int value = 0;
        boolean readValue = false;
        int index =0;
        int sign = 1;
        for (int i = 0; i < line.length(); i ++) {
            final char c = line.charAt(i);
            if (Character.isDigit(c)) {
                readValue = true;
                value = value * 10 + (c - '0');
            }
            else if (c == '-') {
                sign = -1;
            }
            else {
                if (readValue) {
                    list[index] = value * sign;
                    index += 1;
                }
                sign = 1;
                readValue = false;
                value = 0;
            }
        }
        if (readValue) {
            list[index] = value * sign;
        }
        return list;
    }
}
