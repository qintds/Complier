
public class Word extends Token {
    public String lexeme = "";

    public Word(String s, Tag tag) {
        super(tag);
        lexeme = s;
    }
    public String toString() {
        return lexeme;
    }
    public static final Word
    equal = new Word("==", Tag.EQ),
    notEqual = new Word("!=", Tag.NE),
    lowerEqual = new Word("<=", Tag.LE),
    greaterEqual = new Word(">=", Tag.GE),
    addEqual = new Word("+=", Tag.AddEQ),
    subEqual = new Word("-=", Tag.SubEQ),
    mulEqual = new Word("*=", Tag.MulEQ),
    divEqual = new Word("/=", Tag.DivEQ),
    modEqual = new Word("%=", Tag.ModEQ),
    comment = new Word("//", Tag.Comment),
    true_ = new Word("true", Tag.True),
    false_ = new Word("false", Tag.False);

}
