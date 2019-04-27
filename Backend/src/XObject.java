public abstract class XObject {
    XType type;
    int refCount;
    XEnv env;
    public XObject(XType xType) {
        type = xType;
        env = new XEnv(null);
    }

    public abstract void print();
    public abstract void genCode();
//    public abstract boolean equals(XObject object);
    public abstract boolean equals(Object object);
    public abstract int hashCode();
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
            env.setXObjectByNameQualify(identifier, object);
    }


    public abstract void initialEnv();

}
