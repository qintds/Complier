public class XClassObject extends XObject{

    private XEnv originalInstanceEnv = new XEnv(null);
    private XEnv classEnv = new XEnv(null);
    private String className;
    private CNode classBody;
    private boolean isInherit;
    private int level;
    private boolean hasInitial = false;

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

    public void setLevel(int i) {
        level = i;
    }

    public void setBaseEnv(XEnv p) {
        originalInstanceEnv.setParent(p);
        classEnv.setParent(p);
    }

    public XInstanceObject initial() {
        XInstanceObject instance = new XInstanceObject(className);
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
        if (identifier == className) {
            hasInitial = true;
        }
        originalInstanceEnv.setXObjectByName(identifier, funcObject);
    }

    public void addInstanceVariable(String identifier, XObject object) {
        originalInstanceEnv.setXObjectByName(identifier, object);
    }

    public XObject getFromClass(String identifier) {
        return classEnv.getXObjectByName(identifier);
    }

    public XObject getFromInstance(String identifier) {
        return originalInstanceEnv.getXObjectByName(identifier);
    }

    public XEnv getClassEnv() {
        return classEnv;
    }

    public boolean isHasInitial(){
        return hasInitial;
    }
}
