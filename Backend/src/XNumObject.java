public class XNumObject extends XObject {
    public int value;
    public XNumObject(Num num) {
        super(XType.xNum);
        value = num.value;
    }

    public XNumObject(int num) {
        super(XType.xNum);
        value = num;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public void genCode() {

    }

    @Override
    public boolean equals(Object obj) {
        XObject object = (XObject)obj;
        if (object.type == XType.xNum) {
            return ((XNumObject)object).value == this.value;
        }
        return false;
    }
    @Override
    public int hashCode() {
        return Integer.hashCode(value);
    }

    @Override
    public void initialEnv() {

    }
}
