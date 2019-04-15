public class XBoolObject extends XObject{

    public boolean value;
    public XBoolObject(Word word) {
        super(XType.xBool);
        if (word.tag == Tag.True)
            value = true;
        else if (word.tag == Tag.False)
            value = false;
    }

    public XBoolObject(boolean bool) {
        super(XType.xBool);
        value = bool;
    }


    @Override
    public void print() {

    }

    @Override
    public void genCode() {

    }

    @Override
    public boolean equals(XObject object) {
        if (object.type == XType.xBool) {
            return ((XBoolObject)object).value == this.value;
        }
        return false;
    }
}
