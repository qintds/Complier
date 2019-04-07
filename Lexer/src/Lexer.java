import java.util.Hashtable;

public class Lexer {
    public static int line = 1;

    char peek = ' ';
    Hashtable words = new Hashtable();

    Input input = new InputSB();
    CodeLine presentLine = new CodeLine();
    CodeFile presentFile = new CodeFile();

    private void readch() {
        peek = input.advance();
    }

    private boolean readch(char c) {
        readch();
        if(peek != c)
            return false;
        peek = ' ';
        return true;
    }

    private void expectWrap() {
        for( ; ; readch()) {
            if (peek == '\n') {
                addLine();
                return;
            }
        }
    }

    private boolean isUnderlineOrLetterOrDigit(char c) {
        return Character.isLetterOrDigit(c) || c == '_';
    }

    private void addLine() {
        if (presentLine.totalTokenCount > 0){
            presentLine.trueLineNum = line;
            presentFile.append(presentLine);
            presentLine = new CodeLine();
        }
        line += 1;
    }


    void reserve(Word w) {
        words.put(w.lexeme, w);
    }



    public Lexer(String fileName) {

        reserve(new Word("import", Tag.Import));
        reserve(new Word("class", Tag.Class));
        reserve(new Word("if", Tag.If));
        reserve(new Word("else", Tag.Else));
        reserve(new Word("elif", Tag.Elif));
        reserve(new Word("repeat", Tag.Repeat));
        reserve(new Word("break", Tag.Break));
        reserve(new Word("continue", Tag.Continue));
        reserve(new Word("and", Tag.And));
        reserve(new Word("or", Tag.Or));
        reserve(new Word("not", Tag.Not));
        reserve(new Word("return", Tag.Return));
        reserve(new Word("func", Tag.Func));
        reserve(new Word("none", Tag.None));
        reserve(new Word("null", Tag.Null));
        reserve(new Word("in", Tag.In));
        reserve(new Word("main", Tag.Main));
        reserve(Word.true_);
        reserve(Word.false_);
        input.initial(fileName);
    }

    public void run() {

        while (true) {
            Token t = scan();
            if (t.tag == Tag.Comment) {
                continue;
            }
            if (t.tag == Tag.End) {
                 break;
            }
            presentLine.append(t);
        }

    }

    public Token scan() {
        for( ; ; readch()) {
            if (peek == ' ' || peek == '\t')
                continue;
            else if (peek == '\n')
                addLine();
            else if (peek == '\0') {
                addLine();
                return new Token(Tag.End);
            }
            else break;
        }

        switch (peek) {
            case '=':
                if (readch('='))
                    return Word.equal;
                else
                    return new Token(Tag.Assign);
            case '!':
                if (readch('='))
                    return Word.notEqual;
                //else error
            case '<':
                if (readch('='))
                    return Word.lowerEqual;
                else
                    return new Token(Tag.LT);
            case '>':
                if (readch('='))
                    return Word.greaterEqual;
                else
                    return new Token(Tag.GT);
            case '+':
                if (readch('='))
                    return Word.addEqual;
                else
                    return new Token(Tag.Add);
            case '-':
                if (readch('='))
                    return Word.subEqual;
                else
                    return new Token(Tag.Sub);
            case '*':
                if (readch('='))
                    return Word.mulEqual;
                else
                    return new Token(Tag.Mul);
            case '/':
                if (readch('='))
                    return Word.divEqual;
                else if (peek == '/') {
                    expectWrap();
                    return Word.comment;
                }
                else
                    return new Token(Tag.Div);
            case '%':
                if (readch('='))
                    return Word.modEqual;
                else
                    return new Token(Tag.Mod);
            case '(':readch();return new Token(Tag.LBracket);
            case ')':readch();return new Token(Tag.RBracket);
            case '{':readch();return new Token(Tag.LBrace);
            case '}':readch();return new Token(Tag.RBrace);
            case '[':readch();return new Token(Tag.LSquare);
            case ']':readch();return new Token(Tag.RSquare);
            case ',':readch();return new Token(Tag.Comma);
            case '.':readch();return new Token(Tag.Dot);
            case ':':readch();return new Token(Tag.Colon);
            case '"': {
                StringBuffer b = new StringBuffer();
                while (true) {
                    if (readch('"')) break;
                    b.append(peek);
                }
                String s = b.toString();
                return new Word(s, Tag.String);
            }

        }
        if (Character.isDigit(peek)) {
            int value = 0;
            do {
                value = 10 * value + Character.digit(peek, 10);
                readch();
            } while (Character.isDigit(peek));

            if (peek != '.')
                return new Num(value);

            float fvalue = value, d = 10;

            while (true) {
                readch();
                if (!Character.isDigit(peek)) break;//error
                fvalue = fvalue + Character.digit(peek, 10) / d;
                d = d * 10;
            }
            return new Real(fvalue);
        }

        // an id start from a letter and follow with letter digit or underline
        if (Character.isLetter(peek)) {
            StringBuffer b = new StringBuffer();
            do {
                b.append(peek);
                readch();
            } while (isUnderlineOrLetterOrDigit(peek));
            String s = b.toString();
            Word w = (Word)words.get(s);
            if (w != null)
                return w;
            w = new Word(s, Tag.Identifier);
            words.put(s, w);
            return w;
        }
        return new UnknownChar(peek);
    }

    public CodeFile getFile() {
        return presentFile;
    }
}
