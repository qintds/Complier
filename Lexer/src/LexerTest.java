import java.util.ArrayList;

public class LexerTest {
    public static void main(String[] args) {
        LexerTest inner = new LexerTest();
        inner.run();
    }

    public void run() {
//        System.out.println(Tag.Add.compareTo(Tag.AddEQ));
//        ArrayList<Integer> compressedStateList = new ArrayList<Integer>();
//        Iterator it = compressedStateList.iterator();
//        System.out.println(it.hasNext());
////        Lexer lex = new Lexer("codeFile");
////        lex.run();
////        printCF(lex.presentFile);

    }

    public void printCF(CodeFile c) {
        for(int i = 0; i < c.totalLine; i++) {
            CodeLine l = c.getLine(i);
            for(int j = 0; j < l.totalTokenCount; j++) {
                System.out.print(l.get(j).toString() + ' ');
            }
            System.out.println();
        }
    }
}
