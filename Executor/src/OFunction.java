public class OFunction {

    public static XObject print(XObject object) {
        if (object == null)return null;
        XTupleObject temp = (XTupleObject)object;
        for (int i = 0; i < temp.size(); i++) {
            if (i != 0)
                System.out.print(", ");
            temp.get(i).print();

        }
        return null;
    }

    public static XObject println(XObject object) {
        if (object == null){System.out.println();return null;}
        XTupleObject temp = (XTupleObject)object;
        for (int i = 0; i < temp.size(); i++) {
            if (i != 0)
                System.out.print(", ");
            temp.get(i).print();

        }
        System.out.println();
        return null;
    }

    public static XObject printType(XObject object) {
        if (object == null){
            // too less
            return null;}
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            System.out.println(temp.get(0).type);
        } else {
            //too much
        }return null;
    }

    //list
    public static XObject listAppend(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 2) {
            ((XListObject)temp.get(0)).add(temp.get(1));
        } else {
            // too much or to less
        }
        return null;
    }

    public static XObject listClear(XObject object) {
        return null;
    }

    public static XObject listSize(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            return new XNumObject(((XListObject)temp.get(0)).size());
        } else {
            // too much or to less
        }
        return null;
    }

    public static XObject str(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            return new XStringObject(temp.get(0));
        } else {
            // too much or to less
        }
        return null;
    }

    public static XObject stringSplit(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            String[] stringList = (((XStringObject)temp.get(0)).value.split(" "));
            XListObject rList = new XListObject();
            for (int i = 0; i < stringList.length; i++) {
                rList.add(new XStringObject(stringList[i]));
            }
            return rList;
        } else if (temp.size() == 2){
            String[] stringList = (((XStringObject)temp.get(0)).value.split(((XStringObject)temp.get(1)).value));
            XListObject rList = new XListObject();
            for (int i = 0; i < stringList.length; i++) {
                rList.add(new XStringObject(stringList[i]));
            }
            return rList;
        } else {
            // too much or to less
        }
        return null;
    }

    public static XObject sqrt(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            if (temp.get(0).type == XType.xNum || temp.get(0).type == XType.xReal) {
                double v;
                if (temp.get(0).type == XType.xNum)
                    v = ((XNumObject)temp.get(0)).value;
                else
                    v = ((XRealObject)temp.get(0)).value;
                return new XRealObject(Math.sqrt(v));
            }
        } else {
            // too much or to less
        }
        return null;
    }

    public static XObject range(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        XListObject listObject = new XListObject();
        if (temp.size() == 1) {
            if(temp.get(0).type == XType.xNum) {
                int end = ((XNumObject)temp.get(0)).value;
                for (int i=0;i<end;i++) {
                    listObject.add(new XNumObject(i));
                }
            }
        } else if(temp.size() == 2) {
            if(temp.get(0).type == XType.xNum && temp.get(1).type == XType.xNum) {
                int start = ((XNumObject)temp.get(0)).value;
                int end = ((XNumObject)temp.get(1)).value;
                for (int i=start;i<end;i++) {
                    listObject.add(new XNumObject(i));
                }
            }
        } else if(temp.size() == 3){
            if(temp.get(0).type == XType.xNum && temp.get(1).type == XType.xNum &&  temp.get(2).type == XType.xNum) {
                int start = ((XNumObject)temp.get(0)).value;
                int end = ((XNumObject)temp.get(1)).value;
                int step = ((XNumObject)temp.get(2)).value;
                for (int i=start;i<end;i+=step) {
                    listObject.add(new XNumObject(i));
                }
            }
        }else
         {
            // too much or to less
             return null;
        }
        return listObject;
    }

    public static XObject toNum(XObject object) {
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            if (temp.get(0).type == XType.xNum || temp.get(0).type == XType.xReal) {
                int v;
                if (temp.get(0).type == XType.xNum)
                    v = ((XNumObject)temp.get(0)).value;
                else
                    v = (int)((XRealObject)temp.get(0)).value;
                return new XNumObject(v);
            }
        } else {
            // too much or to less
        }
        return null;
    }
}
