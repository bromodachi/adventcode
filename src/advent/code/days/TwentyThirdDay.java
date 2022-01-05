package advent.code.days;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

public class TwentyThirdDay extends DaySolver{

    private final int[][] DIRECTIONS = new int[][]{
            new int [] {-1,0}, // UP
            new int [] {1,0},  // DOWN
            new int [] {0,-1}, // LEFT
            new int [] {0,1}, // RIGHT
    };

    Hallway hallway;

    static class Hallway {
        Space [][] spaces;

        List<List<Room>> roomLocations;

        Set<Point> roomPoints = new HashSet<>();
        Set<Point> hallwayPoints;
        Queue<Pod> podsToProcess;
        static class Room {
            int row;
            int column;

            public Room(int row, int column) {
                this.row = row;
                this.column = column;
            }
        }
        static class Pod {
            protected char type;
            protected int cost;

            public void setRow(int row) {
                this.row = row;
            }

            public void setColumn(int column) {
                this.column = column;
            }

            int row;
            int column;

            public Pod(char type, int cost) {
                this.type = type;
                this.cost = cost;
            }
            public static Pod of(char c) {
                switch (c) {
                    case 'A':
                        return new Amber();
                    case 'B':
                        return new Bronze();
                    case 'C':
                        return new Copper();
                    case 'D':
                        return new Desert();
                    case '#':
                        return new Wall();
                }
                return null;
            }
        }
        static class Wall extends Pod {

            public Wall() {
                super('#', -1);
            }
        }
        static class Amber extends Pod {
            public Amber() {
                super('A', 1);
            }
        }
        static class Bronze extends Pod {

            public Bronze() {
                super('B', 10);
            }
        }


        static class Copper extends Pod {

            public Copper() {
                super('C', 100);
            }
        }

        static class Desert extends Pod {
            public Desert() {
                super('D', 1000);
            }
        }

        // call this only when created.
        private void podsToProcessInBeginning() {
            char [] chars = new char[] {'A', 'B', 'C', 'D'};
            LinkedList<Pod> process = new LinkedList<>();
            for (int i = 0; i < 4; i ++) {
                Room room = roomLocations.get(i).get(0);
                for (Room rooms: roomLocations.get(i)) {
                    roomPoints.add(new Point(rooms.row, rooms.column, 0));
                }
                if (spaces[room.row][room.column] instanceof MutableSpace && ((MutableSpace)spaces[room.row + 1][room.column]).isCompleted && spaces[room.row][room.column].getSpace().type == chars[i]) {
                    // should never happen
                    ((MutableSpace) spaces[room.row][room.column]).setIsCompleted(true);
                    continue;
                }
                if (spaces[room.row][room.column].getSpace() != null) {
                    process.addFirst(spaces[room.row][room.column].getSpace());
                }
            }
            this.podsToProcess = new LinkedList<>(process);
        }

        public LinkedList<Pod> podsThatAreNeedProcessing() {
            LinkedList<Pod> podList = new LinkedList<>();
            for (int row = 0; row < spaces.length; row ++) {
                for (int column = 0; column < spaces[0].length; column ++) {
                    if (spaces[row][column] instanceof MutableSpace) {
                        if (spaces[row][column].getSpace() != null && !((MutableSpace) spaces[row][column]).isCompleted) {
                            podList.add(spaces[row][column].getSpace());
                        }
                    }
                }
            }
            return podList;
        }

        public boolean canMoveToRoom(Pod pod, int row, int column) {
             int index = pod.type - 'A';
             List<Room> list = roomLocations.get(index);
             if (list.get(0).column == column) {
                 int startRow = 5;
                 while (startRow >= 2 && spaces[startRow][column].getSpace() != null) {
                     if (spaces[startRow][column].getSpace() != null && spaces[startRow][column].getSpace().type != pod.type) {
                         return false;
                     }
                     startRow -= 1;
                 }
                 return startRow != 1;
             }
             else {
                 return false;
             }
        }
        public Hallway(Space[][] spaces, List<List<Room>> roomLocations, Set<Point> hallwayPoints) {
            this.spaces = spaces;
            this.roomLocations = new ArrayList<>(roomLocations);
            this.hallwayPoints = hallwayPoints;
            podsToProcessInBeginning();
        }
        interface Space {
            Pod getSpace();
            boolean isRoom();
            boolean isInFrontOfRoom();
        }

        static class ImmutableSpace implements Space{
            final Pod space;
            final boolean isRoom = false;

            public boolean isInFrontOfRoom() {
                return isInFrontOfRoom;
            }

            final boolean isInFrontOfRoom;

