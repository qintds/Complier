import java.util.ArrayList;

public class XListObject extends XObject implements XIterable {

    ArrayList<XObject> list;

    public XListObject() {
        super(XType.xList);
        list = new ArrayList<>();
    }

    public void add(XObject object) {
        list.add(object);
    }

    @Override
    public XObject get(int i) {
        return null;
    }

    @Override
    public int remove(int i) {
        return 0;
    }

    @Override
    public int remove(XObject object) {
        return 0;
    }

    @Override
    public int clear() {
        return 0;
    }

    @Override
    public XObject clone() {
        return null;
    }

    @Override
    public int length() {
        return 0;
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
