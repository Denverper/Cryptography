package tools;

public class Tuple<T, U> {
    private T first;
    private U second;

    public Tuple(T first, U second) {
        this.first = first;
        this.second = second;
    }

    public T getFirst() {
        return first;
    }

    public U getSecond() {
        return second;
    }

    public void setFirst(T obj) {
        this.first = obj;
    }

    public void setSecond(U obj) {
        this.second = obj;
    }
}