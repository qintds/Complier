import java.util.Vector;

public class CodeFile {
    public int totalLine = 0;
    public Vector<CodeLine> lineList = new Vector<CodeLine>();
    private boolean isRead = false;
    private boolean isEnd = false;
    private int presentLineNum = 0;
    private int presentTokenNum = 0;
    private CodeLine presentLine = new CodeLine();
    private Token previousToken = new Token(Tag.Unknown);
    private Token presentToken = new Token(Tag.Unknown);

    public void append(CodeLine c) {
        totalLine += 1;
        lineList.add(c);
    }

    public CodeLine getLine(int i) {
        return lineList.get(i);
    }

    public void initRead() {
        if (!isRead) {
            isRead = true;
            isEnd = false;
            presentLineNum = 0;
            presentTokenNum = 0;
            presentLine = lineList.get(presentLineNum);
        }
    }

    public Token next() {
        if (isEnd) return null;
        if (presentTokenNum >= presentLine.totalTokenCount) {
            presentTokenNum = 0;
            if (presentLineNum >= lineList.size() - 1) {
                isEnd = true;
                return new Token(Tag.End);
            } else {
                presentLineNum++;
                presentLine = lineList.get(presentLineNum);
            }
        }
        previousToken = presentToken;
        presentToken = presentLine.get(presentTokenNum++);
        return presentToken;
    }

    public Token getPreviousToken() {
        return previousToken;
    }

    public int getPresentLineOriginalNum() {
        return presentLine.trueLineNum;
    }

    public int getPresentTokenOriginalNum() {
        return presentTokenNum;
    }


}
