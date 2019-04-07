public class InnerTest {

    public Input input = new InputSB();
    public static void main(String[] args) {
//        Scanner scan = new Scanner(System.in);
//
//        String i = scan.nextLine();
//        System.out.println(i.length());
//        byte[] j = i.getBytes();
//        System.out.println(i);
//        System.out.println(j.length);
//        System.out.println(j);
//        String k = new String(j);
//        System.out.println(k);
//        String i = "今天天气goodgood";
//        char[] j = i.toCharArray();
//        char f = j[0];
//
//        System.out.println(f);
//        for(int k=0;k<j.length;k++){
//            System.out.println(j[k]);
//        }

        InnerTest inner = new InnerTest();
        inner.run();




    }
    public void run() {
        char a='5';

        System.out.println(Character.isDigit(a));
        System.out.println(a-'0');
//        System.out.println(System.getProperty("user.dir"));
//        input.initial("codeFile");
//        File file = new File("codeFile");
//        Long fileLength = file.length();
//        byte[] fileContent = new byte[fileLength.intValue()];
//        try {
//            InputStream in = new FileInputStream(file);
//            in.read(fileContent);
//            in.close();
//            String inputBuffer = new String(fileContent);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        ((InputSB)input).reconizeStart();
//        printWord();
//        ((InputSB)input).reconizeEnd();
//        ((InputSB)input).moveReconize();
//        printWord();
//        printWord();
//        printWord();
//        printWord();
    }
    public void printWord() {
        char c;
        while ((c = input.advance()) != ' ' && c != '\n' && c != 0){
            System.out.print(c);
        }
        System.out.println("");
    }
}
