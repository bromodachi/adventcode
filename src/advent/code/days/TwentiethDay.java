package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class TwentiethDay extends DaySolver{

    char [] chars;
    char [][] input;
    protected TwentiethDay(String fileName) throws FileNotFoundException {
        super(fileName);
        List<char []> tempInput = new ArrayList<>();
        Scanner scanner = readFile.toScanner();
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            if (chars == null) {
                chars = line.toCharArray();
            }
            else if (!line.isEmpty()) {
                tempInput.add(line.toCharArray());
            }
        }
        input = new char[tempInput.size() + 2][tempInput.get(0).length + 2];
        for (char[] value : input) {
            Arrays.fill(value, '.');
        }
        for (int row = 1; row <= tempInput.size(); row++) {
            System.arraycopy(tempInput.get(row - 1), 0, input[row], 1, tempInput.get(0).length);
        }
    }

    private char [][] fillGraph( char [][] from, char fillWith){
        char [][] input = new char[from.length + 2][from[0].length + 2];
        for (char[] value : input) {
            Arrays.fill(value, fillWith);
        }
        for (int row = 1; row <= from.length; row++) {
            System.arraycopy(from[row - 1], 0, input[row], 1, from[0].length);
        }

        return input;
    }

    private final int[][] DIRECTIONS = new int[][]{
            new int [] {-1,-1},  // UP LEFT
            new int [] {-1,0}, // UP
            new int [] {-1,1}, // UP RIGHT
            new int [] {0,-1}, // LEFT
            new int [] {0,0}, // CURRENT
            new int [] {0,1}, // RIGHT
            new int [] {1,-1},  // DOWN LEFT
            new int [] {1,0},  // DOWN
            new int [] {1,1},  // DOWN RIGHT
    };

    private boolean isValid(int newValue, int length) {
        return newValue >= 0 && newValue < length;
    }

    private String getBinaryString(int row, int column, char [][] oldInput, boolean isDark) {
        StringBuilder sb = new StringBuilder();
        for (int [] direction : DIRECTIONS) {
            final int newRow = row + direction[0];
            final int newColumn = column + direction[1];
            if (isValid(newRow, oldInput.length) && isValid(newColumn, oldInput[0].length)) {
                if (oldInput[newRow][newColumn] == '#') {
                    sb.append('1');
                }
                else {
                    sb.append('0');
                }
            }
            else {
                if (isDark) {
                    sb.append('0');
                }
                else {
                    sb.append('1');
                }
            }
        }
        return sb.toString();
    }

    private char changeChar(int row, int column, char [][] oldInput, boolean isDark) {
        int value = Integer.parseInt(getBinaryString(row, column, oldInput, isDark), 2);
        return chars[value];
    }

    private int solve(int iteration){
        int times = 0;

        char [][] enhanced = new char[input.length][input[0].length];
        char [][] newArray = new char[enhanced.length][enhanced[0].length];

        for (int row = 0; row < input.length; row++) {
            System.arraycopy(input[row], 0, enhanced[row], 0, input[0].length);
        }
        boolean isDark = true;
        while (times < iteration) {
            for (int row = 0; row < enhanced.length; row++) {
                for (int column = 0; column < enhanced[0].length; column++) {
                    newArray[row][column] = changeChar(row, column, enhanced, isDark);
                }
            }
            enhanced = fillGraph(newArray, isDark ? '#' : '.');
            newArray = new char[enhanced.length][enhanced[0].length];
            isDark = !isDark;
            times += 1;
        }
        int count = 0;
        for (char[] value : enhanced) {
            for (int column = 0; column < enhanced[0].length; column++) {
                if (value[column] == '#') {
                    count += 1;
                }
            }
        }
        return count;
    }
    @Override
    public void solveFirstProblem() {
        printAnswer(solve(2));

    }

    @Override
    public void solveSecondProblem() {
        printAnswer(solve(50));
    }
}
