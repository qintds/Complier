import java.util.ArrayList;
import java.util.HashMap;

public class GrammarInitializer {
    // sentence num for interpreter, will compensate later
    // public static final int SENTENCE

    public int productionNum = 0;
    private static GrammarInitializer self = null;
    // one non terminal may have a couple of productions
    public HashMap<Tag, ArrayList<Production>> productionMap = new HashMap<>();
    // those productions with same left will be a symbol
    public HashMap<Tag, Symbol> symbolMap = new HashMap<>();
    // symbols store all symbol
    public ArrayList<Symbol> symbols = new ArrayList<>();
    // single instance mode
    public static GrammarInitializer getInstance() {
        if (self == null)
            self = new GrammarInitializer();
        return self;
    }

    private GrammarInitializer() {
        initAllProduction();
        addTerminalToSymbolMapAndArray();
    }
    private void initAllProduction() {
        // Program -> ExtDefList
        generateProduction(Tag.Program, new Tag[]{Tag.ExtDefList}, true);

        // ExtDefList -> ExtDefList ExtDef | ExtDef
        generateProduction(Tag.ExtDefList, new Tag[]{Tag.ExtDef}, false);
        generateProduction(Tag.ExtDefList, new Tag[]{Tag.ExtDefList, Tag.ExtDef}, false);


//        ExtDef -> ImportDeclaration
//                | ClassDeclaration
//                | FuncDeclaration
//                | StmtList
        generateProduction(Tag.ExtDef, new Tag[]{Tag.ImportDeclaration}, false);
        generateProduction(Tag.ExtDef, new Tag[]{Tag.ClassDeclaration}, false);
        generateProduction(Tag.ExtDef, new Tag[]{Tag.FuncDeclaration}, false);
        generateProduction(Tag.ExtDef, new Tag[]{Tag.StmtList}, false);


//        ImportDeclaration -> IMPORT PackageChain
        generateProduction(Tag.ImportDeclaration, new Tag[]{Tag.Import, Tag.PackageChain}, false);


//        PackageChain -> IDENTIFIER | PackageChain . IDENTIFIER
        generateProduction(Tag.PackageChain, new Tag[]{Tag.PackageChain, Tag.Dot, Tag.Identifier}, false);
        generateProduction(Tag.PackageChain, new Tag[]{Tag.Identifier}, false);

        //ClassDeclaration -> CLASS IDENTIFIER : Super ClassBody
        //ClassDeclaration -> CLASS IDENTIFIER ClassBody
        generateProduction(Tag.ClassDeclaration, new Tag[]{Tag.Class, Tag.Identifier, Tag.Colon, Tag.Super, Tag.ClassBody}, false);
        generateProduction(Tag.ClassDeclaration, new Tag[]{Tag.Class, Tag.Identifier, Tag.ClassBody}, false);


//        Super -> Super , PackageChain | PackageChain
        generateProduction(Tag.Super, new Tag[]{Tag.Super, Tag.Comma, Tag.PackageChain}, false);
        generateProduction(Tag.Super, new Tag[]{Tag.PackageChain}, false);

//        ClassBody -> { ClassBodyDeclarations }
        generateProduction(Tag.ClassBody, new Tag[]{Tag.LBrace, Tag.ClassBodyDeclarations, Tag.RBrace}, false);

//        ClassBodyDeclarations -> ClassBodyDeclaration
//                | ClassBodyDeclarations ClassBodyDeclaration
        generateProduction(Tag.ClassBodyDeclarations, new Tag[]{Tag.ClassBodyDeclarations, Tag.ClassBodyDeclaration}, false);
        generateProduction(Tag.ClassBodyDeclarations, new Tag[]{Tag.ClassBodyDeclaration}, false);

//        ClassBodyDeclaration -> ClassMemberDeclaration
//                | FuncDeclaration
        generateProduction(Tag.ClassBodyDeclaration, new Tag[]{Tag.ClassMemberDeclaration}, false);
        generateProduction(Tag.ClassBodyDeclaration, new Tag[]{Tag.FuncDeclaration}, false);

//        ClassMemberDeclaration -> Assignment
        generateProduction(Tag.ClassMemberDeclaration, new Tag[]{Tag.Assignment}, false);

        //FuncDeclaration -> FUNC IDENTIFIER ( ParamList ) CompSt
        //FuncDeclaration -> FUNC IDENTIFIER ( ) CompSt
        generateProduction(Tag.FuncDeclaration, new Tag[]{Tag.Func, Tag.Identifier, Tag.LBracket, Tag.ParamList, Tag.RBracket, Tag.CompSt}, false);
        generateProduction(Tag.FuncDeclaration, new Tag[]{Tag.Func, Tag.Identifier, Tag.LBracket, Tag.RBracket, Tag.CompSt}, false);

//        ParamList -> IDENTIFIER DefaultValue | ParamList , IDENTIFIER DefaultValue
        generateProduction(Tag.ParamList, new Tag[]{Tag.Identifier, Tag.DefaultValue}, false);
        generateProduction(Tag.ParamList, new Tag[]{Tag.ParamList, Tag.Comma, Tag.Identifier, Tag.DefaultValue}, false);
        //ParamList -> IDENTIFIER | ParamList , IDENTIFIER
        generateProduction(Tag.ParamList, new Tag[]{Tag.Identifier}, false);
        generateProduction(Tag.ParamList, new Tag[]{Tag.ParamList, Tag.Comma, Tag.Identifier}, false);


//        DefaultValue -> = Primary
        generateProduction(Tag.DefaultValue, new Tag[]{Tag.Assign, Tag.Primary}, false);


        // Data Structure
        // Dictionary -> { MapList }
        // Dictionary -> { }
        generateProduction(Tag.Dictionary, new Tag[]{Tag.LBrace, Tag.MapList, Tag.RBrace}, false);
        generateProduction(Tag.Dictionary, new Tag[]{Tag.LBrace, Tag.RBrace}, false);
//        MapList -> MapList , Map | Map
        generateProduction(Tag.MapList, new Tag[]{Tag.MapList, Tag.Comma, Tag.Map}, false);
        generateProduction(Tag.MapList, new Tag[]{Tag.Map}, false);
//        Map -> Primary : Item
        generateProduction(Tag.Map, new Tag[]{Tag.Primary, Tag.Colon, Tag.Item}, false);
        //List -> [ ItemList ]
        //List -> [  ]
        generateProduction(Tag.List, new Tag[]{Tag.LSquare, Tag.ItemList, Tag.RSquare}, false);
        generateProduction(Tag.List, new Tag[]{Tag.LSquare, Tag.RSquare}, false);
//        ItemList -> ItemList , Item | Item
        generateProduction(Tag.ItemList, new Tag[]{Tag.ItemList, Tag.Comma, Tag.Item}, false);
        generateProduction(Tag.ItemList, new Tag[]{Tag.Item}, false);

        //TupleStart ->  Item , Item  TupleFollow
        //TupleStart ->  Item , Item
        generateProduction(Tag.TupleStart, new Tag[]{Tag.Item, Tag.Comma, Tag.Item, Tag.TupleFollow}, false);
        generateProduction(Tag.TupleStart, new Tag[]{Tag.Item, Tag.Comma, Tag.Item}, false);
//        TupleFollow -> TupleFollow , Item | Item
        generateProduction(Tag.TupleFollow, new Tag[]{Tag.TupleFollow, Tag.Comma, Tag.Item}, false);
        generateProduction(Tag.TupleFollow, new Tag[]{Tag.Item}, false);
//        Item -> Primary | Dictionary | List | Tuple
        generateProduction(Tag.Item, new Tag[]{Tag.Primary}, false);
        generateProduction(Tag.Item, new Tag[]{Tag.Dictionary}, false);
        generateProduction(Tag.Item, new Tag[]{Tag.List}, false);
        generateProduction(Tag.Item, new Tag[]{Tag.Tuple}, false);
        // Tuple -> ( TupleStart )
        // TupleNoBracket -> TupleStart
        generateProduction(Tag.Tuple, new Tag[]{Tag.LBracket, Tag.TupleStart, Tag.RBracket}, false);
        generateProduction(Tag.TupleNoBracket, new Tag[]{Tag.TupleStart}, false);
//        ListAndTuple -> List | Tuple | TupleNoBracket
        generateProduction(Tag.ListAndTuple, new Tag[]{Tag.List}, false);
        generateProduction(Tag.ListAndTuple, new Tag[]{Tag.Tuple}, false);
        generateProduction(Tag.ListAndTuple, new Tag[]{Tag.TupleNoBracket}, false);

        // Statement
        ///CompSt -> { StmtList }
        //CompSt -> {  }
        generateProduction(Tag.CompSt, new Tag[]{Tag.LBrace, Tag.StmtList, Tag.RBrace}, false);
        generateProduction(Tag.CompSt, new Tag[]{Tag.LBrace, Tag.RBrace}, false);
//        StmtList -> StmtList Stmt | Stmt
        generateProduction(Tag.StmtList, new Tag[]{Tag.StmtList, Tag.Stmt}, false);
        generateProduction(Tag.StmtList, new Tag[]{Tag.Stmt}, false);
//        Stmt -> Exp
//                | MultiAssignment
//                | CompSt
//                | ReturnStmt
//                | IfElseStmt
//                | RepeatStmt
        generateProduction(Tag.Stmt, new Tag[]{Tag.Exp, Tag.LF}, false);
        generateProduction(Tag.Stmt, new Tag[]{Tag.MultiAssignment, Tag.LF}, false);
        generateProduction(Tag.Stmt, new Tag[]{Tag.CompSt, Tag.LF}, false);
        generateProduction(Tag.Stmt, new Tag[]{Tag.ReturnStmt, Tag.LF}, false);
        generateProduction(Tag.Stmt, new Tag[]{Tag.IfElseStmt, Tag.LF}, false);
        generateProduction(Tag.Stmt, new Tag[]{Tag.RepeatStmt, Tag.LF}, false);

//        ReturnStmt -> RETURN ReturnParam | RETURN
        generateProduction(Tag.ReturnStmt, new Tag[]{Tag.Return, Tag.RepeatParam}, false);
        generateProduction(Tag.ReturnStmt, new Tag[]{Tag.Return}, false);
//        ReturnParam -> NoAssignExp | ListAndTuple | Dictionary
        generateProduction(Tag.ReturnParam, new Tag[]{Tag.NoAssignExp}, false);
        generateProduction(Tag.ReturnParam, new Tag[]{Tag.ListAndTuple}, false);
        generateProduction(Tag.ReturnParam, new Tag[]{Tag.Dictionary}, false);

//
//        IfStmt -> IF NoAssignExp CompSt
        generateProduction(Tag.IfStmt, new Tag[]{Tag.If, Tag.NoAssignExp, Tag.CompSt}, false);
//        ElifStmt -> IfStmt |
//                ElifStmt elif NoAssignExp CompSt
        generateProduction(Tag.ElifStmt, new Tag[]{Tag.IfStmt}, false);
        generateProduction(Tag.ElifStmt, new Tag[]{Tag.ElifStmt, Tag.Elif, Tag.NoAssignExp, Tag.CompSt}, false);
//        IfElseStmt -> ElifStmt
//                | IfElseStmt ELSE CompSt
        generateProduction(Tag.IfElseStmt, new Tag[]{Tag.ElifStmt}, false);
        generateProduction(Tag.IfElseStmt, new Tag[]{Tag.IfElseStmt, Tag.Else, Tag.CompSt}, false);
//
//        RepeatStmt -> REPEAT RepeatCond CompSt
        generateProduction(Tag.RepeatStmt, new Tag[]{Tag.Repeat, Tag.RepeatCond, Tag.CompSt}, false);
//        RepeatParam -> IDENTIFIER | RepeatParam , IDENTIFIER
        generateProduction(Tag.RepeatParam, new Tag[]{Tag.RepeatParam, Tag.Comma, Tag.Identifier}, false);
        generateProduction(Tag.RepeatParam, new Tag[]{Tag.Identifier}, false);
//        IterateValue -> Variable
//                | Tuple
//                | List
        generateProduction(Tag.IterateValue, new Tag[]{Tag.Variable}, false);
        generateProduction(Tag.IterateValue, new Tag[]{Tag.Tuple}, false);
        generateProduction(Tag.IterateValue, new Tag[]{Tag.List}, false);
//        RepeatCond -> NoAssignExp
//                | RepeatParam IN IterateValue
        generateProduction(Tag.RepeatCond, new Tag[]{Tag.NoAssignExp}, false);
        generateProduction(Tag.RepeatCond, new Tag[]{Tag.RepeatParam, Tag.In, Tag.IterateValue}, false);


//        FuncInvocation -> IDENTIFIER ( ) | IDENTIFIER ( Args )
        generateProduction(Tag.FuncInvocation, new Tag[]{Tag.Identifier, Tag.LBracket, Tag.RBracket}, false);
        generateProduction(Tag.FuncInvocation, new Tag[]{Tag.Identifier, Tag.LBracket, Tag.Args, Tag.RBracket}, false);


//        AssignableValue -> Variable . AssignableValue
//                | Variable [ Variable ]
//                | Variable [ STRING ]
//                | Variable [ NUM ]
//                | Variable [ NoAssignExp ]
//                | IDENTIFIER
        generateProduction(Tag.AssignableValue, new Tag[]{Tag.Variable, Tag.Dot, Tag.AssignableValue}, false);
        generateProduction(Tag.AssignableValue, new Tag[]{Tag.Variable, Tag.LSquare, Tag.Variable, Tag.RSquare}, false);
        generateProduction(Tag.AssignableValue, new Tag[]{Tag.Variable, Tag.LSquare, Tag.String, Tag.RSquare}, false);
        generateProduction(Tag.AssignableValue, new Tag[]{Tag.Variable, Tag.LSquare, Tag.Num, Tag.RSquare}, false);
        generateProduction(Tag.AssignableValue, new Tag[]{Tag.Variable, Tag.LSquare, Tag.NoAssignExp, Tag.RSquare}, false);
        generateProduction(Tag.AssignableValue, new Tag[]{Tag.Identifier}, false);

//        Variable -> AssignableValue
//                | Variable . FuncInvocation
//                | FuncInvocation
//                | SELF
        generateProduction(Tag.Variable, new Tag[]{Tag.AssignableValue}, false);
        generateProduction(Tag.Variable, new Tag[]{Tag.FuncInvocation}, false);
        generateProduction(Tag.Variable, new Tag[]{Tag.Variable, Tag.Dot, Tag.FuncInvocation}, false);
        generateProduction(Tag.Variable, new Tag[]{Tag.Self}, false);

//        Primary -> ( NoAssignExp )
//                | Variable
//                | NUM
//                | REAL
//                | TRUE
//                | FALSE
//                | STRING
        generateProduction(Tag.Primary, new Tag[]{Tag.LBracket, Tag.NoAssignExp, Tag.RBracket}, false);
        generateProduction(Tag.Primary, new Tag[]{Tag.Variable}, false);
        generateProduction(Tag.Primary, new Tag[]{Tag.Num}, false);
        generateProduction(Tag.Primary, new Tag[]{Tag.Real}, false);
        generateProduction(Tag.Primary, new Tag[]{Tag.True}, false);
        generateProduction(Tag.Primary, new Tag[]{Tag.False}, false);
        generateProduction(Tag.Primary, new Tag[]{Tag.String}, false);
//
//        UnaryExp -> Primary
//                | - Primary
//                | NOT Primary
        generateProduction(Tag.UnaryExp, new Tag[]{Tag.Primary}, false);
        generateProduction(Tag.UnaryExp, new Tag[]{Tag.Sub, Tag.Primary}, false);
        generateProduction(Tag.UnaryExp, new Tag[]{Tag.Not, Tag.Primary}, false);

//        MultiplicativeExp -> UnaryExp
//                | MultiplicativeExp * UnaryExp
//                | MultiplicativeExp / UnaryExp
//                | MultiplicativeExp % UnaryExp
        generateProduction(Tag.MultiplicativeExp, new Tag[]{Tag.UnaryExp}, false);
        generateProduction(Tag.MultiplicativeExp, new Tag[]{Tag.MultiplicativeExp, Tag.Mul, Tag.UnaryExp}, false);
        generateProduction(Tag.MultiplicativeExp, new Tag[]{Tag.MultiplicativeExp, Tag.Div, Tag.UnaryExp}, false);
        generateProduction(Tag.MultiplicativeExp, new Tag[]{Tag.MultiplicativeExp, Tag.Mod, Tag.UnaryExp}, false);

//        AdditiveExp -> MultiplicativeExp
//                | AdditiveExp + MultiplicativeExp
//                | AdditiveExp - MultiplicativeExp
        generateProduction(Tag.AdditiveExp, new Tag[]{Tag.MultiplicativeExp}, false);
        generateProduction(Tag.AdditiveExp, new Tag[]{Tag.AdditiveExp, Tag.Add, Tag.MultiplicativeExp}, false);
        generateProduction(Tag.AdditiveExp, new Tag[]{Tag.AdditiveExp, Tag.Sub, Tag.MultiplicativeExp}, false);

//        RelationExp -> AdditiveExp
//                | RelationExp < AdditiveExp
//                | RelationExp > AdditiveExp
//                | RelationExp <= AdditiveExp
//                | RelationExp >= AdditiveExp
        generateProduction(Tag.RelationExp, new Tag[]{Tag.AdditiveExp}, false);
        generateProduction(Tag.RelationExp, new Tag[]{Tag.RelationExp, Tag.LT, Tag.AdditiveExp}, false);
        generateProduction(Tag.RelationExp, new Tag[]{Tag.RelationExp, Tag.GT, Tag.AdditiveExp}, false);
        generateProduction(Tag.RelationExp, new Tag[]{Tag.RelationExp, Tag.LE, Tag.AdditiveExp}, false);
        generateProduction(Tag.RelationExp, new Tag[]{Tag.RelationExp, Tag.GE, Tag.AdditiveExp}, false);

//        EqualityExp -> RelationExp
//                | EqualityExp == RelationExp
//                | EqualityExp != RelationExp
        generateProduction(Tag.EqualityExp, new Tag[]{Tag.RelationExp}, false);
        generateProduction(Tag.EqualityExp, new Tag[]{Tag.EqualityExp, Tag.EQ, Tag.RelationExp}, false);
        generateProduction(Tag.EqualityExp, new Tag[]{Tag.EqualityExp, Tag.NE, Tag.RelationExp}, false);

//        ConditionAndExp -> EqualityExp
//                | ConditionAndExp AND EqualityExp
        generateProduction(Tag.ConditionAndExp, new Tag[]{Tag.EqualityExp}, false);
        generateProduction(Tag.ConditionAndExp, new Tag[]{Tag.ConditionAndExp, Tag.And, Tag.EqualityExp}, false);

//        ConditionOrExp -> ConditionAndExp
//                | ConditionOrExp OR ConditionAndExp
        generateProduction(Tag.ConditionOrExp, new Tag[]{Tag.ConditionAndExp}, false);
        generateProduction(Tag.ConditionOrExp, new Tag[]{Tag.ConditionOrExp, Tag.Or, Tag.ConditionAndExp}, false);

//        NoAssignExp -> ConditionOrExp
        generateProduction(Tag.NoAssignExp, new Tag[]{Tag.ConditionOrExp}, false);

//        AssignmentExp -> NoAssignExp
//                | Assignment
        generateProduction(Tag.AssignmentExp, new Tag[]{Tag.NoAssignExp}, false);
        generateProduction(Tag.AssignmentExp, new Tag[]{Tag.Assignment}, false);

//        Assignment -> LeftSide = AssignmentExp
//                | MultiAssignment
        generateProduction(Tag.Assignment, new Tag[]{Tag.LeftSide, Tag.Assign, Tag.AssignmentExp}, false);
        generateProduction(Tag.Assignment, new Tag[]{Tag.MultiAssignment}, false);

//        LeftSide -> AssignableValue | ListAndTuple
        generateProduction(Tag.LeftSide, new Tag[]{Tag.AssignableValue}, false);
        generateProduction(Tag.LeftSide, new Tag[]{Tag.ListAndTuple}, false);

//        MultiAssignment -> ListAndTuple = ListAndTuple
//                | AssignableValue = Dictionary
//                | AssignableValue = ListAndTuple
        generateProduction(Tag.MultiAssignment, new Tag[]{Tag.ListAndTuple, Tag.Assign, Tag.ListAndTuple}, false);
        generateProduction(Tag.MultiAssignment, new Tag[]{Tag.AssignableValue, Tag.Assign, Tag.Dictionary}, false);
        generateProduction(Tag.MultiAssignment, new Tag[]{Tag.AssignableValue, Tag.Assign, Tag.ListAndTuple}, false);
//
//        Exp -> AssignmentExp
        generateProduction(Tag.Exp, new Tag[]{Tag.AssignmentExp}, false);
//        Args -> Args , Exp | Exp
        generateProduction(Tag.Args, new Tag[]{Tag.Args, Tag.Comma, Tag.Exp}, false);
        generateProduction(Tag.Args, new Tag[]{Tag.Exp}, false);

        // Stmt -> BREAK
        //      | CONTINUE
        generateProduction(Tag.Stmt, new Tag[]{Tag.Break, Tag.LF}, false);
        generateProduction(Tag.Stmt, new Tag[]{Tag.Continue, Tag.LF}, false);
    }

