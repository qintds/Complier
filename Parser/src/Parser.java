
// Bottom up parser LALR grammar
public class Parser {

    public static void printCF(CodeFile c) {
        for(int i = 0; i < c.totalLine; i++) {
            CodeLine l = c.getLine(i);
            for(int j = 0; j < l.totalTokenCount; j++) {
                System.out.print(l.get(j).toString() + ' ');
            }
            System.out.println();
        }
    }

    public static void main(String[] args) {
        ProductionManager productionManager = ProductionManager.getInstance();
        System.out.println(GrammarInitializer.getInstance().productionNum);
        productionManager.runFirstSet();
        // productionManager.printAllProductionOnly();
        GrammarStateManager stateManager = GrammarStateManager.getInstance();
        stateManager.buildTransitionStateMechine();

        Lexer lexer = new Lexer("codeFile");
        lexer.run();

        printCF(lexer.presentFile);

        LRStateTableParser parser = new LRStateTableParser(lexer.presentFile);
        parser.parse();

    }


}
