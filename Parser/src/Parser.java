
// Bottom up parser LALR grammar
public class Parser {
    public static void main(String[] args) {
        ProductionManager productionManager = ProductionManager.getInstance();
        System.out.println(GrammarInitializer.getInstance().productionNum);
        productionManager.runFirstSet();
        // productionManager.printAllProductionOnly();
        GrammarStateManager stateManager = GrammarStateManager.getInstance();
        stateManager.buildTransitionStateMechine();

        Lexer lexer = new Lexer(null);
        lexer.run();


    }
}
