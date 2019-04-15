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
                ClassDeclaration(node.getChild(0));
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
                PackageChain(node.getChild(0));
        }
    }

    // find path
    public void	PackageChain(CNode node) {
        switch (node.production) {
            case PackageChain_To_Identifier:
            case PackageChain_To_PackageChain_Dot_Identifier:
                PackageChain(node.getChild(0));
        }
    }
public void	ClassDeclaration	(CNode node)	{
        switch (node.production) {}}
public void	SuperOpt	(CNode node)	{
        switch (node.production) {}}
public void	Super	(CNode node)	{
        switch (node.production) {}}
public void	ClassBody	(CNode node)	{
        switch (node.production) {}}
public void	ClassBodyDeclarations	(CNode node)	{
        switch (node.production) {}}
public void	ClassBodyDeclaration	(CNode node)	{
        switch (node.production) {}}
public void	ClassMemberDeclaration	(CNode node)	{
        switch (node.production) {}}
public void	FuncDeclaration	(CNode node)	{
        switch (node.production) {}}
public void	ParamListOpt	(CNode node)	{
        switch (node.production) {}}
public void	ParamList	(CNode node)	{
        switch (node.production) {}}
public void	DefaultValue	(CNode node)	{
        switch (node.production) {}}
public void	Dictionary	(CNode node)	{
        switch (node.production) {}}
public void	MapListOpt	(CNode node)	{
        switch (node.production) {}}
public void	MapList	(CNode node)	{
        switch (node.production) {}}
public void	Map	(CNode node)	{
        switch (node.production) {}}
public void	List	(CNode node)	{
        switch (node.production) {}}
public void	ItemListOpt	(CNode node)	{
        switch (node.production) {}}
public void	ItemList	(CNode node)	{
        switch (node.production) {}}
public void	TupleStart	(CNode node)	{
        switch (node.production) {}}
public void	TupleFollowOpt	(CNode node)	{
        switch (node.production) {}}
public void	TupleFollow	(CNode node)	{
        switch (node.production) {}}
public void	Item	(CNode node)	{
        switch (node.production) {}}
public void	Tuple	(CNode node)	{
        switch (node.production) {}}
public void	TupleNoBracket	(CNode node)	{
        switch (node.production) {}}
public void	ListAndTuple	(CNode node)	{
        switch (node.production) {}}
public void	CompSt	(CNode node)	{
        switch (node.production) {}}
public void	StmtListOpt	(CNode node)	{
        switch (node.production) {}}
    public void	StmtList(CNode node) {
        switch (node.production) {
            case StmtList_To_Stmt:
                Stmt(node.getChild(0));
                break;
            case StmtList_To_StmtList_Stmt:
                StmtList(node.getChild(0));
                Stmt(node.getChild(1));
                break;
        }
    }
    public void	Stmt(CNode node) {
        switch (node.production) {
            case Stmt_To_Break:
            case Stmt_To_CompSt_LF:
                CompSt(node.getChild(0));break;
            case Stmt_To_Continue:
            case Stmt_To_Exp_LF:
                Exp(node.getChild(0));break;
            case Stmt_To_IfElseStmt_LF:
            case Stmt_To_MultiAssignment_LF:
            case Stmt_To_RepeatStmt_LF:
            case Stmt_To_ReturnStmt_LF:
        }}
public void	ReturnStmt	(CNode node)	{
        switch (node.production) {}}
public void	ReturnParam	(CNode node)	{
        switch (node.production) {}}
public void	IfStmt	(CNode node)	{
        switch (node.production) {}}
public void	ElifStmt	(CNode node)	{
        switch (node.production) {}}
public void	IfElseStmt	(CNode node)	{
        switch (node.production) {}}
public void	RepeatStmt	(CNode node)	{
        switch (node.production) {}}
public void	RepeatParam	(CNode node)	{
        switch (node.production) {}}
public void	IterateValue	(CNode node)	{
        switch (node.production) {}}
public void	RepeatCond	(CNode node)	{
        switch (node.production) {}}
public void	FuncInvocation	(CNode node)	{
        switch (node.production) {}}
public void	AssignableValue	(CNode node)	{
        switch (node.production) {}}
public void	Variable	(CNode node)	{
        switch (node.production) {}}
    public void	Primary	(CNode node) {
        switch (node.production) {
            case Primary_To_Num:
            case Primary_To_Real:
            case Primary_To_String:
            case Primary_To_True:
            case Primary_To_False:
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
                node.setXObject(node.getChild(1).getXObject());
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

            case Assignment_To_MultiAssignment:
        }}
    public void	LeftSide(CNode node){
        switch (node.production) {
            case LeftSide_To_AssignableValue:
                AssignableValue(node.getChild(0));
            case LeftSide_To_ListAndTuple:
        }
    }

    public void	MultiAssignment(CNode node)	{
        switch (node.production) {

        }
    }
    public void	Exp	(CNode node){
        switch (node.production) {
            case Exp_To_AssignmentExp:
                AssignmentExp(node.getChild(0));
                break;
        }
    }
public void	ArgsName	(CNode node)	{
        switch (node.production) {}}
public void	Args(CNode node)	{
        switch (node.production) {}}

}
