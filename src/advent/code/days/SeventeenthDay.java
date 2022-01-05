package advent.code.days;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class SeventeenthDay extends DaySolver{

    Range range;

    class Point {
        int x = 0;
        int y = 0;

        int xVelocity;
        int yVelocity;

        public Point(int xVelocity, int yVelocity) {
            this.xVelocity = xVelocity;
            this.yVelocity = yVelocity;
        }


        /**
         * @return true if we can move along the x axis.
         */
        public void movePoint() {
            if (xVelocity != 0) {
                x += xVelocity;
            }
            y += yVelocity;
            if (xVelocity > 0) {
                xVelocity -= 1;
            }
            else if (xVelocity < 0) {
                xVelocity += 1;
            }
            yVelocity -= 1;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }
    }

    class Range {
        final int xStart;
        final int xEnd;

        final int yStart;
        final int yEnd;

        public Range(int xStart, int xEnd, int yStart, int yEnd) {
            this.xStart = xStart;
            this.xEnd = xEnd;
            this.yStart = yStart;
            this.yEnd = yEnd;
        }

        public boolean isWithinRange(Point point) {
            return xStart <= point.getX() && point.getX() <= xEnd &&  yStart <= point.getY() && point.getY() <= yEnd;
        }

        public boolean isOverRange(Point point) {
            return  point.getY() < yStart;
        }
    }
    protected SeventeenthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        final String [] targetLine = scanner.nextLine().split(",");
        final String xLineWithEqual = targetLine[0].split(":")[1].split("=")[1];
        final String [] xValues = xLineWithEqual.split("\\.\\.");
        final int xStart = Integer.parseInt(xValues[0]);
        final int xEnd = Integer.parseInt(xValues[1]);
        final String yLineRemovedEqual = targetLine[1].split("=")[1];
        final String [] yValues = yLineRemovedEqual.split("\\.\\.");
        final int yStart = Integer.parseInt(yValues[0]);
        final int yEnd = Integer.parseInt(yValues[1]);
        range = new Range(xStart, xEnd, yStart, yEnd);
    }

    @Override
    public void solveFirstProblem() {
        int maximumHeight = Integer.MIN_VALUE;
        for (int x = 1 ; x < range.xEnd; x ++) {
            for (int y = 1 ; y < Math.abs(range.yStart); y ++) {
                Point point = new Point(x, y);
                int maxHeight = Integer.MIN_VALUE;
                while (true) {
                    point.movePoint();
                    maxHeight = Math.max(maxHeight, point.y);
                    if (range.isWithinRange(point)) {
                        maximumHeight = Math.max(maxHeight, maximumHeight);
                        break;
                    }
                    else if (range.isOverRange(point)) {
                        break;
                    }
                }

            }
        }
        printAnswer(maximumHeight);
    }

    @Override
    public void solveSecondProblem() {
        int count = 0;
        for (int x = 1; x <= range.xEnd ; x ++) {
            for (int y = range.yStart; y <= Math.abs(range.yStart) ; y ++) {
                Point point = new Point(x, y);
                int maxHeight = Integer.MIN_VALUE;
                while (true) {
                    point.movePoint();
                    maxHeight = Math.max(maxHeight, point.y);
                    if (range.isWithinRange(point)) {
                        count += 1;
                        break;
                    }
                    else if (range.isOverRange(point)) {
                        break;
                    }
                }

            }
        }
        printAnswer(count);
    }
}
