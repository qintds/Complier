public class XClassObject extends XObject{

    private XEnv originalEnv = new XEnv(null);
    private String className;
    private CNode classBody;
    private boolean isInherit;

    public void setInherit(boolean inherit) {
        isInherit = inherit;
    }

    public XClassObject(Word word, CNode cNode) {
        super(XType.xClass);
        if (word.tag == Tag.Identifier) {
            className = word.toString();
        }
        classBody = cNode;
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

    public XEnv getOriginalEnv() {
        return originalEnv;
    }

    public String getClassName() {
        return className;
    }

    public CNode getClassBody() {
        return classBody;
    }

    public boolean isInherit() {
        return isInherit;
    }

    public void addClassFunction(String identifier, XFuncObject funcObject) {
        if (!originalEnv.hasFunctionDefine(identifier))
        {
            originalEnv.setFunction(identifier, funcObject);
        }
        else {
            // error repeat member function
        }
    }
}
