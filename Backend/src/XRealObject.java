public class XRealObject extends XObject {
    public double value;

    public XRealObject(Real real) {
        super(XType.xReal);
        this.value = real.value;
    }

    @Override
    public void print() {

    }

    @Override
    public void genCode() {

    }
}
