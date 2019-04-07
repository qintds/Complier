public class Num extends Token {
    public final int value;
    public Num(int v) {
        super(Tag.Num);
        value = v;
    }
    public String toString() {
        return "" + value;
    }
}
