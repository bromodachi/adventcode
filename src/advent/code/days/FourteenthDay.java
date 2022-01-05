package advent.code.days;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class FourteenthDay extends DaySolver{

    String startString;

    Map<String, Character> map;
    Map<String, Character> mapCopy;

    Map<String, Long> charCount;

    protected FourteenthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        map = new HashMap<>();
        Scanner scanner = readFile.toScanner();
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            if (startString == null) {
                startString = line;
            }
            else if (!line.isBlank()) {
                final String [] split = line.split(" -> ");
                map.put(split[0], split[1].charAt(0));
            }
        }
        mapCopy = new HashMap<>(map);
        charCount = loadMap();
    }

    private Map<String, Long> loadMap() {
        Map<String, Long> newMap = new HashMap<>();
        for (int i = 1; i < startString.length(); i ++) {
            final String key = "" + startString.charAt(i - 1) + startString.charAt(i);
            newMap.put(key, newMap.getOrDefault(key, 0L) + 1);
        }
        newMap.put("" + startString.charAt(startString.length() - 1), 1L);
        return newMap;
    }

    private Map<String, Long> getNextStep() {
        Map<String, Long> newMap = new HashMap<>();
        for (String key : charCount.keySet()) {
            if (map.containsKey(key)) {
                final String key1 = "" + key.charAt(0) + map.get(key);
                final String key2 = "" + map.get(key) + key.charAt(1);
                newMap.put(key1, newMap.getOrDefault(key1, 0L) + charCount.get(key));
                newMap.put(key2, newMap.getOrDefault(key2, 0L) + charCount.get(key));
            }
            else {
                newMap.put(key, charCount.get(key));
            }
        }
        return newMap;
    }

    @Override
    public void solveFirstProblem() {
        calculate(0, 10);
    }

    @Override
    public void solveSecondProblem() {
        calculate(10, 40);
    }


    private void calculate(int start, int end){
        for (int step = start; step < end; step ++) {
            charCount = getNextStep();
        }
        Map<Character, Long> helper = new HashMap<>();
        for (String key: charCount.keySet()) {
            final char c1 = key.charAt(0);
            helper.put(c1, helper.getOrDefault(c1, 0L) + charCount.get(key));
            // you don't have to count the second char
//            if (key.length() > 1) {
//                final char c2 = key.charAt(1);
//                helper.put(c2, helper.getOrDefault(c2, 0L) + charCount.get(key));
//            }
        }
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        for (Character key : helper.keySet()) {
            min = Math.min(helper.get(key), min);
            max = Math.max(helper.get(key), max);
        }
        printAnswer(max - min);
        // if counting the second char.
//        BigDecimal a = BigDecimal.valueOf(max);
//        BigDecimal b = BigDecimal.valueOf(min);
//
//        BigDecimal answer = a.subtract(b).setScale(0, RoundingMode.HALF_UP).divide(BigDecimal.valueOf(2));
//        printAnswer(answer.longValue());
    }
}
