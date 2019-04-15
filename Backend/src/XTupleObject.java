import java.util.ArrayList;

public class XTupleObject extends XObject {

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
}
