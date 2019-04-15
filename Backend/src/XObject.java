public abstract class XObject {
    XType type;
    int refCount;
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

}
