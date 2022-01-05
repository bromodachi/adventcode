package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class TwentyFiftiethDay extends DaySolver{
    char [][] input;
    protected TwentyFiftiethDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        List<char []> tempList = new ArrayList<>();
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            tempList.add(line.toCharArray());
        }
        input = tempList.toArray(new char[0][]);
    }

    public int twoDToOneD(int row, int column, int columnLength) {
        return row * columnLength + column;
    }

    private int toRow(int value, int columnLength) {
        return value / columnLength;
    }

    private int toColumn(int value, int columnLength) {
        return value % columnLength;
    }


    private boolean fishMoved(char [][] input) {
        boolean moved = false;
        Set<Integer> set = new HashSet<>();
        Set<Integer> needToDelete = new HashSet<>();
        for (int row= 0; row < input.length; row ++) {
            for (int column= 0; column < input[0].length; column ++) {
                if (set.contains(twoDToOneD(row, column, input[0].length))) {
                    continue;
                }
                switch (input[row][column]) {
                    case '>':
                        if (column + 1 < input[0].length) {
                            if (input[row][column + 1] == '.') {
                                needToDelete.add(twoDToOneD(row, column, input[0].length));
                                input[row][column + 1] = '>';
                                set.add(twoDToOneD(row, column + 1, input[0].length));
                                moved = true;
                            }
                        }
                        // can move to 0?
                        else {
                            if (input[row][0] == '.') {
                                needToDelete.add(twoDToOneD(row, column, input[0].length));
                                input[row][0] = '>';
                                moved = true;
                            }
                        }
                        break;
                    default:
                }
            }
        }
        for (int i : needToDelete) {
            final int row = toRow(i, input[0].length);
            final int column = toColumn(i, input[0].length);
            input[row][column] = '.';
        }
        needToDelete.clear();

        for (int row= 0; row < input.length; row ++) {
            for (int column= 0; column < input[0].length; column ++) {
                if (set.contains(twoDToOneD(row, column, input[0].length))) {
                    continue;
                }
                switch (input[row][column]) {
                    case 'v':
                        if (row + 1 < input.length) {
                            if (input[row + 1][column] == '.') {
                                needToDelete.add(twoDToOneD(row, column, input[0].length));
                                input[row + 1][column] = 'v';
                                set.add(twoDToOneD(row + 1, column, input[0].length));
                                moved = true;
                            }
                        }
                        // can move to 0?
                        else {
                            if (input[0][column] == '.') {
                                needToDelete.add(twoDToOneD(row, column, input[0].length));
                                input[0][column] = 'v';
                                moved = true;
                            }
                        }
                        break;
                    default:
                }
            }
        }
        for (int i : needToDelete) {
            final int row = toRow(i, input[0].length);
            final int column = toColumn(i, input[0].length);
            input[row][column] = '.';
        }
        return moved;
    }
    @Override
    public void solveFirstProblem() {
        char [][] mutable = new char[input.length][input[0].length];
        for (int row = 0; row < input.length; row ++) {
            System.arraycopy(input[row], 0, mutable[row], 0, input[0].length);
        }
        int step = 0;
       for (; step < Integer.MAX_VALUE; step ++) {
           if (!fishMoved(mutable)) {
               break;
           }
       }

       printAnswer(step);
    }

    @Override
    public void solveSecondProblem() {

    }
}