            public ImmutableSpace(char space) {
                this.space = Pod.of(space);
                isInFrontOfRoom = false;
            }

            public ImmutableSpace(char space, boolean isInFrontOfRoom) {
                this.space = Pod.of(space);
                this.isInFrontOfRoom = isInFrontOfRoom;
            }

            public Pod getSpace() {
                return space;
            }

            public boolean isRoom() {
                return isRoom;
            }

            @Override
            public String toString() {
                if (isInFrontOfRoom) {
                    return ".";
                }
                return ""+(space == null ? ' ' : space.type);
            }
        }

        static class MutableSpace implements Space{
            Pod space;
            final boolean isRoom;

            public void setIsCompleted(boolean setCompleted) {
                this.isCompleted = setCompleted;
            }

            boolean isCompleted;
            public MutableSpace(char space, boolean isRoom) {
                this.space = Pod.of(space);
                this.isRoom = isRoom;
            }

            public void setSpace(char space) {
                this.space = Pod.of(space);
            }


            public void setSpaceToPod(Pod pod) {
                this.space = pod;
            }
            @Override
            public String toString() {
                return ""+(space == null ? '.' : space.type);
            }

            @Override
            public Pod getSpace() {
                return space;
            }


            @Override
            public boolean isRoom() {
                return isRoom;
            }

            @Override
            public boolean isInFrontOfRoom() {
                return false;
            }
        }

        public static Hallway of(List<String> list) {
            Space [][] spaces = new Space[7][14];
            List<List<Room>> roomLocations = new ArrayList<>();
            Set<Point> hallwayPoints = new HashSet<>();
            for (int row = 0; row < list.size(); row ++) {
                final String curr = list.get(row);
                int column = 0;
                for (; column < curr.length(); column ++) {
                    if (curr.charAt(column) == '#') {
                        spaces[row][column] = new ImmutableSpace('#');
                    }
                    else if (curr.charAt(column) == '.') {
                        spaces[row][column] = new MutableSpace('.', false);
                        hallwayPoints.add(new Point(row, column, 0));
                    }
                    else if (Character.isAlphabetic(curr.charAt(column))) {
                        if (spaces[row - 1][column].getSpace() == null) {
                            roomLocations.add(
                                    List.of(
                                    new Room(row, column),
                                    new Room(row + 1, column),
                                    new Room(row + 2, column),
                                    new Room(row + 3, column)

                            ));
                            spaces[row - 1][column] = new ImmutableSpace('.', true);
                        }
                        spaces[row][column] = new MutableSpace(curr.charAt(column), true);
                        spaces[row][column].getSpace().setColumn(column);
                        spaces[row][column].getSpace().setRow(row);
                    }
                    else {
                        spaces[row][column] = new ImmutableSpace(' ');
                    }
                }
                while (column < 14) {
                    spaces[row][column] = new ImmutableSpace(' ');
                    column ++;
                }
            }
            return new Hallway(spaces, roomLocations, hallwayPoints);
        }

        private void printSpaces(){
            for (Space [] space: spaces) {
                for (Space s : space) {
                    System.out.print(s.toString());
                }
                System.out.println();
            }
        }

        public boolean isCompleted() {
            char [] roomLabel = new char[] {'A', 'B','C','D'};
            for (int room = 0 ; room < 4; ) {
                for (List<Room> roomList : roomLocations) {
                    for (Room r : roomList) {
                        if (spaces[r.row][r.column].getSpace() == null) {
                            return false;
                        }
                        if (spaces[r.row][r.column].getSpace().type != roomLabel[room]) {
                            return false;
                        }
                    }
                    room ++;
                }
            }
            return true;
        }
    }

    long energyUsed = Long.MAX_VALUE;

    static class Point {
        int row;
        int column;
        int cost;

        public Point(int row, int column, int cost) {
            this.row = row;
            this.column = column;
            this.cost = cost;
        }

