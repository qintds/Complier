public abstract class XObject {
    XType type;
    int refCount;
    XEnv env;
    public XObject(XType xType) {
        type = xType;
    }

    public abstract void print();
    public abstract void genCode();
    public abstract boolean equals(XObject object);
    public void refAdd() {
        refCount++;
    }
    public void refSub() {
        refCount--;
    }

    public XObject getInstanceMember(String identifier) {
        return env.getXObjectByNameQualify(identifier);
    }
    public void setInstanceMember(String identifier, XObject object) {
        if (type == XType.xClass || type == XType.xInstance)
            env.setXObjectByNameQualify(identifier, object);
    }

}
