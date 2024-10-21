package backend.academy.seminar4.overview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class ExceptionTypes {

    private static final Logger log = LogManager.getLogger(ExceptionTypes.class);

    public static void main(String[] args) {
        ExceptionTypes exceptionTypes = new ExceptionTypes();
        exceptionTypes.runtimeExceptionExample(30, 0);
    }

    // Unchecked exception
    public void runtimeExceptionExample(int a, int b) {
        try {

            int c = a/b;  // cannot divide by zero
            log.info("Result = " + c);
        }
        catch(ArithmeticException e) {
            log.error("Can't divide a number by 0", e);
        }
    }

    // Checked exception - surround by try
    public void fileNotFoundException(String filename) {
        try {

            // Following file does not exist
            File file = new File("E://file.txt");

            FileReader fr = new FileReader(file);
        } catch (FileNotFoundException e) {
            log.error("File does not exist", e);
        }
    }

    // Checked exception - throws
    public void fileNotFoundExceptionThrows(String filename) throws FileNotFoundException {
            // Following file does not exist
            File file = new File("E://file.txt");
            FileReader fr = new FileReader(file);
    }

}
