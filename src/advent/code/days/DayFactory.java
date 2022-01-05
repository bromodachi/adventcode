package advent.code.days;

import advent.code.utils.FileReader;
import advent.code.utils.Day;

public class DayFactory {

    public static DaySolver getSolver(Day day) throws Exception {
        return getSolver(day, false, null);
    }

    public static DaySolver getSolver(Day day, boolean isTest) throws Exception {
        return getSolver(day, isTest, null);
    }

    public static DaySolver getSolver(Day day, String path) throws Exception {
        return getSolver(day, false, path);
    }

    public static DaySolver getSolver(Day day, boolean isTest, String customPath) throws Exception {
        final String filePath = customPath == null ? FileReader.getFilePath(day, isTest) : customPath;
        switch (day) {
            case FirstDay:
                return new FirstDay(filePath);
            case SecondDay:
                return new SecondDay(filePath);
            case ThirdDay:
                return new ThirdDay(filePath);
            case FourthDay:
                return new FourthDay(filePath);
            case FifthDay:
                return new FifthDay(filePath);
            case SixthDay:
                return new SixthDay(filePath);
            case SeventhDay:
                return new SeventhDay(filePath);
            case EighthDay:
                return new EighthDay(filePath);
            case NinthDay:
                return new NinthDay(filePath);
            case TenthDay:
                return new TenthDay(filePath);
            case EleventhDay:
                return new EleventhDay(filePath);
            case TwelfthDay:
                return new TwelfthDay(filePath);
            case ThirteenthDay:
                return new ThirteenthDay(filePath);
            case FourteenthDay:
                return new FourteenthDay(filePath);
            case FifteenthDay:
                return new FifteenthDay(filePath);
            case SixteenthDay:
                return new SixteenthDay(filePath);
            case SeventeenthDay:
                return new SeventeenthDay(filePath);
            case EighteenthDay:
                return new EighteenthDay(filePath);
            case NineteenthDay:
                return new NineteenthDay(filePath);
            case TwentiethDay:
                return new TwentiethDay(filePath);
            case TwentyFirstDay:
                return new TwentyFirstDay(filePath);
            case TwentySecondDay:
                return new TwentySecondDay(filePath);
            case TwentyThirdDay:
                return new TwentyThirdDay(filePath);
            case TwentyFourthDay:
                return new TwentyFourthDay(filePath);
            case TwentyFiftiethDay:
                return new TwentyFiftiethDay(filePath);
        }
        throw new IllegalAccessException("Invalid day passed");
    }
}
