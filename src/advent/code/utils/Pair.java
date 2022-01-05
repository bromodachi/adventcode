package advent.code.utils;

public final class Pair<T, U> {
    public T getLeft() {
        return left;
    }

    public U getRight() {
        return right;
    }

    T left;
    U right;

    public Pair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    public static  <T,U> Pair<T, U> of(T t, U u) {
        return new Pair<>(t, u);
    }
}
