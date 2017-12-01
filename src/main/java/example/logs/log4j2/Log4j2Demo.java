package example.logs.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author liuhaibo on 2017/12/02
 */
public class Log4j2Demo {

    private static final Logger log = LogManager.getLogger(Log4j2Demo.class);

    public static void main(String[] args) {
        log.trace("Trace log message");
        log.debug("Debug log message");
        log.info("Info log message");
        log.error("Error log message");
    }
}