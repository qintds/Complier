import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class XEnv {

    public HashMap<String, XObject> variableMap = new HashMap<>();
    // origin func and class, their name can not be changed
    public ArrayList<String> funcAndClass = new ArrayList<>();

    public int level;
    public XEnvOwner envOwner;

    public XEnv parent;

    public XEnv(XEnv env) {
        parent = env;
        envOwner = XEnvOwner.xOther;
    }

    public void merge(XEnv env) {
        variableMap.putAll(env.variableMap);
    }

    public void merge(HashMap<String, XFuncObject> funcMap, HashMap<String, XClassObject> classMap) {
        for (Map.Entry<String, XFuncObject> item: funcMap.entrySet()) {
//            if (!variableMap.containsKey(item.getKey())) {
                variableMap.put(item.getKey(), item.getValue());
//                funcAndClass.add(item.getKey());
//            } else {
                //repeat
//            }
        }
        for (Map.Entry<String, XClassObject> item: classMap.entrySet()) {
//            if (!variableMap.containsKey(item.getKey())) {
                variableMap.put(item.getKey(), item.getValue());
//                funcAndClass.add(item.getKey());
//            } else {
                //repeat
//            }
        }
    }


    public XObject getXObjectByName(String identifier) {
        XObject obj = variableMap.get(identifier);
        if (obj == null && parent != null) {
            obj = parent.getXObjectByName(identifier);
            if (obj == null) {
                // undefined identifier
                return null;
            }
        }
        return obj;
    }

    public XObject getXObjectByNameQualify(String identifier) {
        return variableMap.get(identifier);
    }

    public boolean hasNameQualify(String identifier) {
        return variableMap.containsKey(identifier);
    }

    // just cover it
    public void setXObjectByName(String identifier, XObject obj) {
        if (!hasName(identifier)) {
            // never use this name
            variableMap.put(identifier, obj);
        } else if (hasNameQualify(identifier)) {
            // already use this name, and this env own this name
            variableMap.put(identifier, obj);
        } else if (parent != null) {
            parent.setXObjectByName(identifier, obj);
        }
    }

    public void setXObjectByNameQualify(String identifier, XObject obj) {
        variableMap.put(identifier, obj);
    }

    public boolean hasName(String identifier) {
        boolean has = variableMap.containsKey(identifier);
        if (!has && parent != null) {
            has = parent.hasName(identifier);
        }
       return has;
    }


    public boolean thisHasFuncOrClass(String identifier) {
        return funcAndClass.contains(identifier);
    }

    public void setParent(XEnv env) {
        parent = env;
    }

    public XEnv copy() {
        XEnv newEnv = new XEnv(this.parent);
        newEnv.variableMap.putAll(variableMap);
        return newEnv;
    }

}
