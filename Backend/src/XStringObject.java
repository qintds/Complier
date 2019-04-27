public class XStringObject extends XObject {
    public String value;
    public XStringObject(Word word) {
        super(XType.xString);
        if (word.tag == Tag.String)
            value = word.toString();
        initialEnv();
    }

    public XStringObject(String s) {
        super(XType.xString);
        value = s;
    }

    public XStringObject(XObject object) {
        super(XType.xString);
        if (object.type == XType.xNum) {
            value = Integer.toString(((XNumObject)object).value);
        } else if (object.type == XType.xReal) {
            value = Double.toString(((XRealObject)object).value);
        } else if (object.type == XType.xString) {
            value = ((XStringObject)object).value;
        } else if (object.type == XType.xBool) {
            value = Boolean.toString(((XBoolObject)object).value);
        }
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
        if (object.type == XType.xString) {
            return (((XStringObject)object).value).equals(this.value);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public void initialEnv() {
        env.setXObjectByNameQualify("split", OFunctionTable.getInstance().oFuncCreator("stringSplit"));
    }
}
