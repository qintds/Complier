public class XStringObject extends XObject {
    public String value;
    public XStringObject(Word word) {
        super(XType.xString);
        if (word.tag == Tag.String)
            value = word.toString();
    }
    @Override
    public void print() {

    }

    @Override
    public void genCode() {

    }
}
