import java.util.HashMap;
import java.util.Map;

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
        System.out.print('{');
        int count = 0;
        for (Map.Entry<XObject,XObject> item : hashMap.entrySet()) {
            if (count != 0) {
                System.out.print(", ");
            }
            item.getKey().print();
            System.out.print(':');
            item.getValue().print();
            count++;
        }
        System.out.print('}');
    }

    @Override
    public void genCode() {

    }

    @Override
    public boolean equals(Object obj){
        return false;
    }

    @Override
    public int hashCode() {
        return 0;
    }

    @Override
    public void initialEnv() {

    }
}
