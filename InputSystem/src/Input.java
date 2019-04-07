public interface Input {


    void initial(String fileName);
    //next character
    char advance();

    char lookAhead(int n);

}
