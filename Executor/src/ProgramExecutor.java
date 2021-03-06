import java.util.HashMap;
import java.util.Stack;

public class ProgramExecutor {
    CNode root;
    XEnv runEnv;
    CNode pointer;
    Stack<CNode> nodeStack;
    Stack<XEnv> envStack;
    int statusNum = 0;
    int level;
    AssignLeftStruct assignObject;
    AssignLeftList assignLeftList;
    XObject rightValue;
    XBoolObject TRUE = new XBoolObject(true);
    XBoolObject FALSE = new XBoolObject(false);


    public ProgramExecutor(CNode root, HashMap<String, XFuncObject> funcMap, HashMap<String, XClassObject> classMap) {
        this.root = root;
        runEnv = new XEnv(null);
        runEnv.merge(OFunctionTable.getInstance().getEnv());
        runEnv.merge(funcMap, classMap);
        pointer = root;
        nodeStack = new Stack<>();
        envStack = new Stack<>();
        envStack.push(runEnv);
        level = 0;
    }

    public int run() {
        Program(root);
        return statusNum;
    }

    public void	Program(CNode node) {
        switch (node.production) {
            case Program_To_ExtDefList:
                ExtDefList(node.getChild(0));
                break;
        }
    }
    public void	ExtDefList(CNode node) {
        switch (node.production) {
            case ExtDefList_To_ExtDef:
                ExtDef(node.getChild(0));
                break;
            case ExtDefList_To_ExtDefList_ExtDef:
                ExtDefList(node.getChild(0));
                ExtDef(node.getChild(1));
                break;

        }
    }
    public void	ExtDef(CNode node) {
        switch (node.production) {
            case ExtDef_To_ClassDeclaration:
                ClassDeclaration(node.getChild(0), true);
                break;
            case ExtDef_To_FuncDeclaration:
                FuncDeclaration(node.getChild(0));
                break;
            case ExtDef_To_ImportDeclaration:
                ImportDeclaration(node.getChild(0));
                break;
            case ExtDef_To_StmtList:
                StmtList(node.getChild(0));
                break;
        }
    }

    // load file
    public void	ImportDeclaration(CNode node) {
        switch (node.production) {
            case ImportDeclaration_To_Import_PackageChain:
                XEnv temp = PackageChainImport(node.getChild(0));
                runEnv.merge(temp);
        }
    }

    public XEnv	PackageChainImport(CNode node) {
        switch (node.production) {
            case PackageChain_To_Identifier:
                return Parser.importFile(node.getIdentifier());
            case PackageChain_To_PackageChain_Dot_Identifier:
                XEnv temp = PackageChainImport(node.getChild(0));
                return temp.getXObjectByNameQualify(node.getChild(1).getIdentifier()).env;
        }
        return null;
    }

    // find path
    public void	PackageChain(CNode node) {
        switch (node.production) {
            case PackageChain_To_Identifier:
                node.getChild(0).getIdentifier();
            case PackageChain_To_PackageChain_Dot_Identifier:
                PackageChain(node.getChild(0));
        }
    }
    public void	ClassDeclaration(CNode node, boolean initial) {
        switch (node.production) {
            case ClassDeclaration_To_Class_Identifier_ClassBody:
                XObject xObject = runEnv.getXObjectByName(node.getChild(0).getIdentifier());
                if (xObject.type == XType.xClass) {
                    ((XClassObject)xObject).setBaseEnv(runEnv);
                    node.getChild(1).setBrother(xObject);
                    ClassBody(node.getChild(1));
                }
                break;
            case ClassDeclaration_To_Class_Identifier_Colon_Super_ClassBody:
        }
    }
    public void	Super(CNode node) {
        switch (node.production) {

        }
    }

    public void	ClassBody(CNode node) {
        switch (node.production) {
            case ClassBody_To_LBrace_ClassBodyDeclarations_RBrace:
                node.getChild(0).setBrother(node.getBrother());
                ClassBodyDeclarations(node.getChild(0));
                break;
            case ClassBody_To_LBrace_RBrace:
                break;
        }
    }
    public void	ClassBodyDeclarations(CNode node) {
        node.getChild(0).setBrother(node.getBrother());
        switch (node.production) {
            case ClassBodyDeclarations_To_ClassBodyDeclaration:
                ClassBodyDeclaration(node.getChild(0));
                break;
            case ClassBodyDeclarations_To_ClassBodyDeclarations_ClassBodyDeclaration:
                ClassBodyDeclarations(node.getChild(0));
                node.getChild(1).setBrother(node.getBrother());
                ClassBodyDeclaration(node.getChild(1));
        }
    }
    public void	ClassBodyDeclaration(CNode node) {
        switch (node.production) {
            case ClassBodyDeclaration_To_ClassMemberDeclaration:
                node.getChild(0).setBrother(node.getBrother());
                ClassMemberDeclaration(node.getChild(0));
                break;
            case ClassBodyDeclaration_To_ClassFuncDeclaration:
                node.getChild(0).setBrother(node.getBrother());
                ClassFuncDeclaration(node.getChild(0));
                break;
            case ClassBodyDeclaration_To_FuncDeclaration:
                node.getChild(0).setBrother(node.getBrother());
                FuncDeclaration(node.getChild(0));
        }
    }
    public void	ClassMemberDeclaration(CNode node) {
        switch (node.production) {
            case ClassMemberDeclaration_To_Identifier_Assign_NoAssignExp:
//                node.getChild(0).getIdentifier();
                // get and set
                NoAssignExp(node.getChild(1));
                ((XClassObject)node.getBrother()).addClassVariable(node.getChild(0).getIdentifier(), node.getChild(1).getXObject());
        }
    }

