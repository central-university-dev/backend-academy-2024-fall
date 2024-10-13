package backend.academy.seminar4.overview;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.io.IOException;
import java.sql.SQLException;

public class CompleteExample implements AutoCloseable {

    private static final Logger log = LogManager.getLogger(CompleteExample.class);

    public static void main(String[] args) throws Exception {

        CompleteExample ce = new CompleteExample();
        try {
            ce.someCode();
        } catch (RuntimeException re) {
            log.error("Runtime exception, do something to restore the state and notify users", re);
        } catch (IOException | SQLException sqlException){
            log.error("Some exceptions that require additional actions in finally block", sqlException);
        } catch (Exception e) {
            log.error("Generic exception", e);
        } finally {
            // executes always!!!
            ce.close();
        }
    }

    public static void tryWithResources() {
        try (CompleteExample ce = new CompleteExample()) {
            ce.someCode();
        } catch (RuntimeException re) {
            log.error("Runtime exception, do something to restore the state and notify users", re);
        } catch (IOException | SQLException sqlException){
            log.error("Some exceptions that require additional actions in finally block", sqlException);
        } catch (Exception e) {
            log.error("Generic exception", e);
        } finally {
            // executes always!!!
            // ce will be closed already
        }
    }


    public void someCode() throws Exception {

    }

    @Override
    public void close() throws Exception {
        // some close code
    }
}
