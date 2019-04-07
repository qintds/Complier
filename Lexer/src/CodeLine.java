import java.util.Vector;

public class CodeLine {
    public int totalTokenCount = 0;
    public int trueLineNum = 0;
    public Vector<Token> tokenList = new Vector<Token>();

    public void append(Token t) {
        totalTokenCount += 1;
        tokenList.add(t);
    }

    public Token get(int i) {
        return tokenList.get(i);
    }
}
