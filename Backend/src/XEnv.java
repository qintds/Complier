import java.util.HashMap;

public class XEnv {

    public HashMap<String, XObject> variableMap = new HashMap<>();
    public HashMap<String, XFuncObject> functionMap = new HashMap<>();

    public XEnv parent;

    public XEnv(XEnv env) {
        parent = env;
    }

    public XObject getVariable(String identifier) {
        XObject obj = variableMap.get(identifier);
        if (obj == null) {
            obj = parent.getVariable(identifier);
            if (obj == null) {
                // undefined identifier
            }
        }
        return null;
    }


    public void setVariable(String identifier, XObject obj) {
        variableMap.put(identifier, obj);
    }

}
