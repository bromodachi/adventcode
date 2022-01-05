package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Scanner;

public class FifteenthDay extends DaySolver{

    private final int[][] DIRECTIONS = new int[][]{
            new int [] {-1,0}, // UP
            new int [] {1,0},  // DOWN
            new int [] {0,-1}, // LEFT
            new int [] {0,1}, // RIGHT
    };

    private boolean isValid(int newValue, int length) {
        return newValue >= 0 && newValue < length;
    }

    final int[][] input;

    final int columnLength, rowLength;

    protected FifteenthDay(String fileName) throws FileNotFoundException {
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
        columnLength = this.input[0].length;
        rowLength = this.input.length;
    }

    private int toRow(int value, int columnLength) {
        return value / columnLength;
    }

    private int toColumn(int value, int columnLength) {
        return value % columnLength;
    }
    public int twoDToOneD(int row, int column) {
        return row * columnLength + column;
    }
    public int twoDToOneD(int row, int column, int columnLength) {
        return row * columnLength + column;
    }

    private int findLowest(Map<Integer, List<int []>> graph, int columnLength, int [][]input) {
        PriorityQueue<int[]> heap = new PriorityQueue<>(Comparator.comparingInt(e -> e[1]));
        heap.offer(new int[]{0, 0});
        Map<Integer, Integer> table = new HashMap<>();

        while(!heap.isEmpty()) {
            int [] info = heap.poll();
            int targetNode = info[0];
            int weight = info[1];
            if (table.containsKey(targetNode)) {
                continue;
            }
            table.put(targetNode, weight + input[toRow(targetNode, columnLength)][toColumn(targetNode, columnLength)]);
            if (graph.containsKey(targetNode)) {
                for (int [] edge : graph.get(targetNode)) {
                    int neighbor = edge[0];
                    int neighborWeight = edge[1];
                    if (!table.containsKey(neighbor)) {
                        heap.offer(new int[] {neighbor, weight + neighborWeight});
                    }
                }
            }
        }
        return table.get(twoDToOneD(input.length - 1, input[0].length - 1, columnLength)) - input[0][0];
    }

    @Override
    public void solveFirstProblem() {
        Map<Integer, List<int []>> graph = new HashMap<>();
        for (int row = 0; row < rowLength; row ++ ) {
            for (int column = 0; column < columnLength; column ++ ){
                final int nodeValue = twoDToOneD(row, column);
                if (!graph.containsKey(nodeValue)) {
                    graph.put(nodeValue, new ArrayList<>());
                }
                for (int [] direction : DIRECTIONS) {
                    final int newRow = row + direction[0];
                    final int newColumn = column + direction[1];
                    if (isValid(newRow, rowLength) && isValid(newColumn, columnLength) ) {
                        graph.get(nodeValue).add(new int[]{twoDToOneD(newRow, newColumn), input[row][column]});
                    }
                }
            }
        }

        printAnswer(findLowest(graph, input.length, input));
    }

    @Override
    public void solveSecondProblem() {
        int [][] fiveTimeLarger = new int[rowLength * 5][columnLength * 5];
        for (int row = 0; row < fiveTimeLarger.length; row ++) {
            for (int column = 0; column < fiveTimeLarger[0].length; column ++ ){
                if (row < input.length && column < input[0].length) {
                    fiveTimeLarger[row][column] = input[row][column];
                }
                else {
                    int newValue;
                    if (row <= input.length && column >= input[0].length) {
                        newValue = fiveTimeLarger[row][column - input[0].length] + 1;
                    }
                    else if (column <= input[0].length) {
                        newValue = fiveTimeLarger[row - input.length][column] + 1;
                    }
                    else {
                        newValue = fiveTimeLarger[row][column - input[0].length] + 1;
                    }
                    fiveTimeLarger[row][column] = newValue > 9 ? 1 : newValue;
                }
            }
        }

        Map<Integer, List<int []>> graph = new HashMap<>();
        for (int row = 0; row < fiveTimeLarger.length; row ++ ) {
            for (int column = 0; column < fiveTimeLarger[0].length; column ++ ){
                final int nodeValue = twoDToOneD(row, column, fiveTimeLarger[0].length);
                if (!graph.containsKey(nodeValue)) {
                    graph.put(nodeValue, new ArrayList<>());
                }
                for (int [] direction : DIRECTIONS) {
                    final int newRow = row + direction[0];
                    final int newColumn = column + direction[1];
                    if (isValid(newRow, fiveTimeLarger.length) && isValid(newColumn, fiveTimeLarger[0].length) ) {
                        graph.get(nodeValue).add(new int[]{twoDToOneD(newRow, newColumn, fiveTimeLarger[0].length), fiveTimeLarger[row][column]});
                    }
                }
            }
        }
        printAnswer(findLowest(graph, fiveTimeLarger[0].length, fiveTimeLarger));
    }
}
