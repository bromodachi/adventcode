package advent.code.days;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ThirdDay extends DaySolver{

    List<String> allInputs;

    protected ThirdDay(String fileName) throws IllegalAccessException {
        super(fileName);
        allInputs = readFile.readFileAsString();
    }

    @Override
    public void solveFirstProblem() {
        int [][] buckets = new int[allInputs.get(0).length()][2];
        for (String s : allInputs) {
            for (int i = 0; i < s.length(); i ++) {
                buckets[i][s.charAt(i)  - '0'] += 1;
            }
        }
        int power = 1 << buckets.length - 1;
        final int mask = power - 1;
        int gammaBit = 0;

        for (int [] bucket : buckets) {
            if (bucket[1] > bucket[0]) {
                gammaBit += power;
            }
            power = power >> 1;
        }
        System.out.println("answer:" + gammaBit * (~gammaBit & mask));
    }

    public void solveFirstProblemUsingStringBuilder() {
        long startTime = System.nanoTime();
        int [][] buckets = new int[allInputs.get(0).length()][2];
        for (String s : allInputs) {
            for (int i = 0; i < s.length();i ++) {
                buckets[i][s.charAt(i)  - '0'] += 1;
            }
        }
        int power = 1 << buckets.length - 1;
        final int mask = power - 1;
        StringBuilder gammaRate = new StringBuilder();
        int gammaBit = 0;
        int epsilonBit = 0;
        StringBuilder epsilonRate = new StringBuilder();

        for (int [] bucket : buckets) {
            // one is greater
            if (bucket[1] > bucket[0]) {
                gammaRate.append(1);
                gammaBit += power;
                epsilonRate.append(0);
            } else {
                gammaRate.append(0);
                epsilonBit += power;
                epsilonRate.append(1);
            }
            power = power >> 1;
        }
        System.out.println("answer:" + Integer.parseInt(gammaRate.toString(), 2) * Integer.parseInt(epsilonRate.toString(), 2));
//        System.out.println("answer:" + gammaBit * (~gammaBit & mask));
        System.out.println("execution time string builder: " + (System.nanoTime() - startTime));
    }

    @Override
    public void solveSecondProblem() {
        final int length = allInputs.get(0).length();
        int index = length - 1;
        List<Integer> list = allInputs.stream().map(o-> Integer.parseInt(o, 2)).collect(Collectors.toList());

        long startTime = System.nanoTime();
        System.out.println(lastDecimalInput(allInputs, true, 0) * lastDecimalInput(allInputs, false, 0));
        System.out.println("execution time: " + (System.nanoTime() - startTime));
        startTime = System.nanoTime();
        System.out.println( lastDecimalInputBinary(list, index, true) * lastDecimalInputBinary(list, index, false));
        System.out.println("execution time: " + (System.nanoTime() - startTime));
    }
    private int lastDecimalInput(List<String> values, boolean incremental, int index) {
        if (values.size() == 1) {
            return Integer.parseInt(values.get(0), 2);
        }
        List<String> ones = new ArrayList<>();
        List<String> zeroes = new ArrayList<>();
        for (String binary : values) {
            if (binary.charAt(index) == '0') {
                zeroes.add(binary);
            }
            else {
                ones.add(binary);
            }
        }
        List<String> ptr;
        if (ones.size() >= zeroes.size()) {
            ptr = incremental ? ones : zeroes;
        }
        else {
            ptr = incremental ? zeroes : ones;
        }
        return lastDecimalInput(ptr, incremental, index + 1);
    }

    private int lastDecimalInputBinary(List<Integer> values, int index, boolean incremental) {
        if (values.size() == 1) {
            return values.get(0);
        }
        List<Integer> ones = new ArrayList<>();
        List<Integer> zeroes = new ArrayList<>();
        for (int intValue : values) {
            if (((intValue >> index ) & 1) == 0) {
                zeroes.add(intValue);
            }
            else {
                ones.add(intValue);
            }
        }
        List<Integer> ptr;
        if (ones.size() >= zeroes.size()) {
            ptr = incremental ? ones : zeroes;
        }
        else {
            ptr = incremental ? zeroes : ones;
        }
        return lastDecimalInputBinary(ptr, index - 1, incremental);
    }
}
