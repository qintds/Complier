public class XClassObject extends XObject{

    private XEnv originalInstanceEnv = new XEnv(null);
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
        env.setParent(p);
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
    public boolean equals(Object obj) {
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public void initialEnv() {

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
        env.setXObjectByNameQualify(identifier, funcObject);
    }

    public void addClassVariable(String identifier, XObject object) {
        env.setXObjectByNameQualify(identifier, object);
    }

    public void addInstanceFunction(String identifier, XFuncObject funcObject) {
        if (identifier == className) {
            hasInitial = true;
        }
        originalInstanceEnv.setXObjectByNameQualify(identifier, funcObject);
    }

    public void addInstanceVariable(String identifier, XObject object) {
        originalInstanceEnv.setXObjectByName(identifier, object);
    }

    public XObject getFromClass(String identifier) {
        return env.getXObjectByNameQualify(identifier);
    }

    public XObject getFromInstance(String identifier) {
        return originalInstanceEnv.getXObjectByNameQualify(identifier);
    }

    public XEnv getClassEnv() {
        return env;
    }

    public boolean isHasInitial(){
        return hasInitial;
    }
}
