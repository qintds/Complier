import java.util.HashMap;

public class XEnv {

    public HashMap<String, XObject> variableMap = new HashMap<>();
    public HashMap<String, XFuncObject> functionMap = new HashMap<>();
    public HashMap<String, XClassObject> classMap = new HashMap<>();

    public int level;

    public XEnv parent;

    public XEnv(XEnv env) {
        parent = env;
    }

    public void merge(HashMap<String, XFuncObject> funcMap, HashMap<String, XClassObject> classMap) {
        functionMap.putAll(funcMap);
        this.classMap.putAll(classMap);
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

    public void setFunction(String identifier, XFuncObject funcObject) {
        functionMap.put(identifier, funcObject);
    }

    public boolean hasFunctionDefine(String identifier) {
        return functionMap.containsKey(identifier);
    }

    public void setParent(XEnv env) {
        parent = env;
    }

}
