public interface XIterable {
    void add(XObject object);
    XObject get(int i);
    int remove(int i);
    int remove(XObject object);
    int clear();
    XObject clone();
    int length();
}
