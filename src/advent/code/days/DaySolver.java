package advent.code.days;

import advent.code.utils.ReadFile;

public abstract class DaySolver implements Solver {

    protected final ReadFile readFile;

    protected DaySolver(String fileName) {
        this.readFile = new ReadFile(fileName);
    }

    private String classNameOnly() {
        return this.getClass().getSimpleName();
    }

    protected void printAnswer(Object obj) {
        System.out.println(classNameOnly() + "'s answer is: " + obj);
    }
}
