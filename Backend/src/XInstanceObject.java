public class XInstanceObject extends XObject{

    private String className;

    public XInstanceObject(String className, XEnv funcEnv) {
        super(XType.xInstance);
        this.className = className;
        env.merge(funcEnv);
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

    public String getClassName() {
        return className;
    }
}
