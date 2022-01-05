package advent.code.utils;

public final class FileReader {

    public static String getFilePath(Day day) {
        return getFilePath(day, false);
    }

    public static String getFilePath(Day day, boolean isTest) {
        return "../files/" + day.name() + "Input" + (isTest ? "Test": "") + ".txt";
    }
}
