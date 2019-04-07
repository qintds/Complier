import java.util.Arrays;

public class InputSB implements Input {
    //empty char
    public static final int EOF = 0;
    // max length for look ahead
    private final int MAX_LOOK = 16;
    // max length of a string
    private final int MAX_LEX = 1024;
    // size of the buffer
    private final int BUFFER_SIZE = (MAX_LEX * 3) + (2 * MAX_LOOK);
    // the logical end of th buffer,
    // if there is not enough data that load to the buffer,
    // its value will smaller than BUFFER_SIZE
    private int ACTUAL_END = BUFFER_SIZE;
    // the point forces buffer flush
    private int DANGER = (ACTUAL_END - MAX_LOOK);
    // the physical end of the buffer
    private final int END = BUFFER_SIZE;
    // buffer
    private final char[] BUFFER = new char[BUFFER_SIZE];
    // the point of the next string
    private int next = END;
    // the start of the present string
    private int startPoint = END;
    // the end of the present string
    private int endPoint = END;
    // the start of last string
    private int previousPoint = END;

    public int getPreviousLineNumber() {
        return previousLineNumber;
    }

    //last line number
    private int previousLineNumber = 0;
    //the length of last string
    private int previousStringLength = 0;

    private InputStreamInterface inputStream = null;

    public int getLineNumber() {
        return lineNumber;
    }

    private int lineNumber = 1;

    //record presentLine, relates to lineNumber
    private int presentLine = 1;

    private boolean EOF_LOAD = false;

    private InputStreamInterface loadInputStream(String fileName) {
        if (fileName != null) {
            return new DiskFileInputStream(fileName);
        } else {
            return new StandardInputStream();
        }
    }

    private int fillBuffer(int startAt) {
        // copy integer multiples of MAX_LEX
        int need = ((END - startAt) / MAX_LEX) * MAX_LEX;
        int got = 0;

        if (need > 0) {
            got = inputStream.read(BUFFER, startAt, need);
            if (got == -1) {
                System.err.println("Can't read input file");
            }

        } else if (need < 0) {
            System.err.println("Internal Error(fillBuffer): Bad read-request starting address.");
        } else if (need == 0) {
            return 0;
        }

        ACTUAL_END = startAt + got;
        DANGER = (ACTUAL_END - MAX_LOOK);

        if (got < need) {
            EOF_LOAD = true;
        }

        return got;
    }

    public enum FLUSH_STATUS { fileIsEnd, ok, fail;}

    private FLUSH_STATUS flush(boolean force) {
        int copyLength, shiftLength, leftEdge;

        if (noMoreChars()) {
            return FLUSH_STATUS.fileIsEnd;
        }

        if (EOF_LOAD) {
            return FLUSH_STATUS.ok;
        }

        if (next > DANGER || force) {
            leftEdge = previousPoint < startPoint? previousPoint : startPoint;
            shiftLength = leftEdge;
            copyLength = ACTUAL_END - leftEdge;
            System.arraycopy(BUFFER, leftEdge, BUFFER, 0, copyLength);

            //fill buffer
            if (fillBuffer(copyLength) == 0) {
                System.err.println("Internal Error(flush): Buffer is full, can't read");
            }

            previousPoint -= previousPoint != 0 ? shiftLength : 0;
            startPoint -= shiftLength;
            endPoint -= shiftLength;
            next -= shiftLength;
        }
        return FLUSH_STATUS.ok;
    }

    private boolean noMoreChars() {
        return (EOF_LOAD && next >= ACTUAL_END);
    }

    @Override
    public void initial(String fileName) {
        if (inputStream != null) {
            inputStream.close();
        }

        inputStream = loadInputStream(fileName);
        inputStream.open();

        EOF_LOAD = false;
        next = END;
        previousPoint = END;
        startPoint = END;
        endPoint = END;
        lineNumber = 1;
        presentLine = 1;

    }

    @Override
    public char advance() {

        if (noMoreChars()) {
            return 0;
        }

        if (EOF_LOAD == false && flush(false) == FLUSH_STATUS.fail) {
            return (char)-1;
        }

        if (BUFFER[next] == '\n') {
            lineNumber++;
        }

        return BUFFER[next++];
    }

    @Override
    public char lookAhead(int n) {
        if (EOF_LOAD && next + n -1 >= ACTUAL_END) {
            return EOF;
        }
        char p = BUFFER[next + n -1];
        return (next + n -1 < 0 || next + n - 1 >= ACTUAL_END) ? 0 : p;
    }

    public boolean pushBack(int n) {
        while (--n >= 0 && next > startPoint) {
            if (BUFFER[--next] == '\n' || BUFFER[next] == '\0') {
                lineNumber--;
            }
        }

        if (next < endPoint) {
            endPoint = next;
            presentLine = lineNumber;
        }
        return (next > startPoint);
    }

    public int reconizeStart() {
        presentLine = lineNumber;
        endPoint = startPoint = next;
        return startPoint;
    }
    public int reconizeEnd() {
        presentLine = lineNumber;
        endPoint = next;
        return endPoint;
    }

    public int moveReconize() {
        previousPoint = startPoint;
        previousLineNumber = lineNumber;
        previousStringLength = endPoint - startPoint;
        return previousPoint;
    }


}
