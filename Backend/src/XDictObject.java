import java.util.HashMap;

public class XDictObject extends XObject{

    HashMap<XObject, XObject> hashMap;

    public XDictObject() {
        super(XType.xDict);
        hashMap = new HashMap<>();

    }

    public void put(XObject key, XObject value) {
        hashMap.put(key, value);
    }

    public XObject get(XObject key) {
        return hashMap.get(key);
    }

    @Override
    public void print() {

    }

    @Override
    public void genCode() {

    }

    @Override
    public boolean equals(XObject object){
        return false;
    }
}
