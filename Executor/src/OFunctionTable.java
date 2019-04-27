import java.util.HashMap;

public class OFunctionTable {


    public static OFunctionTable self;
    private XEnv env = new XEnv(null);
    private HashMap<String, XFuncObject> oFuncMap = new HashMap<>();
    private OFunctionTable() {

    }

    public static OFunctionTable getInstance() {
        if (self == null) {
            self = new OFunctionTable();
            self.originalFuncEnv();
        }
        return self;
    }

    public XFuncObject oFuncCreator(String name) {
        XFuncObject temp;
        if (oFuncMap.containsKey(name)){
            temp = oFuncMap.get(name);
        } else {
            temp = new XFuncObject(name, null);
            temp.isOriginal = true;
            temp.setContainParams(true);
            oFuncMap.put(name, temp);
        }
        return temp;
    }

    private void originalFuncEnv() {
        env.setXObjectByNameQualify("print", oFuncCreator("print"));
        env.setXObjectByNameQualify("println", oFuncCreator("println"));
        env.setXObjectByNameQualify("type", oFuncCreator("type"));
        env.setXObjectByNameQualify("str", oFuncCreator("str"));
    }

    public XEnv getEnv() {
        return env;
    }

    public XObject callOriginalFunc(String funcName, XObject args) {
        switch (funcName) {
            case "print":
                return OFunction.print(args);
            case "println":
                return OFunction.println(args);
            case "type":
                return OFunction.printType(args);
            case "str":
                return OFunction.str(args);
                //list
            case "listAppend":
                return OFunction.listAppend(args);
            case "listClear":
                return OFunction.listClear(args);
            case "listSize":
                return OFunction.listSize(args);
                //String
            case "stringSplit":
                return OFunction.stringSplit(args);
        }
        return null;
    }

}
