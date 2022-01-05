package advent.code.utils;

public final class Utils {
    public static void swap(String [] arr, int i, int j) {
        final String s = arr[i];
        arr[i] = arr[j];
        arr[j] = s;
    }

    public static void swap(char [] arr, int i, int j) {
        final char c = arr[i];
        arr[i] = arr[j];
        arr[j] = c;
    }
}
