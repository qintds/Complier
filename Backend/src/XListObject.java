import java.util.ArrayList;

public class XListObject extends XObject {

    ArrayList<XObject> list;

    public XListObject() {
        super(XType.xList);
        list = new ArrayList<>();
    }

    public void add(XObject object) {
        list.add(object);
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
