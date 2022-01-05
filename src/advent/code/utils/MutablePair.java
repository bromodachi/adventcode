package advent.code.utils;

public class MutablePair<T, U> {
    public T getLeft() {
        return left;
    }

    public U getRight() {
        return right;
    }

    public void setLeft(T left) {
        this.left = left;
    }

    public void setRight(U right) {
        this.right = right;
    }

    T left;
    U right;

    public MutablePair(T left, U right) {
        this.left = left;
        this.right = right;
    }

    public static  <T,U> MutablePair<T, U> of(T t, U u) {
        return new MutablePair<>(t, u);
    }
}
