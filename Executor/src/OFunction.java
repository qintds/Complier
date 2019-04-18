public class OFunction {

    public static void print(XObject object) {
        if (object == null)return;
        XTupleObject temp = (XTupleObject)object;
        for (int i = 0; i < temp.size(); i++) {
            temp.get(i).print();
            System.out.print(", ");
        }
    }

    public static void println(XObject object) {
        if (object == null){System.out.println();return;}
        XTupleObject temp = (XTupleObject)object;
        for (int i = 0; i < temp.size(); i++) {
            temp.get(i).print();
            System.out.print(", ");
        }
        System.out.println();
    }

    public static void printType(XObject object) {
        if (object == null){
            // too less
            return;}
        XTupleObject temp = (XTupleObject)object;
        if (temp.size() == 1) {
            System.out.println(temp.get(0).type);
        } else {
            //too much
        }
    }

}
