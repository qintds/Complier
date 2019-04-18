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

    public void setDot(XObject a) {
        base = a;
    }

    public void setValue(XObject value, XEnv table) {
        if (type == AssignableType.single) {
            table.setXObjectByName(identifier, value);
        } else if (type == AssignableType.square) {
            if (base.type == XType.xDict) {
                ((XDictObject)base).put(key, value);
            } else if (base.type == XType.xList) {
                if (key.type == XType.xNum) {
                    ((XListObject)base).set((XNumObject) key, value);
                } else {
                    // can not get by key
                }
            }
        } else if (type == AssignableType.dot) {
            base.setInstanceMember(identifier, value);
        }
    }

}
