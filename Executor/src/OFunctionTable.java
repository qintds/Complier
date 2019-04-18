public class OFunctionTable {


    public static OFunctionTable self;

    private OFunctionTable() {

    }

    public static OFunctionTable getInstance() {
        if (self == null) {
            self = new OFunctionTable();
        }
        return self;
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