    public void ClassFuncDeclaration(CNode node) {
        switch (node.production) {
            case ClassFuncDeclaration_To_Func_Identifier_LBracket_Self_Comma_ParamList_RBracket_CompSt:
            case ClassFuncDeclaration_To_Func_Identifier_LBracket_Self_RBracket_CompSt:
                // set lexical env for class
//                ((XFuncObject)((XClassObject)node.getBrother()).getFromInstance(node.getChild(0).getIdentifier())).setBaseEnv(((XClassObject)node.getBrother()).getClassEnv());
                ((XFuncObject)((XClassObject)node.getBrother()).getFromInstance(node.getChild(0).getIdentifier())).setBaseEnv(runEnv);
        }
    }

    public void	FuncDeclaration	(CNode node)	{
        switch (node.production) {
            case FuncDeclaration_To_Func_Identifier_LBracket_ParamList_RBracket_CompSt:
            case FuncDeclaration_To_Func_Identifier_LBracket_RBracket_CompSt:
                if (node.hasBrother()) {
//                    ((XFuncObject)((XClassObject)node.getBrother()).getFromClass(node.getChild(0).getIdentifier())).setBaseEnv(((XClassObject)node.getBrother()).getClassEnv());
                    ((XFuncObject)((XClassObject)node.getBrother()).getFromClass(node.getChild(0).getIdentifier())).setBaseEnv(runEnv);
                } else {
                    XObject temp = runEnv.getXObjectByName(node.getChild(0).getIdentifier());
                    if (temp.type == XType.xFunc) {
                        ((XFuncObject)temp).setBaseEnv(runEnv);
                    }
                }
        }
    }

