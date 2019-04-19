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
    public boolean equals(XObject object) {
        return false;
    }
}
