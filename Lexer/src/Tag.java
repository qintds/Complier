public enum Tag {

        //Terminal
        // for identifying the num
        Num,
        Real,
        String,
        Identifier,

        // in lower case
        Import,
        Class,
        If,
        Else,
        Elif,
        Break,
        Continue,
        Return,
        Repeat,
        Func,
        None, // none
        Null, // null
        In, // in
        Main, // main
        Self, // self

        And, // and
        Or, // or
        Not, // not
        True, // true
        False, // false
        LT, // <
        GT, // >
        LE, // <=
        GE, // >=
        EQ, // ==
        NE, // !=

        LBracket, // (
        RBracket, // )
        LBrace, // {
        RBrace, // }
        LSquare, // [
        RSquare, // ]
        Comma, // ,
        Dot, //.
        Colon, // :
        //Concat, // &
        Add, // +
        Sub, // -
        Mul, // *
        Div, // /
        //Backslash, // \
        Mod, // %
        Assign, // =
        AddEQ, // +=
        SubEQ, // -=
        MulEQ, // *=
        DivEQ, // /=
        ModEQ, // %=
        LF, // line wrap

        Comment, // //

        STS, // single to single unified tag

        Unknown,
        End,
        NonTerminal, // Start of nonTerminal which separate terminal and NonTerminal
        Program,
        ExtDefList,
        ExtDef,
        ImportDeclaration,
        PackageChain,
        ClassDeclaration,
        SuperOpt,
        Super,
        ClassBody,
        ClassBodyDeclarations,
        ClassBodyDeclaration,
        ClassMemberDeclaration,
        ClassFuncDeclaration,
        FuncDeclaration,
        ParamListOpt,
        ParamList,
        DefaultValue,
        Dictionary,
        MapListOpt,
        MapList,
        Map,
        List,
        ItemListOpt,
        ItemList,
        TupleStart,
        TupleFollowOpt,
        TupleFollow,
        Item,
        Tuple,
        TupleNoBracket,
        ListAndTuple,
        CompSt,
        StmtListOpt,
        StmtList,
        Stmt,
        ReturnStmt,
        ReturnParam,
        IfStmt,
        ElifStmt,
        IfElseStmt,
        RepeatStmt,
        RepeatParam,
        IterateValue,
        RepeatCond,
        FuncInvocation,
        AssignableValue,
        Variable,
        Primary,
        UnaryExp,
        MultiplicativeExp,
        AdditiveExp,
        RelationExp,
        EqualityExp,
        ConditionAndExp,
        ConditionOrExp,
        NoAssignExp,
        AssignmentExp,
        Assignment,
        LeftSide,
        MultiAssignment,
        Exp,
        ArgsName,
        Args;

public static boolean isTerminal(Tag tag) {
        return Tag.NonTerminal.compareTo(tag) > 0;
        }

        }
