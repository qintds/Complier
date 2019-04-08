public abstract class XObject {
    XType type;
    int refCount;

    public abstract void print();
    public abstract void genCode();

}
