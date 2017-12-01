package example.logs.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author liuhaibo on 2017/12/02
 */
public class LogbackRollingDemo {

    private static final Logger log = LoggerFactory.getLogger(LogbackRollingDemo.class);

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i < 2000; i++){
            log.info("This is the {} time I say 'Hello World'.", i);
            Thread.sleep(10);
        }
    }
}