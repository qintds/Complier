public class XRealObject extends XObject {
    public double value;

    public XRealObject(Real real) {
        super(XType.xReal);
        this.value = real.value;
    }

    public XRealObject(double d) {
        super(XType.xReal);
        this.value = d;
    }

    public XRealObject(XNumObject numObject) {
        super(XType.xReal);
        this.value = numObject.value;
    }

    public XRealObject(int i) {
        super(XType.xReal);
        this.value = i;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public void genCode() {

    }

    @Override
    public boolean equals(XObject object) {
        if (object.type == XType.xReal) {
            return ((XRealObject)object).value == this.value;
        }
        return false;
    }
}
