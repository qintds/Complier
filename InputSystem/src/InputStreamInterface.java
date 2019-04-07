public interface InputStreamInterface {


    void open();

    int close();


    int read(char[] buf, int begin, int len);
}
