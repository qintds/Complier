public class Real extends Token {
    public final double value;
    public Real(double v) {
        super(Tag.Real);
        value = v;
    }
    public String toString() {
        return "" + value;
    }
}
