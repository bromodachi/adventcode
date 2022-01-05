package advent.code.days;

import java.util.List;

public class FirstDay extends DaySolver{

    List<Integer> list;
    protected FirstDay(String fileName) throws IllegalAccessException {
        super(fileName);
        list = readFile.readLineConvertToList(Integer::parseInt);
    }
    public void solveFirstProblem() {
        System.out.println("the answer is: " +  solveFirstProblemWithFileName());
    }

    public void solveSecondProblem() {
        System.out.println("the answer is: " +  slidingWindow(list, 3));
    }

    private int solveFirstProblemWithFileName() {
        int countContinuousIncrease = 0;
        boolean readLine = false;
        int previousValue = 0;
        for (int value : list) {
            if (readLine && value - previousValue > 0) {
                countContinuousIncrease += 1;
            }
            readLine = true;
            previousValue = value;
        }
        return countContinuousIncrease;
    }

    private int slidingWindow(List<Integer> list, int k) {
        int sum = 0;
        int totalIncreasedValues = 0;
        int left = 0;
        int right = 0;
        int previousValue = -1;
        while (right < list.size()) {
            sum += list.get(right);
            right += 1;
            if (previousValue != -1 && sum - previousValue > 0) {
                totalIncreasedValues += 1;
            }
            if (right - left == k) {
                previousValue = sum;
                sum -= list.get(left);
                left += 1;
            }
        }
        return totalIncreasedValues;
    }
}
