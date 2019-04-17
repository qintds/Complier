public class AssignLeftStruct {
    private AssignableType type;
    private XObject base;
    private XObject key;
    private String identifier;

    public AssignLeftStruct() {
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public void setType(AssignableType type) {
        this.type = type;
    }

    public void setSquare(XObject a, XObject b) {
        base = a;
        key = b;
    }

    public void setValue(XObject value, XEnv table) {
        if (type == AssignableType.single) {
            table.setXObjectByName(identifier, value);
        } else if (type == AssignableType.square) {
            //list or dict
        }
    }

}
