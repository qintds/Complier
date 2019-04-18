public class XFuncObject extends XObject {
    private XEnv funcEnv;
    private CNode funcBody;
    private String funcName;
    private boolean containParams = false;
    private int level;
    public boolean isOriginal = false;


    public XFuncObject(Word word, CNode body) {
        super(XType.xFunc);
        if (word.tag == Tag.Identifier) {
            this.funcName = word.toString();
        }
        funcBody = body;
        funcEnv = new XEnv(null);
    }

    public void setLevel(int i) {
        level = i;
    }

    public void setBaseEnv(XEnv p) {
        funcEnv.setParent(p);
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

    @Override
    public boolean equals(XObject object) {
        return false;
    }

    public CNode getFuncBody() {
        return funcBody;
    }
    //ClassFuncDeclaration -> FUNC IDENTIFIER ( SELF , ParamList ) CompSt
    //ClassFuncDeclaration -> FUNC IDENTIFIER ( SELF ) CompSt
    //
    //FuncDeclaration -> FUNC IDENTIFIER ( ParamList ) CompSt
    //FuncDeclaration -> FUNC IDENTIFIER ( ) CompSt
    public CNode getParams() {
        return funcBody.getChildFromLast(2);
    }

    public CNode getContent() {
        return funcBody.getChildFromLast(1);
    }

    public String getFuncName() {
        return funcName;
    }

    public boolean isContainParams() {
        return containParams;
    }

    public XEnv getFuncEnv() {
        return funcEnv.copy();
    }
}
