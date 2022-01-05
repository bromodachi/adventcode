package advent.code.days;

import java.util.List;

public class SecondDay extends DaySolver{

    private static class Direction {
        enum Directions {
            UP, DOWN, FORWARD;
            public static Directions of(String s) {
                switch (s) {
                    case "up":
                        return Directions.UP;
                    case "down":
                        return Directions.DOWN;
                    case "forward":
                        return Directions.FORWARD;
                }
                return null; // <- will never happen so safe to do this.
            }
        }
        private final Directions direction;
        private final int value;

        public Direction(String direction, int value) {
            this.direction = Directions.of(direction);
            this.value = value;
        }
        public static Direction of(String s) {
            String [] values = s.split(" ");
            return new Direction(values[0], Integer.parseInt(values[1]));
        }
    }


    List<Direction> directions;
    protected SecondDay(String fileName) throws IllegalAccessException {
        super(fileName);
        this.directions = readFile.readLineConvertToList(Direction::of);
    }

    @Override
    public void solveFirstProblem() {
        int horizontal = 0;
        int depth = 0;
        for (Direction direction : directions) {
            switch (direction.direction) {
                case UP:
                    depth -= direction.value;
                    break;
                case DOWN:
                    depth += direction.value;
                    break;
                case FORWARD:
                    horizontal += direction.value;
                    break;
            }
        }
        printAnswer(horizontal * depth);
    }

    @Override
    public void solveSecondProblem() {
        int aim = 0;
        int horizontal = 0;
        int depth = 0;
        for (Direction direction : directions) {
            switch (direction.direction) {
                case UP:
                    aim -= direction.value;
                    break;
                case DOWN:
                    aim += direction.value;
                    break;
                case FORWARD:
                    horizontal += direction.value;
                    depth += direction.value * aim;
                    break;
            }
        }
        printAnswer(horizontal * depth);
    }
}
