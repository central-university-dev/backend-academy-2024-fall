package backend.academy.seminar4.overview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class StackOverflowErrorClass {

    private static final Logger log = LogManager.getLogger(StackOverflowErrorClass.class);
    static int i = 0;

    // Method to print numbers
    public static int printNumber(int x)
    {

        i = i + 2;
        log.info(i);
        return i + printNumber(i + 2);
    }

    public static void main(String[] args)
    {
        // Recursive call without any
        // terminating condition
        StackOverflowErrorClass.printNumber(i);
    }
}
