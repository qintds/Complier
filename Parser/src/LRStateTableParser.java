import java.util.HashMap;
import java.util.Stack;

public class LRStateTableParser {
    private CodeFile parseFile;
    // some variable for interpreter
    // ....
    int envLevel = 0;

    // status stack record the order of status
    private Stack<Integer> statusStack = new Stack<>();
    private Stack<Object> valueStack = new Stack<>();
    private Stack<Tag> parseStack = new Stack<>();

    // Executor
    private Executor executor;
    private int execMode = 0;
    private TreeBuilder treeBuilder;

    private HashMap<Integer, HashMap<Tag, Integer>> lrStateTable;

    private Tag inputTag;
    private Token inputToken;

    private void initExecutor() {
        treeBuilder = new TreeBuilder(valueStack);
    }

    public LRStateTableParser(CodeFile parseFile) {
        this.parseFile = parseFile;
        this.parseFile.initRead();

        statusStack.push(0);
        valueStack.push(null);

        lrStateTable = GrammarStateManager.getInstance().getLRStateTable();
        inputTag = Tag.ExtDefList;
        inputToken = nextToken();
        parseStack.push(inputTag);
        // Tree Builder

        initExecutor();


    }

    public void parse() {
        while (true) {
            Integer action = getAction(statusStack.peek(), inputTag);

            if (action == null) {
                if (inputTag == Tag.LF) {
                    inputToken = nextToken();
                    inputTag = inputToken.tag;
                    continue;
                }
                // error
                System.out.println("something wrong");
                return;
            }

            if (action > 0) {
                // shift
                // push status, push tag
                statusStack.push(action);



                // shift when terminal, goto when non terminal
                if (Tag.isTerminal(inputTag)) {
                    parseStack.push(inputTag);
                    valueStack.push(inputToken);
                    envMove();
                    // read
                    inputToken = nextToken();
                    inputTag = inputToken.tag;
                } else {
                    inputTag = inputToken.tag;
                }



            } else if (action == 0) {
                // accept
                System.out.println("Process finished");// with exit code 0
                return;
            } else {
                // reduce
                int reduceProductionNum = - action;
                Production production = ProductionManager.getInstance().getProductionByIndex(reduceProductionNum);
                CNode reduceResult = doReduce(reduceProductionNum);

                // pop the length of the production
                int rightSize = production.right.size();
                while (rightSize > 0) {
                    parseStack.pop();
                    valueStack.pop();
                    statusStack.pop();
                    rightSize--;
                }
                inputTag = production.left;
                parseStack.push(inputTag);
                valueStack.push(reduceResult);
            }
        }
    }

    public Token nextToken() {
        return parseFile.next();
    }

    public Token token() {
        return parseFile.getPreviousToken();
    }

    private Integer getAction(Integer currentState, Tag currentInput) {
        HashMap<Tag, Integer> action = lrStateTable.get(currentState);
        if (action != null) {
            Integer next = action.get(currentInput);
            if (next != null) {
                return next;
            }
        }
        return null;
    }

    private void envMove() {
        if (inputTag == Tag.LBrace)
            envLevel++;
        if (inputTag == Tag.RBrace)
            envLevel--;
    }

    private CNode doReduce(int productionNum) {
        return treeBuilder.reduceAction(productionNum);
    }
}
