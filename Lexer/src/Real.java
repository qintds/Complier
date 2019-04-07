public class Real extends Token {
    public final float value;
    public Real(float v) {
        super(Tag.Real);
        value = v;
    }
    public String toString() {
        return "" + value;
    }
}
