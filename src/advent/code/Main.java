package advent.code;

import advent.code.days.DayFactory;
import advent.code.days.Solver;
import advent.code.utils.Day;
import advent.code.utils.MeasureExecutionTime;

public class Main {

    public static void main(String[] args) throws Exception {
        Solver solver = DayFactory.getSolver(Day.TwentyFiftiethDay, false);
        MeasureExecutionTime.measureTime(solver::solveFirstProblem);
        MeasureExecutionTime.measureTime(solver::solveSecondProblem);
    }
}
