package advent.code.days;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class TwelfthDay extends DaySolver{

    Map<String, List<String>> graph;
    int count =0;

    private void insertKey(String key, String value) {
        if (!graph.containsKey(key)) {
            graph.put(key, new ArrayList<>());
        }
        graph.get(key).add(value);
    }

    private void addForBothValues(String left, String right) {
        insertKey(left, right);
        insertKey(right, left);
    }
    protected TwelfthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        graph = new HashMap<>();
        Scanner scanner = readFile.toScanner();
        while (scanner.hasNext()) {
            String [] paths = scanner.nextLine().split("-");
            addForBothValues(paths[0], paths[1]);
        }
    }

    private void dfs(List<List<String>> permutations, String key, List<String> currList, Set<String> smallKeys) {
        if (key.equals("end")) {
            currList.add(key);
            permutations.add(new ArrayList<>(currList));
            currList.remove(currList.size() - 1);
            return;
        }
        if (smallKeys.contains(key)) {
            return;
        }
        currList.add(key);
        if (Character.isLowerCase(key.charAt(0))) {
            smallKeys.add(key);
        }
        for (String neighbor : graph.get(key)) {

            dfs(permutations, neighbor, currList,smallKeys);
        }
        smallKeys.remove(key);
        currList.remove(currList.size() - 1);



    }


    private void dfs(List<List<String>> permutations, String key, List<String> currList, Map<String, Integer> smallKeys, boolean hasValueGreaterThanOne) {
        if (key.equals("end")) {
            currList.add(key);
            permutations.add(new ArrayList<>(currList));
            currList.remove(currList.size() - 1);
            return;
        }
        if (smallKeys.containsKey(key)) {
            if (key.equals("start") || smallKeys.get(key) >= 2 || hasValueGreaterThanOne) {
                return;
            }
        }
        currList.add(key);
        if (Character.isLowerCase(key.charAt(0))) {
            smallKeys.put(key, smallKeys.getOrDefault(key, 0) + 1 );
            hasValueGreaterThanOne = hasValueGreaterThanOne || smallKeys.get(key) == 2;
        }
        for (String neighbor : graph.get(key)) {
            dfs(permutations, neighbor, currList, smallKeys, hasValueGreaterThanOne);
        }
        if (smallKeys.containsKey(key) && smallKeys.get(key) == 1) {
            smallKeys.remove(key);
        }
        else if (smallKeys.containsKey(key)) {
            smallKeys.put(key , smallKeys.get(key) - 1);
        }
        currList.remove(currList.size() - 1);
    }

    @Override
    public void solveFirstProblem() {
        List<List<String>> permutations = new ArrayList<>();
        Set<String> set = new HashSet<>();
        List<String> list = new ArrayList<>();
        dfs(permutations, "start", list, set);
        printAnswer(permutations.size());
    }

    @Override
    public void solveSecondProblem() {
        List<List<String>> permutations = new ArrayList<>();
        Map<String, Integer> set = new HashMap<>();
        List<String> list = new ArrayList<>();
        dfs(permutations, "start", list, set, false);
        printAnswer(count );
    }
}
