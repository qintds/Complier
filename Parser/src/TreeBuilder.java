import java.util.Stack;

public class TreeBuilder{

    private Stack<Object> valueStack;
    private XEnv environment;
    private String address = "";
    private Stack<CNode> nodeStack =new Stack<>();

    public TreeBuilder(Stack<Object> valueStack){
        this.valueStack = valueStack;
        environment = new XEnv(null);
    }

    public CNode reduceAction(int productionNum) {
        CNode node = null;
        CNode temp = null;
        // when collapsing single to single reduction, don't refresh node's production num
        boolean collapse = false;
        GrammarEnum n = GrammarEnum.values()[productionNum];
        switch (n) {
            case Primary_To_Num:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setObject(new XNumObject((Num)valueStack.peek()));
                break;
            case Primary_To_Real:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setObject(new XRealObject((Real)valueStack.peek()));
                break;
            case Primary_To_True:
            case Primary_To_False:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setObject(new XBoolObject((Word)valueStack.peek()));
                break;
            case Primary_To_String:
                node = CNodeFactroy.createCNode(Tag.Primary);
                node.setObject(new XStringObject((Word)valueStack.peek()));
                break;
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
                collapse = true;
                node = (CNode) valueStack.peek();
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
                temp.setObject(new XNumObject((Num)valueStack.get(valueStack.size() - 2)));
                node.addChild(temp);
                break;
            case AssignableValue_To_Variable_LSquare_String_RSquare:
                node = CNodeFactroy.createCNode(Tag.AssignableValue);
                node.addChild((CNode)valueStack.get(valueStack.size() - 4));
                temp = CNodeFactroy.createCNode(Tag.String);
                temp.setObject(new XStringObject((String) valueStack.get(valueStack.size() - 2)));
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
                collapse = true;
                node = (CNode) valueStack.peek();
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
                collapse = true;
                node = (CNode) valueStack.peek();
                break;
            case MultiAssignment_To_AssignableValue_Assign_Dictionary:
            case MultiAssignment_To_AssignableValue_Assign_ListAndTuple:
            case MultiAssignment_To_ListAndTuple_Assign_ListAndTuple:
                node = CNodeFactroy.createCNode(Tag.MultiAssignment);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case Assignment_To_LeftSide_Assign_AssignmentExp:
                node = CNodeFactroy.createCNode(Tag.Assignment);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;
            case Assignment_To_MultiAssignment:
            case Exp_To_AssignmentExp:
            case Args_To_Exp:
                collapse = true;
                node = (CNode) valueStack.peek();
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
                collapse = true;
                node = (CNode) valueStack.peek();
                break;
            case ItemList_To_ItemList_Comma_Item:
                node = CNodeFactroy.createCNode(Tag.ItemList);
                node.addChild((CNode)valueStack.get(valueStack.size() - 3));
                node.addChild((CNode)valueStack.get(valueStack.size() - 1));
                break;



        }

        if (node != null && !collapse) {
            node.production = n;
        }
        return node;
    }


    private XObject unaryExpNot(XObject x) {
        if (x.type == XType.xBool) {
            XBoolObject xBool = (XBoolObject)x;
            xBool.value = !xBool.value;
            return xBool;
        } else {
            //error
            return null;
        }
    }

    private XObject unaryExpSub(XObject x) {
        if (x.type == XType.xNum) {
            XNumObject xNum = (XNumObject)x;
            xNum.value = - xNum.value;
            return xNum;
        } else if (x.type == XType.xReal) {
            XRealObject xReal = (XRealObject)x;
            xReal.value = - xReal.value;
            return xReal;
        } else {
            //error
            return null;
        }
    }

    private XObject binaryExp(XObject a, XObject b, Token op) {
        if (a.type == b.type){
            XType bothType = a.type;
            if (bothType == XType.xNum) {
                return numBinaryExp((XNumObject)a, (XNumObject)b, op);
            } else if (bothType == XType.xReal) {
                return realBinaryExp((XRealObject)a, (XRealObject)b, op);
            } else if (bothType == XType.xString) {
                return stringBinaryExp((XStringObject)a, (XStringObject)b, op);
            } else if (bothType == XType.xBool) {
                return boolBinaryExp((XBoolObject)a, (XBoolObject)b, op);
            } else if (bothType == XType.xInstance) {
                // user define class operator
            } else if (bothType == XType.xList) {

            }
        } else {
            if (a.type == XType.xReal && b.type ==XType.xNum
                    || a.type == XType.xNum && b.type ==XType.xReal) {
                return realBinaryExp((XRealObject)a, (XRealObject)b, op);
            }
        }
        return null;
    }

    private XObject numBinaryExp(XNumObject a, XNumObject b, Token op) {
        if (op.tag == Tag.Add)
            return new XNumObject(a.value + b.value);
        if (op.tag == Tag.Sub)
            return new XNumObject(a.value - b.value);
        if (op.tag == Tag.Mul)
            return new XNumObject(a.value * b.value);
        if (op.tag == Tag.Div)
            return new XNumObject(a.value / b.value);
        if (op.tag == Tag.Mod)
            return new XNumObject(a.value % b.value);
        if (op.tag == Tag.LT)
            return new XBoolObject(a.value < b.value);
        if (op.tag == Tag.GT)
            return new XBoolObject(a.value > b.value);
        if (op.tag == Tag.LE)
            return new XBoolObject(a.value <= b.value);
        if (op.tag == Tag.GE)
            return new XBoolObject(a.value >= b.value);
        if (op.tag == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op.tag == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }

    private XObject realBinaryExp(XRealObject a, XRealObject b, Token op) {
        if (op.tag == Tag.Add)
            return new XRealObject(a.value + b.value);
        if (op.tag == Tag.Sub)
            return new XRealObject(a.value - b.value);
        if (op.tag == Tag.Mul)
            return new XRealObject(a.value * b.value);
        if (op.tag == Tag.Div)
            return new XRealObject(a.value / b.value);
        if (op.tag == Tag.LT)
            return new XBoolObject(a.value < b.value);
        if (op.tag == Tag.GT)
            return new XBoolObject(a.value > b.value);
        if (op.tag == Tag.LE)
            return new XBoolObject(a.value <= b.value);
        if (op.tag == Tag.GE)
            return new XBoolObject(a.value >= b.value);
        if (op.tag == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op.tag == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }

    private XObject boolBinaryExp(XBoolObject a, XBoolObject b, Token op) {
        if (op.tag == Tag.And)
            return new XBoolObject(a.value && b.value);
        if (op.tag == Tag.Or)
            return new XBoolObject(a.value || b.value);
        if (op.tag == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op.tag == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }

    private XObject stringBinaryExp(XStringObject a, XStringObject b, Token op) {
        if (op.tag == Tag.Add)
            return new XStringObject(a.value + b.value);
        if (op.tag == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op.tag == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }


}
