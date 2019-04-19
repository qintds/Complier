public class XStringObject extends XObject {
    public String value;
    public XStringObject(Word word) {
        super(XType.xString);
        if (word.tag == Tag.String)
            value = word.toString();
    }

    public XStringObject(String s) {
        super(XType.xString);
        value = s;
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
        return false;
    }
}
