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
                node = (CNode)valueStack.get(valueStack.size() - 2);
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
                node = (CNode) valueStack.peek();
                break;
            case UnaryExp_To_Not_Primary:
                unaryExpNot((XObject) valueStack.peek());
                break;
            case UnaryExp_To_Sub_Primary:
                unaryExpSub((XObject) valueStack.peek());
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
                XObject a = (XObject)valueStack.get(valueStack.size() - 3);
                XObject b = (XObject)valueStack.get(valueStack.size() - 1);
                Token op = (Token)valueStack.get(valueStack.size()-2);
                xObject = binaryExp(a, b, op);
                break;
            case Assignment_To_LeftSide_Assign_AssignmentExp:



        }

        if (node != null) {
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
