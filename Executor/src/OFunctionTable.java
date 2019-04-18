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

    }

}