        public static Point of(int row, int column, int cost) {
            return new Point(row, column, cost);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return row == point.row && column == point.column;
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    int totalRun = 0;
    private boolean backtrack(LinkedList<Hallway.Pod> list, long totalCost) {
        if (hallway.isCompleted()) {
            if (energyUsed > totalCost ) {
                energyUsed = totalCost;
                System.out.println("energyUsed: " + energyUsed);
            }
            energyUsed = Math.min(totalCost, energyUsed);
            return true;
        }
        int count = 0;
        LinkedList<Hallway.Pod> process = new LinkedList<>();
        LinkedList<Hallway.Pod> backup = new LinkedList<>(list);
        totalRun ++;
        while (true) {
            while (!list.isEmpty()) {
                Hallway.Pod toProcess = list.poll();

                final int originalRow = toProcess.row;
                final int originalColumn = toProcess.column;
                final Point p = Point.of(originalRow, originalColumn, 0);
                Queue<Point> queue = new LinkedList<>();
                boolean inHallway = hallway.hallwayPoints.contains(p);
                queue.add(p);
                Set<Point> set = new HashSet<>();
                boolean wasProcessed = false;
                while (!queue.isEmpty()) {
                    Point point = queue.poll();
                    if (set.contains(point)) {
                        continue;
                    }
                    set.add(point);
                    if (!hallway.spaces[point.row][point.column].isInFrontOfRoom() && hallway.spaces[point.row][point.column].getSpace() == null) {
                        if (inHallway) {
                            if (hallway.roomPoints.contains(point) && hallway.canMoveToRoom(toProcess, point.row, point.column)) {
                                // move to furthest down
                                int startRow = point.row;
                                while (startRow <= 5 && hallway.spaces[startRow][point.column].getSpace() == null) {
                                    startRow += 1;
                                    point.cost += 1;
                                }
                                startRow -= 1;
                                point.cost -= 1;
                                ((Hallway.MutableSpace) hallway.spaces[originalRow][originalColumn]).setSpaceToPod(null);
                                ((Hallway.MutableSpace) hallway.spaces[startRow][point.column]).setSpaceToPod(toProcess);
                                toProcess.row = startRow;
                                toProcess.column = point.column;
                                ((Hallway.MutableSpace) hallway.spaces[startRow][point.column]).setIsCompleted(true);
                                set.add(new Point(startRow, point.column, 0));
                                backtrack(hallway.podsThatAreNeedProcessing(), totalCost + ((long) (point.cost) * toProcess.cost));
                                ((Hallway.MutableSpace) hallway.spaces[originalRow][originalColumn]).setSpaceToPod(toProcess);
                                ((Hallway.MutableSpace) hallway.spaces[startRow][point.column]).setSpaceToPod(null);
                                toProcess.row = originalRow;
                                toProcess.column = originalColumn;
                                ((Hallway.MutableSpace) hallway.spaces[startRow][point.column]).setIsCompleted(false);
                                wasProcessed = true;
                            } else {
                                for (int[] direction : DIRECTIONS) {
                                    final int row = direction[0] + point.row;
                                    final int column = direction[1] + point.column;
                                    if (hallway.spaces[row][column].getSpace() == null) {
                                        queue.add(new Point(row, column, point.cost + 1));
                                    }
                                }
                                continue;
                            }
                        } else {
                            if (!hallway.roomPoints.contains(point)) {
                                ((Hallway.MutableSpace) hallway.spaces[originalRow][originalColumn]).setSpaceToPod(null);
                                ((Hallway.MutableSpace) hallway.spaces[point.row][point.column]).setSpaceToPod(toProcess);
                                toProcess.row = point.row;
                                toProcess.column = point.column;
                                backtrack(hallway.podsThatAreNeedProcessing(), totalCost + ((long) point.cost * toProcess.cost));
                                ((Hallway.MutableSpace) hallway.spaces[originalRow][originalColumn]).setSpaceToPod(toProcess);
                                ((Hallway.MutableSpace) hallway.spaces[point.row][point.column]).setSpaceToPod(null);
                                toProcess.row = originalRow;
                                toProcess.column = originalColumn;
                                wasProcessed = true;
                            }
                        }
                    }
                    for (int[] direction : DIRECTIONS) {
                        final int row = direction[0] + point.row;
                        final int column = direction[1] + point.column;
                        if (hallway.spaces[row][column].getSpace() == null) {
                            queue.add(new Point(row, column, point.cost + 1));
                        }
                    }
                }
                if (!wasProcessed) {
                    process.add(toProcess);
                }
                count += 1;
            }
            if (backup.size() == process.size()) {
                break;
            }
            else  {
                list = new LinkedList<>(process);
                backup = new LinkedList<>(process);
                process.clear();
            }
        }
        return false;
    }
    protected TwentyThirdDay(String fileName) throws IllegalAccessException {
        super(fileName);
        List<String> list = readFile.readFileAsString();
        hallway = Hallway.of(list);
    }

    @Override
    public void solveFirstProblem() {
        LinkedList<Hallway.Pod> list = new LinkedList<>(hallway.podsToProcess);
        backtrack(list, 0);
        printAnswer(energyUsed + "total used: " + totalRun);
    }

    @Override
    public void solveSecondProblem() {

    }
}
