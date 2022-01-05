package advent.code.days;

import advent.code.utils.Point;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ThirteenthDay extends DaySolver{

    List<FoldAlong> foldAlongList;
    List<Point> listOfPoints;
    int maxRow;
    int maxColumn;
    static class FoldAlong {
        Integer row;
        Integer column;

        public FoldAlong(Integer x, Integer y) {
            this.row = y;
            this.column = x;
        }

        public static FoldAlong of(String s) {
            int index = s.indexOf('=');
            Integer point = Integer.valueOf(s.substring(index + 1));
            if (s.charAt(index - 1) == 'x') {
                return new FoldAlong(point, null);
            }
            else {
                return new FoldAlong(null, point);
            }
        }

        private Point adjustPoint(Point point, int maxRow, int maxColumn) {
            // fold up
            if (this.row != null && point.getRow() > this.row) {
                return Point.of(Math.abs(maxRow - point.getRow()) - 1, point.getColumn());
            }
            // fold to left
            if (this.column != null && point.getColumn() > this.column) {
                return Point.of(point.getRow(), Math.abs(maxColumn - point.getColumn()) -1);
            }
            return point;
        }
    }
    protected ThirteenthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        boolean foldAlongCommands = false;
        foldAlongList = new ArrayList<>();
        listOfPoints = new ArrayList<>();
        while (scanner.hasNext()) {
            final String line = scanner.nextLine();
            if (line.isBlank()) {
                foldAlongCommands = true;
            }
            else if(foldAlongCommands) {
                foldAlongList.add(FoldAlong.of(line));
            }
            else {
                listOfPoints.add(Point.of(line));
            }
        }
    }

    private int drawBoard(boolean showOutput){

        char [][] input = new char[maxRow][maxColumn];

        for (int row = 0; row < input.length; row ++) {
            for (int column = 0; column < input[0].length; column ++) {
                input[row][column]  = '.';
            }
        }

        for (Point point : listOfPoints) {
            if (point.getRow() < 0 || point.getColumn() < 0) {
                continue;
            }
            input[point.getRow()][point.getColumn()] = '#';
        }
        int count = 0;
        for (char[] chars : input) {
            for (int column = 0; column < input[0].length; column++) {
                if (chars[column] == '#') {
                    count += 1;
                }
            }
        }
        if (showOutput) {
            for (char[] arr : input) {
                for (char c : arr) {
                    if (c == '#') {
                        System.out.print("██");
                    }
                    else {
                        System.out.print("  ");
                    }
                }
                System.out.println();
            }
        }
        return count;
    }

    private void calculateMaxRowAndColumn() {
        int maxRow = Integer.MIN_VALUE;
        int maxColumn = Integer.MIN_VALUE;

        for (Point point : listOfPoints) {
            maxRow = Math.max(point.getRow(), maxRow);
            maxColumn = Math.max(point.getColumn(), maxColumn);
        }
        this.maxRow = maxRow + 1;
        this.maxColumn = maxColumn + 1;
    }
    private void fold(FoldAlong along) {
        for (int i = 0; i < listOfPoints.size() ; i ++) {
            listOfPoints.set(i, along.adjustPoint(listOfPoints.get(i), this.maxRow, this.maxColumn));
        }
        this.maxRow = along.row == null ? maxRow  : along.row;
        this.maxColumn = along.column == null ? maxColumn : along.column;
    }
    @Override
    public void solveFirstProblem() {
        calculateMaxRowAndColumn();
        fold(foldAlongList.get(0));
        printAnswer(drawBoard(false));
    }

    @Override
    public void solveSecondProblem() {
        for (int i = 1; i < foldAlongList.size(); i ++) {
            fold(foldAlongList.get(i));
        }
        printAnswer(drawBoard(true));
    }
}
