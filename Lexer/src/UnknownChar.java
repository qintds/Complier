public class UnknownChar extends Token {
    public final char c;
    public UnknownChar(char c) {
        super(Tag.Unknown);
        this.c = c;
    }

    public String toString() {
        return String.valueOf(c);
    }
}
