public class XNumObject extends XObject {
    public int value;
    public XNumObject(Num num) {
        super(XType.xNum);
        value = num.value;
    }

    @Override
    public void print() {
        System.out.print(value);
    }

    @Override
    public void genCode() {

    }
}