    public void	ParamList(CNode node) {
        switch (node.production) {
            case ParamList_To_Identifier:
                assignObject = new AssignLeftStruct();
                assignObject.setType(AssignableType.single);
                assignObject.setIdentifier(node.getIdentifier());
                assignLeftList.addLeft(assignObject);
                break;
            case ParamList_To_ParamList_Comma_Identifier:
                ParamList(node.getChild(0));
                assignObject = new AssignLeftStruct();
                assignObject.setType(AssignableType.single);
                assignObject.setIdentifier(node.getChild(1).getIdentifier());
                assignLeftList.addLeft(assignObject);
                break;
            case ParamList_To_Identifier_DefaultValue:
            case ParamList_To_ParamList_Comma_Identifier_DefaultValue:
                // implement later, need change the grammar
        }
    }
    public void	DefaultValue	(CNode node)	{
        switch (node.production) {
            case DefaultValue_To_Assign_Primary:
                Primary(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
        }
    }
    public void	Dictionary(CNode node) {
        XDictObject dictObject = new XDictObject();
        switch (node.production) {
            case Dictionary_To_LBrace_MapList_RBrace:
                node.getChild(0).setBrother(dictObject);
                MapList(node.getChild(0));
                node.setXObject(dictObject);
                break;
            case Dictionary_To_LBrace_RBrace:
                node.setXObject(dictObject);
                break;
        }
        node.setXObject(dictObject);
    }
    public void	MapList	(CNode node) {
        node.getChild(0).setBrother(node.getBrother());
        switch (node.production) {
            case MapList_To_Map:
                Map(node.getChild(0));
                break;
            case MapList_To_MapList_Comma_Map:
                MapList(node.getChild(0));
                node.getChild(1).setBrother(node.getBrother());
                Map(node.getChild(1));
                break;
        }
    }
    public void	Map	(CNode node) {
        switch (node.production) {
            case Map_To_Primary_Colon_Item:
                Primary(node.getChild(0));
                Item(node.getChild(1));
                ((XDictObject)node.getBrother()).put(node.getChild(0).getXObject(), node.getChild(1).getXObject());
                break;
        }
    }
    public void	List(CNode node) {
        XListObject listObject = new XListObject();
        switch (node.production) {
            case List_To_LSquare_ItemList_RSquare:
                node.getChild(0).setBrother(listObject);
                ItemList(node.getChild(0));
                node.setXObject(listObject);
                break;
            case List_To_LSquare_RSquare:
                node.setXObject(listObject);
                break;
        }
        node.setXObject(listObject);
    }
    public void	ItemList(CNode node) {
        switch (node.production) {
            case ItemList_To_Item:
                Item(node.getChild(0));
                ((XListObject)node.getBrother()).add(node.getChild(0).getXObject());
                break;
            case ItemList_To_ItemList_Comma_Item:
                node.getChild(0).setBrother(node.getBrother());
                ItemList(node.getChild(0));
                Item(node.getChild(1));
                ((XListObject)node.getBrother()).add(node.getChild(1).getXObject());
                break;
        }
    }
    public void	TupleStart(CNode node) {
        switch (node.production) {
            case TupleStart_To_Item_Comma_Item:
                Item(node.getChild(0));
                Item(node.getChild(1));
                ((XTupleObject)node.getBrother()).initial(node.getChild(0).getXObject());
                ((XTupleObject)node.getBrother()).initial(node.getChild(1).getXObject());
                break;
            case TupleStart_To_Item_Comma_Item_TupleFollow:
                Item(node.getChild(0));
                Item(node.getChild(1));
                ((XTupleObject)node.getBrother()).initial(node.getChild(0).getXObject());
                ((XTupleObject)node.getBrother()).initial(node.getChild(1).getXObject());
                node.getChild(2).setBrother(node.getBrother());
                TupleFollow(node.getChild(2));
                break;
        }
    }
    public void	TupleFollow(CNode node) {
        switch (node.production) {
            case TupleFollow_To_Item:
                Item(node.getChild(0));
                ((XTupleObject)node.getBrother()).initial(node.getChild(0).getXObject());
                break;
            case TupleFollow_To_TupleFollow_Comma_Item:
                node.getChild(0).setBrother(node.getBrother());
                TupleFollow(node.getChild(0));
                Item(node.getChild(1));
                ((XTupleObject)node.getBrother()).initial(node.getChild(1).getXObject());
                break;
        }
    }
    public void	Item(CNode node) {
        switch (node.production) {
            case Item_To_Dictionary:
                Dictionary(node.getChild(0));
                break;
            case Item_To_List:
                List(node.getChild(0));
                break;
            case Item_To_Primary:
                Primary(node.getChild(0));
                break;
            case Item_To_Tuple:
                Tuple(node.getChild(0));
                break;
        }
        node.setXObject(node.getChild(0).getXObject());
    }
    public void	Tuple(CNode node) {
        XTupleObject tupleObject = new XTupleObject();
        switch (node.production) {
            case Tuple_To_LBracket_TupleStart_RBracket:
            case TupleNoBracket_To_TupleStart:
                node.getChild(0).setBrother(tupleObject);
                TupleStart(node.getChild(0));
                break;
        }
        tupleObject.setSettle();
        node.setXObject(tupleObject);
    }

    public void	ListAndTuple(CNode node) {
        switch (node.production) {
            case ListAndTuple_To_List:
                List(node.getChild(0));
                break;
            case ListAndTuple_To_Tuple:
            case ListAndTuple_To_TupleNoBracket:
                Tuple(node.getChild(0));
                break;
        }
        node.setXObject(node.getChild(0).getXObject());
    }
    public void	CompSt(CNode node, boolean createEnv) {
        switch (node.production) {
            case CompSt_To_LBrace_RBrace:
                break;
            case CompSt_To_LBrace_StmtList_RBrace:
                if (createEnv) {
                    envStack.push(new XEnv(runEnv));
                    runEnv = envStack.peek();
                    runEnv.envOwner = XEnvOwner.xOther;
                }
                StmtList(node.getChild(0));
                if (createEnv) {
                    envStack.pop();
                    runEnv = envStack.peek();
                }
                if (alignAction(node, 0)) return;
                break;
        }
    }

    public boolean returnAction(CNode node, int child) {
        if (node.getChild(child).getAlignAction() == AlignAction.xReturn) {
            node.xReturn();
            node.getChild(child).setBackAlignAction();
            node.setXObject(node.getChild(child).getXObject());
            return true;
        }
        return false;
    }

    public boolean breakAction(CNode node, int child) {
        if (node.getChild(child).getAlignAction() == AlignAction.xBreak) {
            node.xBreak();
            node.getChild(child).setBackAlignAction();
            return true;
        }
        return false;
    }

    public boolean continueAction(CNode node, int child) {
        if (node.getChild(child).getAlignAction() == AlignAction.xContinue) {
            node.xContinue();
            node.getChild(child).setBackAlignAction();
            return true;
        }
        return false;
    }

    public boolean alignAction(CNode node, int child) {
        if (node.getChild(child).getAlignAction() == AlignAction.xReturn) {
            node.xReturn();
            node.getChild(child).setBackAlignAction();
            node.setXObject(node.getChild(child).getXObject());
            return true;
        } else if (node.getChild(child).getAlignAction() == AlignAction.xBreak) {
            node.xBreak();
            node.getChild(child).setBackAlignAction();
            return true;
        } else if (node.getChild(child).getAlignAction() == AlignAction.xContinue) {
            node.xContinue();
            node.getChild(child).setBackAlignAction();
            return true;
        }
        return false;
    }

    public void	StmtList(CNode node) {
        switch (node.production) {
            case StmtList_To_Stmt:
                Stmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                break;
            case StmtList_To_StmtList_Stmt:
                StmtList(node.getChild(0));
                if (alignAction(node, 0)) return;
                Stmt(node.getChild(1));
                if (alignAction(node, 1)) return;
                break;
        }
    }
    public void	Stmt(CNode node) {
        switch (node.production) {
            case Stmt_To_Break:
                if (insideRepeat()) {
                    node.xBreak();
                } else {
                    // no break
                }
                break;
            case Stmt_To_CompSt_LF:
                CompSt(node.getChild(0), true);
                if (alignAction(node, 0)) return;
                break;
            case Stmt_To_Continue:
                if (insideRepeat()) {
                    node.xContinue();
                } else {
                    // no continue
                }
                break;
            case Stmt_To_Exp_LF:
                Exp(node.getChild(0));break;
            case Stmt_To_IfElseStmt_LF:
                IfElseStmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                break;
            case Stmt_To_RepeatStmt_LF:
                RepeatStmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                break;
            case Stmt_To_ReturnStmt_LF:
                if (insideFunc()){
                    ReturnStmt(node.getChild(0));
                    node.setXObject(node.getChild(0).getXObject());
                    node.xReturn();
                } else {
                    // can not return
                }
                break;

        }}
    public void	ReturnStmt(CNode node) {
        switch (node.production) {
            case ReturnStmt_To_Return:
                node.setXObject(null);
                break;
            case ReturnStmt_To_Return_ReturnParam:
                ReturnParam(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
        }
    }
    public void	ReturnParam(CNode node) {
        switch (node.production) {
            case ReturnParam_To_Dictionary:
                Dictionary(node.getChild(0));
                break;
            case ReturnParam_To_ListAndTuple:
                ListAndTuple(node.getChild(0));
                break;
            case ReturnParam_To_NoAssignExp:
                NoAssignExp(node.getChild(0));
                break;
        }
        node.setXObject(node.getChild(0).getXObject());
    }
    public void	IfStmt(CNode node) {
        switch (node.production) {
            case IfStmt_To_If_NoAssignExp_CompSt:
                NoAssignExp(node.getChild(0));
                if (node.getChild(0).getXObject().equals(TRUE)) {
                    CompSt(node.getChild(1), true);
                    if (alignAction(node, 1)) return;
                }
                node.setXObject(node.getChild(0).getXObject());
        }
    }
    public void	ElifStmt(CNode node) {
        switch (node.production) {
            case ElifStmt_To_IfStmt:
                IfStmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                node.setXObject(node.getChild(0).getXObject());
                break;
            case ElifStmt_To_ElifStmt_Elif_NoAssignExp_CompSt:
                ElifStmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                if (node.getChild(0).getXObject().equals(FALSE)) {
                    NoAssignExp(node.getChild(1));
                    if (node.getChild(1).getXObject().equals(TRUE)) {
                        CompSt(node.getChild(2), true);
                        if (alignAction(node, 2)) return;
                    }
                    node.setXObject(node.getChild(1).getXObject());
                } else {
                    node.setXObject(node.getChild(0).getXObject());
                }
                break;
        }
    }
    public void	IfElseStmt(CNode node) {
        switch (node.production) {
            case IfElseStmt_To_ElifStmt:
                ElifStmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                break;
            case IfElseStmt_To_ElifStmt_Else_CompSt:
                ElifStmt(node.getChild(0));
                if (alignAction(node, 0)) return;
                if (node.getChild(0).getXObject().equals(FALSE)) {
                    CompSt(node.getChild(1), true);
                    if (alignAction(node, 1)) return;
                }
                break;

        }
    }
    public void	RepeatStmt(CNode node)	{
        int repeatCount = 0;
        switch (node.production) {
            case RepeatStmt_To_Repeat_RepeatCond_CompSt:
                CNode cond = node.getChild(0);
                if (cond.production == GrammarEnum.RepeatCond_To_NoAssignExp) {
                    while (true){
                        NoAssignExp(cond.getChild(0));
                        cond.setXObject(cond.getChild(0).getXObject());
                        if (cond.getXObject().equals(TRUE)) {
                            envStack.push(new XEnv(runEnv));
                            runEnv = envStack.peek();
                            runEnv.envOwner = XEnvOwner.xRepeat;
                            CompSt(node.getChild(1), false);
                            envStack.pop();
                            runEnv = envStack.peek();
                            if (returnAction(node, 1)) return;
                            if (breakAction(node, 1)) {
                                node.setBackAlignAction();
                                break;
                            }
                            if (continueAction(node, 1)) {
                                node.setBackAlignAction();
                            }
                            repeatCount++;
                        } else break;
                    }
                } else if (cond.production == GrammarEnum.RepeatCond_To_RepeatParam_In_IterateValue) {
                    IterateValue(cond.getChild(1));
                    XObject iterate = cond.getChild(1).getXObject();

                    if (iterate.type == XType.xList || iterate.type == XType.xTuple)
                    {
                        XIterable xIterate = (XIterable) iterate;
                        int maxRepeat = xIterate.size();
                        while (repeatCount < maxRepeat) {
                            assignLeftList = new AssignLeftList();
                            RepeatParam(cond.getChild(0));
                            envStack.push(new XEnv(runEnv));
                            runEnv = envStack.peek();
                            runEnv.envOwner = XEnvOwner.xRepeat;
                            assignLeftList.assign(xIterate.get(repeatCount), runEnv);
                            CompSt(node.getChild(1), false);
                            envStack.pop();
                            runEnv = envStack.peek();
                            if (returnAction(node, 1)) return;
                            if (breakAction(node, 1)) {
                                node.setBackAlignAction();
                                break;
                            }
                            if (continueAction(node, 1)) {
                                node.setBackAlignAction();
                            }
                            repeatCount++;
                        }


                    } else {
                        // not iterable
                    }
                }
        }}
    public void	RepeatParam(CNode node) {
        switch (node.production) {
            case RepeatParam_To_Identifier:
                assignObject = new AssignLeftStruct();
                assignObject.setType(AssignableType.single);
                assignObject.setIdentifier(node.getIdentifier());
                assignLeftList.addLeft(assignObject);
                break;
            case RepeatParam_To_RepeatParam_Comma_Identifier:
                RepeatParam(node.getChild(0));
                assignObject = new AssignLeftStruct();
                assignObject.setType(AssignableType.single);
                assignObject.setIdentifier(node.getChild(1).getIdentifier());
                assignLeftList.addLeft(assignObject);
                break;
        }
    }
    public void	IterateValue(CNode node) {
        switch (node.production) {
            case IterateValue_To_List:
                List(node.getChild(0));
                break;
            case IterateValue_To_Variable:
                Variable(node.getChild(0));
                break;
            case IterateValue_To_Tuple:
                Tuple(node.getChild(0));
                break;
        }
        node.setXObject(node.getChild(0).getXObject());
    }



    public void	FuncInvocation(CNode node) {
        // func obj
        XObject object = null;
        boolean isInstanceDotInvocation = false;
        if (node.hasBrother()) {
            XObject brother = node.getBrother();
            object = brother.getInstanceMember(node.getIdentifier());
            isInstanceDotInvocation = true;
            if (brother.type == XType.xClass) {
                isInstanceDotInvocation = false;
            }
//            if (brother.type == XType.xClass) {
//                XClassObject xClassObject = (XClassObject)brother;
//                object = xClassObject.getFromClass(node.getIdentifier());
//            } else if (brother.type == XType.xInstance) {
//                isInstanceDotInvocation = true;
//                XInstanceObject xInstanceObject = (XInstanceObject)brother;
//                object = ((XClassObject)runEnv.getXObjectByName(xInstanceObject.getClassName())).getFromInstance(node.getIdentifier());
//            }
        } else {
            object = runEnv.getXObjectByName(node.getIdentifier());
        }

        if (object.type == XType.xFunc || object.type == XType.xClass) {
            switch (node.production) {
                case FuncInvocation_To_Identifier_LBracket_RBracket:
                    if (object.type == XType.xClass) {
                        XClassObject xClassObject = (XClassObject)object;
                        //init
                        XInstanceObject newInstance = xClassObject.initial();
                        if (xClassObject.isHasInitial()) {
                            XFuncObject initialFunc = (XFuncObject)xClassObject.getFromInstance(xClassObject.getClassName());
                            envStack.push(initialFunc.getFuncEnv());
                            runEnv = envStack.peek();
                            runEnv.envOwner = XEnvOwner.xInitialFunc;
                            runEnv.setXObjectByName("self", newInstance);
                            CompSt(initialFunc.getContent(), false);
                            envStack.pop();
                            runEnv = envStack.peek();
                            node.setXObject(newInstance);
                        }
                    } else {
                        XFuncObject xFuncObject = (XFuncObject)object;
                        if (xFuncObject.isOriginal) {
                            if (isInstanceDotInvocation) {
                                XTupleObject argTuple = new XTupleObject();
                                argTuple.initial(node.getBrother());
                                node.setXObject(OFunctionTable.getInstance().callOriginalFunc(xFuncObject.getFuncName(), argTuple));
                            } else {
                                node.setXObject(OFunctionTable.getInstance().callOriginalFunc(xFuncObject.getFuncName(), null));
                            }
                        } else {
                            envStack.push(xFuncObject.getFuncEnv());
                            runEnv = envStack.peek();
                            if (isInstanceDotInvocation) {
                                runEnv.envOwner = XEnvOwner.xInstanceFunc;
                                runEnv.setXObjectByName("self", node.getBrother());
                            } else {
                                runEnv.envOwner = XEnvOwner.xNormalFunc;
                            }
                            CompSt(xFuncObject.getContent(), false);
                            xFuncObject.getContent().setBackAlignAction();
                            envStack.pop();
                            runEnv = envStack.peek();
                            node.setXObject(xFuncObject.getContent().getXObject());
                        }
                    }
                    break;
                case FuncInvocation_To_Identifier_LBracket_Args_RBracket:
                    if (object.type == XType.xClass) {
                        XClassObject xClassObject = (XClassObject)object;
                        //init
                        XInstanceObject newInstance = xClassObject.initial();
                        if (xClassObject.isHasInitial()) {
                            XFuncObject initialFunc = (XFuncObject)xClassObject.getFromInstance(xClassObject.getClassName());

                            assignLeftList = new AssignLeftList();
                            ParamList(initialFunc.getParams());

                            XTupleObject argTuple = new XTupleObject();
                            node.getChild(0).setBrother(argTuple);
                            Args(node.getChild(0));

                            envStack.push(initialFunc.getFuncEnv());
                            runEnv = envStack.peek();
                            runEnv.envOwner = XEnvOwner.xInitialFunc;
                            runEnv.setXObjectByName("self", newInstance);
                            assignLeftList.assign(argTuple, runEnv);
                            CompSt(initialFunc.getContent(), false);
                            envStack.pop();
                            runEnv = envStack.peek();
                            node.setXObject(newInstance);
                        }
                    } else {
                        XFuncObject xFuncObject = (XFuncObject)object;
                        if (xFuncObject.isOriginal) {
                            XTupleObject argTuple = new XTupleObject();
                            if (isInstanceDotInvocation) {
                                argTuple.initial(node.getBrother());
                            }
                            node.getChild(0).setBrother(argTuple);
                            Args(node.getChild(0));
                            node.setXObject(OFunctionTable.getInstance().callOriginalFunc(xFuncObject.getFuncName(), argTuple));
                        } else {
                            assignLeftList = new AssignLeftList();
                            ParamList(xFuncObject.getParams());
                            XTupleObject argTuple = new XTupleObject();
                            node.getChild(0).setBrother(argTuple);
                            Args(node.getChild(0));
                            envStack.push(xFuncObject.getFuncEnv());
                            runEnv = envStack.peek();
                            if (isInstanceDotInvocation) {
                                runEnv.envOwner = XEnvOwner.xInstanceFunc;
                                runEnv.setXObjectByName("self", node.getBrother());
                            } else {
                                runEnv.envOwner = XEnvOwner.xNormalFunc;
                            }
                            assignLeftList.assign(argTuple, runEnv);
                            CompSt(xFuncObject.getContent(), false);
                            xFuncObject.getContent().setBackAlignAction();
                            envStack.pop();
                            runEnv = envStack.peek();
                            node.setXObject(xFuncObject.getContent().getXObject());
                        }
                    }


            }
        } else {
            // not call able
        }
    }

    // Variable [ Variable ]
    // Variable [ STRING ]
    // Variable [ NUM ]
    // Variable [ NoAssignExp ]
    public void assignListOrDict(CNode node, boolean assign) {
        if (assign) {
            assignObject.setType(AssignableType.square);
            assignObject.setSquare(node.getChild(0).getXObject(),node.getChild(1).getXObject());
            assignObject.setValue(rightValue, runEnv);
        } else {
            XObject temp = node.getChild(0).getXObject();
            XObject key = node.getChild(1).getXObject();
            if (temp.type == XType.xList || temp.type == XType.xTuple) {
                if (key.type == XType.xNum) {
                    node.setXObject(((XIterable)temp).get((XNumObject)key));
                } else {
                    // iterable value can not deal with key
                }
            } else if (temp.type == XType.xDict) {
                node.setXObject(((XDictObject)temp).get(key));
            }
        }
    }

    public void	AssignableValue	(CNode node, boolean assign) {
        switch (node.production) {
            case AssignableValue_To_Identifier:
                if (assign) {
                    if (node.hasBrother()) {
                        assignObject.setType(AssignableType.dot);
                        assignObject.setIdentifier(node.getIdentifier());
                        assignObject.setDot(node.getBrother());
                        // runEnv is meaningless
                        assignObject.setValue(rightValue, runEnv);
                    } else {
                        assignObject.setType(AssignableType.single);
                        assignObject.setIdentifier(node.getIdentifier());
                        assignObject.setValue(rightValue, runEnv);
                    }
                } else {
                    if (node.hasBrother()) {
                        node.setXObject(node.getBrother().getInstanceMember(node.getIdentifier()));
                    } else {
                        node.setXObject(runEnv.getXObjectByName(node.getIdentifier()));
                    }
                }
                break;
            case AssignableValue_To_Variable_Dot_AssignableValue:
                Variable(node.getChild(0));
                node.getChild(1).setBrother(node.getChild(0).getXObject());
                AssignableValue(node.getChild(1), assign);
                node.setXObject(node.getChild(1).getXObject());
                break;
            case AssignableValue_To_Variable_LSquare_Num_RSquare:
            case AssignableValue_To_Variable_LSquare_String_RSquare:
                if (node.hasBrother())
                    node.getChild(0).setBrother(node.getBrother());
                Variable(node.getChild(0));
                assignListOrDict(node, assign);
                break;
            case AssignableValue_To_Variable_LSquare_NoAssignExp_RSquare:
                if (node.hasBrother())
                    node.getChild(0).setBrother(node.getBrother());
                Variable(node.getChild(0));
                NoAssignExp(node.getChild(1));
                assignListOrDict(node, assign);
                break;
            case AssignableValue_To_Variable_LSquare_Variable_RSquare:
                if (node.hasBrother())
                    node.getChild(0).setBrother(node.getBrother());
                Variable(node.getChild(0));
                Variable(node.getChild(1));
                assignListOrDict(node, assign);
                break;
        }
    }
    public void	Variable(CNode node) {
        switch (node.production) {
            case Variable_To_AssignableValue:
                if (node.hasBrother())
                    node.getChild(0).setBrother(node.getBrother());
                AssignableValue(node.getChild(0), false);
                node.setXObject(node.getChild(0).getXObject());
                break;
            case Variable_To_FuncInvocation:
                FuncInvocation(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case Variable_To_Variable_Dot_FuncInvocation:
                Variable(node.getChild(0));
                node.getChild(1).setBrother(node.getChild(0).getXObject());
                FuncInvocation(node.getChild(1));
                node.setXObject(node.getChild(1).getXObject());
                break;
            case Variable_To_Self:
                node.setXObject(runEnv.getXObjectByName("self"));
                break;
        }
    }
    public void	Primary	(CNode node) {
        switch (node.production) {
            case Primary_To_Num:
            case Primary_To_Real:
            case Primary_To_String:
            case Primary_To_True:
            case Primary_To_False:
            case Primary_To_None:
                break;
            case Primary_To_Variable:
                Variable(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case Primary_To_LBracket_NoAssignExp_RBracket:
                NoAssignExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
        }
    }
    public void	UnaryExp(CNode node) {
        switch (node.production) {
            case UnaryExp_To_Not_Primary:
                Primary(node.getChild(0));
                node.setXObject(BasicCal.unaryExpNot(node.getChild(0).getXObject()));
                break;
            case UnaryExp_To_Primary:
                Primary(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case UnaryExp_To_Sub_Primary:
                Primary(node.getChild(0));
                node.setXObject(BasicCal.unaryExpSub(node.getChild(0).getXObject()));
                break;
        }
    }
    public void	MultiplicativeExp(CNode node) {
        switch (node.production) {
            case MultiplicativeExp_To_UnaryExp:
                UnaryExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case MultiplicativeExp_To_MultiplicativeExp_Div_UnaryExp:
                MultiplicativeExp(node.getChild(0));
                UnaryExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.Div));
                break;
            case MultiplicativeExp_To_MultiplicativeExp_Mod_UnaryExp:
                MultiplicativeExp(node.getChild(0));
                UnaryExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.Mod));
                break;
            case MultiplicativeExp_To_MultiplicativeExp_Mul_UnaryExp:
                MultiplicativeExp(node.getChild(0));
                UnaryExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.Mul));
                break;
        }
    }
    public void	AdditiveExp(CNode node) {
        switch (node.production) {
            case AdditiveExp_To_MultiplicativeExp:
                MultiplicativeExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case AdditiveExp_To_AdditiveExp_Add_MultiplicativeExp:
                AdditiveExp(node.getChild(0));
                MultiplicativeExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.Add));
                break;
            case AdditiveExp_To_AdditiveExp_Sub_MultiplicativeExp:
                AdditiveExp(node.getChild(0));
                MultiplicativeExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.Sub));
                break;
        }
    }
    public void	RelationExp(CNode node) {
        switch (node.production) {
            case RelationExp_To_AdditiveExp:
                AdditiveExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case RelationExp_To_RelationExp_GE_AdditiveExp:
                RelationExp(node.getChild(0));
                AdditiveExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.GE));
                break;
            case RelationExp_To_RelationExp_GT_AdditiveExp:
                RelationExp(node.getChild(0));
                AdditiveExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.GT));
                break;
            case RelationExp_To_RelationExp_LE_AdditiveExp:
                RelationExp(node.getChild(0));
                AdditiveExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.LE));
                break;
            case RelationExp_To_RelationExp_LT_AdditiveExp:
                RelationExp(node.getChild(0));
                AdditiveExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.LT));
                break;
        }}
    public void	EqualityExp(CNode node) {
        switch (node.production) {
            case EqualityExp_To_EqualityExp_EQ_RelationExp:
                EqualityExp(node.getChild(0));
                RelationExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.EQ));
                break;
            case EqualityExp_To_EqualityExp_NE_RelationExp:
                EqualityExp(node.getChild(0));
                RelationExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.NE));
                break;
            case EqualityExp_To_RelationExp:
                RelationExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
        }}
    public void	ConditionAndExp(CNode node) {
        switch (node.production) {
            case ConditionAndExp_To_ConditionAndExp_And_EqualityExp:
                ConditionAndExp(node.getChild(0));
                EqualityExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.And));
                break;
            case ConditionAndExp_To_EqualityExp:
                EqualityExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
        }}
    public void	ConditionOrExp(CNode node) {
        switch (node.production) {
            case ConditionOrExp_To_ConditionAndExp:
                ConditionAndExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
            case ConditionOrExp_To_ConditionOrExp_Or_ConditionAndExp:
                ConditionOrExp(node.getChild(0));
                ConditionAndExp(node.getChild(1));
                node.setXObject(BasicCal.binaryExp(node.getChild(0).getXObject(), node.getChild(1).getXObject(), Tag.Or));
                break;
        }
    }
    public void	NoAssignExp(CNode node)	{
        switch (node.production) {
            case NoAssignExp_To_ConditionOrExp:
                ConditionOrExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
        }}
    public void	AssignmentExp(CNode node) {
        switch (node.production) {
            case AssignmentExp_To_Assignment:
                Assignment(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                 break;
            case AssignmentExp_To_NoAssignExp:
                NoAssignExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
        }
    }
    public void	Assignment(CNode node)	{
        switch (node.production) {
            case Assignment_To_LeftSide_Assign_AssignmentExp:
                AssignmentExp(node.getChild(1));
                rightValue = node.getChild(1).getXObject();
                LeftSide(node.getChild(0));
                node.setXObject(rightValue);
                break;
            case Assignment_To_LeftSide_Assign_Dictionary:
                Dictionary(node.getChild(1));
                rightValue = node.getChild(1).getXObject();
                LeftSide(node.getChild(0));
                node.setXObject(rightValue);
                break;
            case Assignment_To_LeftSide_Assign_ListAndTuple:
                ListAndTuple(node.getChild(1));
                rightValue = node.getChild(1).getXObject();
                LeftSide(node.getChild(0));
                node.setXObject(rightValue);
                break;

        }}
    public void	LeftSide(CNode node){
        switch (node.production) {
            case LeftSide_To_AssignableValue:
                assignObject = new AssignLeftStruct();
                AssignableValue(node.getChild(0), true);
            case LeftSide_To_ListAndTuple:
                if (rightValue.type == XType.xTuple || rightValue.type == XType.xList) {
                    assignLeftList = new AssignLeftList();
                    ListAndTupleAssign(node.getChild(0));
                }
                else {
                    // type can not assign to left
                }
        }
    }

    public void ListAndTupleAssign(CNode node) {

    }
    public void	Exp	(CNode node){
        switch (node.production) {
            case Exp_To_AssignmentExp:
                AssignmentExp(node.getChild(0));
                node.setXObject(node.getChild(0).getXObject());
                break;
        }
    }

    public void	Args(CNode node) {

        switch (node.production) {
            case Args_To_Args_Comma_Exp:
                node.getChild(0).setBrother(node.getBrother());
                Args(node.getChild(0));
                Exp(node.getChild(1));
                ((XTupleObject)node.getBrother()).initial(node.getChild(1).getXObject());
                break;
            case Args_To_Exp:
                Exp(node.getChild(0));
                ((XTupleObject)node.getBrother()).initial(node.getChild(0).getXObject());
//                node.setXObject(node.getChild(0).getXObject());
                break;
        }
    }


    private boolean insideFunc() {
        for (int i = 0; i < envStack.size(); i++) {
            if (envStack.get(i).envOwner == XEnvOwner.xInstanceFunc || envStack.get(i).envOwner == XEnvOwner.xNormalFunc) {
                return true;
            }
        }
        return false;
    }

    private boolean insideRepeat() {
        for (int i = 0; i < envStack.size(); i++) {
            if (envStack.get(i).envOwner == XEnvOwner.xRepeat) {
                return true;
            }
            if (envStack.get(i).envOwner == XEnvOwner.xInstanceFunc || envStack.get(i).envOwner == XEnvOwner.xNormalFunc) {
                return false;
            }
        }
        return false;
    }

}
