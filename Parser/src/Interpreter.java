import java.util.Stack;

public class Interpreter implements Executor {

    private Stack<Object> valueStack = new Stack<>();
    private XEnv environment = new XEnv();

    @Override
    public void reduceAction(int productionNum) {
        switch (productionNum) {

        }
    }
}
