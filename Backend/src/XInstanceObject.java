public class XInstanceObject extends XObject{

    private XEnv env;
    private String className;

    public XInstanceObject(String className) {
        super(XType.xInstance);
        this.className = className;
        env = new XEnv(null);
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

    public String getClassName() {
        return className;
    }

    public XObject getInstanceMember(String identifier) {
        return env.getXObjectByNameQualify(identifier);
    }


    public void setInstanceMember(String identifier, XObject object) {
        env.setXObjectByName(identifier, object);
    }
}
