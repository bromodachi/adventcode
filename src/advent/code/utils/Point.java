package advent.code.utils;

public class Point {
    private final int row;
    private final int column;
    public static class Builder {
        private int row;
        private int column;

        public Point.Builder setRow(int r) {
            this.row = r;
            return this;
        }

        public Point.Builder setColumn(int c) {
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

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    /**
     * sets the row and column to the respective variables.
     * @param row
     * @param column
     * @return
     */
    public static Point of(int row, int column) {
        return new Point(column, row);
    }

    public static Point of(String s) {
        int value = 0;
        Point.Builder builder = new Point.Builder();
        for (int i = 0; i < s.length(); i ++) {
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
