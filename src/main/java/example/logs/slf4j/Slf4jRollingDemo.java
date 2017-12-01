package example.logs.slf4j;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Since we are using `logback-classic`, it'll use logback.xml as configuration.
 * So it can only have 2 appenders: stdout & fileout.
 */
public class Slf4jRollingDemo {

    private static Logger logger = LoggerFactory.getLogger(Slf4jRollingDemo.class);

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0; i<200; i++){
            logger.info("This is the {} time I say 'Hello World'.", i);
            Thread.sleep(100);
        }
    }

}
