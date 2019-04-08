import java.util.HashMap;

public class XEnv {

    public HashMap<String, XObject> variableMap = new HashMap<>();
    public HashMap<String, XFuncObject> functionMap = new HashMap<>();

    public XEnv parent;

}
