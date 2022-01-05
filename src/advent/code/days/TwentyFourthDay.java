package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.function.Function;

public class TwentyFourthDay extends DaySolver{

    List<Input> allInputs;
    static class Input {
        List<Operation> operations;

        char inputChar;
        public Input(char c) {
            inputChar = c;
        }

        public boolean performOperation(Map<Character, Integer> map) throws IllegalAccessException {
            for (Operation operation : operations) {
                operation.performOperation(map);

            }
            return true;
        }

        public void setOperations(List<Operation> operations) {
            this.operations = new ArrayList<>(operations);
        }
    }

    static class IntOrChar {

        Integer intValue;
        Character charVal;
        public IntOrChar(String s) {
            if (Character.isAlphabetic(s.charAt(0))) {
                charVal = s.charAt(0);
            }
            else {
                intValue = Integer.valueOf(s);
            }
        }
        public boolean isCharVal() {
            return intValue == null;
        }
        public int getValue(Map<Character, Integer> map) {
            if (isCharVal()) {
                return map.get(charVal);
            }
            return intValue;
        }
    }

    static abstract class Operation {
        char leftSide;
        IntOrChar rightSide;

        public Operation(char leftSide, String rightSide) {
            this.leftSide = leftSide;
            this.rightSide = new IntOrChar(rightSide);
        }
        public abstract int performOperation(Map<Character, Integer> map) throws IllegalAccessException;

        public static Operation of(String line) {
            final String [] lineArr = line.split(" ");

            switch (lineArr[0]) {
                case "add":
                    return new Add(lineArr[1].charAt(0), lineArr[2]);
                case "mul":
                    return new Multiply(lineArr[1].charAt(0), lineArr[2]);
                case "div":
                    return new Div(lineArr[1].charAt(0), lineArr[2]);
                case "mod":
                    return new Mod(lineArr[1].charAt(0), lineArr[2]);
                case "eql":
                    return new Eql(lineArr[1].charAt(0), lineArr[2]);
            }
            return null;
        }
    }
    static class Div extends Operation {

        public Div(char leftSide, String rightSide) {
            super(leftSide, rightSide);
        }

        @Override
        public int performOperation(Map<Character, Integer> map) throws IllegalAccessException {
            int val = map.get(leftSide) / rightSide.getValue(map);
            if (rightSide.getValue(map) == 0) {
                throw new IllegalAccessException("right side can't be zero");
            }
            map.put(leftSide, val);
            return val;
        }
    }

    static class Mod extends Operation {

        public Mod(char leftSide, String rightSide) {
            super(leftSide, rightSide);
        }

        @Override
        public int performOperation(Map<Character, Integer> map) throws IllegalAccessException {
            int val = map.get(leftSide) % rightSide.getValue(map);
            if (map.get(leftSide) < 0 || rightSide.getValue(map) <= 0) {
                throw new IllegalAccessException("right side nor left side can be zero");
            }
            map.put(leftSide, val);
            return val;
        }
    }

    static class Eql extends Operation {

        public Eql(char leftSide, String rightSide) {
            super(leftSide, rightSide);
        }

        @Override
        public int performOperation(Map<Character, Integer> map) {
            int val = map.get(leftSide) == rightSide.getValue(map) ? 1 : 0;
            map.put(leftSide, val);
            return val;
        }
    }
    static class Add extends Operation {

        public Add(char leftSide, String rightSide) {
            super(leftSide, rightSide);
        }

        @Override
        public int performOperation(Map<Character, Integer> map) {
            int val = map.get(leftSide) + rightSide.getValue(map);
            map.put(leftSide, val);
            return val;
        }
    }

    static class Multiply extends Operation {

        public Multiply(char leftSide, String rightSide) {
            super(leftSide, rightSide);
        }

        @Override
        public int performOperation(Map<Character, Integer> map) {
            int val = map.get(leftSide) * rightSide.getValue(map);
            map.put(leftSide, val);
            return val;
        }
    }

    protected TwentyFourthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        allInputs = new ArrayList<>();
        Scanner scanner = readFile.toScanner();
        Input input = null;
        List<Operation> operations = new ArrayList<>();
        while (scanner.hasNext())  {
            final String line = scanner.nextLine();
            final String [] info = line.split(" ");
            if (info[0].equals("inp")) {
                if (input != null) {
                    input.setOperations(operations);
                    allInputs.add(input);
                }
                input = new Input(info[1].charAt(0));
                operations = new ArrayList<>();
            }
            else {
                operations.add(Operation.of(line));
            }
        }
        if (input != null) {
            input.setOperations(operations);
            allInputs.add(input);
        }
    }
    long answer = Integer.MIN_VALUE;
    long minAnswer = Long.MAX_VALUE;


    public void calculateLargest(StringBuilder stringBuilder){
        answer = Math.max(Long.parseLong(stringBuilder.toString()), answer);
    }


    public void calculateMin(StringBuilder stringBuilder){
        minAnswer = Math.min(Long.parseLong(stringBuilder.toString()), minAnswer);
    }

    Map<String, Boolean> memo = new HashMap<>();

    public boolean backtrack(int index, Map<Character, Integer> map, StringBuilder sb, int start, Function<Integer, Boolean> condition, int increment, Consumer<StringBuilder> consumer) {
        if (index == allInputs.size()) {
            if (map.get('z') == 0) {
                consumer.accept(sb);
            }
            return map.get('z') == 0;
        }
        Input input = allInputs.get(index);
        for (int i = start; condition.apply(i) ; i += increment) {
            Map<Character, Integer> mutable = new LinkedHashMap<>(map);
            mutable.put(input.inputChar, i);
            try {
                if (input.performOperation(mutable)) {
                    final String key = index+ ":" + i +":" + mutable;
                    if (memo.containsKey(key)) {
                        return memo.get(key);
                    }
                    sb.append(i);
                    boolean isTrue = backtrack(index + 1, mutable, sb, start, condition, increment, consumer);
                    memo.put(key, isTrue);
                    sb.deleteCharAt(index);
                    if (isTrue) {
                        return true;
                    }
                }
            } catch (IllegalAccessException e) {
                continue;
            }
            map.remove(input.inputChar);
        }
        return false;
    }

    @Override
    public void solveFirstProblem() {
        Map<Character, Integer> map = new LinkedHashMap<>();
        map.put('w', 0);
        map.put('x', 0);
        map.put('y', 0);
        map.put('z', 0);
        StringBuilder sb = new StringBuilder();
        backtrack(0, map, sb, 9, i-> i > 0, -1, this::calculateLargest);
        printAnswer(answer);
    }

    @Override
    public void solveSecondProblem() {
        Map<Character, Integer> map = new LinkedHashMap<>();
        memo = new HashMap<>();
        map.put('w', 0);
        map.put('x', 0);
        map.put('y', 0);
        map.put('z', 0);
        StringBuilder sb = new StringBuilder();
        backtrack(0, map, sb, 1, i-> i < 10, +1, this::calculateMin);
        printAnswer(minAnswer);
    }
}
