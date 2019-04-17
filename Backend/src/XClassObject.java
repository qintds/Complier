public class XClassObject extends XObject{

    private XEnv originalInstanceEnv = new XEnv(null);
    private XEnv classEnv = new XEnv(null);
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

    public XInstanceObject initial() {
        XInstanceObject instance = new XInstanceObject(className, originalInstanceEnv);
        return instance;
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

    public XEnv getOriginalInstanceEnv() {
        return originalInstanceEnv;
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
        classEnv.setXObjectByName(identifier, funcObject);
    }

    public void addClassVariable(String identifier, XObject object) {
        classEnv.setXObjectByName(identifier, object);
    }

    public void addInstanceFunction(String identifier, XFuncObject funcObject) {
        originalInstanceEnv.setXObjectByName(identifier, funcObject);
    }

    public void addInstanceVariable(String identifier, XObject object) {

    }
}
