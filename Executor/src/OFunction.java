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
}
