public class OFunctionTable {


    public static OFunctionTable self;
    private XEnv env = new XEnv(null);
    private OFunctionTable() {

    }

    public static OFunctionTable getInstance() {
        if (self == null) {
            self = new OFunctionTable();
            self.originalFuncEnv();
        }
        return self;
    }

    private XFuncObject oFuncCreater(String name) {
        XFuncObject temp = new XFuncObject(name, null);
        temp.isOriginal = true;
        temp.setContainParams(true);
        return temp;
    }

    private void originalFuncEnv() {
        env.setXObjectByName("print", oFuncCreater("print"));
        env.setXObjectByName("println", oFuncCreater("println"));
        env.setXObjectByName("type", oFuncCreater("type"));
    }

    public XEnv getEnv() {
        return env;
    }

    public void callOriginalFunc(String funcName, XObject args) {
        switch (funcName) {
            case "print":
                OFunction.print(args);
            case "println":
                OFunction.println(args);
            case "type":
                OFunction.printType(args);
        }
    }

}
