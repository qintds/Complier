public class XBoolObject extends XObject{

    public boolean value;
    public XBoolObject(Word word) {
        super(XType.xBool);
        if (word.tag == Tag.True)
            value = true;
        else if (word.tag == Tag.False)
            value = false;
    }


    @Override
    public void print() {

    }

    @Override
    public void genCode() {

    }
}
