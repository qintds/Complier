import java.util.HashMap;

public class XFuncObject extends XObject {
    private HashMap<String, XObject> params = new HashMap<>();
    private CNode funcBody;
    private String funcName;
    private boolean containParams = false;

    public XFuncObject(Word word, CNode body) {
        super(XType.xFunc);
        if (word.tag == Tag.Identifier) {
            this.funcName = word.toString();
        }
        funcBody = body;
    }

    public void setContainParams(boolean b) {
        containParams = b;
    }

    @Override
    public void print() {

    }

    @Override
    public void genCode() {

    }
}
