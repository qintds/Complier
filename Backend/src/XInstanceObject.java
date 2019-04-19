public class XInstanceObject extends XObject{

    private String className;

    public XInstanceObject(String className) {
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

    public String getClassName() {
        return className;
    }
}
