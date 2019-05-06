
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

    public static XEnv importFile(String  filename) {
        Lexer lexer = new Lexer(filename);
        lexer.run();

        printCF(lexer.presentFile);

        LRStateTableParser lrParser = new LRStateTableParser(lexer.presentFile);
        lrParser.parse();

        ProgramExecutor executor = new ProgramExecutor(lrParser.getProgram(), lrParser.getFuncMap(), lrParser.getClassMap());
        executor.run();
        return executor.runEnv;
    }

    public static void main(String[] args) {
        ProductionManager productionManager = ProductionManager.getInstance();
//        System.out.println(GrammarInitializer.getInstance().productionNum);
        productionManager.runFirstSet();
        // productionManager.printAllProductionOnly();
        GrammarStateManager stateManager = GrammarStateManager.getInstance();
        stateManager.buildTransitionStateMechine();

        String fileName = "codeFile";
        if (args.length >0) {
            fileName = args[0];
        }
        Lexer lexer = new Lexer(fileName);
        lexer.run();

//        printCF(lexer.presentFile);

        LRStateTableParser lrParser = new LRStateTableParser(lexer.presentFile);
        lrParser.parse();

        ProgramExecutor executor = new ProgramExecutor(lrParser.getProgram(), lrParser.getFuncMap(), lrParser.getClassMap());
        executor.run();

    }


}
