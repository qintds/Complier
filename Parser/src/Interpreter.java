import java.util.Stack;

public class Interpreter implements Executor {

    private Stack<Object> valueStack;
    private XEnv environment;

    public Interpreter(Stack<Object> valueStack){
        this.valueStack = valueStack;
        environment = new XEnv();
    }

    @Override
    public XObject reduceAction(int productionNum) {
        XObject xObject = null;
        GrammarEnum n = GrammarEnum.values()[productionNum];
        switch (n) {
            case Primary_To_Num:
                xObject = new XNumObject((Num)valueStack.peek());
                break;
            case Primary_To_Real:
                xObject =  new XRealObject((Real)valueStack.peek());
                break;
            case Primary_To_True:
            case Primary_To_False:
                xObject = new XBoolObject((Word)valueStack.peek());
                break;
            case Primary_To_String:
                xObject = new XStringObject((Word)valueStack.peek());break;
            case Primary_To_LBracket_NoAssignExp_RBracket:
                xObject = (XObject) valueStack.get(valueStack.size() - 2);
            case Primary_To_Variable:
                xObject = (XObject) valueStack.peek();

        }
        return xObject;
    }
}
