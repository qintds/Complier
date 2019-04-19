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
        System.out.print('(');
        for (int i = 0; i < list.size(); i++) {
            if (i != 0)
                System.out.print(", ");
            list.get(i).print();
        }
        System.out.print(')');
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
        return list.get(i);
    }

    @Override
    public XObject get(XNumObject i) {
        return list.get(i.value);
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
    public int size() {
        return list.size();
    }
}
