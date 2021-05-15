import java.io.Serializable;

public class Pair <T, U> implements Serializable {
    private T a;
    private U b;

    public Pair() {
        a = null;
        b = null;
    }

    public Pair(T a, U b) {
        this.a = a;
        this.b = b;
    }

    public T getFirst() {
        return a;
    }

    public U getSecond() {
        return b;
    }

    public void setFirst(T a) {
        this.a = a;
    }

    public void setSecond(U b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "<" + a + ", " + b + ">";
    }
}
