public class BasicCal {
    public static XObject unaryExpNot(XObject x) {
        if (x.type == XType.xBool) {
            XBoolObject xBool = (XBoolObject)x;
            xBool.value = !xBool.value;
            return xBool;
        } else {
            //error
            return null;
        }
    }

    public static XObject unaryExpSub(XObject x) {
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

    public static XObject binaryExp(XObject a, XObject b, Tag op) {
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
            if (a.type == XType.xReal && b.type ==XType.xNum){
                return realBinaryExp((XRealObject)a, new XRealObject((XNumObject) b), op);
            } else if (a.type == XType.xNum && b.type ==XType.xReal) {
                return realBinaryExp(new XRealObject((XNumObject) a), (XRealObject)b, op);
            }
        }
        return null;
    }

    public static XObject numBinaryExp(XNumObject a, XNumObject b, Tag op) {
        if (op == Tag.Add)
            return new XNumObject(a.value + b.value);
        if (op == Tag.Sub)
            return new XNumObject(a.value - b.value);
        if (op == Tag.Mul)
            return new XNumObject(a.value * b.value);
        if (op == Tag.Div)
            return new XNumObject(a.value / b.value);
        if (op == Tag.Mod)
            return new XNumObject(a.value % b.value);
        if (op == Tag.LT)
            return new XBoolObject(a.value < b.value);
        if (op == Tag.GT)
            return new XBoolObject(a.value > b.value);
        if (op == Tag.LE)
            return new XBoolObject(a.value <= b.value);
        if (op == Tag.GE)
            return new XBoolObject(a.value >= b.value);
        if (op == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }

    public static XObject realBinaryExp(XRealObject a, XRealObject b, Tag op) {
        if (op == Tag.Add)
            return new XRealObject(a.value + b.value);
        if (op == Tag.Sub)
            return new XRealObject(a.value - b.value);
        if (op == Tag.Mul)
            return new XRealObject(a.value * b.value);
        if (op == Tag.Div)
            return new XRealObject(a.value / b.value);
        if (op == Tag.LT)
            return new XBoolObject(a.value < b.value);
        if (op == Tag.GT)
            return new XBoolObject(a.value > b.value);
        if (op == Tag.LE)
            return new XBoolObject(a.value <= b.value);
        if (op == Tag.GE)
            return new XBoolObject(a.value >= b.value);
        if (op == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }

    public static XObject boolBinaryExp(XBoolObject a, XBoolObject b, Tag op) {
        if (op == Tag.And)
            return new XBoolObject(a.value && b.value);
        if (op == Tag.Or)
            return new XBoolObject(a.value || b.value);
        if (op == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }

    public static XObject stringBinaryExp(XStringObject a, XStringObject b, Tag op) {
        if (op == Tag.Add)
            return new XStringObject(a.value + b.value);
        if (op == Tag.EQ)
            return new XBoolObject(a.value == b.value);
        if (op == Tag.NE)
            return new XBoolObject(a.value != b.value);
        else {
            //error
        }
        return null;
    }
}
