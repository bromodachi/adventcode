package advent.code.days;

import advent.code.utils.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class TwentySecondDay extends DaySolver{

    List<Cube> list;
    static class Cube {

        final boolean on;
        final int fromX;
        final int toX;

        final int fromY;
        final int toY;

        final int fromZ;
        final int toZ;

        public Cube(boolean on,int fromX, int toX, int fromY, int toY, int fromZ, int toZ) {
            this.on = on;
            this.fromX = fromX;
            this.toX = toX;
            this.fromY = fromY;
            this.toY = toY;
            this.fromZ = fromZ;
            this.toZ = toZ;
        }

        public long volume() {
            return (toX - fromX + 1L) * (toY - fromY + 1L) * (toZ - fromZ + 1L) * (on ? 1L : -1L);
        }

        public Cube intersect(Cube info, boolean on) {
            if (
                    this.fromX > info.toX ||
                    info.fromX > this.toX ||
                    this.fromY > info.toY ||
                    info.fromY > this.toY ||
                    this.fromZ > info.toZ ||
                    info.fromZ > this.toZ
            ) {
                return null;
            }

            return new Cube(
                    on,
                    Math.max(this.fromX, info.fromX),
                    Math.min(this.toX, info.toX),
                    Math.max(this.fromY, info.fromY),
                    Math.min(this.toY, info.toY),
                    Math.max(this.fromZ, info.fromZ),
                    Math.min(this.toZ, info.toZ)
            );
        }

        private static Pair<Integer, Integer> info(String line) {
            final String [] in =  line.split("\\.\\.");
            return Pair.of(Integer.parseInt(in[0]), Integer.parseInt(in[1]));
        }

        public static Cube of(String line) {
            final String [] lineArray = line.split(",");
            final String xInfo = lineArray[0].split(" ")[1].split("=")[1];
            final Pair<Integer, Integer> xPair = info(xInfo);
            final Pair<Integer, Integer> yPair = info(lineArray[1].split("=")[1]);
            final Pair<Integer, Integer> zPair = info(lineArray[2].split("=")[1]);

            return new Cube(lineArray[0].split(" ")[0].equals("on"), xPair.getLeft(), xPair.getRight(), yPair.getLeft(), yPair.getRight(), zPair.getLeft(), zPair.getRight());
        }

        public boolean isWithinForPartOne() {
            return within(fromX, toX) && within(fromY, toY) && within(fromZ, toZ);
        }

        private boolean within(int value) {
            return value <= 50 && value >= -50;
        }

        private boolean within(int from, int to) {
            return within(from) && within(to);
        }
    }

    static class Point {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && z == point.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

        final int x;
        final int y;
        final int z;
        public Point(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }


    }

    protected TwentySecondDay(String fileName) throws IllegalAccessException {
        super(fileName);
        list = readFile.readLineConvertToList(Cube::of);
    }


    @Override
    public void solveFirstProblem() {
        Set<Point> onSet = new HashSet<>();
        for (Cube point : list) {
            if (point.isWithinForPartOne()) {
                for (int x = point.fromX; x <= point.toX; x ++) {
                    for (int y = point.fromY; y <= point.toY; y ++) {
                        for (int z = point.fromZ; z <= point.toZ; z ++) {
                            if (point.on) {
                                onSet.add(new Point(x, y, z));
                            }
                            else {
                                onSet.remove(new Point(x, y, z));
                            }
                        }
                    }
                }
            }
        }
        printAnswer(onSet.size());
    }

    @Override
    public void solveSecondProblem() {
        List<Cube> intersected = new ArrayList<>();

        for (Cube info: list) {
            for (int i = intersected.size() - 1; i >= 0 ; i --){
                Optional.ofNullable(intersected.get(i).intersect(info, !intersected.get(i).on)).ifPresent(intersected::add);
            }
            if (info.on) {
                intersected.add(info);
            }
        }
        printAnswer(intersected.stream().mapToLong(Cube::volume).sum());

    }
}
