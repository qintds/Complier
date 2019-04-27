public class XNoneObject extends XObject {

    public XNoneObject() {
        super(XType.XNone);
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
}
