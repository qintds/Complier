import java.util.ArrayList;

public class AssignLeftList {
    ArrayList<AssignLeftStruct> assignList = new ArrayList<>();
    public void assign(XObject object) {

    }

    public void addLeft(AssignLeftStruct struct) {
        assignList.add(struct);
    }

    public int length() {
        return assignList.size();
    }
}
