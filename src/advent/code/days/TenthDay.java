package advent.code.days;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class TenthDay extends DaySolver{

    List<String> list;
    List<String> nonCorrupted;
    protected TenthDay(String fileName) throws IllegalAccessException {
        super(fileName);
        list = readFile.readFileAsString();
    }

    private int worthPoint(char c) {
        switch (c) {
            case ')':
                return 3;
            case ']':
                return 57;
            case '}':
                return 1197;
            case '>':
                return 25137;
        }
        return 0;
    }

    private int worthPointProblem2(char c) {
        switch (c) {
            case ')':
                return 1;
            case ']':
                return 2;
            case '}':
                return 3;
            case '>':
                return 4;
        }
        return 0;
    }

    private boolean isClosing(char c) {
        return c == ')' || c == ']' || c == '}' || c == '>';
    }

    private char getOpposite(char c) {
        switch (c) {
            case '(':
                return ')';
            case '[':
                return ']';
            case '{':
                return '}';
            case '<':
                return '>';
        }
        return '0'; // never going to happen
    }

    private boolean isValidPair(char open, char close) {
        return open == '(' && close == ')' ||
                open == '[' && close == ']' ||
                open == '{' && close == '}' ||
                open == '<' && close == '>';

    }
    public boolean isCorrupted(Stack<Character> stack, Map<Character, Integer> map, String s) {

        for (int i = 0; i < s.length(); i ++) {
            final char curr = s.charAt(i);
            if (!stack.isEmpty() && isClosing(curr)) {
                if (!isValidPair(stack.peek(),curr)) {
                    map.put(curr, map.getOrDefault(curr,  0) + 1);
                    return true;
                }
                stack.pop();
            }
            else {
                stack.push(curr);
            }
        }
        return false;
    }

    public long foundInvalidCharInLine(Stack<Character> stack, List<Character> listOfCharacters, String s) {

        for (int i = 0; i < s.length(); i ++) {
            final char curr = s.charAt(i);
            if (!stack.isEmpty() && isClosing(curr)) {
                if (!isValidPair(stack.peek(),curr)) {
                    listOfCharacters.add(getOpposite(stack.peek()));
                }
                stack.pop();
            }
            else {
                stack.push(curr);
            }
        }
        while (!stack.isEmpty()) {
            listOfCharacters.add(getOpposite(stack.pop()));
        }
        long totalScore = 0;
        for (char c:  listOfCharacters) {
            totalScore = (totalScore * 5) + worthPointProblem2(c);
        }
        return totalScore;
    }

    @Override
    public void solveFirstProblem() {
        Stack<Character> stack = new Stack<>();

        Map<Character, Integer> map = new HashMap<>();

        nonCorrupted = new ArrayList<>();
        for (String input : list) {
            if (!isCorrupted(stack, map, input)) {
                nonCorrupted.add(input);
            }
        }
        while (!stack.isEmpty()) {
            map.put(stack.peek(), map.getOrDefault(stack.peek(),  0) + 1);
            stack.pop();
        }
        int count = 0;
        for (char key : map.keySet()) {
            count += map.get(key) * worthPoint(key);
        }
        printAnswer(count);
    }

    @Override
    public void solveSecondProblem() {
        Stack<Character> stack = new Stack<>();

        List<Long> values = new ArrayList<>();
        for (String input : nonCorrupted) {
            values.add(foundInvalidCharInLine(stack,new ArrayList<>(), input));
        }
        Collections.sort(values);
        printAnswer(values.get(values.size() / 2));
    }
}
