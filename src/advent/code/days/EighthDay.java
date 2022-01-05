package advent.code.days;

import advent.code.utils.Pair;
import advent.code.utils.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class EighthDay extends DaySolver {
    enum Direction{
        TOP,
        TOP_LEFT,
        TOP_RIGHT,
        MIDDLE,
        MIDDLE_LEFT,
        MIDDLE_RIGHT,
        BOTTOM
    }

    List<Pair<String[], String[]>> input;

    protected EighthDay(String fileName) throws IllegalAccessException {
        super(fileName);
        input = readFile.readLineConvertToList(o->  {
            final String [] values = o.split("\\| ");
            return Pair.of(values[0].split(" "), values[1].split(" "));
        });
    }

    @Override
    public void solveFirstProblem() {
        int count = 0;
        for (Pair<String[], String[]> pair: input) {
            for (String s: pair.getRight()) {
                if (Set.of(2,4,3,7).contains(s.length())) {
                    count += 1;
                }
            }
        }
        printAnswer(count);
    }

    private void getPermutations(StringBuilder s, List<List<Character>> answers, List<Character> innerList) {
        if (s .length() ==0 ) {
            answers.add(new ArrayList<>(innerList));
        }
        for (int i = 0; i < s.length(); i ++) {
            final char c = s.charAt(i);
            innerList.add(c);
            s.deleteCharAt(i);
            getPermutations(s, answers, innerList);
            s.insert(i, c);
            innerList.remove(innerList.size() - 1);
        }
    }

    private List<List<Character>> getPermutations(String s) {
        List<List<Character>> list = new ArrayList<>();
        getPermutations(new StringBuilder(s), list, new ArrayList<>());
        return list;
    }

    private List<List<Character>> getPermutations(StringBuilder s) {
        List<List<Character>> list = new ArrayList<>();
        getPermutations(s, list, new ArrayList<>());
        return list;
    }

    private Set<Character> getCharSet(char [] chars) {
        Set<Character> set = new HashSet<>();
        for (char c : chars) {
            set.add(c);
        }
        return set;
    }

    private char [] createList(Map<Direction, Character> directionCharacterMap, Direction ... directions) {
        char [] chars = new char[directions.length];
        int index = 0;
        for (Direction direction : directions) {
            chars[index ++] = directionCharacterMap.get(direction);
        }
        return chars;
    }

    private void addDirections(Map<Direction, Character> directionCharacterMap, List<Character> arr, Direction ... directions) {
        for (int i = 0; i < directions.length; i ++) {
            directionCharacterMap.put(directions[i], arr.get(i));
        }
    }

    private void removeDirections(Map<Direction, Character> directionCharacterMap, Direction ... directions) {
        for (Direction direction : directions) {
            directionCharacterMap.remove(direction);
        }
    }

    private StringBuilder getDifferenceString(Map<Direction, Character> directionCharacterMap, String s) {
        Set<Character> difference = getCharSet(s.toCharArray());
        difference.removeAll(directionCharacterMap.values());
        StringBuilder sb = new StringBuilder();
        for (char c: difference) {
            sb.append(c);
        }
        return sb;
    }

    private boolean doesContainAllCharacters(Map<Direction, Character> directionCharacterMap, String s, int expectedSize) {
        Set<Character> intersection = getCharSet(s.toCharArray());
        intersection.retainAll(directionCharacterMap.values());
        return intersection.size() == expectedSize;
    }

    private int getExpectedSize(int currentInteger){
        switch (currentInteger) {
            case 2: case 5: case 3: case 8:
                return 5;
            case 4: case 7:
                return 2;
            default:
                return 6;
        }
    }

    /**
     * Gets the next integer that builds from the previous integer.
     * E.G: 1 has two sides which 7 uses. 4 also uses two sides from 1.
     * Once we finished 4, we know the top, top left, top right, middle, and middle right.
     * Thus we can tackle 8 next.
     * @param currentInteger
     * @return integer that we will tackle next.
     */
    private int getNextInteger(int currentInteger) {
        switch (currentInteger) {
            case 1:
                return 7;
            case 7:
                return 4;
            case 4:
                return 8;
            case 8:
                return 2;
            case 2:
                return 5;
            case 5:
                return 3;
            case 3:
                return 0;
            case 0:
                return 6;
            case 6:
                return 9;
            default:
                return 10;
        }
    }

    /**
     * Gets the corresponding direction for an integer
     * @param currentInteger
     * @return Direction[] of where the letters should lie.
     */
    private Direction[] getDirections(int currentInteger) {
        switch (currentInteger) {
            case 1:
                return createDirections(Direction.TOP_RIGHT, Direction.MIDDLE_RIGHT);
            case 7:
                return createDirections(Direction.TOP);
            case 4:
                return createDirections(Direction.TOP_LEFT, Direction.MIDDLE);
            case 8:
                return createDirections(Direction.MIDDLE_LEFT, Direction.BOTTOM);
            case 2:
                return createDirections(Direction.TOP, Direction.TOP_RIGHT, Direction.MIDDLE, Direction.MIDDLE_LEFT, Direction.BOTTOM);
            case 5:
                return createDirections(Direction.TOP, Direction.TOP_LEFT, Direction.MIDDLE, Direction.MIDDLE_RIGHT, Direction.BOTTOM);
            case 3:
                return createDirections(Direction.TOP, Direction.TOP_RIGHT, Direction.MIDDLE, Direction.MIDDLE_RIGHT, Direction.BOTTOM);
            case 0:
                return createDirections(Direction.TOP, Direction.TOP_LEFT, Direction.MIDDLE_LEFT, Direction.BOTTOM, Direction.MIDDLE_RIGHT, Direction.TOP_RIGHT);
            case 6:
                return createDirections(Direction.TOP, Direction.TOP_LEFT, Direction.MIDDLE, Direction.MIDDLE_LEFT, Direction.MIDDLE_RIGHT, Direction.BOTTOM);
            case 9:
                return createDirections(Direction.TOP, Direction.TOP_LEFT, Direction.TOP_RIGHT, Direction.MIDDLE, Direction.MIDDLE_RIGHT, Direction.BOTTOM);
            default:
                // never going to happen, so it's safe to do this.
                return new Direction[0];
        }
    }

    /**
     * Just to avoid writing new Direction [] {}
     * @param directions
     * @return Direction []
     */
    private Direction [] createDirections(Direction ... directions) {
        return directions;
    }

    private boolean backtrack(
            Map<Integer, String> known,
            String [] unknowns,
            int integerToConstruct,
            Map<Direction, Character> directionCharacterMap,
            int unknownsSized // we're going to be swapping the index we used with the last and decrease the array. This way we can simulate deletion when looping
    ) {
        if (unknownsSized == 0) {
            return true;
        }
        Direction [] directions = getDirections(integerToConstruct);
        if (known.containsKey(integerToConstruct)) {
            List<List<Character>> permutations;
            if (integerToConstruct == 1) {
                permutations = getPermutations(known.get(integerToConstruct));
            }
            // 7, 4, 8
            else {
                if (!doesContainAllCharacters(directionCharacterMap, known.get(integerToConstruct), getExpectedSize(integerToConstruct))) {
                    return false;
                }
                StringBuilder sb = getDifferenceString(directionCharacterMap, known.get(integerToConstruct));
                permutations = getPermutations(sb);
            }
            for (List<Character> arr: permutations) {
                addDirections(directionCharacterMap, arr, directions);
                if (backtrack(known, unknowns, getNextInteger(integerToConstruct), directionCharacterMap, unknownsSized)) {
                    return true;
                }
                removeDirections(directionCharacterMap, directions);
            }
        }
        else {
            for (int i = unknownsSized - 1; i >= 0; i --) {
                final String curr = unknowns[i];
                if (!checkSize(curr, getExpectedSize(integerToConstruct), directionCharacterMap, directions)) {
                    continue;
                }
                addValueToProcessed(known, integerToConstruct, curr, unknowns, i, unknownsSized);
                if (backtrack(known, unknowns, getNextInteger(integerToConstruct), directionCharacterMap, unknownsSized - 1)) {
                    return true;
                }
                removeValueFromProcessed(known, integerToConstruct, unknowns, unknownsSized, i);
            }
        }
        return false;
    }

    private void addValueToProcessed(Map<Integer, String> known, int value, String curr, String [] unknowns, int i, int unknownsSized) {
        known.put(value, curr);
        Utils.swap(unknowns, i, unknownsSized - 1);
    }

    private void removeValueFromProcessed(Map<Integer, String> known, int value, String [] unknowns, int unknownsSized, int i) {
        Utils.swap(unknowns, unknownsSized - 1, i);
        known.remove(value);
    }

    private boolean checkSize(String curr, int expectedSize, Map<Direction, Character> directionCharacterMap, Direction ... directions){
        if (curr.length() == expectedSize) {
            char [] chars = createList(directionCharacterMap, directions);
            Set<Character> intersection = getCharSet(curr.toCharArray());
            intersection.retainAll(getCharSet(chars));
            return intersection.size() == expectedSize;
        }
        return false;
    }
    public static String sortString(String s) {
        char [] temp = s.toCharArray();
        Arrays.sort(temp);
        return new String(temp);
    }

    @Override
    public void solveSecondProblem() {
        int finalAnswer = 0;
        for (Pair<String[], String[]> pair: input) {
            Map<Integer, String>  mapper  = new HashMap<>();
            String [] unknowns = new String[6];
            int index = 0;
            for (String s: pair.getLeft()) {
                if (s.length() == 2) {
                    mapper.put(1, s);
                }
                else if (s.length() == 4) {
                    mapper.put(4, s);
                }
                else if (s.length() == 3) {
                    mapper.put(7, s);
                }
                else if (s.length() == 7) {
                    mapper.put(8, s);
                }
                else {
                    unknowns[index++] = s;
                }
            }
            Map<Direction, Character> directionCharacterMap = new HashMap<>();
            Map<String, Integer> mapToInteger = new HashMap<>();
            if (backtrack(mapper, unknowns, 1, directionCharacterMap, unknowns.length)) {
                for (int value : mapper.keySet()) {
                    final String newString = sortString(mapper.get(value));
                    mapper.put(value, newString);
                    mapToInteger.put(newString, value);
                }
            }
            int value = 0;
            for (String s: pair.getRight()) {
                final String newString = sortString(s);
                 value = value * 10 + mapToInteger.get(newString);
            }
            finalAnswer += value;
        }
        printAnswer(finalAnswer);
    }
}
