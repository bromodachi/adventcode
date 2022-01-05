package advent.code.utils;

import java.util.concurrent.TimeUnit;

public final class MeasureExecutionTime {
    public static void measureTime(VoidFunction function) {
        long startTime = System.nanoTime();
        function.call();
        System.out.println("Finished: " + TimeUnit.MILLISECONDS.convert(System.nanoTime() - startTime, TimeUnit.NANOSECONDS) + "ms");
    }
}
