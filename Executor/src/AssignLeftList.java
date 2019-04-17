import java.util.ArrayList;

public class AssignLeftList {
    ArrayList<AssignLeftStruct> assignList = new ArrayList<>();
    public void assign(XObject object, XEnv env) {
        if (length() == 1) {
            assignList.get(0).setValue(object, env);
            return;
        }
        if (length() > 1) {
            if (object.type == XType.xList) {
                XListObject valueList = (XListObject)object;
                if (valueList.length() == length()) {
                    for (int i = 0; i < length(); i++) {
                        assignList.get(i).setValue(valueList.get(i), env);
                    }
                } else {
                    //To much value
                }
            } else if (object.type == XType.xTuple) {
                XTupleObject valueList = (XTupleObject)object;
                if (valueList.length() == length()) {
                    for (int i = 0; i < length(); i++) {
                        assignList.get(i).setValue(valueList.get(i), env);
                    }
                } else {
                    //To much value
                }
            } else {
                // not iterable value
            }
        }
    }

    public void addLeft(AssignLeftStruct struct) {
        assignList.add(struct);
    }

    public int length() {
        return assignList.size();
    }
}
