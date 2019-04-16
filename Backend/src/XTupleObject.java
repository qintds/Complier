import java.util.ArrayList;

public class XTupleObject extends XObject implements XIterable {

    ArrayList<XObject> list;
    boolean settle = false;

    public XTupleObject(){
        super(XType.xTuple);
        list = new ArrayList<>();
    }

    public void initial(XObject object) {
        if (!settle)
            list.add(object);
    }

    public void setSettle() {
        settle = true;
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

    @Override
    public void add(XObject object) {

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
}
