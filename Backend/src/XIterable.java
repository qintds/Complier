public interface XIterable {
    void add(XObject object);
    XObject get(int i);
    XObject get(XNumObject i);
    int clear();
    XObject clone();
    int length();
}
