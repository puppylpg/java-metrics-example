package example.logs.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuhaibo on 2017/12/02
 */
public class LogbackDemo {

    private static final Logger log = LoggerFactory.getLogger(LogbackDemo.class);

    public static void main(String[] args) {
        log.debug("Debug log message");
        log.info("Info log message");
        log.error("Error log message");
    }
}