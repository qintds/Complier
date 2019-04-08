import java.util.Stack;

public class Interpreter implements Executor {

    private Stack<Object> valueStack;
    private XEnv environment;

    public Interpreter(Stack<Object> valueStack){
        this.valueStack = valueStack;
        environment = new XEnv();
    }

    @Override
    public void reduceAction(int productionNum) {
        GrammarEnum n = GrammarEnum.values()[productionNum];
        switch (n) {
            case Primary_To_Num:

                break;
        }
    }
}