    private void generateProduction(Tag left, Tag [] arr, boolean nullable) {
        ArrayList<Tag> right = new ArrayList<Tag>();
        for (int i = 0; i < arr.length; i++) {
            right.add(arr[i]);
        }
        addProduction(new Production(productionNum++, left, 0, right), nullable);
    }

    private void addProduction(Production production, boolean nullable) {
        ArrayList<Production> productions = productionMap.get(production.left);

        if (productions == null) {
            productions = new ArrayList<Production>();
            productionMap.put(production.left, productions);
        }

        if (!productions.contains(production)) {
            productions.add(production);
        }
        addSymbolMapAndArray(production, nullable);
    }

    private void addSymbolMapAndArray(Production production, boolean nullable) {
        if (symbolMap.containsKey(production.left)) {
            symbolMap.get(production.left).addProduction(production.right);
        } else {
            ArrayList<ArrayList<Tag>> productions = new ArrayList<>();
            productions.add(production.right);
            Symbol symbol = new Symbol(nullable, production.left, productions);
            symbolMap.put(production.left, symbol);
            symbols.add(symbol);
        }
    }

    private void addTerminalToSymbolMapAndArray() {
        for (Tag tag : Tag.values()) {
            if (Tag.isTerminal(tag)) {
                Symbol symbol = new Symbol(false, tag, null);
                symbolMap.put(tag, symbol);
                symbols.add(symbol);
            } else return;
        }
    }
}

