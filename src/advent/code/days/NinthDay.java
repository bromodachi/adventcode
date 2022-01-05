package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class NinthDay extends DaySolver{

    final int[][] input;

    final int columnLength, rowLength;

    protected NinthDay(String fileName) throws FileNotFoundException {
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

    private final int[][] DIRECTIONS = new int[][]{
            new int [] {-1,0}, // UP
            new int [] {1,0},  // DOWN
            new int [] {0,-1}, // LEFT
            new int [] {0,1}, // RIGHT
    };

    private boolean isValid(int newValue, int length) {
        return newValue >= 0 && newValue < length;
    }

    private boolean isLowestValue(int[][] grid, int row, int column) {
        final int value = grid[row][column];

        for (int[] direction : DIRECTIONS) {
            int newRow = row + direction[0];
            int newColumn = column + direction[1];

            if (isValid(newRow ,rowLength) && isValid(newColumn, columnLength)) {
                if (grid[newRow][newColumn] <= value) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void solveFirstProblem() {
        long answer = 0;
        for (int row = 0; row < input.length; row ++) {
            for (int column = 0; column < input[0].length; column ++) {
                // top or bottom.
                if (isLowestValue(input, row, column)) {
                    answer += input[row][column] + 1;
                }
            }
        }
        printAnswer(answer);
    }

    public int twoDToOneD(int row, int column) {
        return row * columnLength + column;
    }

    private int toRow(int value) {
        return value / columnLength;
    }

    private int toColumn(int value) {
        return value % columnLength;
    }

    private void performBFS(boolean [][] visited, int row, int column, PriorityQueue<Integer> priorityQueue) {
        Queue<int []> queue = new LinkedList<>();
        queue.add(new int [] {twoDToOneD(row, column), input[row][column]});

        int maxVisited = 0;
        while (!queue.isEmpty()) {
            final int [] values = queue.poll();
            final int innerRow = toRow(values[0]);
            final int innerColumn = toColumn(values[0]);
            final int currValue = values[1];
            if (visited[innerRow][innerColumn]) {
                continue;
            }
            maxVisited += 1;
            visited[innerRow][innerColumn] = true;
            for (int[] direction : DIRECTIONS) {
                int newRow = innerRow + direction[0];
                int newColumn = innerColumn + direction[1];

                if (isValid(newRow ,rowLength) && isValid(newColumn, columnLength) && input[newRow][newColumn] != 9 && !visited[newRow][newColumn]) {
                    queue.add(new int[]{twoDToOneD(newRow, newColumn), currValue + 1});
                }
            }
        }
        priorityQueue.add(maxVisited);
    }

    @Override
    public void solveSecondProblem() {
        long answer = 1;
        boolean [][] visited = new boolean[rowLength][columnLength];
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(Collections.reverseOrder());
        for (int row = 0; row < rowLength; row ++) {
            for (int column = 0; column < columnLength; column ++) {
                if (isLowestValue(input, row, column)) {
                    performBFS(visited, row, column, priorityQueue);
                }
            }
        }
        int k = 3;
        while (k != 0 && !priorityQueue.isEmpty()) {
            long ans = priorityQueue.poll();
            answer *= ans;
            k --;
        }
        printAnswer(answer);
    }
}
