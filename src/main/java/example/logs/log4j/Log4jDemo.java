package example.logs.log4j;

import org.apache.log4j.Logger;

/**
 * @author liuhaibo on 2017/12/02
 */
public class Log4jDemo {

    private static final Logger log = Logger.getLogger(Log4jDemo.class);
    private static final Logger learnFromGorgon = Logger.getLogger("invokeByName");

    public static void main(String[] args) {
        log.trace("Trace log message");
        log.debug("Debug log message");
        log.info("Info log message");
        log.error("Error log message");

        learnFromGorgon.info("This is learnt from Gorgon~");
    }
}