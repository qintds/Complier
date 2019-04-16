public class XInstanceObject extends XObject{

    private XEnv env;
    private String className;

    public XInstanceObject(String className, XEnv env) {
        super(XType.xInstance);
        this.className = className;
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
}
