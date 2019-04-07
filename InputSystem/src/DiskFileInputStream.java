import java.io.*;
import java.nio.charset.StandardCharsets;

public class DiskFileInputStream implements InputStreamInterface {

    private String fileName;
    private String inputBuffer = "";
    private int currentPosition = 0;

    public DiskFileInputStream(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void open() {
        File file = new File(fileName);
        if(!file.exists()) {
            System.err.println("Can't not find the specify file");
        }
        Long fileLength = file.length();
        byte[] fileContent = new byte[fileLength.intValue()];
        try {
            InputStream in = new FileInputStream(file);
            in.read(fileContent);
            in.close();
            inputBuffer = new String(fileContent);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
