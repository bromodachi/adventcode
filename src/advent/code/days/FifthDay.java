package advent.code.days;

import java.util.List;

public class FifthDay extends DaySolver{
    int [][] grid;
    List<LineSegment> list;
    public static class LineSegment {
        public LineSegment(Point from, Point to) {
            this.from = from;
            this.to = to;
        }

        private final Point from;
        private final Point to;

        public static LineSegment of(String line) {
            int start = 0;
            final int until = line.indexOf("-");
            return new LineSegment(Point.of(line, start, until), Point.of(line, until, line.length() - 1));
        }

        public boolean isRowSame() {
            return from.row == to.row;
        }

        public boolean isColumnSame() {
            return from.column == to.column;
        }
    }
    public static class Point {
        int row;
        int column;
        public static class Builder {
            private int row;
            private int column;

            public Builder setRow(int r) {
                this.row = r;
                return this;
            }

            public Builder setColumn(int c) {
                this.column = c;
                return this;
            }

            public Point build(){
                return new Point(this.row, this.column);
            }
        }


        public Point(int fromX, int fromY) {
            // switching them to match the example
            this.row = fromY;
            this.column = fromX;
        }

        public static Point of(String s, int start, int end) {
            int value = 0;
            Point.Builder builder = new Point.Builder();
            for (int i = start; i <= end; i ++) {
                final char c = s.charAt(i);
                if (Character.isDigit(c)) {
                    value = value * 10 + (c - '0');
                }
                else if (c == ','){
                    builder.setRow(value);
                    value = 0;
                }
            }
            builder.setColumn(value);
            return builder.build();
        }
    }
    protected FifthDay(String fileName) throws IllegalAccessException {
        super(fileName);
        list =  readFile.readLineConvertToList(LineSegment::of);
        int maxColumn = 0;
        int maxRow = 0;
        for (LineSegment lineSegment : list) {
            maxRow = Math.max(Math.max(lineSegment.from.row, maxRow), lineSegment.to.row);
            maxColumn = Math.max(Math.max(lineSegment.from.column, maxColumn), lineSegment.to.column);
        }
        grid = new int[maxRow + 1][maxColumn + 1];
    }

    @Override
    public void solveFirstProblem() {
        for (LineSegment lineSegment : list) {
            if (lineSegment.isRowSame()) {
                int from = Math.min(lineSegment.from.column, lineSegment.to.column);
                int to = Math.max(lineSegment.from.column, lineSegment.to.column);
                for (int column = from; column <= to; column ++) {
                    grid[lineSegment.from.row][column] ++;
                }
            }
            else if (lineSegment.isColumnSame()) {
                int from = Math.min(lineSegment.from.row, lineSegment.to.row);
                int to = Math.max(lineSegment.from.row, lineSegment.to.row);
                for (int row = from; row <= to; row ++) {
                    grid[row][lineSegment.from.column] ++;
                }
            }
        }
        printAnswer(getCount());
    }

    private int getCount() {
        int count = 0;
        for (int row = 0; row < this.grid.length; row ++) {
            for (int column = 0; column < this.grid[0].length; column ++) {
                if (grid[row][column] > 1) {
                    count += 1;
                }
            }
        }
        return count;
    }

    @Override
    public void solveSecondProblem() {
        for (LineSegment lineSegment : list) {
            // can move diagonally?
            int row = Math.abs(lineSegment.from.row - lineSegment.to.row);
            int column = Math.abs(lineSegment.from.column - lineSegment.to.column);
            if (row == column) {
                int distanceRow = lineSegment.from.row - lineSegment.to.row;
                int distanceColumn = lineSegment.from.column - lineSegment.to.column;

                int stepForRow = distanceRow < 0 ? 1 : -1;
                int stepForColumn = distanceColumn < 0 ? 1 : -1;

                int rowStart = lineSegment.from.row;
                int columnStart = lineSegment.from.column;
                while (rowStart != lineSegment.to.row ) {
                    grid[rowStart][columnStart] += 1;
                    rowStart += stepForRow;
                    columnStart += stepForColumn;
                }
                grid[lineSegment.to.row][lineSegment.to.column] += 1;
            }
        }
        printAnswer(getCount());
    }
}
