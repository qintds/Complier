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

    public void set(int i, XObject object) {
        list.set(i, object);
    }

    public void set(XNumObject i, XObject object) {
        list.set(i.value, object);
    }


    @Override
    public XObject get(int i) {
        return list.get(i);
    }

    @Override
    public XObject get(XNumObject i) {
        return list.get(i.value);
    }

    public int remove(int i) {
        return 0;
    }

    public int remove(XNumObject i) {
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
