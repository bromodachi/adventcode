package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Scanner;

public class EleventhDay extends DaySolver{

    final int[][] input;

    final int columnLength, rowLength;

    protected EleventhDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        List<int[]> input = new ArrayList<>();
        while (scanner.hasNext()) {
            char [] line = scanner.nextLine().toCharArray();
            int [] arr = new int[line.length];
            int index =0;
            for (char c : line) {
                arr[index++] = c - '0';
            }
            input.add(arr);
        }
        this.input = new int[input.size()][input.get(0).length];
        for (int row = 0; row < input.size(); row  ++) {
            System.arraycopy(input.get(row), 0, this.input[row], 0, input.get(0).length);
        }
        input = null;
        columnLength = this.input.length;
        rowLength = this.input.length;
    }


    public int twoDToOneD(int row, int column) {
        return row * columnLength + column;
    }

    private final int[][] DIRECTIONS = new int[][]{
            new int [] {-1,0}, // UP
            new int [] {1,0},  // DOWN
            new int [] {0,-1}, // LEFT
            new int [] {0,1}, // RIGHT
            new int [] {-1,1}, // UP RIGHT
            new int [] {1,1},  // DOWN RIGHT
            new int [] {1,-1},  // DOWN LEFT
            new int [] {-1,-1},  // UP LEFT
    };

    private int toRow(int value) {
        return value / columnLength;
    }

    private int toColumn(int value) {
        return value % columnLength;
    }

    private boolean isValid(int newValue, int length) {
        return newValue >= 0 && newValue < length;
    }

    private void incrementAllByOne(Queue<Integer> queue) {
        for (int row = 0; row < rowLength; row++) {
            for (int column = 0; column < columnLength; column++) {
                input[row][column] += 1;
                if (input[row][column] == 10) {
                    queue.add(twoDToOneD(row, column));
                }
            }
        }
    }

    private long flashAllNines(Queue<Integer> queue){
        long count = 0;
        while (!queue.isEmpty()) {
            final int value = queue.poll();
            final int row = toRow(value);
            final int column = toColumn(value);

            if (input[row][column] == 0) {
                continue;
            }
            input[row][column] = 0;
            count += 1;
            for (int [] direction : DIRECTIONS) {
                final int newRow = row + direction[0];
                final int newColumn = column + direction[1];
                if (isValid(newRow, rowLength) && isValid(newColumn, columnLength) && input[newRow][newColumn] != 0) {
                    input[newRow][newColumn] += 1;
                    if (input[newRow][newColumn] >= 10) {
                        queue.add(twoDToOneD(newRow, newColumn));
                    }
                }
            }
        }
        return count;
    }

    @Override
    public void solveFirstProblem() {
        // first increase by one.
        // then any number that is 10, set the numbers to zero. Add all adjacent values to 1.
        Queue<Integer> queue = new LinkedList<>();
        long count = 0;
        for (int step = 0; step< 100; step ++ ) {
            incrementAllByOne(queue);
            count += flashAllNines(queue);
        }

        printAnswer(count);
    }

    @Override
    public void solveSecondProblem() {

        Queue<Integer> queue = new LinkedList<>();
        for (int step = 0; step< Integer.MAX_VALUE; step ++ ) {
            incrementAllByOne(queue);
            if (flashAllNines(queue) == ((long) rowLength * columnLength)) {
                printAnswer(step + 1 + 100) ;
                break;
            }
        }
    }
}
