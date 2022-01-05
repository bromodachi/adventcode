package advent.code.days;

import advent.code.utils.ConverterUtil;

import java.io.FileNotFoundException;
import java.util.Scanner;

public class SeventhDay extends DaySolver{

    int [] input;

    protected SeventhDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        while (scanner.hasNext()) {
            input = ConverterUtil.readLineAsArray(scanner.nextLine());
        }
    }

    private int solve(boolean includePreviousStep) {
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;

        for (int value : input) {
            min = Math.min(value, min);
            max = Math.max(value, max);
        }

        int [][] board = new int[input.length][max - min + 1];

        for (int row = 0; row < input.length; row ++) {
            final int value = input[row];
            int step = 1;
            for (int column = value - 1; column >= 0; column --) {
                board[row][column] += (includePreviousStep ? board[row][column + 1] : 0) + step;
                step += 1;
            }
            step = 1;
            for (int column = value + 1; column < board[0].length; column ++) {
                board[row][column] += (includePreviousStep ? board[row][column - 1] : 0) + step;
                step += 1;
            }
        }
        int [] dp = new int[board[0].length];
        System.arraycopy(board[0], 0, dp, 0, dp.length);

        for (int row = 1; row < input.length; row ++) {
            for (int column = 0; column < dp.length; column ++) {
                dp[column] += board[row][column];
            }
        }
        min = Integer.MAX_VALUE;
        for (int val : dp) {
            min = Math.min(val, min);
        }
        return min;
    }


    @Override
    public void solveFirstProblem() {
        printAnswer(solve(false));
    }

    @Override
    public void solveSecondProblem() {
        printAnswer(solve(true));
    }
}
