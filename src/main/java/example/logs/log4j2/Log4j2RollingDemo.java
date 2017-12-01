package example.logs.log4j2;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author liuhaibo on 2017/12/02
 */
public class Log4j2RollingDemo {

    private static final Logger log = LogManager.getLogger(Log4j2RollingDemo.class);

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 2000; i++){
            log.info("This is the {} time I say 'Hello World'.", i);
            Thread.sleep(100);
        }
        LogManager.shutdown();
    }
}