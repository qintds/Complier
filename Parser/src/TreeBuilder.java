import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class TreeBuilder{

    private Stack<Object> valueStack;
    private XEnv environment;
    private String address = "";
    private Stack<XFuncObject> funcStack = new Stack<>();
    private Stack<XFuncObject> classFuncStack = new Stack<>();
    private ArrayList<String> funcAndClassName = new ArrayList<>();
    public HashMap<String, XFuncObject> functionMap = new HashMap<>();
    public HashMap<String, XClassObject> classMap = new HashMap<>();
    public CNode program;


    public TreeBuilder(Stack<Object> valueStack){
        this.valueStack = valueStack;
        environment = new XEnv(null);
    }

    public CNode reduceAction(int productionNum) {
        CNode node = null;
        CNode temp = null;
        // when collapsing single to single reduction, don't refresh node's production num
        // DON'T SUGGEST TO COLLAPSE THE NODE
        // NOT GOOD FOR EXECUTING
//        boolean collapse = false;
        GrammarEnum n = GrammarEnum.values()[productionNum];
        switch (n) {
            case Primary_To_Num:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setXObject(new XNumObject((Num)valueStack.peek()));
                break;
            case Primary_To_Real:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setXObject(new XRealObject((Real)valueStack.peek()));
                break;
            case Primary_To_True:
            case Primary_To_False:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setXObject(new XBoolObject((Word)valueStack.peek()));
                break;
            case Primary_To_String:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setXObject(new XStringObject((Word)valueStack.peek()));
                break;
            case Primary_To_None:
                node = CNodeFactroy.createCNode(Tag.None);
                node.setXObject(new XNoneObject());
            case Primary_To_LBracket_NoAssignExp_RBracket:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
                // directly single to single
            case Primary_To_Variable:
            case UnaryExp_To_Primary:
            case MultiplicativeExp_To_UnaryExp:
            case AdditiveExp_To_MultiplicativeExp:
            case RelationExp_To_AdditiveExp:
            case EqualityExp_To_RelationExp:
            case ConditionAndExp_To_EqualityExp:
            case ConditionOrExp_To_ConditionAndExp:
            case NoAssignExp_To_ConditionOrExp:
            case AssignmentExp_To_NoAssignExp:
            case AssignmentExp_To_Assignment:
                node = singleToSingle();
                //node = CNodeFactroy.createCNode(Tag.Primary);
                //node.addChild();
                break;
            case UnaryExp_To_Not_Primary:
            case UnaryExp_To_Sub_Primary:
                node = CNodeFactroy.createCNode(Tag.UnaryExp);
                node.addChild((CNode)valueStack.peek());
                break;
            case MultiplicativeExp_To_MultiplicativeExp_Mul_UnaryExp:
            case MultiplicativeExp_To_MultiplicativeExp_Div_UnaryExp:
            case MultiplicativeExp_To_MultiplicativeExp_Mod_UnaryExp:
            case AdditiveExp_To_AdditiveExp_Add_MultiplicativeExp:
            case AdditiveExp_To_AdditiveExp_Sub_MultiplicativeExp:
            case RelationExp_To_RelationExp_GE_AdditiveExp:
            case RelationExp_To_RelationExp_GT_AdditiveExp:
            case RelationExp_To_RelationExp_LE_AdditiveExp:
            case RelationExp_To_RelationExp_LT_AdditiveExp:
            case EqualityExp_To_EqualityExp_EQ_RelationExp:
            case EqualityExp_To_EqualityExp_NE_RelationExp:
            case ConditionAndExp_To_ConditionAndExp_And_EqualityExp:
            case ConditionOrExp_To_ConditionOrExp_Or_ConditionAndExp:
                node = CNodeFactroy.createCNode(Tag.NoAssignExp);
                node.addChild((CNode)valueStack.get(valueStack.size()-3));
                node.addChild((CNode)valueStack.get(valueStack.size()-1));
                break;
            case AssignableValue_To_Identifier:
                node = CNodeFactroy.createCNode(Tag.Identifier);
                node.setIdentifier((Word)valueStack.peek());
                break;
            case AssignableValue_To_Variable_Dot_AssignableValue:
                node = CNodeFactroy.createCNode(Tag.AssignableValue);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case AssignableValue_To_Variable_LSquare_Num_RSquare:
                // when processing an assignment, will start from right CNode
                node = CNodeFactroy.createCNode(Tag.AssignableValue);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                temp = CNodeFactroy.createCNode(Tag.Num);
                temp.setXObject(new XNumObject((Num)valueStack.get(valueStack.size() - 2)));
                node.addChild(temp);
                break;
            case AssignableValue_To_Variable_LSquare_String_RSquare:
                node = CNodeFactroy.createCNode(Tag.AssignableValue);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                temp = CNodeFactroy.createCNode(Tag.String);
                temp.setXObject(new XStringObject((String) valueStack.get(valueStack.size() - 2)));
                node.addChild(temp);
                break;
            case AssignableValue_To_Variable_LSquare_Variable_RSquare:
            case AssignableValue_To_Variable_LSquare_NoAssignExp_RSquare:
                node = CNodeFactroy.createCNode(Tag.AssignableValue);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
            case Variable_To_AssignableValue:
            case Variable_To_FuncInvocation:
                node = singleToSingle();
                break;
            case Variable_To_Variable_Dot_FuncInvocation:
                node = CNodeFactroy.createCNode(Tag.Variable);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case Variable_To_Self:
//                node = CNodeFactroy.createCNode(Tag.Variable);
                node = CNodeFactroy.createCNode(Tag.Self);
                break;
            case FuncInvocation_To_Identifier_LBracket_Args_RBracket:
                node = CNodeFactroy.createCNode(Tag.FuncInvocation);
                node.setIdentifier((Word)valueStack.get(valueStack.size() - 4));
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
            case FuncInvocation_To_Identifier_LBracket_RBracket:
                node = CNodeFactroy.createCNode(Tag.FuncInvocation);
                node.setIdentifier((Word)valueStack.get(valueStack.size() - 3));
                break;
            case LeftSide_To_AssignableValue:
            case LeftSide_To_ListAndTuple:
                node = singleToSingle();
                break;
            case Assignment_To_LeftSide_Assign_AssignmentExp:
            case Assignment_To_LeftSide_Assign_Dictionary:
            case Assignment_To_LeftSide_Assign_ListAndTuple:
                node = CNodeFactroy.createCNode(Tag.Assignment);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case Exp_To_AssignmentExp:
            case Args_To_Exp:
                node = singleToSingle();
                break;
            case Args_To_Args_Comma_Exp:
                node = CNodeFactroy.createCNode(Tag.Args);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case Item_To_Dictionary:
            case Item_To_List:
            case Item_To_Tuple:
            case Item_To_Primary:
            case ItemList_To_Item:
                node = singleToSingle();
                break;
            case ItemList_To_ItemList_Comma_Item:
                node = CNodeFactroy.createCNode(Tag.ItemList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case List_To_LSquare_ItemList_RSquare:
                node = CNodeFactroy.createCNode(Tag.List);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
            case List_To_LSquare_RSquare:
                node = CNodeFactroy.createCNode(Tag.List);
                break;
            case Map_To_Primary_Colon_Item:
                node = CNodeFactroy.createCNode(Tag.Map);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case MapList_To_MapList_Comma_Map:
                node = CNodeFactroy.createCNode(Tag.MapList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case MapList_To_Map:
                node = singleToSingle();
                break;
            case Dictionary_To_LBrace_MapList_RBrace:
                node = CNodeFactroy.createCNode(Tag.Dictionary);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
            case Dictionary_To_LBrace_RBrace:
                node = CNodeFactroy.createCNode(Tag.Dictionary);
                break;
            case TupleStart_To_Item_Comma_Item:
                node = CNodeFactroy.createCNode(Tag.TupleStart);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case TupleStart_To_Item_Comma_Item_TupleFollow:
                node = CNodeFactroy.createCNode(Tag.TupleStart);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case TupleFollow_To_TupleFollow_Comma_Item:
                node = CNodeFactroy.createCNode(Tag.TupleFollow);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case TupleFollow_To_Item:
                node = singleToSingle();
                break;
            case Tuple_To_LBracket_TupleStart_RBracket:
                node = CNodeFactroy.createCNode(Tag.Tuple);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
            case TupleNoBracket_To_TupleStart:
                node = CNodeFactroy.createCNode(Tag.Tuple);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ListAndTuple_To_List:
            case ListAndTuple_To_Tuple:
            case ListAndTuple_To_TupleNoBracket:
                node = singleToSingle();
                break;
            case Stmt_To_Break:
                node = CNodeFactroy.createCNode(Tag.Break);
                break;
            case Stmt_To_Continue:
                node = CNodeFactroy.createCNode(Tag.Continue);
                break;
            case RepeatParam_To_Identifier:
                node = CNodeFactroy.createCNode(Tag.Identifier);
                node.setIdentifier((Word)valueStack.get(valueStack.size() - 1));
                break;
            case IterateValue_To_List:
            case IterateValue_To_Tuple:
            case IterateValue_To_Variable:
                node = singleToSingle();
                break;
            case RepeatParam_To_RepeatParam_Comma_Identifier:
                node = CNodeFactroy.createCNode(Tag.RepeatParam);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 1));
                node.addChild(temp);
                break;
            case RepeatCond_To_NoAssignExp:
                node = CNodeFactroy.createCNode(Tag.RepeatCond);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case RepeatCond_To_RepeatParam_In_IterateValue:
                node = CNodeFactroy.createCNode(Tag.RepeatCond);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case RepeatStmt_To_Repeat_RepeatCond_CompSt:
                node = CNodeFactroy.createCNode(Tag.RepeatStmt);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case IfStmt_To_If_NoAssignExp_CompSt:
                node = CNodeFactroy.createCNode(Tag.IfStmt);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ElifStmt_To_ElifStmt_Elif_NoAssignExp_CompSt:
                node = CNodeFactroy.createCNode(Tag.ElifStmt);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ElifStmt_To_IfStmt:
            case IfElseStmt_To_ElifStmt:
                node = singleToSingle();
                break;
            case IfElseStmt_To_ElifStmt_Else_CompSt:
                node = CNodeFactroy.createCNode(Tag.IfElseStmt);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ReturnParam_To_Dictionary:
            case ReturnParam_To_ListAndTuple:
            case ReturnParam_To_NoAssignExp:
                node = singleToSingle();
                break;
            case ReturnStmt_To_Return_ReturnParam:
                node = CNodeFactroy.createCNode(Tag.ReturnStmt);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ReturnStmt_To_Return:
                node = CNodeFactroy.createCNode(Tag.ReturnStmt);
                break;
            case Stmt_To_CompSt_LF:
            case Stmt_To_Exp_LF:
            case Stmt_To_IfElseStmt_LF:
            case Stmt_To_RepeatStmt_LF:
            case Stmt_To_ReturnStmt_LF:
                node = CNodeFactroy.createCNode(Tag.Stmt);
                node.addChild((CNode) valueStack.get(valueStack.size() - 2));
                break;
            case StmtList_To_Stmt:
                node = singleToSingle();
                break;
            case StmtList_To_StmtList_Stmt:
                node = CNodeFactroy.createCNode(Tag.StmtList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                 break;
            case CompSt_To_LBrace_RBrace:
                node = CNodeFactroy.createCNode(Tag.CompSt);
                break;
            case CompSt_To_LBrace_StmtList_RBrace:
                node = CNodeFactroy.createCNode(Tag.CompSt);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
                //function part, need record function
            case DefaultValue_To_Assign_Primary:
                node = CNodeFactroy.createCNode(Tag.DefaultValue);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ParamList_To_Identifier:
                node = CNodeFactroy.createCNode(Tag.Identifier);
                node.setIdentifier((Word)valueStack.get(valueStack.size() - 1));
                break;
            case ParamList_To_Identifier_DefaultValue:
                node = CNodeFactroy.createCNode(Tag.ParamList);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 2));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ParamList_To_ParamList_Comma_Identifier:
                node = CNodeFactroy.createCNode(Tag.ParamList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 1));
                node.addChild(temp);
                break;
            case ParamList_To_ParamList_Comma_Identifier_DefaultValue:
                node = CNodeFactroy.createCNode(Tag.ParamList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 2));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case FuncDeclaration_To_Func_Identifier_LBracket_RBracket_CompSt:
                node = CNodeFactroy.createCNode(Tag.FuncDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 4));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                //record the entrance of the function
                generateFunctionEntrance((Word)valueStack.get(valueStack.size() - 4), node, false);
                break;
            case FuncDeclaration_To_Func_Identifier_LBracket_ParamList_RBracket_CompSt:
                node = CNodeFactroy.createCNode(Tag.FuncDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 5));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                //record the entrance of the function
                generateFunctionEntrance((Word)valueStack.get(valueStack.size() - 5), node, true);
                break;
            case ClassMemberDeclaration_To_Identifier_Assign_NoAssignExp:
                node = CNodeFactroy.createCNode(Tag.ClassMemberDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 3));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ClassFuncDeclaration_To_Func_Identifier_LBracket_Self_RBracket_CompSt:
                node = CNodeFactroy.createCNode(Tag.ClassFuncDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 5));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                //record the entrance of the function
                generateClassFunctionEntrance((Word)valueStack.get(valueStack.size() - 5), node, false);
                break;
            case ClassFuncDeclaration_To_Func_Identifier_LBracket_Self_Comma_ParamList_RBracket_CompSt:
                node = CNodeFactroy.createCNode(Tag.ClassFuncDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 7));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                //record the entrance of the function
                generateClassFunctionEntrance((Word)valueStack.get(valueStack.size() - 7), node, true);
                break;
            case ClassBodyDeclaration_To_ClassMemberDeclaration:
            case ClassBodyDeclaration_To_FuncDeclaration:
            case ClassBodyDeclaration_To_ClassFuncDeclaration:
            case ClassBodyDeclarations_To_ClassBodyDeclaration:
            case Super_To_PackageChain:
                node = singleToSingle();
                break;
            case ClassBodyDeclarations_To_ClassBodyDeclarations_ClassBodyDeclaration:
                node = CNodeFactroy.createCNode(Tag.ClassBodyDeclarations);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ClassBody_To_LBrace_ClassBodyDeclarations_RBrace:
                node = CNodeFactroy.createCNode(Tag.ClassBody);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                break;
            case ClassBody_To_LBrace_RBrace:
                node = CNodeFactroy.createCNode(Tag.ClassBody);
                break;
            case Super_To_Super_Comma_PackageChain:
                node = CNodeFactroy.createCNode(Tag.Super);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case PackageChain_To_Identifier:
                node = CNodeFactroy.createCNode(Tag.Identifier);
                node.setIdentifier((Word)valueStack.get(valueStack.size() - 1));
                break;
            case PackageChain_To_PackageChain_Dot_Identifier:
                node = CNodeFactroy.createCNode(Tag.PackageChain);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 1));
                node.addChild(temp);
                break;
            case ClassDeclaration_To_Class_Identifier_ClassBody:
                node = CNodeFactroy.createCNode(Tag.ClassBodyDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 2));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                generateClassEntrance((Word)valueStack.get(valueStack.size() - 2), node, false);
                break;
            case ClassDeclaration_To_Class_Identifier_Colon_Super_ClassBody:
                node = CNodeFactroy.createCNode(Tag.ClassBodyDeclaration);
                temp = CNodeFactroy.createCNode(Tag.Identifier);
                temp.setIdentifier((Word)valueStack.get(valueStack.size() - 4));
                node.addChild(temp);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                generateClassEntrance((Word)valueStack.get(valueStack.size() - 4), node, true);
                break;
            case ImportDeclaration_To_Import_PackageChain:
                node = CNodeFactroy.createCNode(Tag.ImportDeclaration);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case ExtDef_To_ClassDeclaration:
            case ExtDef_To_ImportDeclaration:
                node = CNodeFactroy.createCNode(Tag.ExtDef);
                node.addChild((CNode) valueStack.get(valueStack.size() - 2));
                break;
            case ExtDef_To_StmtList:
                node = singleToSingle();
                break;
            case ExtDef_To_FuncDeclaration:
                node = CNodeFactroy.createCNode(Tag.ExtDef);
                node.addChild((CNode) valueStack.get(valueStack.size() - 2));
                recordNormalFunction();
                break;
            case ExtDefList_To_ExtDef:
                node = CNodeFactroy.createCNode(Tag.ExtDefList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
            case ExtDefList_To_ExtDefList_ExtDef:
                node = CNodeFactroy.createCNode(Tag.ExtDefList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 2));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case Program_To_ExtDefList:
                node = CNodeFactroy.createCNode(Tag.Program);
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                program = node;
                System.out.println("built tree");
                break;
        }

        if (node != null) {
            node.production = n;
        }
        return node;
    }

    public CNode singleToSingle() {
        CNode node = CNodeFactroy.createCNode(Tag.STS);
        node.addChild((CNode)valueStack.get(valueStack.size() - 1));
        return node;
    }

    private void generateFunctionEntrance(Word word, CNode funcBody, boolean hasParams) {
        XFuncObject newFunc = new XFuncObject(word, funcBody);
        newFunc.setContainParams(hasParams);
        funcStack.push(newFunc);
        // add it into the env
        // put to the stack
        // function in extDef and Class will be put in respective env
    }

    private void generateClassFunctionEntrance(Word word, CNode funcBody, boolean hasParams) {
        XFuncObject newFunc = new XFuncObject(word, funcBody);
        newFunc.setContainParams(hasParams);
        classFuncStack.push(newFunc);
    }

    private void recordNormalFunction() {
        if (funcStack.size() > 0) {
            XFuncObject temp = funcStack.pop();
            if (!funcAndClassName.contains(temp.getFuncName())) {
                functionMap.put(temp.getFuncName(), temp);
            } else {
                // repeat name
            }

        } else {
            //error
        }
    }

    private void generateClassEntrance(Word word, CNode funcBody, boolean isInherit) {
        XClassObject newClass = new XClassObject(word, funcBody);
        if (!funcAndClassName.contains(newClass.getClassName())) {
            newClass.setInherit(isInherit);
            if (funcStack.size()>0) {
                for (int i = funcStack.size(); i > 0; i--) {
                    XFuncObject temp = funcStack.get(funcStack.size() - i);
                    newClass.addClassFunction(temp.getFuncName(), temp);
                }
                funcStack.clear();
            }
            while (!classFuncStack.isEmpty()) {
                XFuncObject temp = classFuncStack.pop();
                newClass.addInstanceFunction(temp.getFuncName(), temp);
            }
            classMap.put(newClass.getClassName(), newClass);
        } else {
            // repeat name
        }
    }

}
