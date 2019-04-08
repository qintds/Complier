import java.util.Scanner;

public class StandardInputStream implements InputStreamInterface {

    private String inputBuffer = "";
    private int currentPosition = 0;


    @Override
    public void open() {
        Scanner scan = new Scanner(System.in);
        while (true) {
            String line = scan.nextLine();
            if (line.equals("end")) {
                break;
            }
            inputBuffer += line;
        }
        scan.close();
    }

    @Override
    public int close() {
        return 0;
    }


    @Override
    public int read(char[] buf, int begin, int len) {

        if (currentPosition >= inputBuffer.length()) {
            return 0;
        }

        int readCount = 0;
        char[] input = inputBuffer.toCharArray();
        while (currentPosition + readCount < inputBuffer.length() && readCount < len) {
            buf[begin + readCount] = input[currentPosition + readCount];
            readCount++;
        }
        currentPosition += readCount;
        return readCount;
    }
}
