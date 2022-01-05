package advent.code.days;

import advent.code.utils.ConverterUtil;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class SixthDay extends DaySolver{

    List<Integer> input;

    protected SixthDay(String fileName) throws FileNotFoundException {
        super(fileName);
        Scanner scanner = readFile.toScanner();
        while (scanner.hasNext()) {
            input = ConverterUtil.readLineAsList(scanner.nextLine());
        }
    }

    @Override
    public void solveFirstProblem() {
        printAnswer(amountOfFishAfterDays(80));
    }

    @Override
    public void solveSecondProblem() {
        printAnswer(amountOfFishAfterDays(256));
    }

    private long amountOfFishAfterDays(int days) {
        long [] ageBuckets = new long[10];
        for (Integer integer : input) {
            ageBuckets[integer] += 1;
        }
        for (int day = 1; day <= days; day ++) {
            for (int age = 0; age < ageBuckets.length; age ++) {
                if (age == 0 && ageBuckets[age] > 0) {
                    long numberToTransfer = ageBuckets[age];
                    ageBuckets[9] +=  numberToTransfer; // new fish born
                    ageBuckets[7] +=  numberToTransfer; // reset
                    ageBuckets[age] -=  numberToTransfer;
                }
                else if (ageBuckets[age] > 0){
                    long numberToTransfer = ageBuckets[age];
                    ageBuckets[age] -= numberToTransfer;
                    ageBuckets[age - 1] += numberToTransfer;
                }

            }
        }
        long sum =0;
        for (long count : ageBuckets) {
            sum += count;
        }
        return sum;
    }
}
