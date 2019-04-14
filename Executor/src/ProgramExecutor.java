import java.util.Stack;

public class ProgramExecutor {
    CNode root;
    XEnv runEnv;
    CNode pointer;
    Stack<CNode> nodeStack;
    int statusNum = 0;
    int level;

    public ProgramExecutor(CNode root) {
        this.root = root;
        runEnv = new XEnv(null);
        pointer = root;
        nodeStack = new Stack<>();
    }

    public int run() {
        if (root.production == GrammarEnum.Program_To_ExtDefList) {
            extDefList(root.children.get(0));
        } else {

        }
        return statusNum;
    }

    public void extDefList(CNode node) {
        
    }
}
